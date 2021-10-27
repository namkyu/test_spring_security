package com.example.demo.utils;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataOutputStream;
import java.io.IOException;


@Slf4j
public class HttpUtil {

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes sra = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = sra.getRequest();
        return request;
    }

    public static String getRequestIP() {
        HttpServletRequest request = getRequest();
        String ipAddress = request.getHeader("X-FORWARDED-FOR");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }

        return ipAddress;
    }

    public static void response(HttpServletResponse res, String body) {
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(res.getOutputStream());
            byte[] bytes = body.getBytes("UTF-8");
            res.setContentLength(bytes.length);
            res.setStatus(200);
            res.setContentType(MediaType.TEXT_PLAIN_VALUE);
            out.write(bytes);
            out.flush();
        } catch (IOException ex) {
            log.error("##ERROR", ex);
        } finally {
            try {
                out.close();
            } catch (IOException ex) {
                log.error("##ERROR", ex);
            }
        }
    }
}
