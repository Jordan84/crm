package com.etalk.crm.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.etalk.crm.service.UserService;
import com.etalk.crm.utils.EncryptAndDecrypt;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.json.JsonParseException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jordan
 */
@Controller
public class LoginController {
    protected static final Logger logger= LogManager.getLogger(LoginController.class);
    @Resource
    private UserService userService;

    @Value("${web.crm.old.url}")
    String oldCrmUrl;

    @ResponseBody
    @RequestMapping("/check")
    public String check(){
        return "测试服务可用性成功";
    }
    @RequestMapping("/index")
    public String index(Model model,HttpServletRequest request,HttpServletResponse response){
        model.addAttribute("oldCrmUrl",oldCrmUrl);
        Cookie[] cookie=request.getCookies();
        if (cookie!=null) {
            //获取cookie中保存的用户名和密码
            for (Cookie aCookie : cookie) {
                if (aCookie.getName().equalsIgnoreCase(EncryptAndDecrypt.MD5("loginCrmUser"))) {
                    try {
	                    JSONObject map = JSON.parseObject(EncryptAndDecrypt.decrypt(aCookie.getValue()));
	                    if (map != null && !StringUtils.isEmpty(map.getString("loginName"))) {
		                    model.addAttribute("loginName", map.getString("loginName"));
		                    model.addAttribute("loginPwd", map.getString("loginPwd"));
		                    model.addAttribute("remember", 1);
		                    break;
	                    }
                    }catch (JsonParseException e){
	                    if (aCookie.getName().equals(EncryptAndDecrypt.MD5("loginCrmUser"))) {
		                    //设置cookie有效时间为0
		                    aCookie.setMaxAge(0);
		                    //不设置存储路径
		                    aCookie.setPath("/");
		                    response.addCookie(aCookie);
		                    break;
	                    }
                    }
                }
            }
        }
        return "index";
    }

    @RequestMapping("/login")
    public String login(Model model, @RequestParam String loginName, @RequestParam String loginPwd, @RequestParam String vcode,Integer remember,HttpServletRequest request,HttpServletResponse response){
        /*model.addAttribute("oldCrmUrl",oldCrmUrl);*/
        //验证码验证正确性
        if(this.checkValue(vcode, request.getSession())) {
            if (userService.login(loginName, loginPwd, request.getSession())) {
                if (!StringUtils.isEmpty(remember) && remember==1){
                    Map<String,Object> map=new HashMap<>(2);
                    map.put("loginName",loginName);
                    map.put("loginPwd",loginPwd);
                    String cookieValue=EncryptAndDecrypt.encrypt(JSON.toJSONString(map));
                    this.setCookie(response,EncryptAndDecrypt.MD5("loginCrmUser"),cookieValue);
                }else {
                    this.deleteCookieByName(request,response,EncryptAndDecrypt.MD5("loginCrmUser"));
                }
                return "redirect:/main";
            }else {
                model.addAttribute("message", "用户名或密码错误");
            }
        }else {
            model.addAttribute("message", "验证码错误");
        }
        return "index";
    }

    @RequestMapping("/loginOut")
    public String loginOut(Model model,HttpServletRequest request,HttpSession session,HttpServletResponse response){
        session.invalidate();
//        model.addAttribute("oldCrmUrl",oldCrmUrl);
        return this.index(model,request,response);
    }

    /**
     * 检测验证码是否正确
     *  返回：booblean(验证码正确 true 否则false)
     **/
    private Boolean checkValue(String vcode,HttpSession session){

        Boolean bl=false;
        String code=session.getAttribute("vcode")==null?"":session.getAttribute("vcode").toString();
        logger.debug("----------------提交的验证码----------------:"+vcode);
        logger.debug("----------------session的验证码----------------:"+code);
        if(!StringUtils.isEmpty(code) && !StringUtils.isEmpty(vcode) && code.equals(vcode)){
            bl=true;
        }

        return bl;
    }

    /**
     * 保存Cookies
     *
     * @param response
     *            servlet请求
     * @param value
     *            保存值
     */
    private void setCookie(HttpServletResponse response, String name, String value) {
        // new一个Cookie对象,键值对为参数
        Cookie cookie = new Cookie(name, value);
        // tomcat下多应用共享
        cookie.setPath("/");
        // 如果cookie的值中含有中文时，需要对cookie进行编码，不然会产生乱码
        try {
            URLEncoder.encode(value, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        cookie.setMaxAge(7*24*60*60);
        // 将Cookie添加到Response中,使之生效// addCookie后，如果已经存在相同名字的cookie，则最新的覆盖旧的cookie
        response.addCookie(cookie);
        //return response;
    }

    /**
     * 删除无效cookie
     * @param request request
     * @param response response
     * @param deleteKey 需要删除的key
     */
    private void deleteCookieByName(HttpServletRequest request, HttpServletResponse response,String deleteKey){
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(deleteKey)) {
                //设置cookie有效时间为0
                cookie.setMaxAge(0);
                //不设置存储路径
                cookie.setPath("/");
                response.addCookie(cookie);
                break;
            }
        }
    }
}
