package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author Terwer
 * @Date 2018/11/12 18:26
 * @Version 1.0
 * @Description 订单
 **/
@Getter
@Setter
public class KcOrders {
    /**
     * 订单号
     */
    private String id;
    /**
     * 用户ID
     */
    private Integer personId;
    /**
     * 购买人
     */
    private String loginName;
    private List<String> loginNameList;
    /**
     * 课程类型
     */
    private Integer classifyId;
    /**
     * 上课支付类型（0.课时，1.银币，2金币，3钻石）
     */
    private Integer classifyLevel;
    private String currencyName;
    /**
     * 课程套餐id
     */
    private Integer packageId;
    private String packageTypeFlag;
    private String packageName;
    private String orderName;
    private BigDecimal price;
    private BigDecimal salePrice;
    /**
     * 已支付金额
     */
    private BigDecimal payMoney;
    /**
     * (支付方式)1支付宝 2积分兑换
     */
    private String payType;
    /**
     * 支付宝流水号
     */
    private String payTradeNo;
    /**
     * 购买时间
     */
    private Date buyTime;
    /**
     * 购买人的支付宝id
     */
    private String buyerId;
    /**
     * 总课时
     */
    private Integer lessonCount;
    /**
     * 修改前的课时数
     */
    private Integer oldcount;
    /**
     *已学课时
     */
    private Integer learnedClass;
    /**
     * 已学课时
     */
    private String strLearnedClass;
    /**
     *每天课时
     */
    private Integer everydayclass;
    private String introduce;
    /**
     * 有效天数
     */
    private Integer valid;
    /**
     * 到期日期：yyyy-MM-dd
     */
    private String surplusValid;
    /**
     *说明
     */
    private String remark;
    private int speedProgress;
    private Integer storesId;
    /**
     * 订单创建人
     */
    private String creater;
    /**
     * 所属机构信息
     */
    private Stores stores;
    /**
     * E币或课时赠送数量
     */
    private Integer gift;
    /**
     * E币或课时购买数量
     */
    private Integer buycount;
    /**
     * 修改前的E币或课时购买数量
     */
    private Integer oldbuycount;
    /**
     * 订单状态：0无效，1正常，2待审核，3审核未通过
     */
    private Integer state;
    /**
     * 教材id
     */
    private Integer textbookId;
    /**
     * 每次上课时长
     */
    private Integer classLong;
    /**
     * 按实际课程17分钟算，课程时长占用几节课
     */
    private Integer classNum;
    /**
     * 我正在上的教材列表
     */
    private List<KcOrdersMaterial> myMaterialList;
    /**
     * 机构名称
     */
    private String storesName;
    /**
     * 学生中文名
     */
    private String stuName;
    /**
     *剩余课时
     */
    private BigDecimal surplus;
    /**
     * 时间段上客量
     */
    private int countClass;
    /**
     *消耗币数
     */
    private BigDecimal sumPay;
    /**
     * 统计消耗的银币数
     */
    private BigDecimal sumSilver;
    /**
     * 统计消耗的金币数
     */
    private BigDecimal sumGold;
    /**
     * 统计消耗的钻石币数
     */
    private BigDecimal sumDiamond;
    /**
     * 统计中教老师上课课时
     */
    private int sumChineseTeacher;
    /**
     * 统计欧美老师上课课时
     */
    private int sumEuramTeacher;
    /**
     * 上课时间
     */
    private Date releaseTime;
    /**
     * 完成率
     */
    private double completionRates;
    /**
     * 英文名称
     */
    private String enName;
    /**
     * 教材名称
     */
    private String txtBooksName;
    /**
     * 已用课时（E币）float
     */
    private double learnedClassD;
    /**
     *父级套餐名称
     */
    private String parentName;
    /**
     * 子套餐名称
     */
    private String classifyName;
    /**
     * 到期类型
     */
    private String typeFlag;
    /**
     *微信唯一id
     */
    private String openId;
    /**
     * 订单开始时间
     */
    private Date startDate;
    /**
     * 套餐类型：1为新套餐2为续费套餐
     */
    private String orderType;
    /**
     * 冻结状态：0为已冻结1为正常
     */
    private String freezeState;
    /**
     * 冻结后剩余有效天数
     */
    private int freezeValid;
    /**
     * 冻结日期
     */
    private String freezeDate;
    /**
     * 订单审核表的id
     */
    private String oldOrderId;

    private KcClassify kcClassify;

    private KcPackage kcPackage;

    private Person person;

    private OrderBooks orderBooks;

    private List<Lessons> lessonsList = new ArrayList<Lessons>(0);
}
