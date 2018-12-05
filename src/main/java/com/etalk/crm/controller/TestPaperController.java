package com.etalk.crm.controller;

import com.alibaba.fastjson.JSON;
import com.etalk.crm.pojo.FormToken;
import com.etalk.crm.pojo.PaperInputAnswer;
import com.etalk.crm.pojo.Person;
import com.etalk.crm.pojo.QuestionBanks;
import com.etalk.crm.pojo.QuestionDifficultyDegree;
import com.etalk.crm.pojo.QuestionGrade;
import com.etalk.crm.pojo.QuestionItem;
import com.etalk.crm.pojo.StudentPaper;
import com.etalk.crm.pojo.TestPaper;
import com.etalk.crm.pojo.TestPaperType;
import com.etalk.crm.service.QuestionService;
import com.etalk.crm.service.TestPaperService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Terwer
 * @date 2018/04/28
 */
@Api(value = "试卷", tags = "试卷")
@Controller
@RequestMapping(value = "/testPaper")
public class TestPaperController {

    private static final Logger logger = LogManager.getLogger(TestPaperController.class);
    @Value("${static.server.url}")
    private String testStaticServerUrl;
    @Resource
    private TestPaperService testPaperService;
    @Resource
    private QuestionService questionService;

    /**
     * 试卷列表页面
     *
     * @param model    Model
     * @param search   题目
     * @param pageNum  页码
     * @param pageSize 每页展示的数目
     * @return 试卷列表
     */
    @ApiOperation(value = "获取试卷列表", notes = "根据页码和每页展示的数目来获取试卷列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "题目", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页展示的数目", required = false, dataType = "Integer")
    })
    @RequestMapping(value = "/page/list")
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
        PageInfo<TestPaper> pageInfo = testPaperService.selectByTestPaperNameAndPage(search, pageSize, pageNum);
        if (pageInfo != null) {
            model.addAttribute("pageInfo", pageInfo);
        }
        model.addAttribute("testServerUrl", testStaticServerUrl);
        return "testPaper/test_paper_list";
    }

    /**
     * 添加试卷页面
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "添加试卷页面", notes = "添加试卷页面")
    @FormToken(save = true)
    @RequestMapping(value = "/page/add", method = RequestMethod.GET)
    public String add(Model model) {
        //初始化下拉框
        initSelect(model);
        // 初始化页面数据
        initPage(model, 0);
        return "testPaper/test_paper_add";
    }

    /**
     * 修改试卷页面
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "修改试卷页面", notes = "修改试卷页面")
    @FormToken(save = true)
    @RequestMapping(value = "/page/edit/{paperId}", method = RequestMethod.GET)
    public String edit(Model model, @PathVariable("paperId") Integer paperId) {
        //初始化下拉框
        initSelect(model);
        // 初始化页面数据
        initPage(model, paperId);
        return "testPaper/test_paper_add";
    }

    /**
     * 从题库选择题目
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "从题库选择题目", notes = "根据页码和每页展示的数目来获取试卷列表")
    @RequestMapping(value = "/page/chooseQuestion", method = RequestMethod.GET)
    public String chooseQuestion(Model model) {
        model.addAttribute("testServerUrl", testStaticServerUrl);
        return "testPaper/test_paper_choose_question";
    }

    /**
     * 选择要发送试卷的学生
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "选择要发送试卷的学生", notes = "选择要发送试卷的学生")
    @RequestMapping(value = "/page/chooseStudent", method = RequestMethod.GET)
    public String chooseStudent(Model model, Integer testPaperId) {
        model.addAttribute("testPaperId", testPaperId);
        model.addAttribute("testServerUrl", testStaticServerUrl);
        return "testPaper/test_paper_choose_student";
    }

    /**
     * @return
     */
    @ApiOperation(value = "链接分享", notes = "试卷链接分享")
    @RequestMapping(value = "/page/sendPaper", method = RequestMethod.GET)
    public String sendPaper(Model model) {
        return "testPaper/test_paper_send";
    }

    /**
     * 学生列表
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "学生列表", notes = "学生列表")
    @RequestMapping(value = "/student/list", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getStudentList(Model model, String search, Integer pageNum, Integer pageSize) {
        if (search != null) {
            search = search.trim();
        }
        if (pageNum == null) {
            pageNum = Constants.DEFAULT_PAGE_NUM;
        }
        if (pageSize == null) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }
        PageInfo<Person> pageInfo = testPaperService.getStudentList(search, pageNum, pageSize);
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
     * 根据页码和每页展示的数目来获取学生做过的试卷列表
     *
     * @param model
     * @param search   题目
     * @param pageNum  页码
     * @param pageSize 每页展示的数目
     * @return 学生做过的试卷列表
     */
    @ApiOperation(value = "获取学生做过的试卷列表", notes = "根据页码和每页展示的数目来获取学生做过的试卷列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "search", value = "题目", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页展示的数目", required = false, dataType = "Integer")
    })
    @RequestMapping("/person/page/list")
    public String getPersonPaper(Model model, String search, Integer pageNum, Integer pageSize) {
        if (search != null) {
            search = search.trim();
        }
        if (pageNum == null) {
            pageNum = Constants.DEFAULT_PAGE_NUM;
        }
        if (pageSize == null) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }
        PageInfo<StudentPaper> pageInfo = testPaperService.selectStudentAndPaper(search, pageNum, pageSize);
        if (pageInfo != null) {
            model.addAttribute("pageInfo", pageInfo);
            logger.info("获取到的学生以及试卷的信息：+" + JSON.toJSONString(pageInfo));
        }
        model.addAttribute("testServerUrl", testStaticServerUrl);
        return "testPaper/test_paper_person_list";
    }

    /**
     * 根据试卷标题查询试卷列表
     *
     * @param testPaperName 试卷标题
     * @return 试卷信息
     */
    @ApiOperation(value = "获取试卷列表", notes = "根据试卷标题查询试卷列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "testPaperName", value = "试卷标题", required = false, dataType = "String")
    })
    @ResponseBody
    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    public RestResponseDTO getTestPaperList(String testPaperName) {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        try {
            //获取试卷信息
            PageInfo<TestPaper> testPapers = testPaperService.selectByTestPaperNameAndPage(testPaperName, 1, 10);
            restResponseDTO.setData(testPapers);
            restResponseDTO.setStatus(RestResponseStates.SUCCESS.getValue());
            restResponseDTO.setMsg(RestResponseStates.SUCCESS.getMsg());
        } catch (Exception e) {
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        }
        return restResponseDTO;
    }

    /**
     * 获取试卷信息（预览和发给学生的共用）
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "获取试卷信息", notes = "获取试卷信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "testPaperId", value = "题目ID", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "questionId", value = "试卷ID", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "scoreId", value = "分数ID", required = false, dataType = "Integer")
    })
    @RequestMapping(value = "/detail/{testPaperId}/{questionId}/{personId}/{scoreId}", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseDTO getTestPaperDetail(@PathVariable("testPaperId") Integer testPaperId, @PathVariable("questionId") Integer questionId, @PathVariable("personId") Integer personId, @PathVariable("scoreId") Integer scoreId, Integer isAdmin) {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        try {
            logger.info("查询试卷信息，试卷ID为：" + testPaperId);
            TestPaper testPaper = null;
            if (questionId != null & questionId > 0) {
                testPaper = new TestPaper();
                QuestionItem questionItem = new QuestionItem();
                QuestionBanks questionBanks = questionService.selectByQuestionId(questionId);
                List<QuestionBanks> questionBanksList = new ArrayList<>();
                questionBanksList.add(questionBanks);
                questionItem.setListQuestionBanks(questionBanksList);

                List<QuestionItem> questionItemList = new ArrayList<>();
                questionItemList.add(questionItem);
                testPaper.setListQuestionItems(questionItemList);
            } else if (testPaperId != null && testPaperId > 0) {
                if (scoreId != null && scoreId > 0) {
                    //将该用户最后一次做题答案显示出来
                    testPaper = testPaperService.selectByTestPaperIdAndScoreId(testPaperId, scoreId);
                } else {
                    //只显示题目和选项，不显示学生做的答案
                    testPaper = testPaperService.selectByTestPaperId(testPaperId);
                }
            }
            restResponseDTO.setData(testPaper);
            restResponseDTO.setStatus(RestResponseStates.SUCCESS.getValue());
            restResponseDTO.setMsg(RestResponseStates.SUCCESS.getMsg());
        } catch (Exception e) {
            logger.error("试卷信息查询异常,error=" + e.getMessage());
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        }
        return restResponseDTO;
    }

    /**
     * 学生提交试卷给老师
     * @param inputAnswer 学生的答案
     * @return
     */
    @ApiOperation(value = "学生提交试卷给老师", notes = "学生提交试卷给老师")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "PaperInputAnswer", value = "PaperInputAnswer", required = false, dataType = "PaperInputAnswer")
    })
    @RequestMapping(value = "/handinPaper", method = {RequestMethod.POST, RequestMethod.OPTIONS})
    @ResponseBody
    public RestResponseDTO handinPaper(@RequestBody PaperInputAnswer inputAnswer) {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        boolean flag = false;
        try {
            //检测试卷状态，只有未做才允许提交
            Integer scoreId = inputAnswer.getScoreId();
            if (!testPaperService.checkPaperCanHandin(scoreId)) {
                restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
                restResponseDTO.setMsg("试卷已提交，无法再次提交");
                return restResponseDTO;
            }
            logger.info("提交试卷给老师：入参=" + JSON.toJSONString(inputAnswer));
            flag = testPaperService.handinPaper(inputAnswer);
        } catch (Exception e) {
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        }
        //操作失败
        if (!flag) {
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        } else {
            restResponseDTO.setStatus(RestResponseStates.SUCCESS.getValue());
            restResponseDTO.setMsg(RestResponseStates.SUCCESS.getMsg());
        }
        restResponseDTO.setData(null);
        return restResponseDTO;
    }

    @ApiOperation(value = "试卷批改", notes = "试卷批改")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "scoreId", value = "分数ID", required = false, dataType = "int"),
            @ApiImplicitParam(name = "qbankId", value = "题目ID", required = false, dataType = "int"),
            @ApiImplicitParam(name = "score", value = "分数", required = false, dataType = "int")
    })
    @RequestMapping(value = "/correctQuestionScore/{scoreId}/{qbankId}/{score}", method = RequestMethod.POST)
    @ResponseBody
    public RestResponseDTO correctQuestionScore(@PathVariable("scoreId") Integer scoreId, @PathVariable("qbankId") Integer qbankId, @PathVariable("score") Integer score) {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        boolean flag = false;
        try {
            logger.info("提交试卷给老师：入参qbankId=" + qbankId + " score=" + score);
            flag = testPaperService.correctPaper(scoreId, qbankId, score);
        } catch (Exception e) {
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        }
        //操作失败
        if (!flag) {
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        } else {
            restResponseDTO.setStatus(RestResponseStates.SUCCESS.getValue());
            restResponseDTO.setMsg(RestResponseStates.SUCCESS.getMsg());
        }
        restResponseDTO.setData(null);
        return restResponseDTO;
    }

    /**
     * 试卷相关操作
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "生成试卷提交", notes = "生成试卷提交")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "TestPaper", value = "TestPaper", required = false, dataType = "TestPaper")
    })
    @FormToken(remove = true)
    @RequestMapping(value = "/submit", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public RestResponseDTO submit(TestPaper formData) {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        boolean flag = false;
        try {
            logger.info("生成或者修改试卷：入参=" + JSON.toJSONString(formData));
            flag = testPaperService.saveTestPaper(formData);
        } catch (Exception e) {
            logger.error("系统异常" + e.getLocalizedMessage(), e);
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        }
        //操作失败
        if (!flag) {
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        } else {
            restResponseDTO.setStatus(RestResponseStates.SUCCESS.getValue());
            restResponseDTO.setMsg(RestResponseStates.SUCCESS.getMsg());
        }
        restResponseDTO.setData(null);
        return restResponseDTO;
    }

    @RequestMapping(value = "/sendPaperToStudent", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public RestResponseDTO sendPaperToStudent(Integer testPaperId, Integer personId) {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        boolean flag = false;
        try {
            logger.info("开始生成试卷：入参=testPaperId->" + testPaperId + "studentId->" + personId);
            boolean isEnableToSend = testPaperService.checkTestState(testPaperId, personId);
            logger.info("检查是否可以做试卷结果：isEnableToSend=" + isEnableToSend);
            if (!isEnableToSend) {//不允许再次测试
                restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
                restResponseDTO.setMsg("该用户上次对本试卷的测评还未完成，不允许测试本试卷，请先在试卷批改完成该试卷");
                return restResponseDTO;
            } else {
                Integer scoreId = testPaperService.sendPaperToStudent(testPaperId, personId);
                if (scoreId > 0) {
                    flag = true;
                    Map dataMap = new HashMap();
                    dataMap.put("scoreId", scoreId);
                    restResponseDTO.setData(dataMap);
                }
            }
        } catch (Exception e) {
            logger.error(e);
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        }
        //操作失败
        if (!flag) {
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg("试卷发送失败，请重试");
        } else {
            restResponseDTO.setStatus(RestResponseStates.SUCCESS.getValue());
            restResponseDTO.setMsg(RestResponseStates.SUCCESS.getMsg());
        }
        return restResponseDTO;
    }

    /**
     * 删除试卷
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "删除试卷", notes = "删除试卷")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "testPaperId", value = "试卷ID", required = false, dataType = "Integer"),
    })
    @FormToken(remove = true)
    @RequestMapping(value = "/delete/{testPaperId}", method = RequestMethod.GET, produces = "application/json;charset=utf-8")
    @ResponseBody
    public RestResponseDTO deletePaper(Model model, @PathVariable("testPaperId") Integer testPaperId) {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        boolean flag = false;
        try {
            flag = testPaperService.deleteTestPaper(testPaperId);
            //操作失败
            if (!flag) {
                restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
                restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
            } else {
                restResponseDTO.setStatus(RestResponseStates.SUCCESS.getValue());
                restResponseDTO.setMsg(RestResponseStates.SUCCESS.getMsg());
            }
        } catch (Exception e) {
            logger.error(e);
            restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
            restResponseDTO.setMsg(RestResponseStates.SERVER_ERROR.getMsg());
        }
        restResponseDTO.setData(null);
        return restResponseDTO;
    }

    /**
     * 初始化下拉框
     *
     * @param model
     */
    private void initSelect(Model model) {
        List<TestPaperType> listPaperType = testPaperService.selectPaperTypeList();
        List<QuestionDifficultyDegree> listQDifficultyDegree = questionService.selectQDifficultyDegreeList();
        List<QuestionGrade> listQGrade = questionService.selectQGradeList();
        model.addAttribute("listPaperType", listPaperType);
        model.addAttribute("listQDifficultyDegree", listQDifficultyDegree);
        model.addAttribute("listQGrade", listQGrade);
    }

    /**
     * 初始化页面数据
     *
     * @param model
     * @param paperId
     */
    private void initPage(Model model, Integer paperId) {
        TestPaper testPaper = null;
        if (paperId != null && paperId > 0) {
            try {
                testPaper = testPaperService.selectByTestPaperId(paperId);
            } catch (Exception e) {
                logger.error("试卷信息查询失败");
            }
        } else {
            testPaper = new TestPaper();
        }
        model.addAttribute("paper", testPaper);
        model.addAttribute("paperId", paperId);
    }
}
