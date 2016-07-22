package club.hoy.weixin.endpoint;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import club.hoy.weixin.model.paymch.MchNotifyResult;
import club.hoy.weixin.model.paymch.MchPayNotify;
import club.hoy.weixin.util.ExpireSet;
import club.hoy.weixin.util.MapUtil;
import club.hoy.weixin.util.SignatureUtil;
import club.hoy.weixin.util.StreamUtils;
import club.hoy.weixin.util.XMLConverUtil;

/**
 * 支付回调通知
 * @author ouyanghan
 * @version $Id: V1.0 2016年7月22日 下午3:22:31 Exp $
 */
@Controller
@RequestMapping(value = "/notify")
public class PayMchNotifyController {

    private String key; //mch key

    //重复通知过滤  时效60秒
    private static ExpireSet<String> expireSet = new ExpireSet<String>(60);

    @RequestMapping(value = "/callback")
    public void callback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String xml = StreamUtils.copyToString(request.getInputStream(), Charset.forName("utf-8"));
        request.getInputStream().close();
        Map<String, String> map = MapUtil.xmlToMap(xml);
        if (expireSet.contains(map.get("transaction_id"))) {
            return;
        }
        String sign = SignatureUtil.generateSign(map, key);
        if (!sign.equals(map.get("sign"))) {
            MchNotifyResult result = new MchNotifyResult();
            result.setReturn_code("FAIL");
            result.setReturn_msg("ERROR");
            response.getOutputStream().write(XMLConverUtil.convertToXML(result).getBytes());
        } else {
            MchPayNotify payNotify = XMLConverUtil.convertToObject(MchPayNotify.class, xml);
            expireSet.add(payNotify.getTransaction_id());
            //业务逻辑
            // 通知微信服务器
            MchNotifyResult result = new MchNotifyResult();
            result.setReturn_code("SUCCESS");
            result.setReturn_msg("OK");
            response.getOutputStream().write(XMLConverUtil.convertToXML(result).getBytes());
        }
    }
}
