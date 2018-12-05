package com.etalk.crm.controller;

import com.etalk.crm.pojo.MobileQuestion;
import com.etalk.crm.service.MobileQuestionService;
import com.etalk.crm.utils.RestResponseDTO;
import com.etalk.crm.utils.RestResponseStates;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @Author Terwer
 * @Date 2018/11/6 15:12
 * @Version 1.0
 * @Description 试题移动端接口
 **/
@Api(value = "试题移动端接口", tags = "试题移动端接口")
@Controller
@RequestMapping(value = "/mobileQuestion")
public class MobileQuestionController {
    private static final Logger logger = LogManager.getLogger(MobileQuestionController.class);

    @Resource
    MobileQuestionService mobileQuestionService;

    /**
     * 获取当前所学课程列表
     *
     * @param model
     * @param tokenString 用户token
     * @return
     */
    @ApiOperation(value = "获取当前所学课程列表", notes = "获取当前所学课程列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "tokenString", value = "用户token", dataType = "String")})
    @RequestMapping(value = "/learingLesson/list", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseDTO getLearingLessonList(Model model, String tokenString, HttpSession session) {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        try {
            logger.info("查询当前所学课程列表，tokenString为：" + tokenString);
            Integer personId = (Integer) session.getAttribute("person.id");
            List<Map> lessonList = mobileQuestionService.getLearingLessonList(personId);
            if (!CollectionUtils.isEmpty(lessonList)) {
                restResponseDTO.setData(lessonList);
                restResponseDTO.setStatus(RestResponseStates.SUCCESS.getValue());
                restResponseDTO.setMsg(RestResponseStates.SUCCESS.getMsg());
            } else {
                restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
                restResponseDTO.setMsg("暂无数据");
            }
        } catch (Exception e) {
            logger.error("当前所学课程列表查询异常,error=" + e.getMessage());
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        }
        return restResponseDTO;
    }

    /**
     * 获取题目信息（包括版本号，级别及对应的题目数目）
     *
     * @param model
     * @param lessonId    课程ID
     * @param knowledgeId 知识点ID（获取单元的时候知识点ID为0）
     * @param unitId      单元ID
     * @param tokenString 用户token
     * @param session
     * @return
     */
    @ApiOperation(value = "获取题目信息（包括版本号，级别及对应的题目数目）", notes = "获取题目信息（包括版本号，级别及对应的题目数目）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lessonId", value = "课程ID", dataType = "Integer"),
            @ApiImplicitParam(name = "knowledgeId", value = "知识点ID（获取单元的时候知识点ID为0）", dataType = "Integer"),
            @ApiImplicitParam(name = "unitId", value = "单元ID", dataType = "Integer"),
            @ApiImplicitParam(name = "tokenString", value = "用户token", dataType = "String")})
    @RequestMapping(value = "/getQuestionInfo", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseDTO getQuestionInfo(Model model, Integer lessonId, Integer knowledgeId, Integer unitId, String tokenString, HttpSession session) {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        try {
            logger.info("获取题目信息，lessonId:" + lessonId + ",tokenString:" + tokenString);
            Integer personId = (Integer) session.getAttribute("person.id");
            Map resultMap = mobileQuestionService.getQuestionInfo(personId, lessonId, knowledgeId, unitId);
            if (null != resultMap) {
                restResponseDTO.setData(resultMap);
                restResponseDTO.setStatus(RestResponseStates.SUCCESS.getValue());
                restResponseDTO.setMsg(RestResponseStates.SUCCESS.getMsg());
            } else {
                restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
                restResponseDTO.setMsg("暂无数据");
            }
        } catch (Exception e) {
            logger.error("获取题目异常,error=" + e.getMessage());
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        }
        return restResponseDTO;
    }

    /**
     * 获取题目
     *
     * @param model
     * @param lessonId    课程ID
     * @param knowledgeId 知识点ID（获取单元的时候知识点ID为0）
     * @param unitId      单元ID
     * @param level       难度级别
     * @param version     版本号
     * @param tokenString 用户token
     * @param session
     * @return
     */
    @ApiOperation(value = "获取题目", notes = "获取题目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lessonId", value = "课程ID", dataType = "Integer"),
            @ApiImplicitParam(name = "knowledgeId", value = "知识点ID（获取单元的时候知识点ID为0）", dataType = "Integer"),
            @ApiImplicitParam(name = "unitId", value = "单元ID", dataType = "Integer"),
            @ApiImplicitParam(name = "level", value = "难度级别", dataType = "Integer"),
            @ApiImplicitParam(name = "version", value = "版本号", dataType = "Integer"),
            @ApiImplicitParam(name = "tokenString", value = "用户token", dataType = "String")})
    @RequestMapping(value = "/currentQuestion", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseDTO getCurrentQuestion(Model model, Integer lessonId, Integer knowledgeId, Integer unitId, Integer level, Integer version, String tokenString, HttpSession session) {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        try {
            logger.info("获取题目，lessonId:" + lessonId + ",level:" + level + ",tokenString:" + tokenString);
            Integer personId = (Integer) session.getAttribute("person.id");
            MobileQuestion mobileQuestion = mobileQuestionService.getCurrentQuestion(personId, lessonId, knowledgeId, unitId, level, version);
            if (null != mobileQuestion) {
                restResponseDTO.setData(mobileQuestion);
                restResponseDTO.setStatus(RestResponseStates.SUCCESS.getValue());
                restResponseDTO.setMsg(RestResponseStates.SUCCESS.getMsg());
            } else {
                restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
                restResponseDTO.setMsg("暂无数据");
            }
        } catch (Exception e) {
            logger.error("获取题目异常,error=" + e.getMessage());
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        }
        return restResponseDTO;
    }

    /**
     * 提交答案
     *
     * @param model
     * @param lessonId    课程ID
     * @param tokenString 用户token
     * @param qrId        题目ID
     * @param qrResult    用户选的答案
     * @param isTrue      是否答对
     * @param version     版本号
     * @return
     */
    @ApiOperation(value = "提交答案", notes = "提交答案")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "lessonId", value = "课程ID", dataType = "Integer"),
            @ApiImplicitParam(name = "knowledgeId", value = "知识点ID（获取单元的时候知识点ID为0）", dataType = "Integer"),
            @ApiImplicitParam(name = "unitId", value = "单元ID", dataType = "Integer"),
            @ApiImplicitParam(name = "tokenString", value = "用户token", dataType = "String"),
            @ApiImplicitParam(name = "qrId", value = "题目ID", dataType = "Integer"),
            @ApiImplicitParam(name = "qrResult", value = "用户选的答案", dataType = "String"),
            @ApiImplicitParam(name = "isTrue", value = "是否答对", dataType = "Integer"),
            @ApiImplicitParam(name = "version", value = "版本号", dataType = "Integer"),
    })
    @RequestMapping(value = "/submitAnswer", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseDTO submitAnswer(Model model, Integer lessonId, Integer knowledgeId, Integer unitId, String tokenString, Integer qrId, String qrResult, Integer isTrue, Integer version, HttpSession session) {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        try {
            logger.info("提交答案，lessonId:" + lessonId + ",tokenString:" + tokenString + ",qrId:" + qrId + ",qrResult:" + qrResult + "isTrue:" + isTrue);
            Integer personId = (Integer) session.getAttribute("person.id");
            boolean flag = mobileQuestionService.submitAnswer(personId, lessonId, knowledgeId, unitId, qrId, qrResult, isTrue, version);
            if (flag) {
                restResponseDTO.setStatus(RestResponseStates.SUCCESS.getValue());
                restResponseDTO.setMsg(RestResponseStates.SUCCESS.getMsg());
            } else {
                restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
                restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
            }
        } catch (Exception e) {
            logger.error("提交答案异常,error=" + e.getMessage());
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        }
        return restResponseDTO;
    }
}
