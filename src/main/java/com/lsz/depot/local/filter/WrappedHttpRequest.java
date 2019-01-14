package com.lsz.depot.local.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class WrappedHttpRequest extends HttpServletRequestWrapper {
    private String uri;

    public WrappedHttpRequest(HttpServletRequest request) {
        super(request);
    }

    public String getRequestURI() {
        return uri;
    }

    public void setRequestURI(String u) {
        uri = u;
    }

    public String getRealURI() {
        return super.getRequestURI();
    }

    public String getServletPath() {
        return uri;
    }

    public StringBuffer getRequestURL() {
        return new StringBuffer(uri);
    }

}
