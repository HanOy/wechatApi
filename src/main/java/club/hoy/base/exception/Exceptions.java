package club.hoy.base.exception;

import java.util.WeakHashMap;

import club.hoy.base.exception.util.ErrorMessageUtil;

/**
 * CodedException的工厂类
 * 该类提供了对CodedException的管理，通过使用except方法，不需要每次创建一个新的CodedException
 * TODO 异常信息的管理
 * TODO 异常捕获的统一处理
 * @author shengan.sg
 *
 */
public class Exceptions {
	private static final WeakHashMap<ErrorCode,CodedException> exceptions = new WeakHashMap<ErrorCode,CodedException>();
	/**
	 * @param
	 * @return
	 */
	public static CodedException except(ErrorCode errCode) {
		
		CodedException exception = exceptions.get(errCode);
		if(exception!=null) return exception;
		
		exception = new CodedException(errCode);
		exceptions.put(errCode,exception);
		
		return exception;
		
	}
	
	public static ErrorCode errorCode(String moduleCode,String subErrorCode) {
		
		return new ErrorCode(moduleCode,subErrorCode);
	}
	
	/**
	 * add by Bimu 2014-05-21
	 * 
	 * 根据errorCode获取异常信息,不带参数信息
	 * 
	 * @param errorCode
	 * @return
	 */
	public static String toString(ErrorCode errorCode){
		return ErrorMessageUtil.getErrorMessage(errorCode);
	}
	
	/**
	 * add by Bimu 2014-05-21
	 * 
	 * 根据异常显示异常信息。异常包含，moduleCode.errorCode 和 args
	 * 
	 * @param exception
	 * @return
	 */
	public static String toString(CodedException exception){
		return ErrorMessageUtil.getErrorMessage(exception);
	}
}
