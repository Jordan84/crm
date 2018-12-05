package com.etalk.crm.controller;

import com.alibaba.fastjson.JSON;
import com.etalk.crm.pojo.FormToken;
import com.etalk.crm.pojo.QuestionBanks;
import com.etalk.crm.pojo.QuestionKnowledge;
import com.etalk.crm.pojo.TextbookesCategory;
import com.etalk.crm.pojo.Textbooks;
import com.etalk.crm.service.QuestionService;
import com.etalk.crm.service.TextbooksService;
import com.etalk.crm.utils.Constants;
import com.etalk.crm.utils.ExcelImportUtils;
import com.etalk.crm.utils.RestResponseDTO;
import com.etalk.crm.utils.RestResponseStates;
import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Terwer
 * @Date 2018/10/17 18:25
 * @Version 1.0
 * @Description 知识点
 **/
@Api(value = "知识点", tags = "知识点")
@Controller
@RequestMapping(value = "/questionKnowledge")
public class QuestionKnowledgeController {
    private static final Logger logger = LogManager.getLogger(QuestionKnowledgeController.class);

    @Resource
    private QuestionService questionService;
    @Resource
    private TextbooksService textbooksService;

    /**
     * 知识点列表页面
     *
     * @param model
     * @param search   关键字
     * @param pageNum  页码
     * @param pageSize 每页展示的数目
     * @return 知识点列表
     */
    @ApiOperation(value = "获取知识点列表", notes = "根据页码和每页展示的数目来获取知识点列表")
    @ApiImplicitParams({@ApiImplicitParam(name = "search", value = "关键字", required = false, dataType = "String"),
            @ApiImplicitParam(name = "pageNum", value = "页码", required = false, dataType = "Integer"),
            @ApiImplicitParam(name = "pageSize", value = "每页展示的数目", required = false, dataType = "Integer")})
    @RequestMapping(value = "/page/list")
    public String getQuestionKnowledgeList(Model model, String search, Integer pageNum, Integer pageSize) {
        if (search != null) {
            search = search.trim();
        }
        if (pageNum == null) {
            pageNum = Constants.DEFAULT_PAGE_NUM;
        }
        if (pageSize == null) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }
        PageInfo<QuestionKnowledge> pageInfo = questionService.selectQKnowledgeList(search, pageNum, pageSize);
        if (pageInfo != null) {
            model.addAttribute("pageInfo", pageInfo);
        }
        return "questionKnowledge/question_knowledge_list";
    }

    /**
     * 知识点添加页面
     *
     * @param model
     * @param isFromQuestion 是否来自题目页面
     * @return
     */
    @ApiOperation(value = "添加知识点页面", notes = "添加知识点页面")
    @RequestMapping(value = "/page/edit", method = RequestMethod.GET)
    public String addKnowledge(Model model, Integer isFromQuestion) {
        logger.info("添加试题");
        // 初始化页面数据
        initPage(model, 0);
        model.addAttribute("isFromQuestion", isFromQuestion);
        return "questionKnowledge/question_knowledge_edit";
    }

    /**
     * 知识点修改页面
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "修改知识点页面", notes = "修改知识点页面")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "knowledgeId", value = "知识点ID", dataType = "Integer")})
    @RequestMapping(value = "/page/edit/{knowledgeId}", method = RequestMethod.GET)
    public String editKnowledge(Model model, @PathVariable("knowledgeId") Integer knowledgeId) {
        logger.info("修改知识点，ID为：" + knowledgeId);
        // 初始化页面数据
        initPage(model, knowledgeId);
        return "questionKnowledge/question_knowledge_edit";
    }

    /**
     * 查看知识点关联的题目
     *
     * @param model
     * @param knowledgeId
     * @return
     */
    @ApiOperation(value = "查看知识点关联的题目", notes = "查看知识点关联的题目")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "knowledgeId", value = "知识点ID", dataType = "Integer")})
    @RequestMapping(value = "/page/relatedQuestion/{knowledgeId}")
    public String showRelatedQuestionList(Model model, @PathVariable("knowledgeId") Integer knowledgeId, Integer pageNum, Integer pageSize) {
        logger.info("查看知识点关联的题目，知识点ID为：" + knowledgeId);
        if (pageNum == null) {
            pageNum = Constants.DEFAULT_PAGE_NUM;
        }
        if (pageSize == null) {
            pageSize = 5;//Constants.DEFAULT_PAGE_SIZE;
        }
        PageInfo<QuestionBanks> pageInfo = questionService.getQuestionListByKnowledgeId(knowledgeId, pageNum, pageSize);
        if (pageInfo != null) {
            model.addAttribute("pageInfo", pageInfo);
        }
        return "questionKnowledge/related_question_list";
    }

    /**
     * 查看题目关联的知识点
     *
     * @param model
     * @param qbankId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查看题目关联的知识点", notes = "查看题目关联的知识点")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "qbankId", value = "题目ID", dataType = "Integer")})
    @RequestMapping(value = "/page/relatedKnowledge/{qbankId}")
    public String showRelatedKnowledgeList(Model model, @PathVariable("qbankId") Integer qbankId, Integer pageNum, Integer pageSize) {
        logger.info("查看题目关联的知识点，题目ID为：" + qbankId);
        if (pageNum == null) {
            pageNum = Constants.DEFAULT_PAGE_NUM;
        }
        if (pageSize == null) {
            pageSize = 5;//Constants.DEFAULT_PAGE_SIZE;
        }
        PageInfo<QuestionKnowledge> pageInfo = questionService.getKnowledgeListByQuestionId(qbankId, pageNum, pageSize);
        if (pageInfo != null) {
            model.addAttribute("pageInfo", pageInfo);
        }
        return "questionKnowledge/related_knowledge_list";
    }

    /**
     * 知识点相关操作
     *
     * @param model
     * @return
     */
    @ApiOperation(value = "知识点相关操作", notes = "知识点相关操作")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "QuestionKnowledge", value = "QuestionKnowledge", dataType = "QuestionKnowledge")})
    @FormToken(remove = true)
    @RequestMapping(value = "/submit", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public RestResponseDTO submit(Model model, QuestionKnowledge knowledge, HttpSession session) {
        RestResponseDTO restResponseDTO = new RestResponseDTO();
        try {
            boolean flag = false;
            if (null != knowledge.getId() && knowledge.getId() > 0) {
                String newCnName = knowledge.getCnName();
                String oldCnName = questionService.selectQKnowledgeById(knowledge.getId()).getCnName();
                if (!questionService.checkOtherKnowledgeExist(oldCnName, newCnName)) {
                    restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
                    restResponseDTO.setMsg("知识点已存在");
                    return restResponseDTO;
                }
                String recorder = (String) session.getAttribute("loginname");
                flag = questionService.editQKnowledge(knowledge, recorder);
            } else {
                if (!questionService.checkKnowledgeExist(knowledge.getCnName())) {
                    restResponseDTO.setStatus(RestResponseStates.SERVER_ERROR.getValue());
                    restResponseDTO.setMsg("知识点已存在");
                    return restResponseDTO;
                }
                String recorder = (String) session.getAttribute("loginname");
                String mids = knowledge.getMistakeString();
                restResponseDTO.setData(mids);
                flag = questionService.saveQKnowledge(knowledge, recorder);
            }

            // 操作失败
            if (!flag) {
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

    private void initPage(Model model, Integer knowledgeId) {
        QuestionKnowledge knowledge = null;
        if (knowledgeId > 0) {
            knowledge = questionService.selectQKnowledgeById(knowledgeId);
        } else {
            knowledge = new QuestionKnowledge();
        }
        model.addAttribute("know", knowledge);
        model.addAttribute("knowledgeId", knowledgeId);
    }

    /**
     * 知识点关联的所有题目列表
     *
     * @param model
     * @param searchValue
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/allQuestion/list", method = {RequestMethod.POST}, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getAllQuestionList(Model model, String searchValue, Integer pageNumber, Integer pageSize) {
        if (searchValue != null) {
            searchValue = searchValue.trim();
        }
        if (pageNumber == null) {
            pageNumber = Constants.DEFAULT_PAGE_NUM;
        }
        if (pageSize == null) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }
        List<String> questionIds = new ArrayList<>();
        if (null != searchValue && !StringUtils.isEmpty(searchValue)) {
            questionIds = Arrays.asList(searchValue.split(","));
        }
        PageInfo<QuestionBanks> pageInfo = questionService.getQuestionListByQuestionIdList(questionIds, pageNumber, pageSize);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("totalRow", pageInfo.getTotal());
        resultMap.put("list", pageInfo.getList());
        return JSON.toJSONString(resultMap);
    }

    /**
     * 错词本关联的所有知识点列表
     *
     * @param model
     * @param mistakeId
     * @return
     */
    @RequestMapping(value = "/allKnowledge/list", method = {RequestMethod.POST}, produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getAllKnowledgeList(Model model, String mistakeId) {
        List<QuestionKnowledge> list = questionService.getKnowledgeListByMistakeId(Integer.parseInt(mistakeId));
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("totalRow", list.size());
        resultMap.put("list", list);
        return JSON.toJSONString(resultMap);
    }

    /**
     * 教材列表
     *
     * @param model
     * @param searchValue
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/showTextbooks", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getTextbooks(Model model, String name, Integer searchValue, Integer pageNumber, Integer pageSize) {
        Map map = new HashMap();
        map.put("search", name);
        if (searchValue != null && searchValue > 0) {
            map.put("id", searchValue);
        }
        if (pageNumber == null) {
            pageNumber = Constants.DEFAULT_PAGE_NUM;
        }
        if (pageSize == null) {
            pageSize = Constants.DEFAULT_PAGE_SIZE;
        }
        PageInfo<Textbooks> pageInfo = textbooksService.selectTextbooks(map, pageNumber, pageSize);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("totalRow", pageInfo.getTotal());
        resultMap.put("list", pageInfo.getList());
        return JSON.toJSONString(resultMap);
    }

    /**
     * 查询教材页码
     *
     * @param model
     * @param strUrl
     * @return
     */
    @RequestMapping(value = "/showTextbooksPages", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String getTextbooks(Model model, String strUrl) {
        List<Map> list = textbooksService.showTextbooksPageList(strUrl);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("totalRow", list.size());
        resultMap.put("list", list);
        return JSON.toJSONString(resultMap);
    }

    /**
     * 教材分类
     *
     * @param model
     * @param rootId
     * @param parentId
     * @param classifyLevel
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/showTextbooksClassify", produces = "application/json;charset=utf-8")
    @ResponseBody
    public String showTextbooksClassify(Model model, Integer rootId, Integer parentId, Integer classifyLevel, Integer pageNumber, Integer pageSize) {
        Map map = new HashMap();
        map.put("classifyLevel", classifyLevel);
        if (null != rootId && rootId > 0) {
            map.put("rootId", rootId);
        }
        if (null != parentId && parentId > 0) {
            map.put("parentId", parentId);
        }
        List<TextbookesCategory> categoryList = textbooksService.searchTextbooksCategroyBySearch(map);
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("totalRow", categoryList.size());
        resultMap.put("list", categoryList);
        return JSON.toJSONString(resultMap);
    }

    /**
     * 查看知识点关联的教材
     *
     * @param model
     * @param knowledgeId
     * @return
     */
    @ApiOperation(value = "查看知识点关联的教材", notes = "查看知识点关联的教材")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "knowledgeId", value = "知识点ID", dataType = "Integer")})
    @RequestMapping(value = "/page/relatedTextbooks/{knowledgeId}")
    public String showRelatedTextbooksList(Model model, @PathVariable("knowledgeId") Integer knowledgeId, Integer pageNum, Integer pageSize) {
        logger.info("查看知识点关联的教材，知识点ID为：" + knowledgeId);
        if (pageNum == null) {
            pageNum = Constants.DEFAULT_PAGE_NUM;
        }
        if (pageSize == null) {
            pageSize = 5;//Constants.DEFAULT_PAGE_SIZE;
        }
        PageInfo<Textbooks> pageInfo = textbooksService.getTextbooksListByKnowledgeId(knowledgeId, pageNum, pageSize);
        if (pageInfo != null) {
            model.addAttribute("pageInfo", pageInfo);
        }
        return "questionKnowledge/related_textbooks_list";
    }

    @RequestMapping(value = "/importKnowledge")
    public String importKnowledge(Model model, @RequestParam(value = "filename") MultipartFile file) {
        model.addAttribute("pageInfo", new PageInfo<>());
        // 判断文件是否为空
        if (file == null) {
            model.addAttribute("msg", "文件不能为空！");
            return getQuestionKnowledgeList(model, null, Constants.DEFAULT_PAGE_NUM, Constants.DEFAULT_PAGE_SIZE);
        }

        // 获取文件名
        String fileName = file.getOriginalFilename();
        logger.debug("文件名：" + fileName);
        if (StringUtils.isEmpty(fileName)) {
            model.addAttribute("msg", "文件不能为空！");
            return getQuestionKnowledgeList(model, null, Constants.DEFAULT_PAGE_NUM, Constants.DEFAULT_PAGE_SIZE);
        }

        //验证文件名是否合格
        if (!ExcelImportUtils.validateExcel(fileName)) {
            model.addAttribute("msg", "文件必须是excel格式！");
            return getQuestionKnowledgeList(model, null, Constants.DEFAULT_PAGE_NUM, Constants.DEFAULT_PAGE_SIZE);
        }

        //进一步判断文件内容是否为空（即判断其大小是否为0或其名称是否为null）
        long size = file.getSize();
        if (StringUtils.isEmpty(fileName) || size == 0) {
            model.addAttribute("msg", "文件不能为空！");
            return getQuestionKnowledgeList(model, null, Constants.DEFAULT_PAGE_NUM, Constants.DEFAULT_PAGE_SIZE);
        }

        //批量导入
        Map resultMap = questionService.batchImport(fileName, file);
        model.addAttribute("msg", resultMap.get("msg"));
        logger.debug("data:", resultMap.get("data"));
        return getQuestionKnowledgeList(model, null, Constants.DEFAULT_PAGE_NUM, Constants.DEFAULT_PAGE_SIZE);
    }
}
