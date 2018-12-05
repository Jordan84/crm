package com.etalk.crm.interceptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


/** 
 * 登陆拦截器 
 * 场景：判断用户是否登陆？ 
 * 登陆，则不拦截，没登陆，则转到登陆界面； 
 * @author Jordan 
 *
 */  
public class LoginHandlerInterceptor implements HandlerInterceptor {
	private static final Logger logger= LogManager.getLogger(LoginHandlerInterceptor.class);

    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object arg2, Exception arg3) {
    }  
  
    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
                           Object arg2, ModelAndView arg3) {
  
    }  
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object arg2) throws IOException {
		logger.info(request.getRequestURI());
		// 说明处在编辑的页面
        HttpSession session = request.getSession();
        Integer loginId = (Integer) session.getAttribute("userid");
        if (loginId == null) {
            // 没有登陆，转向登陆界面
            //request.getRequestDispatcher("/WEB-INF/index.jsp").forward(request, response);
            //System.out.println("没有登陆，转向登陆界面:"+request.getContextPath());
            response.sendRedirect(StringUtils.isEmpty(request.getContextPath())?"/":request.getContextPath());

            return false;
        }
		return true;

    }
  
}  
