package com.etalk.crm.dao;

import com.etalk.crm.pojo.CaseShareComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**评论
 * @Auther: James
 * @Date: 2018/7/5 16:40
 * @Description:
 */
@Mapper
public interface CaseShareCommentMapper {
    /**
     * 添加评论
     * @param caseShareComment
     * @return
     */
   int  addComment(CaseShareComment caseShareComment);

    /**
     * 评论列表
     * @param caseShareId
     * @return
     */
   List<CaseShareComment> commentList(@Param("caseShareId")int caseShareId);
}
