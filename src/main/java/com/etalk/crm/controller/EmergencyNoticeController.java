package com.etalk.crm.controller;

import com.alibaba.fastjson.JSON;
import com.etalk.crm.pojo.Lessons;
import com.etalk.crm.pojo.MessageSendRecord;
import com.etalk.crm.pojo.SubmitTemplateInfo;
import com.etalk.crm.service.MessageSendRecordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Jordan
 * @date 2018-04-11 15:38
 */
/*@CrossOrigin(origins = "http://192.168.1.97:8080", maxAge = 3600)*/
@Controller
@RequestMapping(value = "/notice",produces = "application/json; charset=utf-8")
public class EmergencyNoticeController {

	@Resource
	private MessageSendRecordService messageSendRecordService;

	/**
	 * 获取消息列表
	 * @param model 数据
	 * @param pageNum 页码
	 * @param pageSize 一页显示最大记录数
	 * @return 页面地址
	 */
	@RequestMapping("/msg/list")
	public String getMessageSendRecordList(Model model, MessageSendRecord record, Integer pageNum, Integer pageSize){
		if (StringUtils.isEmpty(pageNum)){
			pageNum=1;
		}
		if (StringUtils.isEmpty(pageSize)){
			pageSize=10;
		}
		model.addAttribute("pageInfo",messageSendRecordService.searchSendRecordByPage(record,pageNum,pageSize));
		model.addAttribute("nowTime",new Date());
		return "notice/message_list";
	}

	/**
	 * 关闭消息公告提醒
	 * @param messageId 消息id
	 * @return JSON
	 */
	@ResponseBody
	@RequestMapping("/msg/close/{messageId}")
	public String closeMessageShow(@PathVariable("messageId") Integer messageId){
		Map<String,Integer> map=new HashMap<>(1);
		int num=messageSendRecordService.closeMessageShow(messageId);
		map.put("status",num);
		return JSON.toJSONString(map);
	}
	/**
	 * 获取预定义模板供选择
	 * @return json
	 */
	@RequestMapping("/msg/template")
	public String getTemplateInfo(Model model){
		model.addAttribute("msgInfo",messageSendRecordService.searchMessageTemplate());
		return "notice/message_send";
	}
	@ResponseBody
	@RequestMapping("/msg/template/{id}")
	public String getWeixinTemplate(@PathVariable("id") Integer templateId){
		Map<String,Object> map=new HashMap<>(2);
		map.put("status",1);
		map.put("data",messageSendRecordService.searchMessageTemplateById(templateId));
		return JSON.toJSONString(map);
	}

	/**
	 * 查询符合条件的所有老师
	 * @param lesson 参数对象
	 * @return json
	 */
	@ResponseBody
	@RequestMapping("/msg/teacherList")
	public String getTeacherList(@RequestBody Lessons lesson){
		Map<String,Object> map=new HashMap<>(2);
		map.put("status",1);
		map.put("data",messageSendRecordService.searchTeacherLoginNameListByMsgSend(lesson));
		return JSON.toJSONString(map);
	}

	/**
	 * 查询符合条件的所有学生
	 * @param lesson 参数对象
	 * @return json
	 */
	@ResponseBody
	@RequestMapping("/msg/studentList")
	public String getStudentList(@RequestBody Lessons lesson){
		Map<String,Object> map=new HashMap<>(2);
		map.put("status",1);
		map.put("data",messageSendRecordService.searchStudentIdListByMsgSend(lesson));
		return JSON.toJSONString(map);
	}

	@ResponseBody
	@RequestMapping("/close/free/class")
	public String closeFreeClass(@RequestBody Lessons lesson){
		Map<String,Object> map=new HashMap<>(2);
		int num=messageSendRecordService.closeTeacherFreeClass(lesson);
		if (num>0){
			map.put("status",1);
		}else {
			if (num==-1){
				map.put("status",0);
				map.put("errCode",-1);
				map.put("errMsg","关闭班表开始日期为空");
			}
			if (num==-2){
				map.put("status",0);
				map.put("errCode",-1);
				map.put("errMsg","关闭班表结束日期为空");
			}
		}
		return JSON.toJSONString(map);
	}

	/**
	 *
	 * @param model 传输数据
	 * @param submitTemplateInfo 模板数据
	 * @param result 自动数据验证结果
	 * @param session session
	 * @return view
	 */
	@RequestMapping("/msg/send")
	public String addMessageRecord(Model model, @Validated SubmitTemplateInfo submitTemplateInfo, BindingResult result, HttpSession session){
		if (result.hasErrors()){
			model.addAttribute("state", 0);
			model.addAttribute("errMsg",result.getAllErrors());
			return getTemplateInfo(model);
		}
		if (submitTemplateInfo!=null){

			if (StringUtils.isEmpty(submitTemplateInfo.getPlatformList()) || submitTemplateInfo.getPlatformList().size()==0){
				model.addAttribute("errMsg","消息接收平台数据为空");
				return getTemplateInfo(model);
			}
			if (StringUtils.isEmpty(submitTemplateInfo.getMsgTitle())){
				model.addAttribute("errMsg","请输入消息标题");
				return getTemplateInfo(model);
			}
			if (StringUtils.isEmpty(submitTemplateInfo.getFirstData())){
				model.addAttribute("errMsg","请输入消息内容");
				return getTemplateInfo(model);
			}
			if (submitTemplateInfo.getRemindType() ==2){
				if (StringUtils.isEmpty(submitTemplateInfo.getRemindEndTime())){
					model.addAttribute("errMsg","选择按时间段提醒，请选择结束时间");
					return getTemplateInfo(model);
				}
			}
			if (submitTemplateInfo.getCancelClass() >0 && StringUtils.isEmpty(submitTemplateInfo.getCancelClassReasonId())){
				model.addAttribute("errMsg","请选择取消课程原因");
				return getTemplateInfo(model);
			}
			//操作人赋值
			submitTemplateInfo.setCreator(session.getAttribute("loginname").toString());

			int num=messageSendRecordService.saveMessageSendRecord(submitTemplateInfo);
			if (num>0){
				model.addAttribute("status",1);
			}
		}else {
			model.addAttribute("status",0);
			model.addAttribute("errMsg","提交数据为空");
			return getTemplateInfo(model);
		}
		return this.getTemplateInfo(model);
	}
}
