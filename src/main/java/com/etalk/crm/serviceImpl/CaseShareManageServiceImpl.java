package com.etalk.crm.serviceImpl;

import com.etalk.crm.dao.*;
import com.etalk.crm.pojo.*;
import com.etalk.crm.service.CaseShareManageService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Auther: James
 * @Date: 2018/6/22 16:13
 * @Description:case 分享
 */
@Service
public class CaseShareManageServiceImpl implements CaseShareManageService {
    protected static final Logger logger = LogManager.getLogger(CaseShareManageServiceImpl.class);

    @Resource
    private CaseShareManageMapper caseShareManageMapper;

    @Resource
    private PersonMapper personMapper;

    @Resource
    private CaseShareThumbsMapper caseShareThumbsMapper;

    @Resource
    private CaseShareCommentMapper caseShareCommentMapper;

    @Resource
    private CaseShareLabelMapper caseShareLabelMapper;

    /**
     * caseshare 列表
     *
     * @return
     */
    @Override
    public PageInfo<CaseShareManage> caseSharList(int pageNum, int pageSize,CaseShareManage caseShareManage) {
        PageHelper.startPage(pageNum, pageSize);
        if(StringUtil.isNotEmpty(caseShareManage.getLabelStr())){
            String label[] = caseShareManage.getLabelStr().split(",");
            List<Integer> labelList = new ArrayList();
            for(int i = 0 ;i<label.length;i++){
                labelList.add(Integer.parseInt(label[i]));
            }
            caseShareManage.setLabelList(labelList);
        }
        List<CaseShareManage> csList = caseShareManageMapper.caseShareList(caseShareManage);
        if(csList != null && csList.size()>0){
          for(int i = 0;i<csList.size();i++){
              CaseShareManage cd = csList.get(i);
              if(StringUtil.isNotEmpty(cd.getRecommend())){
                  String str [] = cd.getRecommend().split(",");
                  List<Integer> recommendIds = new ArrayList();
                  for(int j=0;j<str.length;j++){
                     recommendIds.add(Integer.parseInt(str[j]));
                  }
                  List<Recommend> rList = this.recommendList(recommendIds);
                  cd.setRecommendList(rList);
              }
          }
        }
        PageInfo<CaseShareManage> pageInfo = new PageInfo<CaseShareManage>(csList);
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        logger.info("分页信息：total=" + total + "，pages=" + pages + "，pageNum=" + pageNum + "，pageSize=" + pageSize);
        return pageInfo;
    }

    /**
     * 添加caseshare
     *
     * @param caseShareManage
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public int addcaseShar(CaseShareManage caseShareManage) {
        logger.info("添加caseshare");
        int result = caseShareManageMapper.selectByPersonId(caseShareManage.getPersonId());
        if(result>0){
            return -1;
        }else{
            Person person = new Person();
            person.setId(caseShareManage.getPersonId());
            person.setLearningDifficulty(caseShareManage.getLearningDifficulty());
            person.setFamilyInformation(caseShareManage.getFamilyInformation());
            if(caseShareManageMapper.stuInfoIsExist(caseShareManage.getPersonId())>0){
                result = caseShareManageMapper.updateStuInfo(person);
            }else{
                result = caseShareManageMapper.insertIntoStuInfo(person);
            }
            if(result >0){
                if(StringUtil.isEmpty(caseShareManage.getAttention())){
                    caseShareManage.setAttention("");
                }
                if(StringUtil.isEmpty(caseShareManage.getSchool())){
                    caseShareManage.setSchool("");
                }
                result = caseShareManageMapper.addcaseShare(caseShareManage);
                String labStr [] = caseShareManage.getLabelStr().split(",");
                List<CaseLabelSelected> labelList = new ArrayList<CaseLabelSelected>();
                for(int i = 0 ;i<labStr.length; i++){
                    CaseLabelSelected cls = new CaseLabelSelected();
                    cls.setCaseShareId(caseShareManage.getId());
                    cls.setLabelId(Integer.parseInt(labStr[i]));
                    labelList.add(cls);
                }
                caseShareManageMapper.bacthInsertLabel(labelList);
            }
        }
        return result;
    }

    /**
     * 根据sscId查询学员列表
     *
     * @param sscId
     * @return
     */
    @Override
    public List<Person> personList(Integer sscId,String loginName) {
        return personMapper.personList(sscId,loginName);
    }

    /**
     * 根据  personId 查询学员信息
     *
     * @param personId
     * @return
     */
    @Override
    public Person selectBypersonId(int personId) {
        return personMapper.selectByPersonId(personId);
    }

    /**
     * 查询 该学员 记录是否存在
     *
     * @param personId
     * @return
     */
    @Override
    public int caseIsExist(Integer personId) {
        return caseShareManageMapper.selectByPersonId(personId);
    }

    /**
     * 查询id  caaseshare
     *
     * @param caseShareId
     * @return
     */
    @Override
    public CaseShareManage selectByCaseShareId(int caseShareId) {
        return caseShareManageMapper.selectByCaseShareId(caseShareId);
    }

    /**
     * 获取套餐列表
     *
     * @return
     */
    @Override
    public List<CaseShareType> packageList() {
        return caseShareManageMapper.packageList();
    }

    /**
     * 添加caseShare
     *
     * @param caseShareManage
     * @return
     */
    @Override
    @Transactional()
    public int updatCaseShare(CaseShareManage caseShareManage) {
        int result = 0;
        Person person = new Person();
        person.setId(caseShareManage.getPersonId());
        person.setLearningDifficulty(caseShareManage.getLearningDifficulty());
        person.setFamilyInformation(caseShareManage.getFamilyInformation());
        result = caseShareManageMapper.updateStuInfo(person);
        if(result >0){
            caseShareManage.setUpdateTime(new Date());
            result = caseShareManageMapper.updateCaseShare(caseShareManage);
        }
        String labStr [] = caseShareManage.getLabelStr().split(",");
        //根据 caseshareid  删除之前选中的  标签
        int flag = caseShareManageMapper.deleteByCaseId(caseShareManage.getId());
        if(flag >0){
            String labArr [] = caseShareManage.getLabelStr().split(",");
            List<CaseLabelSelected> labelList = new ArrayList<CaseLabelSelected>();
            for(int i = 0 ;i<labArr.length; i++){
                CaseLabelSelected cls = new CaseLabelSelected();
                cls.setCaseShareId(caseShareManage.getId());
                cls.setLabelId(Integer.parseInt(labArr[i]));
                labelList.add(cls);
            }
            caseShareManageMapper.bacthInsertLabel(labelList);
        }
        return result;
    }

    /**
     * 点赞 列表
     *
     * @param caseShareId
     * @return
     */
    @Override
    public List<CaseShareThumbs> caseShareThumbList(int caseShareId) {
        return caseShareThumbsMapper.caseShareThumbList(caseShareId);
    }

    /**
     * 当前用户是否点赞
     *
     * @param caseShareId
     * @param addUserId
     * @return
     */
    @Override
    public int isThumbsUp(int caseShareId, int addUserId) {
        return caseShareThumbsMapper.isThumbsUp(caseShareId,addUserId);
    }

    /**
     *  点赞操作
     * @param type
     * @param caseShareId
     * @param session
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
   @SuppressWarnings("all")
    public int thumbsUp(String type,int caseShareId,int addUserId,String addUserName) {
        CaseShareManage caseShareManage = new CaseShareManage();
        //查询 点赞记录数
        CaseShareManage pointCount = caseShareManageMapper.selectById(caseShareId);
        CaseShareThumbs caseShareThumbs = new CaseShareThumbs();
        caseShareThumbs.setAddUserId(addUserId);
        caseShareThumbs.setAddUserName(addUserName);
        caseShareThumbs.setCaseShareId(caseShareId);
        if("yes".equals(type) && pointCount != null){
            //点赞数量+1
            caseShareManage.setPointCount(pointCount.getPointCount()+1);
            //添加点赞记录
            caseShareThumbsMapper.addThumbsUp(caseShareThumbs);
        }else{
             //点赞数量-1
            caseShareManage.setPointCount(pointCount.getPointCount()-1);
            //删除点赞记录
            caseShareThumbsMapper.cancelThumbsUp(caseShareId,addUserId);
        }
        if(caseShareId >0){
           caseShareManage.setId(caseShareId);
        }
        int result = caseShareManageMapper.updateCaseShare(caseShareManage);
        return  result;
    }

    /**
     * 添加访问量
     *
     * @param caseShareId
     * @return
     */
    @Override
    public int addpageViews(int caseShareId) {
        //获取  访问量
        CaseShareManage pageViews = caseShareManageMapper.selectById(caseShareId);
        CaseShareManage caseShareManage = new CaseShareManage();
        int result = 0;
        if(pageViews != null){
            caseShareManage.setPageViews(pageViews.getPageViews()+1);
            caseShareManage.setId(caseShareId);
            //更新访问量
            result = caseShareManageMapper.updateCaseShare(caseShareManage);
        }
        return result;
    }

    /**
     * 添加评论
     *
     * @param caseShareComment
     * @return
     */
    @Override
    public int addComment(CaseShareComment caseShareComment) {
        return caseShareCommentMapper.addComment(caseShareComment);
    }

    /**
     * 评论列表
     * @param pageNum
     * @param pageSize
     * @param caseShareId
     * @return
     */
    @Override
    public PageInfo<CaseShareComment> commentList(Integer pageNum,Integer pageSize,int caseShareId) {
        PageHelper.startPage(pageNum, pageSize);
        PageInfo<CaseShareComment> pageInfo = new PageInfo<CaseShareComment>(caseShareCommentMapper.commentList(caseShareId));
        long total = pageInfo.getTotal();
        int pages = pageInfo.getPages();
        pageNum = pageInfo.getPageNum();
        pageSize = pageInfo.getPageSize();
        logger.info("分页信息：total=" + total + "，pages=" + pages + "，pageNum=" + pageNum + "，pageSize=" + pageSize);
        return pageInfo;
    }

    @Override
    public List<CaseShareLabel> selectByIds(List<Integer> labelList) {
        return caseShareLabelMapper.selectByIds(labelList);
    }

    /**
     * 审核不通过
     * @param caseRejectReason
     * @return
     */
    @Override
    @Transactional
    public int addRejectReason(CaseRejectReason caseRejectReason) {
        CaseShareManage caseShareManage = new CaseShareManage();
        caseShareManage.setId(caseRejectReason.getCaseId());
        caseShareManage.setState(3);
        int flag= caseShareManageMapper.updateCaseShare(caseShareManage);
        if(flag>0){
            flag = caseShareManageMapper.addRejectReason(caseRejectReason);
        }
        return flag;
    }

      /**
     * 审核通过后  推  精  荐 等标识  列表
     * @return
     */
    @Override
    public List<Recommend> recommendList(List<Integer> recommendIds) {
        return caseShareManageMapper.recommendList(recommendIds);
    }
}
