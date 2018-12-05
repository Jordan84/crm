package com.etalk.crm.controller;

import com.etalk.crm.service.MenuInfoService;
import com.etalk.crm.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

/**
 * @author Jordan
 */
@Controller
public class MainController {
    protected static final Logger logger= LogManager.getLogger(MainController.class);
    @Resource
    private MenuInfoService menuInfoService;
    @Resource
    private UserService userService;

    @Value("${web.crm.old.url}")
    String oldCrmUrl;
    /**
     * 获取第一级菜单栏
     * @param model 返回数据对象
     * @param session HttpSession
     * @return 主页
     */
    @RequestMapping("/main")
    public String main(Model model, HttpSession session){
        model.addAttribute("oldCrmUrl",oldCrmUrl);
        model.addAttribute("mainMenu",menuInfoService.searchMenuFirstByUserId(session));
        return "main";
    }

    /**
     * 修改密码
     * @param model 数据
     * @param session session
     * @param oldPwd 旧密码
     * @param newPwd 新密码
     * @return 结果
     */
    @RequestMapping(value = "/reset/password",method = {RequestMethod.POST,RequestMethod.GET})
    public String resetPassword(Model model, HttpSession session, String oldPwd, String newPwd){
        if (!StringUtils.isEmpty(oldPwd) && !StringUtils.isEmpty(newPwd)) {
            if (newPwd.length() < 6 || newPwd.length() > 20) {
                model.addAttribute("status", 0);
                model.addAttribute("errMsg", "新密码字符6-20个，请重新设置！");
            } else {
                if (session != null && !StringUtils.isEmpty(session.getAttribute("userid"))) {
                    int num = userService.modifyPassword(Integer.parseInt(session.getAttribute("userid").toString()), oldPwd, newPwd);
                    if (num > 0) {
                        model.addAttribute("status", num);
                    } else {
                        model.addAttribute("status", 0);
                        model.addAttribute("errMsg", "原始密码错误请重试！");
                    }
                } else {
                    model.addAttribute("status", 0);
                    model.addAttribute("errMsg", "登录失效，请重新登录后重试！");
                }
            }
        }
        return "setup/repassword";
    }
}
