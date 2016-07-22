package club.hoy.base.exception.util;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import club.hoy.base.exception.CodedException;
import club.hoy.base.exception.ErrorCode;

/**
 * 错误异常信息处理类，根据errorCode匹配错误描述信息,或者CodedException
 * 
 * @author Bimu
 *
 */
@Component
public class ErrorMessageUtil {

	private static Logger LOGGER = LoggerFactory.getLogger(ErrorMessageUtil.class);
	
	//private static PropertiesConfiguration CONFIG;
	
	static{
		
		/*
		 * 用$号作为消息分组功能。暂时用不着消息分组。
		 * 默认为逗号，不合适，所以才修改了设置。
		 * 
		 */
		//PropertiesConfiguration.setDefaultListDelimiter('$');
	}
	
	/**
	 * 初始化properties文件，
	 * 并且设置properties文件被修改时，可以自动的重新加载配置文件。
	 */
	@PostConstruct
	public void init(){
		try {
			//CONFIG = new PropertiesConfiguration("error-files.properties");
			//CONFIG.setReloadingStrategy(new FileChangedReloadingStrategy());
			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(),e);
		}
	}
	
	/**
	 * 根据properties文件的key值获取value
	 * 
	 * @param key
	 * @return
	 */
	private static String getError(String key){
		return "";
	}
	
	/**
	 * 根据错误对象获取错误对象对应的error message
	 * 
	 * @param errorCode
	 * @return
	 */
	public static String getErrorMessage(ErrorCode errorCode){
		
		if(errorCode == null || errorCode.toString() == null) return null;
		
		String errorMessage = getError(errorCode.toString());
		if(errorMessage == null) return errorCode.toString();
		
		return errorMessage;
	}
	
	/**
	 * 把codedException中的参数放入到error message中。
	 * error message的形式为 XXXXX{1}X, XXXX{2}XX....
	 * 以数值为顺序定义替换的顺序
	 * 
	 * @param codedException
	 * @return
	 */
	public static String getErrorMessage(CodedException codedException){
		
		ErrorCode errorCode = codedException.getError();
		Object[]  arguments = codedException.getArguments();
		
		String errorMessage = getErrorMessage(errorCode);
		if(errorMessage == null) return null;
		
		if(arguments == null || arguments.length == 0) return errorMessage;
		
		/*
		 * 替换所有的参数，到消息中。
		 */
		for(int i=0; i< arguments.length; i++ ){
			
			String replacedStr = "";
			
			if(arguments[i] != null){
				replacedStr = arguments[i].toString();
			}
			
			errorMessage = errorMessage.replaceAll("\\{[ ]*" + (i+1) + "[ ]*\\}",replacedStr);
		}
		
		return errorMessage;
	}
}
