package com.etalk.crm.service;

import com.etalk.crm.pojo.*;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Auther: James
 * @Date: 2018/6/22 16:13
 * @Description:case分享
 */
public interface CaseShareManageService {
    /**
     * caseshare 列表
     * @return
     */
    PageInfo<CaseShareManage> caseSharList(int pageNum, int pageSize,CaseShareManage caseShareManage);

    /**
     * 添加caseshare
     * @param caseShareManage
     * @return
     */
    int addcaseShar(CaseShareManage caseShareManage);

    /**
     * 根据sscId查询学员列表
     * @param sscId
     * @return
     */
    List<Person> personList(Integer sscId,String loginName);

    /**
     * 根据  personId 查询学员信息
     * @param personId
     * @return
     */
    Person selectBypersonId(int personId);

    /**
     * 查询 该学员 记录是否存在
     * @param personId
     * @return
     */
    int caseIsExist(Integer personId);

     /**
     * 查询id  caaseshare
     * @param caseShareId
     * @return
     */
    CaseShareManage selectByCaseShareId(int caseShareId);

    /**
     * 获取套餐列表
     * @return
     */
    List<CaseShareType> packageList();

     /**
     * 添加caseShare
     * @param caseShareManage
     * @return
     */
    int updatCaseShare(CaseShareManage caseShareManage);

    /**
     * 点赞 列表
     * @param caseShareId
     * @return
     */
    List<CaseShareThumbs> caseShareThumbList(int caseShareId);

    /**
     * 当前用户是否点赞
     * @param caseShareId
     * @param addUserId
     * @return
     */
    int isThumbsUp(int caseShareId,int addUserId);

    /**
     *  点赞操作
     * @param type
     * @param caseShareId
     * @param addUserId
     * @param addUserName
     * @return
     */
    public int thumbsUp(String type,int caseShareId,int addUserId,String addUserName);

    /**
     * 添加访问量
     * @param caseShareId
     * @return
     */
    public int addpageViews(int caseShareId);

     /**
     * 添加评论
     * @param caseShareComment
     * @return
     */
   int  addComment(CaseShareComment caseShareComment);

    /**
     * 评论列表
     * @param pageNum
     * @param pageSize
     * @param caseShareId
     * @return
     */
   PageInfo<CaseShareComment> commentList(Integer pageNum,Integer pageSize,int caseShareId);

    /**
     * 根据  多个 id  查询标签列表
     * @param labelList
     * @return
     */
    List<CaseShareLabel> selectByIds (List<Integer> labelList);

     /**
     * 添加不通过理由
     * @param caseRejectReason
     * @return
     */
    int  addRejectReason(CaseRejectReason caseRejectReason);


       /**
     * 审核通过后  推  精  荐 等标识  列表
     * @return
     */
    List<Recommend> recommendList(List<Integer> recommendIds);
}
