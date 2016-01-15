package com.xdroid.request.network;

/**
 * HTTP request exception
 * 
 * @author Robin
 * @since 2015-07-02 15:10:49
 */
@SuppressWarnings("serial")
public class HttpException extends Exception {
	
	/** Http request error code */
	private int mHttpErrorCode = HttpError.ERROR_UNKNOW;
	
	/** Http request error text */
	private String mHttpErrorText;
	
	public HttpException() {
	}

	public HttpException(String exceptionMessage,int httpErrorCode) {
		super(exceptionMessage);
		this.mHttpErrorCode = httpErrorCode;
		this.mHttpErrorText = exceptionMessage;
	}

	public HttpException(String exceptionMessage, Throwable reason) {
		super(exceptionMessage, reason);
		this.mHttpErrorText = exceptionMessage;
	}
	
	public HttpException(String exceptionMessage) {
		super(exceptionMessage);
		this.mHttpErrorText = exceptionMessage;
	}
	
	/*public HttpException(Throwable cause) {
		super(cause);
	}*/

	public int getHttpErrorCode() {
		return mHttpErrorCode;
	}

	public void setHttpErrorCode(int httpErrorCode) {
		this.mHttpErrorCode = httpErrorCode;
	}
	

	public String getHttpErrorText() {
		return mHttpErrorText;
	}

	public void setHttpErrorText(String httpErrorText) {
		this.mHttpErrorText = httpErrorText;
	}

	@Override
	public String toString() {
		String errorCodeText = "";
		switch (mHttpErrorCode) {
		case HttpError.ERROR_NOT_NETWORK:
			errorCodeText = "ERROR_NOT_NETWORK";
			break;
		case HttpError.ERROR_REDIRECT:
			errorCodeText = "ERROR_REDIRECT";
			break;
		case HttpError.ERROR_RESPONSE_NULL:
			errorCodeText = "ERROR_RESPONSE_NULL";
			break;
		case HttpError.ERROR_SERVER:
			errorCodeText = "ERROR_SERVER";
			break;
		case HttpError.ERROR_SOCKET_TIMEOUT:
			errorCodeText = "ERROR_SOCKET_TIMEOUT";
			break;
		case HttpError.ERROR_UNAUTHORIZED:
			errorCodeText = "ERROR_UNAUTHORIZED";
			break;
		case HttpError.ERROR_UNKNOW:
			errorCodeText = "ERROR_UNKNOW";
			break;
		case HttpError.ERROR_NO_CONNECTION:
			errorCodeText = "ERROR_NO_CONNECTION";
			break;
		case HttpError.ERROR_PARSE:
			errorCodeText = "ERROR_PARSE";
				break;
		default:
			errorCodeText = "ERROR_UNKNOW";
			break;
		}
		return "HttpException [HttpErrorCode = " + mHttpErrorCode +", HttpErrorCodeText = "+errorCodeText+ ", HttpErrorText = "+getHttpErrorText()+"]";
	}

	
	
}
