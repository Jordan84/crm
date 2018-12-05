package com.etalk.crm.client.interfaces;

import com.etalk.crm.client.fallbacks.OperateClientHystrix;
import com.etalk.crm.pojo.RecommendUsers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Jordan
 * @date 2018-02-04 19:37
 */

@FeignClient(name="${feign.client.operate.name}",url = "${feign.client.operate.url}",fallback= OperateClientHystrix.class)
@RequestMapping(value = "/interface/operate")
public interface OperateClient {

    /**
     * 学员活动导入活动名单，告知产品运营数据
     * @param uidList 参与活动名单，包含推荐人
     * @param activityId 活动ID
     * @param hours 赠送套餐课时数
     * @param isGiveGifts 是否赠送礼物(0不赠送 1赠送)
     * @return 结果
     */
    @PostMapping(value = "/privilegeAndHours/record")
    String privilegeAndHours(@RequestParam(value = "uidList") String uidList, @RequestParam(value = "activityId") Integer activityId, @RequestParam(value = "hours") Integer hours, @RequestParam(value = "isGiveGifts") Integer isGiveGifts);

}
