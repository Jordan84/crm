package com.etalk.crm.client.fallbacks;

import com.etalk.crm.client.interfaces.WeChatSendMessageClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Jordan
 */
public class WeChatSendMessageClientHystrix implements WeChatSendMessageClient {

    private static final Logger logger= LogManager.getLogger(WeChatSendMessageClientHystrix.class);
    private static final String ERROR_INFO_RESULT = "调用微信消息发送服务失败";

    @Override
    public String sendNoBookCourseMsg(String openId, String topMsg, String stuLoginName, String courseName, String remarkMsg) {
        logger.error(ERROR_INFO_RESULT+"，中教课提醒");
        return ERROR_INFO_RESULT+"，中教课提醒";
    }

    @Override
    public String sendCommentMessage(String openId, String stuName, String teacherName, String courseName) {
        logger.error(ERROR_INFO_RESULT+"，老师已点评通知");
        return ERROR_INFO_RESULT+"，老师已点评通知";
    }

    @Override
    public String sendCourseCanceledMessage(String openId, String stuName, String courseName, String teacherName, String classTime, String reason) {
        logger.error(ERROR_INFO_RESULT+"，课程取消通知");
        return ERROR_INFO_RESULT+"，课程取消通知";
    }

    /**
     * 紧急听课通知
     * @param openId     微信唯一id号
     * @param msgContent 消息详情
     * @param reason     停课原因
     * @param stopTime   停课时间
     * @param msgRemark  消息说明文字或备注消息
     * @return 微信返回结果
     */
    @Override
    public String sendSuspendClassesMessage(String openId, String msgContent, String reason, String stopTime, String msgRemark) {
        logger.error(ERROR_INFO_RESULT+"，紧急听课通知");
        return ERROR_INFO_RESULT+"，紧急听课通知";
    }

    @Override
    public String sendEventGiftMessage(String openId, String loginName, String eventName, String orderName) {
        logger.error(ERROR_INFO_RESULT+"，活动套餐或礼品赠送");
        return ERROR_INFO_RESULT+"，活动套餐或礼品赠送";
    }

    @Override
    public String sendFeedbackResultMessage(String openId, String loginName, String feedbackType, String feedbackTime, String problemDescription, String processResult) {
        logger.error(ERROR_INFO_RESULT+"，反馈处理结果通知");
        return ERROR_INFO_RESULT+"，反馈处理结果通知";
    }

    @Override
    public String sendUploadGradeMessage(String openId, String loginName, Integer personId, String subject, String testTime, String param) {
        logger.error(ERROR_INFO_RESULT+"，上传成绩提醒");
        return ERROR_INFO_RESULT+"，上传成绩提醒";
    }
}
