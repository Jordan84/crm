package com.etalk.crm.service.push;

import java.util.List;

/**
 * @author Jordan
 * @date 2018-02-05 10:25
 */
public interface AdvancedPushService {

	/**
	 * 发送app消息或通知
	 * @param listDevices 设备列表
	 * @param title 消息题目
	 * @param content 消息内容
	 * @param replyJson 其它数据
	 */
	void advancedPush(List<String> listDevices, String title, String content, String replyJson);
	
}
