package com.etalk.crm.chuanglan.demo;

import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSON;
import com.etalk.crm.chuanglan.sms.request.SmsVariableRequest;
import com.etalk.crm.chuanglan.sms.response.SmsVariableResponse;
import com.etalk.crm.chuanglan.sms.util.ChuangLanSmsUtil;

/**
 * 
 * @author tianyh 
 * @Description:变量短信发送
 */
public class SmsVariableDemo {

	public static final String charset = "utf-8";
	/**
	 * 用户平台API账号(非登录账号,示例:N1234567)
	 */
	public static String account = "N5134710";
	/**
	 * 用户平台API密码(非登录密码)
	 */
	public static String pswd = "IDAlH6iu9Lfd2f";

	public static void main(String[] args) throws UnsupportedEncodingException {
		
		//变量短信发送
		String smsVarableRequestUrl = "http://smssh1.253.com/msg/variable/json";
		//短信内容
		String msg = "【Etalk英语】恭喜您获得外教一对一试听课，电话未联系上您，我是老师{$var}，需与您确认上课细节，请您回电或微信({$var})";
		//参数组(信息接收手机，变量1、变量2、变量3......依次类推)
		String params = "18566225185,Jordan,jordan";
		//状态报告
		//String report= "false";
		
		SmsVariableRequest smsVarableRequest=new SmsVariableRequest(account, pswd, msg, params);
		
        String requestJson = JSON.toJSONString(smsVarableRequest);
		
		System.out.println("before request string is: " + requestJson);
		
		String response = ChuangLanSmsUtil.sendSmsByPost(smsVarableRequestUrl, requestJson);
		
		System.out.println("response after request result is : " + response);
		
		SmsVariableResponse smsVarableResponse = JSON.parseObject(response, SmsVariableResponse.class);
		
		System.out.println("response  toString is : " + smsVarableResponse);
		
	}

}
