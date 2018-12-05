package com.etalk.crm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

/**
 * @author Terwer
 */
@Controller
public class ApiDocController {

    @ApiIgnore
    @RequestMapping("/swagger-ui")
    public String index(Model model, HttpServletRequest request) {
        return "swagger/index";
    }
}
