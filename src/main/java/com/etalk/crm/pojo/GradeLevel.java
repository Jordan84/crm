package com.etalk.crm.pojo;

/**
 * @Auther: James
 * @Date: 2018/10/11 10:55
 * @Description: 成绩  等级 表
 */
public class GradeLevel {
    private int id ;

    /**
     * 成绩等级名称
     */
    private String levelName;

    /**
     * 成绩等级
     */
    private int level;

    public int getId() {
        return id;
    }

    public String getLevelName() {
        return levelName;
    }

    public int getLevel() {
        return level;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLevelName(String levelName) {
        this.levelName = levelName;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
