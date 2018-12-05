package com.etalk.crm.controller;

import com.alibaba.fastjson.JSONObject;
import com.etalk.crm.dao.CaseShareLabelMapper;
import com.etalk.crm.dao.CaseShareTypeMapper;
import com.etalk.crm.pojo.*;
import com.etalk.crm.service.CaseShareManageService;
import com.etalk.crm.service.QuestionService;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jordan
 * @Auther: James
 * @Date: 2018/6/22 16:06O
 * @Description: case 分享
 */
@RequestMapping("/caseShareManage")
@Controller
public class CaseShareManageController {
    private static final Logger logger= LogManager.getLogger(CaseShareManageController.class);

    @Resource
    private CaseShareManageService caseShareManageService;

    @Resource
    private CaseShareTypeMapper  caseShareTypeMapper;

    @Resource
    private QuestionService questionService;

    @Resource
    private CaseShareLabelMapper caseShareLabelMapper;

    @Value("${web.crm.old.url}")
    String oldCrmUrl;

    /**
     * caseShareList
     * @param model
     * @param sourceTypeId 来源
     * @param sex 性别
     * @param gradeId 年级
     * @param title 标题lable
     * @param pageNum
     * @param pageSize
     * @return
     */
    @RequestMapping("/caseShareList")
    public String caseShareManageList(Model model, Integer sourceTypeId, Integer sex,Integer gradeId,
                                      String title,Integer pageNum, Integer pageSize,HttpSession session,
    Integer publishSort,Integer pageViewsSort,Integer thumpupSort,String labelStr,Integer state){
        CaseShareManage caseShareManage = new CaseShareManage();
        logger.info("caseshare列表");
        logger.info(sourceTypeId);
        logger.info(sex);
        logger.info(gradeId);
        logger.info(title);

        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        model.addAttribute("pageSize",pageSize);

        //条件查询參數 type sex grade title  publishSort,pageViewsSort,thumpupSort
        // 分别是 发布时间、点赞、浏览量 这三个字段的排序字段(1:降序 2：升序)
        if(sourceTypeId != null){
            model.addAttribute("sourceTypeId",sourceTypeId);
            caseShareManage.setSourceTypeId(sourceTypeId);
        }
        if(sex != null){
            model.addAttribute("sex",sex);
            caseShareManage.setSex(sex);
        }
        if(gradeId != null){
            model.addAttribute("gradeId",gradeId);
            caseShareManage.setGradeId(gradeId);
        }
        if(StringUtil.isNotEmpty(title)){
            model.addAttribute("title",title);
            caseShareManage.setTitle(title);
        }
        if(publishSort != null ){
            model.addAttribute("publishSort",publishSort);
            caseShareManage.setPublishSort(publishSort);
        }
        if(pageViewsSort != null){
            model.addAttribute("pageViewsSort",pageViewsSort);
            caseShareManage.setPageViewsSort(pageViewsSort);
        }
        if(thumpupSort != null){
            model.addAttribute("thumpupSort",thumpupSort);
            caseShareManage.setThumpupSort(thumpupSort);
        }
        //存取 caseshare 选择的
        if(StringUtil.isNotEmpty(labelStr)){
            model.addAttribute("labelStr",labelStr);
            caseShareManage.setLabelStr(labelStr);
        }
        if(Integer.parseInt(session.getAttribute("roleId").toString()) == 1 || Integer.parseInt(session.getAttribute("roleId").toString()) == 14){
            caseShareManage.setAddUserId(0);
        }else{
            caseShareManage.setAddUserId((Integer) session.getAttribute("userid"));
        }
        if(state != null){
            caseShareManage.setState(state);
            model.addAttribute("state",state);
        }
        PageInfo<CaseShareManage> pageInfo = caseShareManageService.caseSharList(pageNum,pageSize,caseShareManage);
        model.addAttribute("pageInfo", pageInfo);
        model.addAttribute("userId",(Integer) session.getAttribute("userid"));
        //调用公共  list
        pubList(model);
        return "caseShareManage/case_share_list";
    }

    /**caseshare 添加页面
     * @param model
     * @param session
     * @return
     */
    @RequestMapping("/toAddCaseShare")
    public String toAddCaseShare(Model model, HttpSession session){
        logger.info("添加caseshare");
        //调用公共  list
        pubList(model);
//        List<Person> personList = caseShareManageService.personList((Integer) session.getAttribute("userid"),null);
//        model.addAttribute("personList",personList);
        return "caseShareManage/caseShareAdd";
    }

    /**
     * 老版  crm  添加頁面
     * @param personId
     * @param model
     * @return
     */
    @RequestMapping("/oldToAddCase")
    public String oldToAddCase(int personId,int addUserId,Model model,HttpSession session,String addUserName){
        logger.info("老版crm進入添加 case頁面");
        logger.info("personId:"+personId);
        Person person = caseShareManageService.selectBypersonId(personId);
        model.addAttribute("person",person);
        session.removeAttribute("addUserId");
        session.setAttribute("addUserId",addUserId);
        session.removeAttribute("addUserName");
        session.setAttribute("addUserName",addUserName);
        pubList(model);
        model.addAttribute("oldCrmUrl",oldCrmUrl);
        return "caseShareManage/oldToAddCase";
    }

    /**
     * 添加caseshare分享
     * @param caseShareManage
     * @return
     */
    @RequestMapping(value = "/addCaseShare", method = RequestMethod.POST)
    @ResponseBody
    public Object addCaseShare(CaseShareManage caseShareManage,HttpSession session){
        logger.info("添加caseShare学员分享");
        Map<String,Object> reultMap = new HashMap<String,Object>();
        try {
            if(caseShareManage != null){
                if(session.getAttribute("addUserId") == null || StringUtil.isEmpty (session.getAttribute("addUserId").toString()))
                    caseShareManage.setAddUserId(Integer.parseInt(session.getAttribute("userid").toString()));
                else{
                    caseShareManage.setAddUserId(Integer.parseInt(session.getAttribute("addUserId").toString()));
                }
                if(session.getAttribute("addUserName") == null || StringUtil.isEmpty (session.getAttribute("addUserName").toString())){
                    caseShareManage.setAddUserName(session.getAttribute("loginname").toString());
                }else{
                    caseShareManage.setAddUserName(session.getAttribute("addUserName").toString());
                }

                int result =  caseShareManageService.addcaseShar(caseShareManage);
                if(result==1){
                    //成功
                    reultMap.put("code",1);
                    reultMap.put("msg","caseShare提交成功");
                logger.info("caseShare提交成功");
                }else if (result==-1){
                     //已存在
                    reultMap.put("code",-1);
                    reultMap.put("msg","该学员caseShare已存在");
                    logger.info("该学员caseShare已存在");
                }else{
                     //失败
                    reultMap.put("code",-2);
                    reultMap.put("msg","提交失败");
                    logger.info("提交失败");
                }
            }else{
                //参数错误
               reultMap.put("code",-2);
               reultMap.put("msg","参数错误");
               logger.info("参数错误");
            }
        }catch (Exception e){
          logger.error(e.getMessage(),e);
          reultMap.put("msg","系统错误");
        }
        return JSONObject.toJSON(reultMap);
    }

    @RequestMapping("/editeCaseShare")
    public String EditeCaseShare(Model model,int caseShareId,HttpSession session){
         logger.info("编辑caseshare");
         logger.info(caseShareId);
         pubList(model);
        CaseShareManage caseShareManage = caseShareManageService.selectByCaseShareId(caseShareId);
        model.addAttribute("caseShareManage",caseShareManage);
        //List<Person> personList = caseShareManageService.personList( (Integer) session.getAttribute("userid"),null);
       // model.addAttribute("personList",personList);
        return "caseShareManage/caseShareEdit";
    }

    /**
     * 更新caseShare
     * @param caseShareManage
     * @return
     */
    @RequestMapping(value = "/updateCaseShare",method = RequestMethod.POST)
    @ResponseBody
    public Object updateCaseShare(CaseShareManage caseShareManage){
        logger.info("更新caseShare");
        Map<String,Object> reultMap = new HashMap<String,Object>();
        int result = 0;
        try {
             result   = caseShareManageService.updatCaseShare(caseShareManage);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(result >0){
          reultMap.put("code",1);
          reultMap.put("msg","更新成功");
        }else{
            reultMap.put("code",-1);
            reultMap.put("msg","更新失败");
        }
        return JSONObject.toJSON(reultMap);
    }

    /**
     * 查看  caseShare
     * @param model
     * @param caseShareId
     * @return
     */
    @RequestMapping("/showCaseShare")
    public String showCaseShare(Model model,int caseShareId){
        logger.info("查看caseshare");
        CaseShareManage caseShareManage = caseShareManageService.selectByCaseShareId(caseShareId);
        if(caseShareManage != null){
           String [] str =  caseShareManage.getLabelStr().split(",");
           List<Integer> labelStrList = new ArrayList();
           for(int i = 0; i<str.length;i++){
               labelStrList.add(Integer.parseInt(str[i]));
           }
           List<CaseShareLabel> labelList = caseShareManageService.selectByIds(labelStrList);
           model.addAttribute("labelList",labelList);
        }
        model.addAttribute("caseShareManage",caseShareManage);
        return "caseShareManage/caseShareShow";
    }

    /**
     * 公共list
     * @param model
     */
    private  void pubList(Model model){
         logger.info("调用公共方法");
        //年級列表
        List<QuestionGrade> QGradeList = questionService.selectQGradeList();
       //标签分类列表
        List<CaseShareLabel> labelsList = caseShareLabelMapper.selectLabelList();
          //case_share   分类列表
        List<CaseShareType> csTypeList  = caseShareTypeMapper.selectSourceTypeList();
        //套餐列表
        List<CaseShareType> packageList = caseShareManageService.packageList();
        model.addAttribute("csTypeList",csTypeList);
        model.addAttribute("labelsList",labelsList);
        model.addAttribute("QGradeList",QGradeList);
        model.addAttribute("packageList",packageList);
    }

    /**
     * 根据学员 id  查询 学员信息
     * @param personId
     * @return
     */
    @RequestMapping(value="/selectByPersonId",method = RequestMethod.POST)
    @ResponseBody
    public Object selectByPersonId(int personId){
        logger.info("查询学员信息");
        Map<String,Object> reultMap = new HashMap<String,Object>();
        reultMap.put("person", caseShareManageService.selectBypersonId(personId));
        reultMap.put("code",1);
        return JSONObject.toJSON(reultMap);
    }

    /**
     * 下拉学员列表
     * @param search
     * @param session
     * @return
     */
    @RequestMapping(value = "/personList", method = RequestMethod.POST)
    @ResponseBody
    public Object PersonList(String search,HttpSession session){
        logger.info("加载下拉学员列表");
        Map<String,Object> reultMap = new HashMap<String,Object>();
        Integer userId = null;
        if(2 == Integer.parseInt(session.getAttribute("roleId").toString()) || 12 == Integer.parseInt(session.getAttribute("roleId").toString())){
            userId = (Integer) session.getAttribute("userid");
        }
        List<Person> personList = caseShareManageService.personList(userId,search);
        reultMap.put("personList",personList);
        reultMap.put("code",1);
        return JSONObject.toJSON(reultMap);
    }
    /**
     * 点赞
     * @param type
     * @param caseShareId
     * @param session
     * @return
     */
    @RequestMapping(value = "/thumbsUp",method = RequestMethod.POST)
    @ResponseBody
    public Object thumbsUp(String type,int caseShareId,HttpSession session){
        logger.info("点赞");
        //是否点赞  yes/no
        logger.info("type");
        //caseShareId
        logger.info("caseShareId");
        Map<String,Object> reultMap = new HashMap<String,Object>();
        int addUserId = Integer.parseInt(session.getAttribute("userid").toString());
        String addUserName = session.getAttribute("loginname").toString();
        int result = caseShareManageService.thumbsUp(type,caseShareId,addUserId,addUserName);
        if(result >0){
            reultMap.put("code",1);
            reultMap.put("msg","点赞成功");
        }else{
            reultMap.put("code",-1);
            reultMap.put("msg","点赞失败");
        }
        return JSONObject.toJSON(reultMap);
    }

    /**
     * 异步获取点赞列表
     * @param caseShareId
     * @return
     */
    @RequestMapping(value = "/thumbsUpList",method = RequestMethod.POST)
    @ResponseBody
    public Object thumbsUpList(int caseShareId){
        logger.info("异步获取点赞列表");
         Map<String,Object> reultMap = new HashMap<String,Object>();
         List<CaseShareThumbs> thumbsUpList = caseShareManageService.caseShareThumbList(caseShareId);
         reultMap.put("code",1);
         reultMap.put("thumbsUpList",thumbsUpList);
         return JSONObject.toJSON(reultMap);
    }

    /**
     * 访问量+1
     * @param caseShareId
     * @return
     */
    @RequestMapping(value = "/addpageViews",method = RequestMethod.POST)
    @ResponseBody
    public Object addpageViews (int caseShareId){
        logger.info("更新访问量");
        int result = caseShareManageService.addpageViews(caseShareId);
        Map<String,Object> reultMap = new HashMap<String,Object>();
        if(result>0){
            reultMap.put("code",1);
            reultMap.put("msg","访问量+1");
        }else{
            reultMap.put("code",0);
            reultMap.put("msg","访问量更新失败");
        }
        return JSONObject.toJSON(reultMap);
    }

    /**
     * 是否点赞
     * @param caseShareId
     * @param session
     * @return
     */
    @RequestMapping(value = "/isThumbsUp",method = RequestMethod.POST)
    @ResponseBody
    public Object isThumbsUp (int caseShareId,HttpSession session){
        logger.info("是否点赞");
        //是否点赞
        int isThumbsUp =  caseShareManageService.isThumbsUp(caseShareId,Integer.parseInt(session.getAttribute("userid").toString()));
        Map<String,Object> reultMap = new HashMap<String,Object>();
        reultMap.put("code",1);
        reultMap.put("isThumbsUp",isThumbsUp);
        return JSONObject.toJSON(reultMap);
    }

    /**
     * 添加评论
     * @param caseShareId
     * @param comment
     * @param session
     * @return
     */
    @RequestMapping(value = "/addComment" ,method = RequestMethod.POST )
    @ResponseBody
    public Object addComment(int caseShareId,String comment,HttpSession session){
        logger.info("添加caseShare评论");
        CaseShareComment caseShareComment = new CaseShareComment();
        caseShareComment.setAddUserId(Integer.parseInt(session.getAttribute("userid").toString()));
        caseShareComment.setAddUserName(session.getAttribute("loginname").toString());
        if(StringUtil.isNotEmpty(comment)){
            caseShareComment.setComment(comment);
        }
        if(caseShareId >0){
            caseShareComment.setCaseShareId(caseShareId);
        }
        int result = caseShareManageService.addComment(caseShareComment);
        Map<String,Object> reultMap = new HashMap<String,Object>();
        if(result >0){
            reultMap.put("code",1);
            reultMap.put("msg","评论成功");
        }else{
            reultMap.put("code",-1);
            reultMap.put("msg","评论失败");
        }
        return JSONObject.toJSON(reultMap);
    }

    /**
     * 获取评论列表
     * @param pageNum
     * @param pageSize
     * @param caseShareId
     * @return
     */
    @RequestMapping(value = "/commentList",method = RequestMethod.POST )
    @ResponseBody
    public Object commentList(Integer pageNum,Integer pageSize,int caseShareId){
        logger.info("获取评论列表");
        if (pageNum == null) {
            pageNum = 1;
        }
        if (pageSize == null) {
            pageSize = 10;
        }
        //获取评论列表
        PageInfo<CaseShareComment> pageInfo = caseShareManageService.commentList(pageNum,pageSize,caseShareId);
        Map<String,Object> reultMap = new HashMap<String,Object>();
        reultMap.put("code",1);
        reultMap.put("pageInfo",pageInfo);
        return JSONObject.toJSON(reultMap);
    }

    /**
     *  审核不通过
     * @param rejectReason
     * @param caseId
     * @return
     */
    @RequestMapping(value = "/caseReject",method = RequestMethod.POST )
    @ResponseBody
    public Object caseReject(String rejectReason,int caseId){
        logger.info("审核不通过");
        logger.info("rejectReason");
        logger.info("caseId");
        Map<String,Object> reultMap = new HashMap<String,Object>();
        CaseRejectReason caseRejectReason = new CaseRejectReason();
        caseRejectReason.setCaseId(caseId);
        caseRejectReason.setRejectReason(rejectReason);
        int flag = caseShareManageService.addRejectReason(caseRejectReason);
        if(flag > 0){
            reultMap.put("code",1);
        }else{
             reultMap.put("code",-1);
        }
        return JSONObject.toJSON(reultMap);
    }

    /**
     * 审核通过后  推  精  荐 等标识  列表
     * @return
     */
    @RequestMapping(value = "/getRecommendList",method = RequestMethod.POST )
    @ResponseBody
    public Object getRecommendList(){
        logger.info("审核通过后推精荐等标识列表");
        Map<String,Object> reultMap = new HashMap<String,Object>();
        List<Recommend> recommendList = caseShareManageService.recommendList(null);
        reultMap.put("recommendList",recommendList);
        reultMap.put("code",1);
        return JSONObject.toJSON(reultMap);
    }
}
