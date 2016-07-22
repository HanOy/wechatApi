package club.hoy.base.exception;

import java.util.Arrays;

/**
 * 编码方式异常
 * 在服务层的开发中，需要通过异常来处理特例分支
 * @author shengan.sg
 *
 */
public class CodedException extends RuntimeException {
	
	private static final long serialVersionUID = -5704728092736131237L;
	
	/**
	 * 该异常的错误码
	 */
	private ErrorCode errorCode;
	
	private Object[] args;
		
	public ErrorCode getError() {
		return this.errorCode;
	}
	
	public Object[] getArguments() {
		return this.args;
	}
	
	public CodedException(ErrorCode errorCode,Object...arguments){
		this.errorCode = errorCode;
		this.args = arguments;
	}
	
	/**
	 * 覆盖该方法，以提高服务层异常Runtime时的执行效率
	 */
	public Throwable fillInStackTrace() {
		return null;
	}
	/**
	 * 保留原有的fillInStackTrace方法，给继承该类的类一个选择的机会
	 * @return
	 */
	protected Throwable _fillInStackTrace() {
		return super.fillInStackTrace();
	}

	@Override
	public String toString() {
		return "CodedException [errorCode=" + errorCode + ", args="+ Arrays.toString(args) + "]";
	}
	
}
