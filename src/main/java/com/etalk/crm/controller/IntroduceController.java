package com.etalk.crm.controller;

import com.alibaba.fastjson.JSON;
import com.etalk.crm.pojo.IntroduceGift;
import com.etalk.crm.pojo.IntroduceGiftLog;
import com.etalk.crm.service.IntroduceService;
import com.etalk.crm.service.KcPackageService;
import com.etalk.crm.utils.Constants;
import com.etalk.crm.utils.RestResponseDTO;
import com.etalk.crm.utils.RestResponseStates;
import com.github.pagehelper.PageInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Terwer
 * @Date 2018/9/26 11:11
 * @Version 1.0
 * @Description 转介绍
 **/
@Controller
@RequestMapping("/introduce")
public class IntroduceController {
    private static final Logger logger = LogManager.getLogger(IntroduceController.class);

    @Resource
    private IntroduceService introduceService;
    @Resource
    private KcPackageService kcPackageService;

    @RequestMapping("/set/page/list")
    public String listIntroduce(Model model, Integer pageNum, Integer pageSize) {
        if (pageNum == null) {
            pageNum = Constants.DEFAULT_PAGE_NUM;
        }
        if (pageSize == null) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }
        PageInfo<IntroduceGift> pageInfo = introduceService.getIntroduceGiftList(null, pageNum, pageSize);
        if (pageInfo != null) {
            model.addAttribute("pageInfo", pageInfo);
        }
        return "introduce/introduce_list";
    }


    @RequestMapping("/set/page/edit/{id}")
    public String editIntroduce(Model model, @PathVariable Integer id) {
        initPage(model, id);
        return "introduce/introduce_edit";
    }

    @RequestMapping("/set/page/notice/{id}")
    public String sendIntroduceNotice(Model model, @PathVariable Integer id) {
        initPage(model, id);
        return "introduce/introduce_notice";
    }


    @RequestMapping("/log/page/list")
    public String listIntroduceLog(Model model, String search, Integer pageNum, Integer pageSize) {
        if (pageNum == null) {
            pageNum = Constants.DEFAULT_PAGE_NUM;
        }
        if (pageSize == null) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }
        PageInfo<IntroduceGiftLog> pageInfo = introduceService.getIntroduceGiftLogList(search, pageNum, pageSize);
        if (pageInfo != null) {
            model.addAttribute("pageInfo", pageInfo);
        }
        return "introduceGiftLog/introduce_log_list";
    }

    private void initPage(Model model, int id) {
        IntroduceGift introduceGift = new IntroduceGift();
        if (id > 0) {
            introduceGift = introduceService.getIntroduceGiftById(id);
        }
        model.addAttribute("intro", introduceGift);
        model.addAttribute("id", id);
    }

    @RequestMapping("/set/submit/{id}")
    @ResponseBody
    public RestResponseDTO submit(Model model, IntroduceGift intro, @PathVariable Integer id) {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        try {
            logger.info("提交配置信息，intro：" + JSON.toJSONString(intro));

            boolean flag = false;
            if (id > 0) {//修改
                flag = introduceService.updateIntroInfo(intro);

                Map dataMap = new HashMap<>();
                dataMap.put("newId", String.valueOf(id));
                restResponseDTO.setData(dataMap);
            } else {
                Integer newId = introduceService.insertIntroduceGift(intro);
                if (newId > 0) {
                    flag = true;
                }

                Map dataMap = new HashMap<>();
                dataMap.put("newId", newId);
                restResponseDTO.setData(dataMap);
            }
            if (!flag) {
                restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
                restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
            } else {
                restResponseDTO.setStatus(RestResponseStates.SUCCESS.getValue());
                restResponseDTO.setMsg(RestResponseStates.SUCCESS.getMsg());
            }
        } catch (Exception e) {
            logger.error("配置信息保存异常,error=" + e.getMessage());
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        }
        return restResponseDTO;
    }

    @RequestMapping("/set/state/{id}/{state}")
    @ResponseBody
    public RestResponseDTO submit(Model model, @PathVariable Integer id, @PathVariable Integer state) {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        try {
            logger.info("修改状态，state：" + JSON.toJSONString(state));

            boolean flag = introduceService.updateIntroState(state, id);

            if (!flag) {
                restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
                restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
            } else {
                restResponseDTO.setStatus(RestResponseStates.SUCCESS.getValue());
                restResponseDTO.setMsg(RestResponseStates.SUCCESS.getMsg());
            }
        } catch (Exception e) {
            logger.error("修改状态异常,error=" + e.getMessage());
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        }
        return restResponseDTO;
    }

    @RequestMapping("set/selectKcPackege")
    @ResponseBody
    public Map selectKcPackege(HttpServletRequest request, Integer page_num, Integer per_page) {
        List resultList = new ArrayList();
        if (page_num == null) {
            page_num = Constants.DEFAULT_PAGE_NUM;
        }
        if (per_page == null) {
            per_page = Constants.DEFAULT_PAGE_SIZE;
        }
        String q_word = request.getParameter("q_word[]");
        PageInfo<Map> pageInfo = kcPackageService.getPromationKcPackageList(q_word, page_num, per_page);
        if (pageInfo != null) {
            resultList = pageInfo.getList();
        }

        Map resultMap = new HashMap();
        resultMap.put("result", resultList);
        resultMap.put("cnt_whole", pageInfo.getTotal());
        return resultMap;
    }

    @RequestMapping("set/selectNode")
    @ResponseBody
    public Map selectIntroduceNodeList(HttpServletRequest request, Integer page_num, Integer per_page) {
        List resultList = new ArrayList();
        if (page_num == null) {
            page_num = Constants.DEFAULT_PAGE_NUM;
        }
        if (per_page == null) {
            per_page = Constants.DEFAULT_PAGE_SIZE;
        }
        String q_word = request.getParameter("q_word[]");
        PageInfo<Map> pageInfo = introduceService.selectIntroduceNodeList(q_word, page_num, per_page);
        if (pageInfo != null) {
            resultList = pageInfo.getList();
        }

        Map resultMap = new HashMap();
        resultMap.put("result", resultList);
        resultMap.put("cnt_whole", pageInfo.getTotal());
        return resultMap;
    }
}
