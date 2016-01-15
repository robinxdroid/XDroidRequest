package com.xdroid.request.network;

/**
 * HTTP Status
 * 
 * @author Robin
 * @since 2015-11-07 18:34:57
 *
 */
public interface HttpStatus {
	public int SC_ACCEPTED = 202;
	public int SC_BAD_GATEWAY = 502;
	public int SC_BAD_REQUEST = 400;
	public int SC_CONFLICT = 409;
	public int SC_CONTINUE = 100;
	public int SC_CREATED = 201;
	public int SC_EXPECTATION_FAILED = 417;
	public int SC_FAILED_DEPENDENCY = 424;
	public int SC_FORBIDDEN = 403;
	public int SC_GATEWAY_TIMEOUT = 504;
	public int SC_GONE = 410;
	public int SC_HTTP_VERSION_NOT_SUPPORTED = 505;
	public int SC_INSUFFICIENT_SPACE_ON_RESOURCE = 419;
	public int SC_INSUFFICIENT_STORAGE = 507;
	public int SC_INTERNAL_SERVER_ERROR = 500;
	public int SC_LENGTH_REQUIRED = 411;
	public int SC_LOCKED = 423;
	public int SC_METHOD_FAILURE = 420;
	public int SC_METHOD_NOT_ALLOWED = 405;
	public int SC_MOVED_PERMANENTLY = 301;
	public int SC_MOVED_TEMPORARILY = 302;
	public int SC_MULTIPLE_CHOICES = 300;
	public int SC_MULTI_STATUS = 207;
	public int SC_NON_AUTHORITATIVE_INFORMATION = 203;
	public int SC_NOT_ACCEPTABLE = 406;
	public int SC_NOT_FOUND = 404;
	public int SC_NOT_IMPLEMENTED = 501;
	public int SC_NOT_MODIFIED = 304;
	public int SC_NO_CONTENT = 204;
	public int SC_OK = 200;
	public int SC_PARTIAL_CONTENT = 206;
	public int SC_PAYMENT_REQUIRED = 402;
	public int SC_PRECONDITION_FAILED = 412;
	public int SC_PROCESSING = 102;
	public int SC_PROXY_AUTHENTICATION_REQUIRED = 407;
	public int SC_REQUESTED_RANGE_NOT_SATISFIABLE = 416;
	public int SC_REQUEST_TIMEOUT = 408;
	public int SC_REQUEST_TOO_LONG = 413;
	public int SC_REQUEST_URI_TOO_LONG = 414;
	public int SC_RESET_CONTENT = 205;
	public int SC_SEE_OTHER = 303;
	public int SC_SERVICE_UNAVAILABLE = 503;
	public int SC_SWITCHING_PROTOCOLS = 101;
	public int SC_TEMPORARY_REDIRECT = 307;
	public int SC_UNAUTHORIZED = 401;
	public int SC_UNPROCESSABLE_ENTITY = 422;
	public int SC_UNSUPPORTED_MEDIA_TYPE = 415;
	public int SC_USE_PROXY = 305;
}
