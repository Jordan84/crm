package com.etalk.crm.controller;

import com.alibaba.fastjson.JSON;
import com.etalk.crm.pojo.FormToken;
import com.etalk.crm.pojo.KnowledgePointMistakes;
import com.etalk.crm.pojo.QuestionBanks;
import com.etalk.crm.pojo.QuestionBanksImg;
import com.etalk.crm.pojo.QuestionDifficultyDegree;
import com.etalk.crm.pojo.QuestionExplain;
import com.etalk.crm.pojo.QuestionGrade;
import com.etalk.crm.pojo.QuestionMode;
import com.etalk.crm.pojo.QuestionOptionAnswer;
import com.etalk.crm.pojo.QuestionTerm;
import com.etalk.crm.pojo.QuestionTypes;
import com.etalk.crm.pojo.Questions;
import com.etalk.crm.service.QuestionService;
import com.etalk.crm.utils.Constants;
import com.etalk.crm.utils.RestResponseDTO;
import com.etalk.crm.utils.RestResponseStates;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static com.etalk.crm.pojo.QuestionBanks.INPIY_TYPE_DEFAULT;

/**
 * @author Terwer
 * @date 2018/04/26
 */
@Api(value = "试题", tags = "试题")
@Controller
@RequestMapping(value = "/question")
public class QuestionController {

    private static final Logger logger = LogManager.getLogger(QuestionController.class);
    @Value("${static.server.url}")
    String testStaticServerUrl;

    @Resource
    private QuestionService questionService;

    /**
     * 题目列表页面
     *
     * @param model
     * @param search   题目
     * @param pageNum  页码
     * @param pageSize 每页展示的数目
     * @return 试题列表
     */
    @ApiOperation(value = "获取试题列表", notes = "根据页码和每页展示的数目来获取试题列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "search", value = "题目", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页展示的数目", required = false, dataType = "Integer")})
    @RequestMapping(value = "/bank/page/list")
    public String getBankList(Model model, String search, Integer pageNum, Integer pageSize) {
        if (search != null) {
            search = search.trim();
        }
        if (pageNum == null) {
            pageNum = Constants.DEFAULT_PAGE_NUM;
        }
        if (pageSize == null) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }
        PageInfo<QuestionBanks> pageInfo = questionService.selectByTitleAndPage(search, pageNum, pageSize);
        if (pageInfo != null) {
            model.addAttribute("pageInfo", pageInfo);
        }
        model.addAttribute("testServerUrl", testStaticServerUrl);
        return "question/question_bank_list";
    }

    @RequestMapping(value = "/bank/list", method = {RequestMethod.POST}, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getBankListData(Model model, String search, Integer pageNum, Integer pageSize) {
        if (search != null) {
            search = search.trim();
        }
        if (pageNum == null) {
            pageNum = Constants.DEFAULT_PAGE_NUM;
        }
        if (pageSize == null) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }
        PageInfo<QuestionBanks> pageInfo = questionService.selectByTitleAndPage(search, pageNum, pageSize);
        if (pageInfo != null) {
            model.addAttribute("pageInfo", pageInfo);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("page", pageNum);
        resultMap.put("total", pageInfo.getTotal());
        resultMap.put("rows", pageInfo.getList());
        return JSON.toJSONString(resultMap);
    }

    /**
     * 题目添加页面
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "添加试题页面", notes = "添加试题页面")
    @FormToken(save = true)
    @RequestMapping(value = "/bank/page/edit", method = RequestMethod.GET)
    public String addBank(Model model) {
        logger.info("添加试题");
        // 初始化页面数据
        initPage(model, 0);
        return "question/question_bank_edit";
    }

    /**
     * 题目修改页面
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "修改试题页面", notes = "修改试题页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "questionId", value = "题目ID", required = false, dataType = "Integer")})
    @FormToken(save = true)
    @RequestMapping(value = "/bank/page/edit/{questionId}", method = RequestMethod.GET)
    public String editBank(Model model, @PathVariable("questionId") Integer questionId, Integer isContinue) {
        logger.info("修改试题，ID为：" + questionId);
        // 初始化页面数据
        initPage(model, questionId);
        model.addAttribute("isContinue", isContinue);
        return "question/question_bank_edit";
    }

    /**
     * 试题相关操作
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "修改试题提交", notes = "修改试题提交")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "Questions", value = "Questions", required = false, dataType = "Questions")})
    @FormToken(remove = true)
    @RequestMapping(value = "/bank/submit", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public RestResponseDTO submit(Model model, Questions formData, HttpSession session) {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        try {
            List<QuestionBanks> questionBanksList = formData.getModels();
            logger.info("接收表单数据：formData=" + JSON.toJSONString(questionBanksList));
            // 批量插入题目
            String recorder = (String) session.getAttribute("loginname");
            Integer lastQuestionId = questionService.batchSaveQuestion(questionBanksList, recorder);
            logger.info("试题处理结果，最后一个题目的ID：lastQuestionId=" + lastQuestionId);
            Map dataMap = new HashMap();
            dataMap.put("lastQuestionId", lastQuestionId);
            restResponseDTO.setData(dataMap);

            // 操作失败
            if (null == lastQuestionId || lastQuestionId == 0) {
                restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
                restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
            } else {
                restResponseDTO.setStatus(RestResponseStates.SUCCESS.getValue());
                restResponseDTO.setMsg(RestResponseStates.SUCCESS.getMsg());
            }
        } catch (Exception e) {
            logger.error("系统异常：", e);
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        }
        return restResponseDTO;
    }

    /**
     * 删除题目
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "删除题目", notes = "删除题目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "questionId", value = "试题ID", required = false, dataType = "Integer"),})
    @FormToken(remove = true)
    @RequestMapping(value = "/bank/delete/{questionId}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public RestResponseDTO deleteBank(Model model, @PathVariable("questionId") Integer questionId) {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        try {
            boolean flag = questionService.deleteByQuestionId(questionId);
            // 操作失败
            if (!flag) {
                restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
                restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
            } else {
                restResponseDTO.setStatus(RestResponseStates.SUCCESS.getValue());
                restResponseDTO.setMsg(RestResponseStates.SUCCESS.getMsg());
            }
        } catch (Exception e) {
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        }
        return restResponseDTO;
    }

    /**
     * 根据年级和知识点来获取试题列表
     *
     * @param qgradeId     年级id
     * @param qknowledgeId 知识点id
     * @return 题目信息
     */
    @ApiOperation(value = "获取试题列表", notes = "根据年级和知识点来获取试题列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "qgradeId", value = "年级", required = false, dataType = "String"),
            @ApiImplicitParam(name = "qknowledgeId", value = "知识点", required = false, dataType = "Integer")})
    @ResponseBody
    @RequestMapping(value = "/bank/list/{qgradeId}/{qknowledgeId}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public RestResponseDTO getBankList(@PathVariable("qgradeId") Integer qgradeId,
                                       @PathVariable("qknowledgeId") Integer qknowledgeId, Integer pageNum, Integer pageSize) {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        try {
            // 获取题目列表信息
            if (pageNum == null) {
                pageNum = Constants.DEFAULT_PAGE_NUM;
            }
            if (pageSize == null) {
                pageSize = Constants.DEFAULT_PAGE_SIZE;
            }
            PageInfo<QuestionBanks> pageInfo = questionService.selectByGradeAndKnowledge(qgradeId, qknowledgeId, pageNum, pageSize);
            restResponseDTO.setData(pageInfo);
            restResponseDTO.setStatus(RestResponseStates.SUCCESS.getValue());
            restResponseDTO.setMsg(RestResponseStates.SUCCESS.getMsg());
        } catch (Exception e) {
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        }
        return restResponseDTO;
    }

    /**
     * 获取题目信息
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "获取题目信息", notes = "获取题目信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "questionId", value = "题目ID", required = false, dataType = "Integer")})
    @RequestMapping(value = "/detail/{questionId}", method = RequestMethod.GET)
    @ResponseBody
    public RestResponseDTO getQuestion(Model model, @PathVariable("questionId") Integer questionId) {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        try {

            logger.info("查询题目信息，ID为：" + questionId);
            QuestionBanks questionBanks = null;
            if (questionId != null && questionId > 0) {
                questionBanks = questionService.selectByQuestionId(questionId);
            }
            restResponseDTO.setData(questionBanks);
            restResponseDTO.setStatus(RestResponseStates.SUCCESS.getValue());
            restResponseDTO.setMsg(RestResponseStates.SUCCESS.getMsg());
        } catch (Exception e) {
            logger.error("题目信息查询异常,error=" + e.getMessage());
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        }
        return restResponseDTO;
    }

    /**
     * 根据题目ID集合字符串查询题目标题
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "根据题目ID集合字符串查询题目标题", notes = "获取题目信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "questionIds", value = "题目ID集合字符串，逗号分隔", required = false, dataType = "String")})
    @RequestMapping(value = "/detailList", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseDTO getQuestions(Model model, String questionIds) {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        try {
            logger.info("查询题目信息，ID集合为：" + questionIds);
            List<Map<String, Object>> questionList = null;
            if (!StringUtils.isEmpty(questionIds)) {
                questionList = questionService.selectQuestionsByIds(questionIds);
            }
            restResponseDTO.setData(questionList);
            restResponseDTO.setStatus(RestResponseStates.SUCCESS.getValue());
            restResponseDTO.setMsg(RestResponseStates.SUCCESS.getMsg());
        } catch (Exception e) {
            logger.error("根据题目ID集合字符串查询题目标题异常,error=" + e.getMessage());
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        }
        return restResponseDTO;
    }

    /**
     * 获取错词本分类下拉数据
     *
     * @param parentId 错词本父分类ID
     * @return
     */
    @ApiOperation(value = "获取错词本分类下拉数据", notes = "获取错词本分类下拉数据")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "parentId", value = "获取错词本分类下拉数据", required = false, dataType = "Integer")
    })
    @RequestMapping(value = "/mistakes/{parentId}", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public RestResponseDTO getMistakeList(@PathVariable("parentId") Integer parentId) {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        try {
            logger.info("获取错词本分类下拉数据开始");
            //错词本分类
            List<KnowledgePointMistakes> listKnowledgePointMistakes = questionService.selectChildKnowledgePointMistakes(parentId);
            logger.info("获取错词本分类下拉数据为，listKnowlwdgePointMistakes：" + JSON.toJSONString(listKnowledgePointMistakes));
            restResponseDTO.setData(listKnowledgePointMistakes);
            restResponseDTO.setStatus(RestResponseStates.SUCCESS.getValue());
            restResponseDTO.setMsg(RestResponseStates.SUCCESS.getMsg());
        } catch (Exception e) {
            logger.error("获取错词本分类下拉数据,error=" + e.getMessage());
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        }
        return restResponseDTO;
    }

    /**
     * 查询所有已选中分类的id
     *
     * @param id 最后一级分类ID
     * @return
     */
    @ApiOperation(value = "查询所有已选中分类的id", notes = "查询所有已选中分类的id")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "最后一级分类ID", required = false, dataType = "Integer")
    })
    @RequestMapping(value = "/mistakes/parent/{id}", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public RestResponseDTO getSelectedKnnowledgePointMistakesId(@PathVariable("id") Integer id) {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        try {
            Stack<Integer> selectedKnnowledgePointMistakesId = questionService.getFirstKnnowledgePointMistakesId(id, null);
            logger.info("selectedKnnowledgePointMistakesId：" + JSON.toJSONString(selectedKnnowledgePointMistakesId));
            restResponseDTO.setData(selectedKnnowledgePointMistakesId);
            restResponseDTO.setStatus(RestResponseStates.SUCCESS.getValue());
            restResponseDTO.setMsg(RestResponseStates.SUCCESS.getMsg());
        } catch (Exception e) {
            logger.error("获取FirstKnnowledgePointMistakesId,error=" + e.getMessage());
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        }
        return restResponseDTO;
    }

    /**
     * 学期列表
     *
     * @return
     */
    @RequestMapping(value = "term/page/list")
    public String getQuestionTerms() {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        try {
            restResponseDTO.setStatus(RestResponseStates.SUCCESS.getValue());
            restResponseDTO.setMsg(RestResponseStates.SUCCESS.getMsg());
        } catch (Exception e) {
            logger.error("获取学期数据错误,error=" + e.getMessage());
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        }
        return "question/question_term_list";
    }

    /**
     * 根据年级获取学期
     *
     * @param model
     * @param gradeId      年级
     * @param parentTermId 父级学期ID
     * @return
     */
    @RequestMapping(value = "/terms", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public RestResponseDTO getTerms(Model model, Integer gradeId, Integer parentTermId) {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        try {
            if (null != gradeId && gradeId > 0) {
                // 根据年级获取学期
                List<QuestionTerm> questionTermList = questionService.selectQuestionTermByGradeId(gradeId);
                logger.info("根据年级获取学期：gradeId=" + gradeId);
                Map dataMap = new HashMap();
                dataMap.put("list", questionTermList);
                restResponseDTO.setData(dataMap);
            } else if (null != parentTermId && parentTermId > 0) {
                // 获取单元、课时
                List<QuestionTerm> questionTermList = questionService.selectQuestionTermByParentTermId(parentTermId);
                logger.info("获取单元、课时:parentTermId=" + parentTermId);
                Map dataMap = new HashMap();
                dataMap.put("list", questionTermList);
                restResponseDTO.setData(dataMap);
            } else {
                Map dataMap = new HashMap();
                dataMap.put("list", null);
                restResponseDTO.setData(dataMap);
            }

            restResponseDTO.setStatus(RestResponseStates.SUCCESS.getValue());
            restResponseDTO.setMsg(RestResponseStates.SUCCESS.getMsg());
        } catch (Exception e) {
            logger.error("获取学期数据错误,error=" + e.getMessage());
            Map dataMap = new HashMap();
            dataMap.put("list", null);
            restResponseDTO.setData(dataMap);
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        }
        return restResponseDTO;
    }

    /**
     * 初始化下拉框
     *
     * @param model
     */
    private void initSelect(Model model) {
        List<QuestionGrade> listQGrade = questionService.selectQGradeList();
        List<QuestionTypes> listQTypes = questionService.selectQTypesList();
        List<QuestionMode> listQMode = questionService.selectQModeList();
        List<QuestionDifficultyDegree> listQDifficultyDegree = questionService.selectQDifficultyDegreeList();
        model.addAttribute("listQGrade", listQGrade);
        model.addAttribute("listQTypes", listQTypes);
        model.addAttribute("listQMode", listQMode);
        model.addAttribute("listQDifficultyDegree", listQDifficultyDegree);
    }

    /**
     * 修改时初始化页面
     *
     * @param model
     * @param questionId
     */
    private void initPage(Model model, Integer questionId) {
        initSelect(model);
        QuestionBanks questionBanks = new QuestionBanks(INPIY_TYPE_DEFAULT);
        if (questionId != null && questionId > 0) {
            questionBanks = questionService.selectByQuestionId(questionId);
            // 修改时初始化学期下拉
            List<QuestionTerm> listQuestionTerm = questionService.selectQuestionTermByGradeId(questionBanks.getQgradeId());
            model.addAttribute("listQuestionTerm", listQuestionTerm);
            List<QuestionTerm> listQuestionUnit = questionService.selectQuestionTermByParentTermId(questionBanks.getQtermId());
            model.addAttribute("listQuestionUnit", listQuestionUnit);
            List<QuestionTerm> listQuestionLessonTime = questionService.selectQuestionTermByParentTermId(questionBanks.getQunitId());
            model.addAttribute("listQuestionLessonTime", listQuestionLessonTime);
        }

        // 修改的时候添加默认模板
        if (CollectionUtils.isEmpty(questionBanks.getListQuestionBanksImg())) {
            QuestionBanksImg defaultQuestionBanksImg = new QuestionBanksImg();
            List<QuestionBanksImg> defaultImages = new ArrayList<>();
            defaultImages.add(defaultQuestionBanksImg);
            questionBanks.setListQuestionBanksImg(defaultImages);
        }

        if (CollectionUtils.isEmpty(questionBanks.getListOptionAnswer())) {
            QuestionOptionAnswer defaultQuestionOptionAnswer = new QuestionOptionAnswer();
            List<QuestionOptionAnswer> defaultOptions = new ArrayList<>();
            defaultOptions.add(defaultQuestionOptionAnswer);
            questionBanks.setListOptionAnswer(defaultOptions);
        }

        if(null==questionBanks.getQuestionExplain()){
            questionBanks.setQuestionExplain(new QuestionExplain());
        }

        model.addAttribute("questionId", questionId);
        model.addAttribute("questionBanks", questionBanks);
    }
}
