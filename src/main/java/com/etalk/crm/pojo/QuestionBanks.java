package com.etalk.crm.pojo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Terwer
 * @date 2018-05-14
 */
@Getter
@Setter
public class QuestionBanks {
    /**
     * 默认类型
     */
    public static final int INPIY_TYPE_DEFAULT = 1;
    /**
     * 附加小题
     */
    public static final int INPIY_TYPE_SUB = 2;

    /**
     * 试题ID
     */
    private Integer id;

    /**
     * 录入类型：1默认类型，2附加小题
     */
    private Integer inputType;

    /**
     * 年级ID
     */
    private Integer qgradeId;

    /**
     * 年级
     */
    private String qgradeName;

    /**
     * 第一级试题类型ID
     */
    private Integer qtypeId;

    /**
     * 第一级试题类型
     */
    @NotEmpty
    private String qtypeName;

    /**
     * 第二级试题ID
     */
    private Integer qmodeId;

    /**
     * 第二级试题类型
     */
    private String qmodeName;

    /**
     * 难度系数ID
     */
    private Integer qddegreeId;

    /**
     * 难度系数
     */
    private String qddegreeName;

    /**
     * 最后一级错词本分类
     */
    private Integer mistakeId;

    /**
     * 学期ID
     */
    private Integer qtermId;

    /**
     * 单元ID
     */
    private Integer qunitId;

    /**
     * 课时ID
     */
    private Integer qlessonTimeId;

    /**
     * 关键字标签
     */
    private String questionTags;

    /**
     * 知识点
     */
    @JsonIgnore
    private List<Integer> relatedKnowledge;

    /**
     * 知识点名称
     */
    @JsonProperty(value = "qknowledgeName")
    private String relatedKnowledgeName;

    /**
     * 知识点ID集合
     */
    @JsonProperty(value = "qknowledgeIds")
    private String relatedKnowledgeString;

    /**
     * 错词本分类集合
     */
    private List<Integer> childMistakeId;

    /**
     * 题目标题（此标题仅仅用于题目描述，不在试卷上展示）
     */
    private String title;

    /**
     * 展示标题（此标题会在试卷题干上显示）
     */
    private String qtitle;

    /**
     * 音频标题
     */
    private String audioTitle;

    /**
     * 音频地址
     */
    private String audioUrl;

    /**
     * 阅读题标题
     */
    private String contentTitle;

    /**
     * 阅读题内容
     */
    private String content;

    /**
     * 本题可得分数
     */
    private BigDecimal fraction;

    /**
     * 有效状态：0无效，1有效
     */
    @JsonIgnore
    private Integer state;

    /**
     * 父类ID
     */
    private Integer parentid;

    /**
     * 排序
     */
    @JsonIgnore
    private Integer sort;

    @JsonIgnore
    private String imgUrl;
    @JsonIgnore
    private String optionTextAnswer;
    @JsonIgnore
    private String optionImgAnswer;
    @JsonIgnore
    private String rightAnswer;
    @JsonIgnore
    private String isRight;

    /**
     * 题目图片
     */
    private List<QuestionBanksImg> listQuestionBanksImg;

    /**
     * 选项
     */
    private List<QuestionOptionAnswer> listOptionAnswer;

    /**
     * 正确答案
     */
    private List<QuestionRightAnswer> listRightAnswer;

    /**
     * 输入的答案
     */
    private List<QuestionInputAnswer> listInputAnswer;

    /**
     * 父类题目
     */
    private List<QuestionBanks> listQuestionBanks;

    /**
     * 关键字标签
     */
    private List<QuestionTag> listQuestionTag;

    /**
     * 答案解析
     */
    private QuestionExplain questionExplain;

    /**
     * 添加人
     */
    private String recorder;

    /**
     * 操作时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date recordTime;

    public QuestionBanks() {
    }

    public QuestionBanks(Integer inputType) {
        this.inputType = inputType;

        // 设置默认模板
        QuestionBanksImg defaultQuestionBanksImg = new QuestionBanksImg();
        List<QuestionBanksImg> defaultImgs = new ArrayList<>();
        defaultImgs.add(defaultQuestionBanksImg);
        this.listQuestionBanksImg = defaultImgs;

        QuestionOptionAnswer defaultQuestionOptionAnswer = new QuestionOptionAnswer();
        List<QuestionOptionAnswer> defaultOptions = new ArrayList<>();
        defaultOptions.add(defaultQuestionOptionAnswer);
        this.listOptionAnswer = defaultOptions;

        QuestionRightAnswer defaultQuestionRightAnswer = new QuestionRightAnswer();
        List<QuestionRightAnswer> defaultRightAnswers = new ArrayList<>();
        defaultRightAnswers.add(defaultQuestionRightAnswer);
        this.listRightAnswer = defaultRightAnswers;

        // 如果没有小题，添加默认模板
        if (CollectionUtils.isEmpty(this.getListQuestionBanks())) {
            List<QuestionBanks> template = new ArrayList<>();
            QuestionBanks defaultQuestBanks = new QuestionBanks();
            defaultQuestBanks.listQuestionBanksImg = defaultImgs;
            defaultQuestBanks.listOptionAnswer = defaultOptions;
            defaultQuestBanks.listRightAnswer = defaultRightAnswers;
            template.add(defaultQuestBanks);
            this.listQuestionBanks = template;
        }
    }

    /**
     * 初始化正确结果
     *
     * @param listOptionAnswer
     */
    public void setListOptionAnswer(List<QuestionOptionAnswer> listOptionAnswer) {
        List<QuestionOptionAnswer> options = new ArrayList<>();
        for (int i = 0; i < listOptionAnswer.size(); i++) {
            QuestionOptionAnswer optionAnswer = listOptionAnswer.get(i);
            // 设置正确选项
            optionAnswer = checkRight(optionAnswer);
            options.add(optionAnswer);
        }
        this.listOptionAnswer = options;
    }

    private QuestionOptionAnswer checkRight(QuestionOptionAnswer optionAnswer) {
        QuestionOptionAnswer optionAnswerResult = optionAnswer;
        if (!CollectionUtils.isEmpty(this.listRightAnswer)) {
            for (QuestionRightAnswer rightAnswer : this.listRightAnswer) {
                int idx1 = rightAnswer.getAnswer1().lastIndexOf(",");
                String asText1 = rightAnswer.getAnswer1().substring(0, idx1);
                String asImg1 = rightAnswer.getAnswer1().substring(idx1 + 1, rightAnswer.getAnswer1().length());

                int idx2 = rightAnswer.getAnswer2().lastIndexOf(",");
                String asText2 = rightAnswer.getAnswer2().substring(0, idx2);
                String asImg2 = rightAnswer.getAnswer2().substring(idx2 + 1, rightAnswer.getAnswer2().length());

                int idx3 = rightAnswer.getAnswer3().lastIndexOf(",");
                String asText3 = rightAnswer.getAnswer3().substring(0, idx3);
                String asImg3 = rightAnswer.getAnswer3().substring(idx3 + 1, rightAnswer.getAnswer3().length());

                int idx4 = rightAnswer.getAnswer4().lastIndexOf(",");
                String asText4 = rightAnswer.getAnswer4().substring(0, idx4);
                String asImg4 = rightAnswer.getAnswer4().substring(idx4 + 1, rightAnswer.getAnswer4().length());

                // 选项类型判断
                switch (optionAnswer.getOptionType()) {
                    case 0:// 都是空的
                    {
                        break;
                    }
                    case 1:// 文字
                    {
                        if (asText1.equals(optionAnswer.getAnswer())) {
                            optionAnswer.setRight(true);
                        } else if (asText2.equals(optionAnswer.getAnswer())) {
                            optionAnswer.setRight(true);
                        } else if (asText3.equals(optionAnswer.getAnswer())) {
                            optionAnswer.setRight(true);
                        } else if (asText4.equals(optionAnswer.getAnswer())) {
                            optionAnswer.setRight(true);
                        }
                        break;
                    }
                    case 2:// 图片
                    {
                        if (asImg1.equals(optionAnswer.getImgUrl())) {
                            optionAnswer.setRight(true);
                        } else if (asImg2.equals(optionAnswer.getImgUrl())) {
                            optionAnswer.setRight(true);
                        } else if (asImg3.equals(optionAnswer.getImgUrl())) {
                            optionAnswer.setRight(true);
                        } else if (asImg4.equals(optionAnswer.getImgUrl())) {
                            optionAnswer.setRight(true);
                        }
                        break;
                    }
                    case 3:// 文字和图片都有
                    {
                        if (asText1.equals(optionAnswer.getAnswer()) && asImg1.equals(optionAnswer.getImgUrl())) {
                            optionAnswer.setRight(true);
                        } else if (asText2.equals(optionAnswer.getAnswer()) && asImg2.equals(optionAnswer.getImgUrl())) {
                            optionAnswer.setRight(true);
                        } else if (asText3.equals(optionAnswer.getAnswer()) && asImg3.equals(optionAnswer.getImgUrl())) {
                            optionAnswer.setRight(true);
                        } else if (asText4.equals(optionAnswer.getAnswer()) && asImg4.equals(optionAnswer.getAnswer())) {
                            optionAnswer.setRight(true);
                        }
                        break;
                    }
                    default:
                        break;
                }
            }
        }
        return optionAnswerResult;
    }

    public void initQuestionImgs() {
        if (StringUtils.isEmpty(this.imgUrl)) {
            this.listQuestionBanksImg = new ArrayList<>();
        } else {
            String[] qimgs = this.imgUrl.split(",", -1);
            this.listQuestionBanksImg = new ArrayList<>(qimgs.length);
            for (int i = 0, qimgsLength = qimgs.length; i < qimgsLength; i++) {
                String qimg = qimgs[i];
                QuestionBanksImg questionBanksImg = new QuestionBanksImg();
                questionBanksImg.setQbankId(this.id);
                questionBanksImg.setImgTitle("");
                questionBanksImg.setImgUrl(qimg);
                questionBanksImg.setSort(i + 1);
                this.listQuestionBanksImg.add(questionBanksImg);
            }
        }
    }

    /**
     * 自定义转换选项
     */
    public void initListOptionAnswer() {
        if (this.optionTextAnswer == null) {
            this.optionTextAnswer = "";
        }
        if (this.optionImgAnswer == null) {
            this.optionImgAnswer = "";
        }
        if (this.isRight == null) {
            this.isRight = "false";
        }
        String[] texts = this.optionTextAnswer.split(",", -1);
        String[] imgs = this.optionImgAnswer.split(",", -1);
        String[] isRights = this.isRight.split(",", -1);
        int count = Math.max(texts.length, imgs.length);

        this.listOptionAnswer = new ArrayList<>(count);
        List<QuestionOptionAnswer> rightOption = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            // 如果选项都是空的，则不插入
            if (StringUtils.isEmpty(texts[i]) && StringUtils.isEmpty(imgs[i])) {
                continue;
            }

            // 解密
            final Base64.Decoder decoder = Base64.getDecoder();
            String txt = new String(decoder.decode(texts[i]));
            String img = new String(decoder.decode(imgs[i]));

            QuestionOptionAnswer questionOptionAnswer = new QuestionOptionAnswer();
            questionOptionAnswer.setQbankId(this.id);
            Integer optionType = 0;
            // 选项类型判断
            if (StringUtils.isEmpty(txt) && StringUtils.isEmpty(img)) {
                optionType = 0;
            } else if (!StringUtils.isEmpty(txt) && StringUtils.isEmpty(img)) {
                optionType = 1;
            } else if (StringUtils.isEmpty(txt) && !StringUtils.isEmpty(img)) {
                optionType = 2;
            } else {
                optionType = 3;
            }
            questionOptionAnswer.setOptionType(optionType);
            questionOptionAnswer.setAnswer(txt);
            questionOptionAnswer.setImgUrl(img);
            questionOptionAnswer.setSort(i + 1);
            this.listOptionAnswer.add(questionOptionAnswer);

            // 暂存正确选项
            if (Boolean.parseBoolean(isRights[i])) {
                rightOption.add(questionOptionAnswer);
            }
        }

        // 处理正确答案
        this.listRightAnswer = new ArrayList<>();
        for (int j = 0; j < rightOption.size(); j++) {
            QuestionOptionAnswer questionOptionAnswer = rightOption.get(j);
            QuestionRightAnswer questionRightAnswer = initRightAnswer(j, questionOptionAnswer);
            // 正确答案数目不定
            if (null != questionRightAnswer) {
                this.listRightAnswer.add(questionRightAnswer);
            }
        }
    }

    private QuestionRightAnswer initRightAnswer(int i, QuestionOptionAnswer questionOptionAnswer) {
        QuestionRightAnswer questionRightAnswer = new QuestionRightAnswer();
        questionRightAnswer.setQbankId(this.id);
        ArrayList answerList = new ArrayList(2);
        answerList.add(questionOptionAnswer.getAnswer());
        answerList.add(questionOptionAnswer.getImgUrl());
        String answer = String.join(",", answerList);
        // 设置答案
        switch (i) {
            case 0:
                questionRightAnswer.setAnswer1(String.join(",", answer));
                questionRightAnswer.setAnswer2(",");
                questionRightAnswer.setAnswer3(",");
                questionRightAnswer.setAnswer4(",");
                break;
            case 1:
                questionRightAnswer.setAnswer1(",");
                questionRightAnswer.setAnswer2(String.join(",", answer));
                questionRightAnswer.setAnswer3(",");
                questionRightAnswer.setAnswer4(",");
                break;
            case 2:
                questionRightAnswer.setAnswer1(",");
                questionRightAnswer.setAnswer2(",");
                questionRightAnswer.setAnswer3(String.join(",", answer));
                questionRightAnswer.setAnswer4(",");
                break;
            default:
                questionRightAnswer.setAnswer1(",");
                questionRightAnswer.setAnswer2(",");
                questionRightAnswer.setAnswer3(",");
                questionRightAnswer.setAnswer4(String.join(",", answer));
                break;
        }
        return questionRightAnswer;
    }

    /**
     * 自定义转换选项
     */
    public void initListQuestionTag() {
        String tags = this.getQuestionTags();
        List<QuestionTag> tagList = new ArrayList<>();
        if (null != tags) {
            String[] tagArr = tags.split(",");
            for (String tag : tagArr) {
                QuestionTag questionTag = new QuestionTag();
                questionTag.setQbankId(this.getId());
                questionTag.setTagName(tag);
                questionTag.setTagTitle(tag);
                tagList.add(questionTag);
            }
        }
        this.listQuestionTag = tagList;
    }
}
