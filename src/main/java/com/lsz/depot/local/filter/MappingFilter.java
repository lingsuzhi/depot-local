package com.lsz.depot.local.filter;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lsz.depot.local.util.GetRequestJsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class MappingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain
            filterChain) throws ServletException, IOException {
//
        String uri = request.getRequestURI();
        CachedResponse targetResponse = CachedResponse.wrap(response);
        String parame = JSONObject.toJSONString(request.getParameterMap(),false);//post 不好拿  算了
        log.info("###### 请求方式：{}    路径:{}    参数:{}", request.getMethod(), uri, parame);


        long startMills = System.currentTimeMillis();

        WrappedHttpRequest wrappedHttpRequest = new WrappedHttpRequest(request);
        wrappedHttpRequest.setRequestURI(uri);
        filterChain.doFilter(wrappedHttpRequest, targetResponse);

        if (targetResponse != null) {
            targetResponse.process(s -> {
                log.info("###### ZUUL Cost[{}ms]  返回：{}", System.currentTimeMillis() - startMills, s.replace("\n",""));
                return s;
            });

        }


    }
}
