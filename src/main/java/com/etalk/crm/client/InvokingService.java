package com.etalk.crm.client;

import com.etalk.crm.client.interfaces.AppSendMessageClient;
import com.etalk.crm.client.interfaces.OperateClient;
import com.etalk.crm.client.interfaces.WeChatSendMessageClient;
import com.etalk.crm.dao.AppPersonMapper;
import com.etalk.crm.dao.LessonsMapper;
import com.etalk.crm.dao.WeixinUserMapper;
import com.etalk.crm.pojo.Lessons;
import com.etalk.crm.pojo.RecommendUsers;
import com.etalk.crm.pojo.WeixinUser;
import com.etalk.crm.service.push.AdvancedPushService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.List;

/**
 * 异步调用其它服务
 * @author Jordan
 * @date 2018-02-12 16:11
 */
@Component
public class InvokingService{
    protected static final Logger logger= LogManager.getLogger(InvokingService.class);

    @Resource
    private OperateClient operateClient;
    @Resource
    private WeChatSendMessageClient weChatSendMessageClient;
	@Resource
	private AppSendMessageClient appSendMessageClient;
    @Resource
    private LessonsMapper lessonsMapper;
    @Resource
    private AppPersonMapper appPersonMapper;
    @Resource
    private WeixinUserMapper weixinUserMapper;

	//------调用产品运营项目接口开始--------------------------------------------------------------------------------------------------------------------------

	/**
	 * 学员活动导入活动名单，告知产品运营数据
	 * @param uidList 参与活动名单，包含推荐人
	 * @param activityId 活动ID
	 * @param hours 赠送套餐课时数
	 * @param isGiveGifts 是否赠送礼物(0不赠送 1赠送)
	 */
	@Async
	public void privilegeAndHours(String uidList, Integer activityId, Integer hours, Integer isGiveGifts){
		String result=operateClient.privilegeAndHours(uidList,activityId,hours,isGiveGifts);
		logger.info("调用产品运营活动赠送接口结果："+result);
	}

	//------调用产品运营项目接口结束--------------------------------------------------------------------------------------------------------------------------------------------------------

    //------发送APP消息开始--------------------------------------------------------------------------------------------------------------------------------------------------------

	/**
	 * 发送APP消息
	 * @param msgTitle 消息标题
	 * @param msgContent 消息内容
	 * @param replyJson json格式参数
	 * @param list 发送到设备
	 */
	private void sendAppMessage(String msgTitle, String msgContent, String replyJson, List<String> list) {
		if (list!=null && list.size()>0) {

			for (int i=0;i<list.size();){
				//advancedPushService.advancedPush(list.subList(i,(i+=100>list.size()?list.size():i)), msgTitle, msgContent, replyJson);
				List<String> listTemp=list.subList(i,((i+100)>list.size()?list.size():(i+100)));
				String result=appSendMessageClient.pushSuspendClassesMessage(listTemp, msgTitle, msgContent, replyJson);
				i+=100;
				logger.info("调用APP消息发送服务："+result+" devices:"+listTemp.toString());
			}

			logger.info("调用APP消息发送服务："+list.toString());
			list.clear();
		}
	}

	/**
	 * 发送APP消息到ios和安卓
	 * @param lessons 查询APP设备条件
	 * @param msgTitle 消息标题
	 * @param msgContent 消息内容
	 * @param replyJson json格式参数
	 */
	@Async
	public void sendAppMsg(Lessons lessons,String msgTitle,String msgContent,String replyJson){
		List<String> list=lessonsMapper.selectAppDeviceIdListByMsgSend(lessons);
		sendAppMessage(msgTitle, msgContent, replyJson, list);
	}

	/**
	 * 发送app消息给所有人
	 * @param msgTitle 消息标题
	 * @param msgContent 消息内容
	 * @param replyJson json格式参数
	 */
	@Async
	public void sendAppMsg(String msgTitle,String msgContent,String replyJson){
		List<String> list=appPersonMapper.selectDeviceIdList();
		sendAppMessage(msgTitle, msgContent, replyJson, list);
	}

    //------发送微信消息方法开始---------------------------------------------------------------------------------------------------------------------------------------------------

    /**
     * 课程取消通知
     * @param openId      用户微信唯一标志
     * @param stuName     学员姓名
     * @param courseName  课程名称
     * @param teacherName 老师登录名
     * @param classTime   课程时间
     * @param reason      取消课程原因
     */
    @Async
    public void sendCourseCanceledMessage(String openId, String stuName, String courseName, String teacherName, String classTime, String reason) {
        String result=weChatSendMessageClient.sendCourseCanceledMessage(openId,stuName,courseName,teacherName,classTime,reason);
        logger.info("调用微信课程取消通知发送服务："+result);
    }

    /**
     * 紧急停课通知
     * @param msgContent 消息详情
     * @param reason 停课原因
     * @param stopTime 停课时间
     * @param msgRemark 消息说明文字或备注消息
     * @return 微信返回结果
     */
    @Async
    public void sendSuspendClassesMessage(Lessons lessons,String msgContent, String reason, String stopTime, String msgRemark){
	    List<String> weixinList=lessonsMapper.selectWXOpenIdListByMsgSend(lessons);
	    for (String openId:weixinList) {
		    String result = weChatSendMessageClient.sendSuspendClassesMessage(openId, msgContent, reason, stopTime, msgRemark);
		    logger.info(openId+"调用微信紧急停课通知发送服务："+result);
	    }
	    weixinList.clear();
    }

	/**
	 * 紧急停课通知发送所有人
	 * @param msgContent 消息详情
	 * @param reason 停课原因
	 * @param stopTime 停课时间
	 * @param msgRemark 消息说明文字或备注消息
	 * @return 微信返回结果
	 */
	@Async
	public void sendSuspendClassesMessage(String msgContent, String reason, String stopTime, String msgRemark){
		List<String> weixinList=weixinUserMapper.selectOpenIdList();
		for (String openId:weixinList) {
			String result = weChatSendMessageClient.sendSuspendClassesMessage(openId, msgContent, reason, stopTime, msgRemark);
			logger.info(openId+"调用微信紧急停课通知发送服务："+result);
		}
		weixinList.clear();
	}

	/**
	 * 活动套餐或礼品赠送
	 * @param personId 用户id
	 * @param loginName 学生姓名
	 * @param eventName 活动名称
	 * @param orderName 套餐或礼品名称
	 * @return 结果
	 */
	@Async
	public void sendEventGiftMessage(Integer personId,String loginName, String eventName, String orderName){
		List<String> weixinList=weixinUserMapper.selectOpenIdByPersonId(personId);
		for (String openId:weixinList) {
			String result = weChatSendMessageClient.sendEventGiftMessage(openId, loginName, eventName, orderName);
			logger.info(openId+"调用微信活动套餐或礼品赠送通知发送服务："+result);
		}
		weixinList.clear();
	}

	/**
	 * 反馈处理结果通知
	 * @param personId 用户id
	 * @param loginName 反馈者
	 * @param feedbackType  反馈类型
	 * @param feedbackTime 反馈时间
	 * @param problemDescription 问题描述
	 * @param processResult 处理结果
	 * @return 结果
	 */
	@Async
	public void sendFeedbackResultMessage(Integer personId,String loginName, String feedbackType,String feedbackTime,String problemDescription,String processResult){
		List<String> weixinList=weixinUserMapper.selectOpenIdByPersonId(personId);
		for (String openId:weixinList) {
			String result = weChatSendMessageClient.sendFeedbackResultMessage(openId, loginName, feedbackType, feedbackTime,problemDescription,processResult);
			logger.info(openId+"调用微信反馈处理结果通知发送服务："+result);
		}
		weixinList.clear();
	}

	/**
	 * 上传成绩提醒
	 * @param subject 科目
	 * @param testTime 测试时间
	 * @param param url参数字符串，首字符不含?和&
	 * @return 结果
	 */
	@Async
	public void sendUploadGradeMessage(String subject, String testTime, String param){
		List<WeixinUser> weixinList=weixinUserMapper.selectOpenIdAndPersonIdList();
		for (WeixinUser wu:weixinList) {
			String result = weChatSendMessageClient.sendUploadGradeMessage(wu.getOpenId(), wu.getStudentLoginName(),wu.getStudentId(), subject, testTime, param);
			logger.info(wu.getOpenId()+"调用微信上传成绩提醒发送服务："+result);
		}
		//插入接收人

		weixinList.clear();
	}

    //------发送微信消息方法结束---------------------------------------------------------------------------------------------------------------------------------------------------
}
