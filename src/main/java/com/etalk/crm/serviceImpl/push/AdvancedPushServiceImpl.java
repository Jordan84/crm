package com.etalk.crm.serviceImpl.push;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.http.ProtocolType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.aliyuncs.push.model.v20160801.PushRequest;
import com.aliyuncs.push.model.v20160801.PushResponse;
import com.aliyuncs.utils.ParameterHelper;
import com.etalk.crm.service.push.AdvancedPushService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author Jordan
 */
@Service
public class AdvancedPushServiceImpl implements AdvancedPushService {

	protected static final Logger logger=LogManager.getLogger(AdvancedPushServiceImpl.class);
	
	private static DefaultAcsClient client;
	private static final String REGION_SHENZHEN = "cn-shenzhen";
	private static final String ACCESS_KEY_ID = "s3hlGZSUwlvnDVmF";
	private static final String ACCESS_KEY_SECRET = "gSJ861b26EBGuMuDML30WWrYJ9lq6W";
	//private static long appKey=24539156;
	private static long appKeyNew=23443213;

	public AdvancedPushServiceImpl(){
		IClientProfile profile = DefaultProfile.getProfile(REGION_SHENZHEN, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        client = new DefaultAcsClient(profile);
	}
	
    /**
     * 推送通知高级接口app3.0
     * <p>
     * 参见文档 https://help.aliyun.com/document_detail/48089.html
     *
     */
	@Override
    public void advancedPush(List<String> listDevices,String title,String content,String replyJson){
		try {
		    if(listDevices!=null && listDevices.size()>0){
		        PushRequest pushRequest = new PushRequest();
		        //安全性比较高的内容建议使用HTTPS
		        pushRequest.setProtocol(ProtocolType.HTTPS);
		        //内容较大的请求，使用POST请求
		        pushRequest.setMethod(MethodType.POST);
		        // 推送目标
		        pushRequest.setAppKey(appKeyNew);
				//推送目标: DEVICE:按设备推送 ALIAS : 按别名推送 ACCOUNT:按帐号推送  TAG:按标签推送; ALL: 广播推送
		        pushRequest.setTarget("DEVICE");
				//根据Target来设定，如Target=DEVICE, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)
		        pushRequest.setTargetValue(listDevices.toString().substring(1, listDevices.toString().length()-1).replace(" ", ""));
				//pushRequest.setTarget("ALL"); //推送目标: device:推送给设备; account:推送给指定帐号,tag:推送给自定义标签; all: 推送给全部
				//pushRequest.setTargetValue("ALL"); //根据Target来设定，如Target=device, 则对应的值为 设备id1,设备id2. 多个值使用逗号分隔.(帐号与设备有一次最多100个的限制)
				// 消息类型 MESSAGE NOTICE
		        pushRequest.setPushType("NOTICE");
				// 设备类型 ANDROID iOS ALL.
		        pushRequest.setDeviceType("ALL"); 
		
		
		        // 推送配置
				// 消息的标题
		        pushRequest.setTitle(title);
				// 消息的内容
		        pushRequest.setBody(content); 
		        
		        // 推送配置: iOS
				// iOS应用图标右上角角标
		        pushRequest.setIOSBadge(0);
				//开启静默通知
		        pushRequest.setIOSSilentNotification(true);
				// iOS通知声音
		        pushRequest.setIOSMusic("default"); 
		        //pushRequest.setiOSSubtitle("iOS10 subtitle");//iOS10通知副标题的内容
		        //pushRequest.setiOSNotificationCategory("iOS10 Notification Category");//指定iOS10通知Category
				//是否允许扩展iOS通知内容
		        pushRequest.setIOSMutableContent(true);
				//iOS的通知是通过APNs中心来发送的，需要填写对应的环境信息。"DEV" : 表示开发环境 "PRODUCT" : 表示生产环境
		        pushRequest.setIOSApnsEnv("PRODUCT");
				//pushRequest.setIOSApnsEnv("DEV");
				// 消息推送时设备不在线（既与移动推送的服务端的长连接通道不通），则这条推送会做为通知，通过苹果的APNs通道送达一次。注意：离线消息转通知仅适用于生产环境
		        pushRequest.setIOSRemind(true);
				//iOS消息转通知时使用的iOS通知内容，仅当iOSApnsEnv=PRODUCT && iOSRemind为true时有效
		        pushRequest.setIOSRemindBody("iOSRemindBody");
		       // pushRequest.setiOSExtParameters("{\"_ENV_\":\"DEV\",\"k2\":\"v2\"}"); //通知的扩展属性(注意 : 该参数要以json map的格式传入,否则会解析出错)
		        pushRequest.setIOSExtParameters(replyJson);
		        // 推送配置: Android
				//通知的提醒方式 "VIBRATE" : 震动 "SOUND" : 声音 "BOTH" : 声音和震动 NONE : 静音
		        pushRequest.setAndroidNotifyType("BOTH");
				//通知栏自定义样式0-100
		        pushRequest.setAndroidNotificationBarType(1);
				//通知栏自定义样式0-100
		        pushRequest.setAndroidNotificationBarPriority(1);
				//点击通知后动作 "APPLICATION" : 打开应用 "ACTIVITY" : 打开AndroidActivity "URL" : 打开URL "NONE" : 无跳转
		        pushRequest.setAndroidOpenType("ACTIVITY"); 
		        //pushRequest.setAndroidOpenUrl("http://www.aliyun.com"); //Android收到推送后打开对应的url,仅当AndroidOpenType="URL"有效
		        pushRequest.setAndroidActivity("");
				// 设定通知打开的activity，仅当AndroidOpenType="Activity"有效
		        pushRequest.setAndroidActivity("com.alibaba.push2.demo.XiaoMiPushActivity");
				// Android通知音乐
		        pushRequest.setAndroidMusic("default"); 
		        //小米手机设置
		       /*pushRequest.setAndroidXiaoMiActivity("com.ali.demo.MiActivity");//设置该参数后启动小米托管弹窗功能, 此处指定通知点击后跳转的Activity（托管弹窗的前提条件：1. 集成小米辅助通道；2. StoreOffline参数设为true）
		        pushRequest.setAndroidXiaoMiNotifyTitle("Mi title");
		        pushRequest.setAndroidXiaoMiNotifyBody("MiActivity Body");*/
				//设定通知的扩展属性。(注意 : 该参数要以 json map 的格式传入,否则会解析出错)
		        //pushRequest.setAndroidExtParameters("{\"k1\":\"android\",\"k2\":\"v2\"}");
		        pushRequest.setAndroidExtParameters(replyJson);
		        // 推送控制
				// 30秒之间的时间点, 也可以设置成你指定固定时间
		        Date pushDate = new Date(System.currentTimeMillis()) ; 
		        String pushTime = ParameterHelper.getISO8601Time(pushDate);
				// 延后推送。可选，如果不设置表示立即推送
		        pushRequest.setPushTime(pushTime);
				// 12小时后消息失效, 不会再发送
		        String expireTime = ParameterHelper.getISO8601Time(new Date(System.currentTimeMillis() + 12 * 3600 * 1000));
		        pushRequest.setExpireTime(expireTime);
				// 离线消息是否保存,若保存, 在推送时候，用户即使不在线，下一次上线则会收到
		        pushRequest.setStoreOffline(true); 
		
		        PushResponse pushResponse = client.getAcsResponse(pushRequest);
		        //System.out.println("RequestId: "+pushResponse.getRequestId()+", MessageId: "+pushResponse.getMessageId());
		        logger.info("RequestId: "+pushResponse.getRequestId()+", MessageId: "+pushResponse.getMessageId());
	
		    }
		} catch (ClientException e) {
			logger.error(e.getMessage(),e);
			// TODO Auto-generated catch block
			//logger.catching(e);
		}
    }
}
