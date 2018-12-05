package com.etalk.crm.dao;

import com.etalk.crm.pojo.CaseShareThumbs;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**点赞
 * @Auther: James
 * @Date: 2018/6/22 16:10
 * @Description:CaseShareTypeMapper
 */
@Mapper
public interface CaseShareThumbsMapper {
    /**
     * 点赞 列表
     * @param caseShareId
     * @return
     */
    List<CaseShareThumbs> caseShareThumbList(@Param("caseShareId")int caseShareId);

    /**
     * 当前用户是否点赞
     * @param caseShareId
     * @param addUserId
     * @return
     */
    int isThumbsUp(@Param("caseShareId")int caseShareId,@Param("addUserId")int addUserId);

    /**
     * 取消点赞
     * @param caseShareId
     * @param addUserId
     * @return
     */
    int cancelThumbsUp(@Param("caseShareId")int caseShareId,@Param("addUserId")int addUserId);

    /**
     * 添加点赞记录
     * @param caseShareThumbs
     * @return
     */
    int addThumbsUp(CaseShareThumbs caseShareThumbs);

}
