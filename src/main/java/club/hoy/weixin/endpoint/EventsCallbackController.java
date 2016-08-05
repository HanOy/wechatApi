package club.hoy.weixin.endpoint;

/**
 * 服务端事件消息接收
 * @author shengan
 *
 */

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import club.hoy.base.Pair;
import club.hoy.base.exception.Exceptions;
import club.hoy.weixin.EVENT_TYPE;
import club.hoy.weixin.model.EventMessage;
import club.hoy.weixin.model.xmlmessage.XMLTextMessage;
import club.hoy.weixin.util.ExpireSet;
import club.hoy.weixin.util.SignatureUtil;
import club.hoy.weixin.events.EventHandler;

@RestController
@RequestMapping("/event")
public class EventsCallbackController {

    //系统提示繁忙信息
    @Value("${message.system_busy}")
    private String ALERT_SYSTEM_BUSY;

    Logger log = LoggerFactory.getLogger(EventsCallbackController.class);

    //微信平台的Token值
    @Value("${weixin.token}")
    private String TOKEN;

    private Map<String, Pair<EVENT_TYPE, EventHandler>> eventHandlers;

    public void setEventHandlers(List<EventHandler> events) {
        eventHandlers = new HashMap<String, Pair<EVENT_TYPE, EventHandler>>();
        for (EventHandler handler : events) {
            for (EVENT_TYPE type : handler.eventTypes()) {
                eventHandlers.put(type.toString(), new Pair<EVENT_TYPE, EventHandler>(type, handler));
            } ;
        }
    }

    //重复通知过滤  时效60秒
    private static ExpireSet<String> messageCache = new ExpireSet<String>(60);

    /**
     * 微信平台验证Token验证
     * @param signature			微信加密签名
     * @param timestamp		时间戳
     * @param nonce				随机数
     * @param echostr				随机字符串
     * @return		
     * 			如果验证通过 	返回随机字符串
     * 			如果验证失败	返回500错误
     */
    @RequestMapping(value = "/", method = RequestMethod.GET, produces = "text/raw")
    public ResponseEntity<String> checkServerState(@RequestParam("signature") String signature, @RequestParam("timestamp") String timestamp, @RequestParam("nonce") String nonce, @RequestParam("echostr") String echostr) {

        log.debug("?signature={}&timestamp={}&nonce={}&echostr={}", signature, timestamp, nonce, echostr);

        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        String sig = SignatureUtil.generateEventMessageSignature(TOKEN, timestamp, nonce);
        if (sig.equals(signature)) {
            return new ResponseEntity<String>(echostr, HttpStatus.OK);
        }

        // 参数验证不通过，返回503
        return Status._503;

    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<String> handleEventMessage(@RequestParam("signature") String signature, @RequestParam("timestamp") String timestamp, @RequestParam("nonce") String nonce, @RequestBody EventMessage event) {

        // 通过检验signature对请求进行校验，如果不正确则返回503
        String sig = SignatureUtil.generateEventMessageSignature(TOKEN, timestamp, nonce);
        if (!sig.equals(signature)) {
            return Status._503;
        }

        //消息去重，以messageId和create time作为key
        final String key = event.getCreateTime() + event.getMsgId();
        if (messageCache.contains(key)) {
            return Status._200;
        }
        messageCache.add(key);

        //根据消息类型匹配合适的handler
        String type = event.getMsgType();

        EventHandler handler = null;

        //判断消息类型。如果是事件推送类型，则进一步区分是哪种事件
        if (EVENT_TYPE.event.equals(type)) {
            type = event.getEvent();
        }
        Pair<EVENT_TYPE, EventHandler> pair = eventHandlers.get(type);
        if (pair == null) {
            throw Exceptions.except(EventHandler.ErrorCodes.CAN_NOT_FOUND_MATHED_EVENT_HANDLER);
        }

        //处理消息
        EVENT_TYPE eventType = pair.getKey();
        handler = pair.getValue();
        String retMessage = null;
        try {
            retMessage = handler.handle(event, eventType);
        } catch (Exception e) {
            log.error("在处理消息时发生异常。event[" + event.toString() + "].", e);
            retMessage = ALERT_SYSTEM_BUSY;
        }

        //创建回复
        XMLTextMessage xmlTextMessage = new XMLTextMessage(event.getFromUserName(), event.getToUserName(), retMessage);

        return new ResponseEntity<String>(xmlTextMessage.toXML(), HttpStatus.OK);
    }
}
