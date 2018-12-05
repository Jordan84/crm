package com.etalk.crm.dao;

import com.etalk.crm.pojo.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Auther: James
 * @Date: 2018/6/22 16:10
 * @Description:
 */
@Mapper
public interface CaseShareManageMapper {
    /**
     * caseshare 列表
     * @return
     */
    List<CaseShareManage> caseShareList(CaseShareManage caseShareManage);

    /**
     * 添加caseshare
     * @param caseShareManage
     * @return
     */
    int addcaseShare(CaseShareManage caseShareManage);

    /**
     * 查询该学员 记录是否存在
     * @param personId
     * @return
     */
    int selectByPersonId(@Param("personId") Integer personId);

    /**
     * 查询id  caaseshare
     * @param caseShareId
     * @return
     */
    CaseShareManage selectByCaseShareId(@Param("caseShareId") int caseShareId);

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
    int updateCaseShare(CaseShareManage caseShareManage);

    /**
     * 查询  pointCount  pageViews
     * @param id
     * @return
     */
    CaseShareManage selectById (@Param("id")int id);

    /**
     * 更新student_info familyInformation learningDifficulty
     * @param person
     * @return
     */
    int updateStuInfo(Person person);

    /**
     * 判断学员 信息是否存在
     * @param personId
     * @return
     */
    int stuInfoIsExist(@Param("personId")int personId);

    /**
     *插入学员信息（家庭信息，学习困难）
     */
    int insertIntoStuInfo(Person person);

    /**
     * 批量插入  选择的标签
     * @param list
     * @return
     */
    int bacthInsertLabel(List<CaseLabelSelected> list);

    /**
     * 查询选择过的的   标签
     * @param caseShareId
     * @return
     */
    int deleteByCaseId(@Param("caseShareId")Integer caseShareId);

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
    List<Recommend> recommendList(@Param("recommendIds") List<Integer> recommendIds);
}
