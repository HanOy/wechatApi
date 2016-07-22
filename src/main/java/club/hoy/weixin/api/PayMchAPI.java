package club.hoy.weixin.api;

import java.nio.charset.Charset;
import java.util.Map;

import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;

import club.hoy.weixin.client.LocalHttpClient;
import club.hoy.weixin.model.paymch.Closeorder;
import club.hoy.weixin.model.paymch.MchBaseResult;
import club.hoy.weixin.model.paymch.MchDownloadbill;
import club.hoy.weixin.model.paymch.MchOrderInfoResult;
import club.hoy.weixin.model.paymch.MchOrderquery;
import club.hoy.weixin.model.paymch.MchReverse;
import club.hoy.weixin.model.paymch.MchReverseResult;
import club.hoy.weixin.model.paymch.MchShorturl;
import club.hoy.weixin.model.paymch.MchShorturlResult;
import club.hoy.weixin.model.paymch.Micropay;
import club.hoy.weixin.model.paymch.MicropayResult;
import club.hoy.weixin.model.paymch.QueryCoupon;
import club.hoy.weixin.model.paymch.QueryCouponResult;
import club.hoy.weixin.model.paymch.QueryCouponStock;
import club.hoy.weixin.model.paymch.QueryCouponStockResult;
import club.hoy.weixin.model.paymch.Refundquery;
import club.hoy.weixin.model.paymch.RefundqueryResult;
import club.hoy.weixin.model.paymch.Report;
import club.hoy.weixin.model.paymch.SecapiPayRefund;
import club.hoy.weixin.model.paymch.SecapiPayRefundResult;
import club.hoy.weixin.model.paymch.SendCoupon;
import club.hoy.weixin.model.paymch.SendCouponResult;
import club.hoy.weixin.model.paymch.Sendredpack;
import club.hoy.weixin.model.paymch.SendredpackResult;
import club.hoy.weixin.model.paymch.Transfers;
import club.hoy.weixin.model.paymch.TransfersResult;
import club.hoy.weixin.model.paymch.Unifiedorder;
import club.hoy.weixin.model.paymch.UnifiedorderResult;
import club.hoy.weixin.util.MapUtil;
import club.hoy.weixin.util.SignatureUtil;
import club.hoy.weixin.util.XMLConverUtil;

/**
 * 微信支付 基于V3.X 版本
 * @author Yi
 *
 */
public class PayMchAPI extends BaseAPI{

	/**
	 * 统一下单
	 * 请使用  payUnifiedorder(Unifiedorder unifiedorder,String key),
	 * 自动生成sign
	 * @param unifiedorder
	 * @return
	 */
	@Deprecated
	public static UnifiedorderResult payUnifiedorder(Unifiedorder unifiedorder){
		return payUnifiedorder(unifiedorder, null);
	}

	/**
	 * 统一下单
	 * @param unifiedorder
	 * @param key
	 * @return
	 */
	public static UnifiedorderResult payUnifiedorder(Unifiedorder unifiedorder,String key){
		Map<String,String> map = MapUtil.objectToMap(unifiedorder);
		if(key != null){
			String sign = SignatureUtil.generateSign(map,key);
			unifiedorder.setSign(sign);
		}
		String unifiedorderXML = XMLConverUtil.convertToXML(unifiedorder);
		HttpUriRequest httpUriRequest = RequestBuilder.post()
										.setHeader(xmlHeader)
										.setUri(MCH_URI + "/pay/unifiedorder")
										.setEntity(new StringEntity(unifiedorderXML,Charset.forName("utf-8")))
										.build();
		return LocalHttpClient.executeXmlResult(httpUriRequest,UnifiedorderResult.class);
	}

	/**
	 * 刷卡支付  提交被扫支付API
	 * @param micropay
	 * @param key
	 * @return
	 */
	public static MicropayResult payMicropay(Micropay micropay,String key){
		Map<String,String> map = MapUtil.objectToMap(micropay);
		String sign = SignatureUtil.generateSign(map,key);
		micropay.setSign(sign);
		String closeorderXML = XMLConverUtil.convertToXML(micropay);
		HttpUriRequest httpUriRequest = RequestBuilder.post()
				.setHeader(xmlHeader)
				.setUri(MCH_URI + "/pay/micropay")
				.setEntity(new StringEntity(closeorderXML,Charset.forName("utf-8")))
				.build();
		return LocalHttpClient.executeXmlResult(httpUriRequest,MicropayResult.class);
	}

	/**
	 * 查询订单
	 * @param mchOrderquery
	 * @param key
	 * @return
	 */
	public static MchOrderInfoResult payOrderquery(MchOrderquery mchOrderquery,String key){
		Map<String,String> map = MapUtil.objectToMap(mchOrderquery);
		String sign = SignatureUtil.generateSign(map,key);
		mchOrderquery.setSign(sign);
		String closeorderXML = XMLConverUtil.convertToXML(mchOrderquery);
		HttpUriRequest httpUriRequest = RequestBuilder.post()
				.setHeader(xmlHeader)
				.setUri(MCH_URI + "/pay/orderquery")
				.setEntity(new StringEntity(closeorderXML,Charset.forName("utf-8")))
				.build();
		return LocalHttpClient.executeXmlResult(httpUriRequest,MchOrderInfoResult.class);
	}



	/**
	 * 关闭订单
	 * @param closeorder
	 * @param key 商户支付密钥
	 * @return
	 */
	public static MchBaseResult payCloseorder(Closeorder closeorder,String key){
		Map<String,String> map = MapUtil.objectToMap(closeorder);
		String sign = SignatureUtil.generateSign(map,key);
		closeorder.setSign(sign);
		String closeorderXML = XMLConverUtil.convertToXML(closeorder);
		HttpUriRequest httpUriRequest = RequestBuilder.post()
				.setHeader(xmlHeader)
				.setUri(MCH_URI + "/pay/closeorder")
				.setEntity(new StringEntity(closeorderXML,Charset.forName("utf-8")))
				.build();
		return LocalHttpClient.executeXmlResult(httpUriRequest,MchBaseResult.class);
	}


	/**
	 * 申请退款
	 *
	 * 注意：
	 *	1.交易时间超过半年的订单无法提交退款；
	 *	2.微信支付退款支持单笔交易分多次退款，多次退款需要提交原支付订单的商户订单号和设置不同的退款单号。一笔退款失败后重新提交，要采用原来的退款单号。总退款金额不能超过用户实际支付金额。
	 * @param secapiPayRefund
	 * @param key 商户支付密钥
	 * @return
	 */
	public static SecapiPayRefundResult secapiPayRefund(SecapiPayRefund secapiPayRefund,String key){
		Map<String,String> map = MapUtil.objectToMap( secapiPayRefund);
		String sign = SignatureUtil.generateSign(map,key);
		secapiPayRefund.setSign(sign);
		String secapiPayRefundXML = XMLConverUtil.convertToXML( secapiPayRefund);
		HttpUriRequest httpUriRequest = RequestBuilder.post()
				.setHeader(xmlHeader)
				.setUri(MCH_URI + "/secapi/pay/refund")
				.setEntity(new StringEntity(secapiPayRefundXML,Charset.forName("utf-8")))
				.build();
		return LocalHttpClient.keyStoreExecuteXmlResult(secapiPayRefund.getMch_id(),httpUriRequest,SecapiPayRefundResult.class);
	}

	/**
	 * 撤销订单
	 * 7天以内的交易单可调用撤销，其他正常支付的单如需实现相同功能请调用申请退款API。提交支付交易后调用【查询订单API】，没有明确的支付结果再调用【撤销订单API】。
	 * @param mchReverse
	 * @param key
	 * @return
	 */
	public static MchReverseResult secapiPayReverse(MchReverse mchReverse,String key){
		Map<String,String> map = MapUtil.objectToMap( mchReverse);
		String sign = SignatureUtil.generateSign(map,key);
		mchReverse.setSign(sign);
		String secapiPayRefundXML = XMLConverUtil.convertToXML( mchReverse);
		HttpUriRequest httpUriRequest = RequestBuilder.post()
				.setHeader(xmlHeader)
				.setUri(MCH_URI + "/secapi/pay/reverse")
				.setEntity(new StringEntity(secapiPayRefundXML,Charset.forName("utf-8")))
				.build();
		return LocalHttpClient.keyStoreExecuteXmlResult(mchReverse.getMch_id(),httpUriRequest,MchReverseResult.class);
	}

	/**
	 * 查询退款
	 *
	 * 提交退款申请后，通过调用该接口查询退款状态。退款有一定延时，用零钱支付的退款
	 * 20 分钟内到账，银行卡支付的退款3 个工作日后重新查询退款状态。
	 * @param refundquery
	 * @param key 商户支付密钥
	 * @return
	 */
	public static RefundqueryResult payRefundquery(Refundquery refundquery,String key){
		Map<String,String> map = MapUtil.objectToMap(refundquery);
		String sign = SignatureUtil.generateSign(map,key);
		refundquery.setSign(sign);
		String refundqueryXML = XMLConverUtil.convertToXML(refundquery);
		HttpUriRequest httpUriRequest = RequestBuilder.post()
				.setHeader(xmlHeader)
				.setUri(MCH_URI + "/pay/refundqueryd")
				.setEntity(new StringEntity(refundqueryXML,Charset.forName("utf-8")))
				.build();
		return LocalHttpClient.executeXmlResult(httpUriRequest,RefundqueryResult.class);
	}

	/**
	 * 下载对账单
	 * @param downloadbill
	 * @param key
	 * @return
	 */
	public static MchBaseResult payDownloadbill(MchDownloadbill downloadbill,String key){
		Map<String,String> map = MapUtil.objectToMap(downloadbill);
		String sign = SignatureUtil.generateSign(map,key);
		downloadbill.setSign(sign);
		String closeorderXML = XMLConverUtil.convertToXML(downloadbill);
		HttpUriRequest httpUriRequest = RequestBuilder.post()
				.setHeader(xmlHeader)
				.setUri(MCH_URI + "/pay/downloadbill")
				.setEntity(new StringEntity(closeorderXML,Charset.forName("utf-8")))
				.build();
		return LocalHttpClient.executeXmlResult(httpUriRequest,MchBaseResult.class);
	}

	/**
	 * 短链接转换
	 * @param shorturl
	 * @param key 商户支付密钥
	 * @return
	 */
	public static MchShorturlResult toolsShorturl(MchShorturl shorturl,String key){
		Map<String,String> map = MapUtil.objectToMap(shorturl);
		String sign = SignatureUtil.generateSign(map,key);
		shorturl.setSign(sign);
		String shorturlXML = XMLConverUtil.convertToXML(shorturl);
		HttpUriRequest httpUriRequest = RequestBuilder.post()
				.setHeader(xmlHeader)
				.setUri(MCH_URI + "/tools/shorturl")
				.setEntity(new StringEntity(shorturlXML,Charset.forName("utf-8")))
				.build();
		return LocalHttpClient.executeXmlResult(httpUriRequest,MchShorturlResult.class);
	}

	/**
	 * 测速上报
	 * @param report
	 * @param key
	 * @return
	 */
	public static MchBaseResult payitilReport(Report report,String key){
		Map<String,String> map = MapUtil.objectToMap(report);
		String sign = SignatureUtil.generateSign(map,key);
		report.setSign(sign);
		String shorturlXML = XMLConverUtil.convertToXML(report);
		HttpUriRequest httpUriRequest = RequestBuilder.post()
				.setHeader(xmlHeader)
				.setUri(MCH_URI + "/payitil/report")
				.setEntity(new StringEntity(shorturlXML,Charset.forName("utf-8")))
				.build();
		return LocalHttpClient.executeXmlResult(httpUriRequest,MchBaseResult.class);
	}

	/**
	 * 发放代金券
	 * @param sendCoupon
	 * @param key
	 * @return
	 */
	public static SendCouponResult mmpaymkttransfersSend_coupon(SendCoupon sendCoupon,String key){
		Map<String,String> map = MapUtil.objectToMap( sendCoupon);
		String sign = SignatureUtil.generateSign(map,key);
		sendCoupon.setSign(sign);
		String secapiPayRefundXML = XMLConverUtil.convertToXML( sendCoupon);
		HttpUriRequest httpUriRequest = RequestBuilder.post()
				.setHeader(xmlHeader)
				.setUri(MCH_URI + "/mmpaymkttransfers/send_coupon")
				.setEntity(new StringEntity(secapiPayRefundXML,Charset.forName("utf-8")))
				.build();
		return LocalHttpClient.keyStoreExecuteXmlResult(sendCoupon.getMch_id(),httpUriRequest,SendCouponResult.class);
	}

	/**
	 * 查询代金券批次
	 * @param queryCouponStock
	 * @param key
	 * @return
	 */
	public static QueryCouponStockResult mmpaymkttransfersQuery_coupon_stock(QueryCouponStock queryCouponStock,String key){
		Map<String,String> map = MapUtil.objectToMap( queryCouponStock);
		String sign = SignatureUtil.generateSign(map,key);
		queryCouponStock.setSign(sign);
		String secapiPayRefundXML = XMLConverUtil.convertToXML( queryCouponStock);
		HttpUriRequest httpUriRequest = RequestBuilder.post()
				.setHeader(xmlHeader)
				.setUri(MCH_URI + "/mmpaymkttransfers/query_coupon_stock")
				.setEntity(new StringEntity(secapiPayRefundXML,Charset.forName("utf-8")))
				.build();
		return LocalHttpClient.executeXmlResult(httpUriRequest,QueryCouponStockResult.class);
	}

	/**
	 * 查询代金券信息
	 * @param queryCoupon
	 * @param key
	 * @return
	 */
	public static QueryCouponResult promotionQuery_coupon(QueryCoupon queryCoupon,String key){
		Map<String,String> map = MapUtil.objectToMap( queryCoupon);
		String sign = SignatureUtil.generateSign(map,key);
		queryCoupon.setSign(sign);
		String secapiPayRefundXML = XMLConverUtil.convertToXML( queryCoupon);
		HttpUriRequest httpUriRequest = RequestBuilder.post()
				.setHeader(xmlHeader)
				.setUri(MCH_URI + "/promotion/query_coupon")
				.setEntity(new StringEntity(secapiPayRefundXML,Charset.forName("utf-8")))
				.build();
		return LocalHttpClient.executeXmlResult(httpUriRequest,QueryCouponResult.class);
	}

	/**
	 * 现金红包
	 *
	 * 微信红包发送规则
	 * 1. 发送频率规则
　	 *	每分钟发送红包数量不得超过1800个；
　	 *	北京时间0：00-8：00不触发红包赠送；（如果以上规则不满足您的需求，请发邮件至wxhongbao@tencent.com获取升级指引）
	 *	2. 红包规则
	 *	单个红包金额介于[1.00元，200.00元]之间；
	 *	同一个红包只能发送给一个用户；（如果以上规则不满足您的需求，请发邮件至wxhongbao@tencent.com获取升级指引）
	 * @param sendredpack
	 * @param key
	 * @return
	 */
	public static SendredpackResult mmpaymkttransfersSendredpack(Sendredpack sendredpack,String key){
		Map<String,String> map = MapUtil.objectToMap( sendredpack);
		String sign = SignatureUtil.generateSign(map,key);
		sendredpack.setSign(sign);
		String secapiPayRefundXML = XMLConverUtil.convertToXML( sendredpack);
		HttpUriRequest httpUriRequest = RequestBuilder.post()
				.setHeader(xmlHeader)
				.setUri(MCH_URI + "/mmpaymkttransfers/sendredpack")
				.setEntity(new StringEntity(secapiPayRefundXML,Charset.forName("utf-8")))
				.build();
		return LocalHttpClient.keyStoreExecuteXmlResult(sendredpack.getMch_id(),httpUriRequest,SendredpackResult.class);
	}

	/**
	 * 企业付款
	 * @param transfers
	 * @param key
	 * @return
	 */
	public static TransfersResult mmpaymkttransfersPromotionTransfers(Transfers transfers,String key){
		Map<String,String> map = MapUtil.objectToMap( transfers);
		String sign = SignatureUtil.generateSign(map,key);
		transfers.setSign(sign);
		String secapiPayRefundXML = XMLConverUtil.convertToXML( transfers);
		HttpUriRequest httpUriRequest = RequestBuilder.post()
				.setHeader(xmlHeader)
				.setUri(MCH_URI + "/mmpaymkttransfers/promotion/transfers")
				.setEntity(new StringEntity(secapiPayRefundXML,Charset.forName("utf-8")))
				.build();
		return LocalHttpClient.keyStoreExecuteXmlResult(transfers.getMchid(),httpUriRequest,TransfersResult.class);
	}

}
