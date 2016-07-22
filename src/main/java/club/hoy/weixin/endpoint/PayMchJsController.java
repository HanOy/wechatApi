package club.hoy.weixin.endpoint;

import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import club.hoy.weixin.api.PayMchAPI;
import club.hoy.weixin.model.paymch.Unifiedorder;
import club.hoy.weixin.model.paymch.UnifiedorderResult;
import club.hoy.weixin.util.PayUtil;

/**
 * 统一下单
 * @author ouyanghan
 * @version $Id: V1.0 2016年7月22日 下午3:08:10 Exp $
 */
@Controller
@RequestMapping(value = "/pay")
public class PayMchJsController {
    
    private String appid;           //appid
    private String mch_id;          //微信支付商户号
    private String key;             //API密钥
    
    /**
     * 生成支付JS请求对象
     * @param json
     * @param request
     * @return
     */
    @RequestMapping(value = "/js/request", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String payJsRequest(HttpServletRequest request) {
      //payPackage 的商品信息，总价可以通过前端传入

        Unifiedorder unifiedorder = new Unifiedorder();
        unifiedorder.setAppid(appid);
        unifiedorder.setMch_id(mch_id);
        unifiedorder.setNonce_str(UUID.randomUUID().toString());

        unifiedorder.setBody("商品信息");
        unifiedorder.setOut_trade_no("123456");
        unifiedorder.setTotal_fee("1");//单位分
        unifiedorder.setSpbill_create_ip(request.getRemoteAddr());//IP
        unifiedorder.setNotify_url("http://mydomain.com/test/notify");
        unifiedorder.setTrade_type("JSAPI");//JSAPI，NATIVE，APP，WAP

        UnifiedorderResult unifiedorderResult = PayMchAPI.payUnifiedorder(unifiedorder,key);

        String json = PayUtil.generateMchPayJsRequestJson(unifiedorderResult.getPrepay_id(), appid, key);
        return json;
    }
}
