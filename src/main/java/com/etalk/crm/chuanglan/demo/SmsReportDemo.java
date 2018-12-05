package com.etalk.crm.chuanglan.demo;

import java.io.UnsupportedEncodingException;

import com.alibaba.fastjson.JSON;
import com.etalk.crm.chuanglan.sms.request.SmsReportRequest;
import com.etalk.crm.chuanglan.sms.response.SmsReportResponse;
import com.etalk.crm.chuanglan.sms.util.ChuangLanSmsUtil;

/**
 * @author tianyh
 * @Description:查询状态报告
 */
public class SmsReportDemo {

	public static final String charset = "utf-8";
	// 用户平台API账号(非登录账号,示例:N1234567)
	public static String account = "";
	// 用户平台API密码(非登录密码)
	public static String pswd = "";
	

	public static void main(String[] args) throws UnsupportedEncodingException {

		//查询状态报告地址
		String smsReportRequestUrl = "http://vsms.253.com/msg/pull/report";
		//状态报告拉取条数
		String count = "1";
		
		SmsReportRequest smsReportRequest = new SmsReportRequest(account, pswd, count);

		String requestJson = JSON.toJSONString(smsReportRequest);

		System.out.println("before request string is: " + requestJson);

		String response = ChuangLanSmsUtil.sendSmsByPost(smsReportRequestUrl, requestJson);

		System.out.println("response after request result is : " + response);

		SmsReportResponse smsReportRespnse = JSON.parseObject(response, SmsReportResponse.class);

		System.out.println("response  toString is : " + smsReportRespnse.getResult());

	}

}