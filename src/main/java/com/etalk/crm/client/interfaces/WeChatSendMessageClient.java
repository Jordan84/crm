package com.etalk.crm.client.interfaces;

import com.etalk.crm.client.fallbacks.WeChatSendMessageClientHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @date 2018-03-28 21:37
 * @author Jordan
 */
@FeignClient(name="${feign.client.message.name}",url = "${feign.client.message.url}",fallback=WeChatSendMessageClientHystrix.class)
@RequestMapping("/weixin/sendmsg")
public interface WeChatSendMessageClient {

    /**
     * 中教未约课提醒
     * @param openId 微信唯一id
     * @param topMsg 第一个消息内容
     * @param stuLoginName 学生姓名
     * @param courseName 课程名称
     * @param remarkMsg 备注消息
     * @return 消息发送结果
     */
    @RequestMapping(value = "/chineseTeacherCourse",method = RequestMethod.POST)
    String sendNoBookCourseMsg(@RequestParam("openId") String openId, @RequestParam("topMsg") String topMsg, @RequestParam("stuLoginName") String stuLoginName, @RequestParam("courseName") String courseName, @RequestParam("remarkMsg") String remarkMsg);

    /**
     * 老师评价后发送点评提醒给用户
     * @param openId 微信唯一id
     * @param stuName 学生姓名
     * @param teacherName 老师名称
     * @param classTime 课程时间
     * @return 消息发送结果
     */
    @RequestMapping(value="/stuComment",method = RequestMethod.POST)
    String sendCommentMessage(@RequestParam("openId") String openId, @RequestParam("stuName") String stuName, @RequestParam("teacherName") String teacherName, @RequestParam("courseName") String classTime);

    /**
     * 课程取消通知
     * @param openId 用户微信唯一标志
     * @param stuName 学员姓名
     * @param courseName 课程名称
     * @param teacherName 老师登录名
     * @param classTime 课程时间
     * @param reason 取消课程原因
     * @return 消息发送结果
     */
    @RequestMapping(value="/courseCanceled",method = RequestMethod.POST)
    String sendCourseCanceledMessage(@RequestParam("openId") String openId, @RequestParam("stuName") String stuName, @RequestParam("courseName") String courseName, @RequestParam("teacherName") String teacherName, @RequestParam("classTime") String classTime, @RequestParam("reason") String reason);
    /**
     * 紧急停课通知
     * @param openId 微信唯一id号
     * @param msgContent 消息详情
     * @param reason 停课原因
     * @param stopTime 停课时间
     * @param msgRemark 消息说明文字或备注消息
     * @return 微信返回结果
     */
    @RequestMapping(value="/suspendClasses",method = RequestMethod.POST)
    String sendSuspendClassesMessage(@RequestParam("openId") String openId, @RequestParam("msgContent")String msgContent, @RequestParam("reason") String reason, @RequestParam("stopTime") String stopTime, @RequestParam("msgRemark")String msgRemark);

    /**
     * 活动套餐或礼品赠送
     * @param openId 微信唯一标识
     * @param loginName 学生姓名
     * @param eventName 活动名称
     * @param orderName 套餐或礼品名称
     * @return 结果
     */
    @PostMapping(value = "/event/gift")
    String sendEventGiftMessage(@RequestParam("openId") String openId, @RequestParam("loginName") String loginName, @RequestParam("eventName") String eventName, @RequestParam("orderName") String orderName);

    /**
     * 反馈处理结果通知
     * @param openId 微信唯一标识
     * @param loginName 反馈者
     * @param feedbackType  反馈类型
     * @param feedbackTime 反馈时间
     * @param problemDescription 问题描述
     * @param processResult 处理结果
     * @return 结果
     */
    @PostMapping(value = "/feedback/result")
    String sendFeedbackResultMessage(@RequestParam("openId") String openId, @RequestParam("loginName") String loginName, @RequestParam("feedbackType") String feedbackType, @RequestParam("feedbackTime") String feedbackTime, @RequestParam("problemDescription") String problemDescription, @RequestParam("processResult") String processResult);

    /**
     * 上传成绩提醒
     * @param openId 微信唯一标识
     * @param loginName 学员名
     * @param personId 学员id
     * @param subject 科目
     * @param testTime 测试时间
     * @param param 连接地址参数字符串，首字符不含?和&
     * @return 结果
     */
    @PostMapping(value = "/upload/grade")
    String sendUploadGradeMessage(@RequestParam("openId") String openId, @RequestParam("loginName") String loginName, @RequestParam("personId") Integer personId, @RequestParam("subject") String subject, @RequestParam("testTime") String testTime, @RequestParam("param") String param);

}
