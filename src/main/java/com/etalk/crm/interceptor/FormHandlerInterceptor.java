package com.etalk.crm.interceptor;

import com.etalk.crm.pojo.FormToken;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 表单防重提交拦截器
 * 场景：表单防重提交
 *
 * @author Terwer
 */
public class FormHandlerInterceptor extends HandlerInterceptorAdapter {

    private static final Logger logger = LogManager.getLogger(HandlerInterceptorAdapter.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            FormToken annotation = method.getAnnotation(FormToken.class);
            if (annotation != null) {
                boolean needSaveSession = annotation.save();
                if (needSaveSession) {
                    logger.info("生成表单token，防止重复提交：" + request.getSession(false).getAttribute("formToken"));
                    request.getSession(false).setAttribute("formToken", UUID.randomUUID().toString());
                }
                boolean needRemoveSession = annotation.remove();
                if (needRemoveSession) {
                    if (isRepeatSubmit(request)) {
                        logger.info("删除表单token：" + request.getSession(false).getAttribute("formToken"));
                        request.getSession(false).removeAttribute("formToken");
                        return false;
                    }
                }
            }
            return true;
        } else {
            return super.preHandle(request, response, handler);
        }
    }

    /**
     * 根据Token校验表单是否重复提交
     *
     * @param request
     * @return
     */
    private boolean isRepeatSubmit(HttpServletRequest request) {
        String serverToken = (String) request.getSession(false).getAttribute("formToken");
        if (serverToken == null) {
            return false;
        }
        String clinetToken = request.getParameter("formToken");
        if (clinetToken == null) {
            return false;
        }
        //如何session中保存到的token和上次相同，则是重复提交表单
        if (serverToken.equals(clinetToken)) {
            return true;
        } else {
            return true;
        }
    }
}

