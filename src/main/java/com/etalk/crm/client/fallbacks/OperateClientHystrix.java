package com.etalk.crm.client.fallbacks;

import com.alibaba.fastjson.JSON;
import com.etalk.crm.client.interfaces.OperateClient;
import com.etalk.crm.pojo.RecommendUsers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * @author Jordan
 * @date 2018-02-05 17:24
 */
public class OperateClientHystrix implements OperateClient {
    private static final Logger logger= LogManager.getLogger(OperateClientHystrix.class);

    private static final String ERROR_INFO_RESULT = "调用运营产品服务失败";

    @Override
    public String privilegeAndHours(String uidList, Integer activityId, Integer hours, Integer isGiveGifts) {
        logger.info(ERROR_INFO_RESULT+",参与活动名单："+ JSON.toJSONString(uidList)+",活动id："+activityId+",赠送套餐课时数："+hours+",是否赠送礼品："+isGiveGifts);
        return ERROR_INFO_RESULT+",参与活动名单："+JSON.toJSONString(uidList)+",活动id："+activityId+",赠送套餐课时数："+hours+",是否赠送礼品："+isGiveGifts;
    }
}
