package com.xdroid.request.network;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Map;

/**
 * Compatible with 6.0, is used to replace "org.apache.http.HttpResponse"
 * @author Robin
 * @since 2016-1-14 18:55:51
 *
 */
public class HttpResponse implements Serializable {

    private static final long serialVersionUID = 1L;

    private Map<String, String> headers;

    private int responseCode;

    private String responseMessage;

    private InputStream contentStream;

    private String contentEncoding;

    private String contentType;

    private long contentLength;

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(int responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public InputStream getContentStream() {
        return contentStream;
    }

    public void setContentStream(InputStream contentStream) {
        this.contentStream = contentStream;
    }

    public String getContentEncoding() {
        return contentEncoding;
    }

    public void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }
}
