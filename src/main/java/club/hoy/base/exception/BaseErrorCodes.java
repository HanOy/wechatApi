package club.hoy.base.exception;

import static club.hoy.base.exception.Exceptions.errorCode;

public final class BaseErrorCodes {
	/**
	 *  异常的模块编码
	 */
	public static final String MODULE_CODE = "BASE_ERROR";

	/**
	 * 验证码错误
	 */
	public static final ErrorCode VERFIY_CODE_ERROR = errorCode(MODULE_CODE, "VERFIY_CODE_ERROR");
	
	/**
	 * HTTP、HTTPS模拟发送请求异常
	 */
	public static final ErrorCode HTTP_REQ_EXCEPTION = errorCode(MODULE_CODE, "HTTP_REQ_EXCEPTION");
	
	/**
	 * HTTP、HTTPS模拟发送请求超时
	 */
	public static final ErrorCode HTTP_REQ_TIMEOUT =  errorCode(MODULE_CODE, "HTTP_REQ_TIMEOUT");
	
	/**
	 * xml解析异常
	 */
	public static final ErrorCode XML_PARSER_EXCEPTION =  errorCode(MODULE_CODE, "XML_PARSER_EXCEPTION");
}
