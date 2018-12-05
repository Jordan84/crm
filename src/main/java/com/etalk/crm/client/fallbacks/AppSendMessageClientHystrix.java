package com.etalk.crm.client.fallbacks;

import com.etalk.crm.client.interfaces.AppSendMessageClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * @author Jordan
 */
public class AppSendMessageClientHystrix implements AppSendMessageClient {

    private static final Logger logger= LogManager.getLogger(AppSendMessageClientHystrix.class);
    private static final String ERROR_INFO_RESULT = "调用APP消息发送服务失败";

    @Override
    public String pushSuspendClassesMessage(List<String> listDeviceId, String title, String content, String replyJson) {
        logger.info(ERROR_INFO_RESULT+",发送紧急公告消息");
        return null;
    }
}
