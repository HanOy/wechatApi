package club.hoy.base.exception;

public final class ErrorCode {
	
	public static String ILLEGAL_ARGUMENT_MODULE = "ILLEGAL_ARGUMENT";
	
	private String moduleCode;
	private String errorCode;

	protected ErrorCode(String moduleCode,String errorCode) {
		this.moduleCode = moduleCode;
		this.errorCode = errorCode;
	}
	
	public String toString() {
		return this.moduleCode+"."+this.errorCode;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((errorCode == null) ? 0 : errorCode.hashCode());
		result = prime * result
				+ ((moduleCode == null) ? 0 : moduleCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ErrorCode other = (ErrorCode) obj;
		if (errorCode == null) {
			if (other.errorCode != null)
				return false;
		} else if (!errorCode.equals(other.errorCode))
			return false;
		if (moduleCode == null) {
			if (other.moduleCode != null)
				return false;
		} else if (!moduleCode.equals(other.moduleCode))
			return false;
		return true;
	}
	
	
}
