package com.etalk.crm.pojo;

import lombok.Getter;
import lombok.Setter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author Terwer
 * @date 2018/06/14
 */
@Getter
@Setter
public class SchoolCollectInfo {
    /**
     * 主键ID
     */
    private Integer id;
    /**
     * 省份编码
     */
    private Integer provinceCode;
    /**
     * 省份名称
     */
    private String provinceName;
    /**
     * 城市编码
     */
    private Integer cityCode;
    /**
     * 城市名称
     */
    private String cityName;
    /**
     * 区域编码
     */
    private Integer areaCode;
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 学校ID
     */
    private Integer schoolId;
    /**
     * 学校名称
     */
    private String schoolName;
    /**
     * 最近地铁站
     */
    private String subway;
    /**
     * 效果
     */
    private Integer effect;
    /**
     * 学校备注
     */
    private String schoolDesc;
    /**
     * 学校备注简介（20-25个字）
     */
    private String schoolDescShort;
    /**
     * 是否有保安驱赶
     */
    private Integer hasPolice;
    /**
     * 操作人
     */
    private String recorder;
    /**
     * 操作时间
     */
    private Date recordTime;

    /**
     * 学校信息
     */
    private SchoolInfo schoolInfo;
    /**
     * 采集时间
     */
    private List<SchoolCollectTime> collectTime;
    /**
     * 采集时间显示
     */
    private String collectTimeString;
    /**
     * 采集日期
     */
    private List<SchoolCollectDate> collectDate;

    public void setSchoolInfo(SchoolInfo schoolInfo) {
        this.schoolInfo = schoolInfo;
        this.schoolInfo.setAreaCode(this.getAreaCode());
    }

    public void setCollectTime(List<SchoolCollectTime> collectTime) {
        this.collectTime = collectTime;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < collectTime.size(); i++) {
            Date d = collectTime.get(i).getCollectTime();
            // 初始化时设置 日期和时间模式
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            // 修改日期和时间模式
            // sdf.applyPattern("yyyy-MM-dd");
            String dataStr = sdf.format(d);
            sb.append(dataStr);
            if (i < collectTime.size() - 1) {
                sb.append("-");
            }
        }
        this.collectTimeString = sb.toString();
    }

    public void setSchoolDesc(String schoolDesc) {
        this.schoolDesc = schoolDesc;
        this.schoolDescShort = this.schoolDesc.length() >= 15 ? this.schoolDesc.substring(0, 14) + "..." : this.schoolDesc;
    }
}
