package club.hoy.base.exception;

import static club.hoy.base.exception.Exceptions.errorCode;
/**
 * 系统级异常编码
 * @author shengan.sg
 *
 */
public  class SystemErrorCodes {
	
	/**
	 *  系统级异常的模块编码
	 */
	public static final String MODULE_CODE = "SYSTEM_ERROR";
	
	/**
	 * 应用不可用
	 */
	public static final ErrorCode APPLICATION_NOT_AVAILABLE = errorCode(MODULE_CODE,"APPLICATION_NOT_AVAILABLE");
	/**
	 * 网络不可用
	 */
	public static final ErrorCode NETWORK_NOT_AVAILABLE = errorCode(MODULE_CODE,"NETWORK_NOT_AVAILABLE");
}
