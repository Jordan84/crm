package com.etalk.crm.serviceImpl;

import com.etalk.crm.client.interfaces.WeChatSendMessageClient;
import com.etalk.crm.dao.KcOrdersMapper;
import com.etalk.crm.dao.KcPackageMapper;
import com.etalk.crm.dao.PersonMapper;
import com.etalk.crm.pojo.KcOrders;
import com.etalk.crm.pojo.KcPackage;
import com.etalk.crm.pojo.OrderBooks;
import com.etalk.crm.pojo.Person;
import com.etalk.crm.service.KcOrdersService;
import com.etalk.crm.service.OrderBooksService;
import com.etalk.crm.utils.DateUtil;
import com.github.pagehelper.util.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Author Terwer
 * @Date 2018/11/12 18:27
 * @Version 1.0
 * @Description 订单
 **/
@Service
public class KcOrdersServiceImpl implements KcOrdersService {
    private static final Logger logger = LogManager.getLogger(KcOrdersServiceImpl.class);

    @Resource
    private PersonMapper personMapper;
    @Resource
    private KcOrdersMapper kcOrdersMapper;
    @Resource
    private OrderBooksService orderBooksService;
    @Resource
    private WeChatSendMessageClient weChatSendMessageClient;

    @Transactional
    @Override
    public boolean addKcOrdersFromActivity(List<String> names, int packageType, KcPackage kcPackage, int activityId, String activityName, int isGift) {
        boolean flag = false;

        // 批量添加订单
        for (String loginName : names) {
            // 学员信息
            Person person = personMapper.selectPersonByLoginName(loginName);
            Integer personId = person.getId();
            int storesId = person.getStoresId();
            String openId = person.getOpenId();

            KcOrders record = new KcOrders();
            record.setBuyTime(DateUtil.getCurrentDatetime());
            record.setState(1);
            KcOrders ko;
            String id = "PO" + DateUtil.getToday("yyyyMMdd") + (int) (Math.random() * 10) + (int) (Math.random() * 10) + (int) (Math.random() * 10) + (int) (Math.random() * 10);
            while (StringUtil.isEmpty(record.getId())) {
                ko = kcOrdersMapper.selectByOrderId(id);
                if (ko == null || StringUtil.isEmpty(ko.getId())) {
                    record.setId(id);
                } else {
                    id = "PO" + DateUtil.getToday("yyyyMMdd") + (int) (Math.random() * 10) + (int) (Math.random() * 10) + (int) (Math.random() * 10) + (int) (Math.random() * 10);
                }

            }

            record.setLoginName(loginName);
            record.setPersonId(personId);
            record.setStoresId(storesId);
            record.setClassifyId(kcPackage.getClassifyId());
            record.setPackageTypeFlag(kcPackage.getTypeFlag());
            record.setLearnedClass(0);
            record.setEverydayclass(kcPackage.getClassCount());
            record.setValid(kcPackage.getValid());
            record.setPackageId(kcPackage.getId());
            record.setPackageName(kcPackage.getName());
            record.setPrice(new BigDecimal(kcPackage.getPrice()));
            record.setSalePrice(new BigDecimal(kcPackage.getPrice()));

            if (kcPackage.getClassNum() == null) {
                record.setClassNum(1);
            } else {
                record.setClassNum(kcPackage.getClassNum());
            }
            if (kcPackage.getClassLong() == null) {
                record.setClassLong(17);
            } else {
                record.setClassLong(kcPackage.getClassLong());
            }
            record.setPayMoney(record.getSalePrice());
            record.setPayType("0");
            record.setOldcount(0);
            record.setOldbuycount(0);
            record.setIntroduce(kcPackage.getIntroduce());
            record.setBuycount(kcPackage.getLessonCount());
            record.setGift(null == kcPackage.getGift() ? 0 : kcPackage.getGift());
            record.setLessonCount(kcPackage.getLessonCount() + (null == kcPackage.getGift() ? 0 : kcPackage.getGift()));
            record.setClassifyLevel(kcPackage.getClassifyLevel());
            if (null == record.getLearnedClass()) {
                record.setLearnedClass(0);
            }
            if (null == record.getEverydayclass()) {
                record.setEverydayclass(0);
            }
            if (StringUtil.isEmpty(record.getRemark())) {
                record.setRemark("");
            }
            if (null == record.getGift()) {
                record.setGift(0);
            }
            if (null == record.getLessonCount()) {
                record.setLessonCount(record.getGift() + record.getLessonCount());
            }

            // 即时生效
            if (packageType == 1) {
                record.setStartDate(new Date());
            } else {
                Date lastoutDate = kcOrdersMapper.getLastOutdate(personId);
                if (null != lastoutDate) {
                    record.setStartDate(lastoutDate);
                } else {
                    record.setStartDate(new Date());
                }
            }
            record.setSurplusValid(DateUtil.format(DateUtil.addDateToDay(record.getStartDate(), (record.getValid() + 1))).trim());

            OrderBooks ob = new OrderBooks();
            ob.setOrderId(record.getId());
            ob.setPersonId(record.getPersonId());
            ob.setComplete(0);
            ob.setTextbooksId(0);
            ob.setStoresId(record.getStoresId());
            ob.setCurrentpage("p0");
            ob.setPageindex(0);
            ob.setFinish(0);
            ob.setSequence(1);
            ob.setRegister("admin");
            boolean result = orderBooksService.addOrderBooks(ob);
            if (result) {
                logger.info("活动赠送匹配教材成功");
                record.setTextbookId(ob.getId());
            } else {
                logger.info("活动赠送匹配教材失败");
            }

            record.setCreater("admin");
            int num = kcOrdersMapper.insertOrder(record);
            if (num > 0) {
                flag = true;
                logger.info("-----------------订单添加成功-------------------");

                // 给赠送成功的学员发送通知
                if (null != openId) {
                    logger.info("给赠送成功的学员发送通知");
                    weChatSendMessageClient.sendEventGiftMessage(openId, loginName, activityName, kcPackage.getName());
                }
            } else {
                logger.error("-----------------添加订单失败-------------------");
            }
        }
        return flag;
    }
}
