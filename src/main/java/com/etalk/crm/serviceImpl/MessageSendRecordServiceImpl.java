package com.etalk.crm.serviceImpl;

import com.alibaba.fastjson.JSON;
import com.etalk.crm.client.InvokingService;
import com.etalk.crm.dao.*;
import com.etalk.crm.pojo.*;
import com.etalk.crm.service.MessageSendRecordService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.*;

/**
 * @author Jordan
 */
@Service
public class MessageSendRecordServiceImpl implements MessageSendRecordService {
	private static final Logger logger= LogManager.getLogger(MessageSendRecordServiceImpl.class);

	@Resource
	private MessageTemplateMapper messageTemplateMapper;
	@Resource
	private MessageReceivingPlatformMapper messageReceivingPlatformMapper;
	@Resource
	private TeacherSiteMapper teacherSiteMapper;
	@Resource
	private ClassWaysMapper classWaysMapper;
	@Resource
	private MessageSendRecordMapper messageSendRecordMapper;
	@Resource
	private LessonChangeReasonMapper lessonChangeReasonMapper;
	@Resource
	private LessonsMapper lessonsMapper;
	@Resource
	private PersonMapper personMapper;
	@Resource
	private MessageSendDeviceRecordMapper messageSendDeviceRecordMapper;
	@Resource
	private MessageSendPlatformRecordMapper messageSendPlatformRecordMapper;
	@Resource
	private LessonIncompleteReasonMapper lessonIncompleteReasonMapper;
	@Resource
	private InvokingService invokingService;

	/**
	 * 分页查询消息记录
	 * @return PageInfo
	 */
	@Override
	public PageInfo<MessageSendRecord> searchSendRecordByPage(MessageSendRecord record,int pageNum, int pageSize){
		PageHelper.startPage(pageNum, pageSize);
		List<MessageSendRecord> list=messageSendRecordMapper.selectMessageSendRecordList(record);
		if (list!=null && list.size()>0){
			//String[] pattens=new String[]{"yyyy-MM-dd HH:mm:ss"};
			//拼接微信模板参数信息
			for (int i=0;i<list.size();i++){
				if (list.get(i).getRemindType().equals(2) && list.get(i).getRemindEndTime().getTime()<System.currentTimeMillis() ){
					list.get(i).setState(0);
				}
				String keyword="";
				if (StringUtil.isNotEmpty(list.get(i).getMessageInfo())){
					String[] keywords=list.get(i).getMessageInfo().split("-");
					for (int j=0;j<keywords.length;j++){
						if (j==0){
							keyword+=keywords[j]+"："+list.get(i).getKeyword1();
						}
						if (j==1){
							keyword+="<br>"+keywords[j]+"："+list.get(i).getKeyword2();
						}
						if (j==2){
							keyword+="<br>"+keywords[j]+"："+list.get(i).getKeyword3();
						}
						if (j==3){
							keyword+="<br>"+keywords[j]+"："+list.get(i).getKeyword4();
						}
					}
					list.get(i).setKeywords(keyword);
				}
			}
		}
		PageInfo<MessageSendRecord> pageInfo = new PageInfo<>(list);
		return pageInfo;
	}
	/**
	 * 查询紧急通知页面需要的信息
	 * @return List<MessageTemplate>
	 */
	@Override
	public Map<String, Object> searchMessageTemplate(){
		Map<String,Object> recordMap=new HashMap<>(4);
		//自定义模板选项
		recordMap.put("templateList",messageTemplateMapper.selectIdAndNameList());
		//消息接收平台
		recordMap.put("platformList",messageReceivingPlatformMapper.selectMessageReceivingPlatformList());
		//教学中心
		recordMap.put("teaSiteList",teacherSiteMapper.selectTeacherSiteList());
		//上课平台
		recordMap.put("classWayList",classWaysMapper.selectClassWaysList());
		//取消课程原因
		recordMap.put("voidReason",lessonChangeReasonMapper.selectVoidReasonList());
		return recordMap;
	}

	/**
	 * 查询微信模板格式和预定义的内容
	 * @param templateId 预定义的消息模板id
	 * @return Map<String,Object>
	 */
	@Override
	public MessageTemplate searchMessageTemplateById(Integer templateId){
		MessageTemplate mtl=messageTemplateMapper.selectMessageTemplateById(templateId);
		if (mtl!=null && !StringUtils.isEmpty(mtl.getKeywordNames())){
			String[] keys=mtl.getKeywordNames().split("-");
			for (int i=0;i<keys.length;i++){
				switch (i){
					case 0:
						mtl.setKey1(keys[i]);
						break;
					case 1:
						mtl.setKey2(keys[i]);
						break;
					case 2:
						mtl.setKey3(keys[i]);
						break;
					case 3:
						mtl.setKey4(keys[i]);
						break;
					default:
				}
			}
			mtl.setKeywordNames(null);
		}

		return mtl;
	}
	/**
	 * 手动关闭消息提醒
	 * @param messageId 消息id
	 * @return
	 */
	@Override
	public int closeMessageShow(Integer messageId) {
		return messageSendRecordMapper.updateMessageStateById(messageId);
	}

	/**
	 * 保存发送的消息内容
	 * @param record 消息对象
	 * @return 记录数
	 */
	@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	@Override
	public int saveMessageSendRecord(SubmitTemplateInfo record){
		logger.info("发送紧急通知参数："+JSON.toJSONString(record));
		if (record.getExcludeTeacher()!=null && record.getExcludeTeacher().size()>0 && StringUtils.isEmpty(record.getExcludeTeacher().get(0))){
			record.getExcludeTeacher().remove(0);
		}
		MessageSendRecord msgSendRecord=new MessageSendRecord();
		msgSendRecord.setTemplateId(record.getTemplateId());
		msgSendRecord.setMsgTitle(record.getMsgTitle());
		msgSendRecord.setFirstData(record.getFirstData());
		msgSendRecord.setKeyword1(StringUtils.isEmpty(record.getKeyword1())?"":record.getKeyword1());
		msgSendRecord.setKeyword2(StringUtils.isEmpty(record.getKeyword2())?"":record.getKeyword2());
		msgSendRecord.setKeyword3(StringUtils.isEmpty(record.getKeyword3())?"":record.getKeyword3());
		msgSendRecord.setKeyword4(StringUtils.isEmpty(record.getKeyword4())?"":record.getKeyword4());
		msgSendRecord.setRemark(StringUtils.isEmpty(record.getRemark())?"":record.getRemark());
		msgSendRecord.setLinkUrl(StringUtils.isEmpty(record.getLinkUrl())?"":record.getLinkUrl());
		msgSendRecord.setCancelClass(record.getCancelClass());
		msgSendRecord.setCloseClass(record.getCloseClass());
		try {
			String[] patten=new String[]{"yyyy-MM-dd HH:mm:ss"};
			if (!StringUtils.isEmpty(record.getCourseStartTime())) {
				msgSendRecord.setCloseStartTime(DateUtils.parseDate(record.getCourseStartTime(), patten));
			}
			if (!StringUtils.isEmpty(record.getCourseEndTime())) {
				msgSendRecord.setCloseEndTime(DateUtils.parseDate(record.getCourseEndTime(), patten));
			}
		} catch (ParseException e) {
			logger.error(e.getMessage(),"紧急公告关闭空闲班表时间格式化转换错误");
			logger.error(e.getMessage(),e);
		}
		msgSendRecord.setState(1);
		msgSendRecord.setIsUrgentNotice(record.getIsUrgentNotice());
		msgSendRecord.setRemindType(record.getRemindType());
		String nowTime=DateFormatUtils.format(new Date(),"yyyy-MM-dd HH:mm:ss");
		msgSendRecord.setShowStartTime(StringUtils.isEmpty(record.getRemindStartTime())?nowTime :record.getRemindStartTime());
		msgSendRecord.setShowEndTime(StringUtils.isEmpty(record.getRemindEndTime())?nowTime:record.getRemindEndTime());

		msgSendRecord.setCreator(record.getCreator());
		if (StringUtils.isEmpty(record.getListStudentId()) || record.getListStudentId().size()==0){
			//标记发送所有人
			msgSendRecord.setReceiveType(1);
		}else {
			//标记发送给指定人
			msgSendRecord.setReceiveType(2);
		}
		//保存消息发送记录
		int num=messageSendRecordMapper.insertMessageSendRecord(msgSendRecord);
		if (num>0) {
			boolean sendAPP=false;
			boolean sendWeinxin=false;
			//保存消息接收平台记录
			if (record.getPlatformList() != null && record.getPlatformList().size() > 0) {
				List<MessageSendPlatformRecord> listRecord = new ArrayList<>(record.getPlatformList().size());
				MessageSendPlatformRecord messageSendPlatformRecord;
				for (int i=0;i<record.getPlatformList().size();i++) {
					if (!StringUtils.isEmpty(record.getPlatformList().get(i))) {
						messageSendPlatformRecord = new MessageSendPlatformRecord();
						messageSendPlatformRecord.setMsgSendId(msgSendRecord.getId());
						messageSendPlatformRecord.setReceivingPlatformId(record.getPlatformList().get(i));
						listRecord.add(messageSendPlatformRecord);
						if (record.getPlatformList().get(i).equals(2)){
							sendAPP=true;
						}
						if (record.getPlatformList().get(i).equals(1) && !StringUtils.isEmpty(record.getTemplateId())){
							sendWeinxin=true;
						}
					}else {
						record.getPlatformList().remove(i);
						--i;
					}
				}
				if (listRecord.size()>0) {
					messageSendPlatformRecordMapper.insertMessageSendPlatformRecord(listRecord);
				}
			}
			//公用的lessons对象
			Lessons lessons = new Lessons();
			lessons.setClassStartTime(record.getClassStartTime());
			lessons.setClassEndTime(record.getClassEndTime());
			lessons.setClassWayId(record.getClassWayId());
			lessons.setState(record.getState());
			lessons.setTeaSiteId(record.getTeaSiteId());
			lessons.setExcludeTeacher(record.getExcludeTeacher());
			lessons.setMsgSendId(msgSendRecord.getId());
			//发送消息部分人
			if (msgSendRecord.getReceiveType()==2) {
				if (sendAPP) {
					//插入App设备信息
					messageSendDeviceRecordMapper.insertMessageSendPlatformRecordByApp(lessons);
					//发送APP消息
					Map<String, Object> replyJson = new HashMap<>(3);
					//紧急通知：4，普通通知：5
					replyJson.put("type", msgSendRecord.getIsUrgentNotice());
					if(!StringUtils.isEmpty(record.getRemindEndTime())) {
						replyJson.put("expireTime", record.getRemindEndTime());
					}
					replyJson.put("msgId", msgSendRecord.getId());
					invokingService.sendAppMsg(lessons, msgSendRecord.getMsgTitle(), msgSendRecord.getFirstData(), JSON.toJSONString(replyJson));
				}
				if (sendWeinxin) {
					//插入微信设备信息
					messageSendDeviceRecordMapper.insertMessageSendPlatformRecordByWeixin(lessons);
					//发送微信消息
					invokingService.sendSuspendClassesMessage(lessons, msgSendRecord.getFirstData(), msgSendRecord.getKeyword1(), msgSendRecord.getKeyword2(), msgSendRecord.getRemark());
				}
			}
			//发送消息所有人,只发消息不保存哪些设备
			if (msgSendRecord.getReceiveType()==1){
				if (sendAPP) {
					Map<String, Object> replyJson = new HashMap<>(3);
					//紧急通知：4，普通通知：5
					replyJson.put("type", msgSendRecord.getIsUrgentNotice());
					if(!StringUtils.isEmpty(record.getRemindEndTime())) {
						replyJson.put("expireTime", record.getRemindEndTime());
					}
					replyJson.put("msgId", msgSendRecord.getId());
					//发送APP消息所有人
					invokingService.sendAppMsg(msgSendRecord.getMsgTitle(), msgSendRecord.getFirstData(), JSON.toJSONString(replyJson));
				}
				if (sendWeinxin) {
					//发送微信消息所有人
					invokingService.sendSuspendClassesMessage(msgSendRecord.getFirstData(), msgSendRecord.getKeyword1(), msgSendRecord.getKeyword2(), msgSendRecord.getRemark());
				}
			}
			//取消课程
			if (record.getCancelClass() > 0) {
				//查询需要取消的课程id列表
				List<Integer> lessonsList = lessonsMapper.selectLessonIdListByMsgSend(lessons);
				if (lessonsList != null && lessonsList.size() > 0) {
					//返还课时
					lessonsMapper.updateLearnedClassByMsgSend(lessons);
					//取消课程，先返还课时再取消课程
					lessonsMapper.updateStateByMsgSend(lessons);
					//增加取消课程原因
					if (!StringUtils.isEmpty(record.getCancelClassReasonId())) {
						List<LessonIncompleteReason> list = new ArrayList<>(1);
						LessonIncompleteReason lessonIncompleteReason;
						for (int i = 0; i < lessonsList.size(); i++) {
							lessonIncompleteReason = new LessonIncompleteReason();
							lessonIncompleteReason.setLessonId(lessonsList.get(i));
							lessonIncompleteReason.setCreator(record.getCreator());
							lessonIncompleteReason.setReasonId(record.getCancelClassReasonId());
							lessonIncompleteReason.setState(1);
							list.add(lessonIncompleteReason);
							//每1000条记录插入数据库
							if (i % 1000 == 0) {
								lessonIncompleteReasonMapper.insertLessonIncompleteReason(list);
								list.clear();
							}
							//循环结束插入一次
							if (i == lessonsList.size() - 1 && list.size() > 0) {
								lessonIncompleteReasonMapper.insertLessonIncompleteReason(list);
								list.clear();
							}
						}
					}
				}
			}
			//关闭班表
			/*if (record.getCloseClass() == 1) {
				//关闭老师空闲班表
				lessonsMapper.deleteLessonByMsgSend(lessons);
			}*/
		}

		return num;
	}

	/**
	 * 关闭空闲班表
	 * @param record 参数
	 */
	@Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,timeout=36000,rollbackFor=Exception.class)
	@Override
	public int closeTeacherFreeClass(Lessons record){
		//关闭老师空闲班表
		if (StringUtils.isEmpty(record.getCourseStartTime())){
			return -1;
		}

		if (StringUtils.isEmpty(record.getCourseEndTime())){
			return -2;
		}
		logger.info("关闭空闲班表参数："+JSON.toJSONString(record));
		return lessonsMapper.deleteLessonByMsgSend(record);
	}
	/**
	 * 查询公告界面排除老师中可选老师数据
	 *
	 * @return
	 */
	@Override
	public List<String> searchTeacherLoginNameListByMsgSend(Lessons record) {
		if (record==null){
			record=new Lessons();
		}else {
			record.setClassStartTime(record.getClassStartTime().replace("T"," "));
			record.setClassEndTime(record.getClassEndTime().replace("T"," "));
		}
		if (record.getExcludeTeacher()!=null && record.getExcludeTeacher().size()>0 && StringUtils.isEmpty(record.getExcludeTeacher().get(0))){
			record.getExcludeTeacher().remove(0);
		}
		boolean isAllTea=!StringUtils.isEmpty(record.getClassEndTime()) || !StringUtils.isEmpty(record.getClassStartTime()) || !StringUtils.isEmpty(record.getClassWayId()) || !StringUtils.isEmpty(record.getState());
		if (isAllTea) {
			return lessonsMapper.selectTeacherListByMsgSend(record);
		}else {
			return personMapper.selectTeacherListByMsgSend(record.getTeaSiteId());
		}
	}

	/**
	 * 查询学员id列表
	 * @param record 查询条件
	 * @return
	 */
	@Override
	public List<Person> searchStudentIdListByMsgSend(Lessons record) {
		logger.info("紧急通知查询学员参数："+JSON.toJSONString(record));
		if (record==null){
			record=new Lessons();
		}else {
			record.setClassStartTime(record.getClassStartTime().replace("T"," "));
			record.setClassEndTime(record.getClassEndTime().replace("T"," "));
			if (record.getExcludeTeacher()!=null && record.getExcludeTeacher().size()>0 && StringUtils.isEmpty(record.getExcludeTeacher().get(0))){
				record.getExcludeTeacher().remove(0);
			}
		}
		boolean isAllTea=!StringUtils.isEmpty(record.getClassEndTime()) || !StringUtils.isEmpty(record.getClassStartTime()) || !StringUtils.isEmpty(record.getClassWayId())
						|| !StringUtils.isEmpty(record.getState()) || record.getExcludeTeacher()!=null || !StringUtils.isEmpty(record.getCancelClass());
		if (isAllTea) {
			return lessonsMapper.selectStudentListByMsgSend(record);
		}else {
			return personMapper.selectStudentListByMsgSend();
		}
	}
}
