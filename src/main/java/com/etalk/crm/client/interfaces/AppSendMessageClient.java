package com.etalk.crm.client.interfaces;

import com.etalk.crm.client.fallbacks.AppSendMessageClientHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


/**
 * @date 2018-03-28 21:37
 * @author Jordan
 */
@FeignClient(name="${feign.client.message.name}",url = "${feign.client.message.url}",path = "${feign.client.message.app.path}",fallback= AppSendMessageClientHystrix.class)
public interface AppSendMessageClient {

    /**
     * 发送紧急公告通知
     * @param listDevices 设备id列表
     * @param title 消息标题
     * @param content 消息内容
     * @param replyJson 数据
     * @return 结果
     */
    @PostMapping(value = "/pushmsg/devices")
    String pushSuspendClassesMessage(@RequestParam("listDevices") List<String> listDevices, @RequestParam("title") String title, @RequestParam("content") String content, @RequestParam("replyJson")String replyJson);

}
