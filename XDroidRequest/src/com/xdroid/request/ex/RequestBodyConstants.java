package com.xdroid.request.ex;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * Constants for Request
 * @author Robin
 * @since 2016-01-07 17:01:11
 *
 */
public class RequestBodyConstants {

    public static final String CRLF = "\r\n";
    public static final String HEADER_CONTENT_TYPE = "Content-Type";
    public static final String HEADER_USER_AGENT = "User-Agent";
    public static final String HEADER_CONTENT_DISPOSITION = "Content-Disposition";
    public static final String HEADER_CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding";
    public static final String CONTENT_TYPE_MULTIPART = "multipart/form-data; charset=%s; boundary=%s";
    public static final String CONTENT_TYPE_TEXT = "text/plain";
    public static final String BINARY = "binary";
    public static final String EIGHT_BIT = "8bit";
    public static final String FORM_DATA = "form-data; name=\"%s\"";
    public static final String BOUNDARY_PREFIX = "--";
    public static final String CONTENT_TYPE_OCTET_STREAM = "application/octet-stream";
    public static final String FILENAME = "filename=\"%s\"";
    public static final String COLON_SPACE = ": ";
    public static final String SEMICOLON_SPACE = "; ";

    public static final int CRLF_LENGTH = CRLF.getBytes().length;
    public static final int HEADER_CONTENT_DISPOSITION_LENGTH = HEADER_CONTENT_DISPOSITION.getBytes().length;
    public static final int COLON_SPACE_LENGTH = COLON_SPACE.getBytes().length;
    public static final int HEADER_CONTENT_TYPE_LENGTH = HEADER_CONTENT_TYPE.getBytes().length;
    public static final int CONTENT_TYPE_OCTET_STREAM_LENGTH = CONTENT_TYPE_OCTET_STREAM.getBytes().length;
    public static final int HEADER_CONTENT_TRANSFER_ENCODING_LENGTH = HEADER_CONTENT_TRANSFER_ENCODING.getBytes().length;
    public static final int BINARY_LENGTH = BINARY.getBytes().length;
    public static final int BOUNDARY_PREFIX_LENGTH = BOUNDARY_PREFIX.getBytes().length;
    
    public static final Charset ASCII = Charset.forName("US-ASCII");

    public static final byte[] CRLF_BYTES = getAsciiBytes(CRLF);

    /*public static int getContentLengthForMultipartRequest(String boundary, Map<String, MultiPartRequest.MultiPartValue> multipartParams, Map<String, String> filesToUpload) {
        final int boundaryLength = boundary.getBytes().length;
        int contentLength = 0;
        for (String key : multipartParams.keySet()) {
            MultiPartRequest.MultiPartValue param = multipartParams.get(key);
            int size = boundaryLength +
                    CRLF_LENGTH + HEADER_CONTENT_DISPOSITION_LENGTH + COLON_SPACE_LENGTH + String.format(FORM_DATA, key).getBytes().length +
                    CRLF_LENGTH + HEADER_CONTENT_TYPE_LENGTH + COLON_SPACE_LENGTH + param.contentType.getBytes().length +
                    CRLF_LENGTH + CRLF_LENGTH + param.value.getBytes().length + CRLF_LENGTH;

            contentLength += size;
        }

        for (String key : filesToUpload.keySet()) {
            File file = new File(filesToUpload.get(key));
            int size = boundaryLength +
                    CRLF_LENGTH + HEADER_CONTENT_DISPOSITION_LENGTH + COLON_SPACE_LENGTH + String.format(FORM_DATA + SEMICOLON_SPACE + FILENAME, key, file.getName()).getBytes().length +
                    CRLF_LENGTH + HEADER_CONTENT_TYPE_LENGTH + COLON_SPACE_LENGTH + CONTENT_TYPE_OCTET_STREAM_LENGTH +
                    CRLF_LENGTH + HEADER_CONTENT_TRANSFER_ENCODING_LENGTH + COLON_SPACE_LENGTH + BINARY_LENGTH + CRLF_LENGTH + CRLF_LENGTH;

            size += (int) file.length();
            size += CRLF_LENGTH;
            contentLength += size;
        }

        int size = boundaryLength + BOUNDARY_PREFIX_LENGTH + CRLF_LENGTH;
        contentLength += size;
        return contentLength;
    }*/
    
    public static int getContentLength(String boundary, Map<?, ?> requestParams) {
        final int boundaryLength = boundary.getBytes().length;
        int contentLength = 0;
        for (Object key : requestParams.keySet()) {
            Object value = requestParams.get(key);
            if (value instanceof File) {
				File fileValue = (File) value;
				 int size = boundaryLength +
		                    CRLF_LENGTH + HEADER_CONTENT_DISPOSITION_LENGTH + COLON_SPACE_LENGTH + String.format(FORM_DATA + SEMICOLON_SPACE + FILENAME, key, fileValue.getName()).getBytes().length +
		                    CRLF_LENGTH + HEADER_CONTENT_TYPE_LENGTH + COLON_SPACE_LENGTH + CONTENT_TYPE_OCTET_STREAM_LENGTH +
		                    CRLF_LENGTH + HEADER_CONTENT_TRANSFER_ENCODING_LENGTH + COLON_SPACE_LENGTH + BINARY_LENGTH + CRLF_LENGTH + CRLF_LENGTH;

		            size += (int) fileValue.length();
		            size += CRLF_LENGTH;
		            contentLength += size;
			}else {
				String stringValue = "";
				if (value instanceof String) {
					stringValue = (String) value;
				}else if (value instanceof Integer) {
					stringValue = (Integer) value +"";
				}
				   int size = boundaryLength +
		                    CRLF_LENGTH + HEADER_CONTENT_DISPOSITION_LENGTH + COLON_SPACE_LENGTH + String.format(FORM_DATA, key).getBytes().length +
		                    CRLF_LENGTH + HEADER_CONTENT_TYPE_LENGTH + COLON_SPACE_LENGTH + 
//		                    param.contentType.getBytes().length +
		                    CRLF_LENGTH + CRLF_LENGTH + stringValue.getBytes().length + CRLF_LENGTH;

		            contentLength += size;
			}
        }

        int size = boundaryLength + BOUNDARY_PREFIX_LENGTH + CRLF_LENGTH;
        contentLength += size;
        return contentLength;
    }
    
    public static byte[] getAsciiBytes(final String data) {
        notNull(data, "Input");
        return data.getBytes(ASCII);
    }
    
    private static <T> T notNull(final T argument, final String name) {
        if (argument == null) {
            throw new IllegalArgumentException(name + " may not be null");
        }
        return argument;
    }


}
