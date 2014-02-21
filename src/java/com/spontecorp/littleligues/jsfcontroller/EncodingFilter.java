package com.spontecorp.littleligues.jsfcontroller;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

public class EncodingFilter implements Filter {

    private String encoding;
    private FilterConfig filterConfig;

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig fc) throws ServletException {
        this.filterConfig = fc;
        this.encoding = filterConfig.getInitParameter("encoding");
    }

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
     * javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
            FilterChain chain) throws IOException, ServletException {
        if(req.getCharacterEncoding() == null){
            req.setCharacterEncoding(encoding);
        }
        chain.doFilter(req, resp);
    }

    /**
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
    }
}
