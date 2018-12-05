package com.etalk.crm.config;

import com.etalk.crm.interceptor.AuthHandlerInterceptor;
import com.etalk.crm.interceptor.CorsHandlerInterceptor;
import com.etalk.crm.interceptor.FormHandlerInterceptor;
import com.etalk.crm.interceptor.LoginHandlerInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * @author Jordan
 */
@Configuration
public class WebAppConfig extends WebMvcConfigurationSupport {

    /**
     * 添加静态文件路径
     *
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/", "");
        super.addResourceHandlers(registry);
    }

    /**
     * 跨域访问
     *
     * @param registry
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/wechatStuAchievement/**").allowedOrigins("*.etalk365.com", "localhost").allowedMethods(HttpMethod.GET.toString(), HttpMethod.POST.toString()).allowedHeaders("*").allowCredentials(true).maxAge(3600);
        registry.addMapping("/mobileQuestion/**").allowedOrigins("*.etalk365.com", "localhost").allowedMethods(HttpMethod.GET.toString(), HttpMethod.POST.toString()).allowedHeaders("*").allowCredentials(true).maxAge(3600);
        super.addCorsMappings(registry);
    }

    /**
     * 设置默认访问页
     *
     * @param registry ViewControllerRegistry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addRedirectViewController("/", "/index");
        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);
        super.addViewControllers(registry);
    }

    /**
     * 如果拦截器里需要注解bean调用，必须将拦截器作为bean写入配置中
     *
     * @return bean
     */
    @Bean
    public LoginHandlerInterceptor loginHandlerInterceptor() {
        return new LoginHandlerInterceptor();
    }

    /**
     * 表单防重提交
     * 在需要生成token的controller上增加@FormToken(save=true)，而在需要检查重复提交的controller上添加@FormToken(remove=true)
     * 需要在view里在form里增加下面代码：
     * <input type="hidden" name="formToken" value="${formToken}" />
     *
     * @return
     */
    @Bean
    public FormHandlerInterceptor formTokenInterceptor() {
        return new FormHandlerInterceptor();
    }

    /**
     * 接口访问，带token
     *
     * @return
     */
    @Bean
    public AuthHandlerInterceptor authHandlerInterceptor() {
        return new AuthHandlerInterceptor();
    }

    /**
     * 允许跨域访问
     *
     * @return
     */
    @Bean
    public CorsHandlerInterceptor corsHandlerInterceptor() {
        return new CorsHandlerInterceptor();
    }

    /**
     * 配置拦截器
     *
     * @param registry InterceptorRegistry
     * @author Jordan
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginHandlerInterceptor()).addPathPatterns("/**").excludePathPatterns("/swagger-ui.html", "/", "/favicon.ico", "/**/check", "/**/index", "/**/login", "/**/loginOut", "/**/error", "/static/**", "/questionnaireOc/**", "/testPaper/**", "/caseShareManage/**", "/mobileQuestion/**", "/wechatStuAchievement/**");
        registry.addInterceptor(formTokenInterceptor()).addPathPatterns("/**");
        registry.addInterceptor(authHandlerInterceptor()).addPathPatterns("/mobileQuestion/**");
        registry.addInterceptor(corsHandlerInterceptor()).addPathPatterns("/wechatStuAchievement/**", "/testPaper/**", "/mobileQuestion/**", "/error");
        super.addInterceptors(registry);
    }
}
