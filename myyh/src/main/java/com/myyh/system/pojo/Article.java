package com.myyh.system.pojo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 *
 * </p>
 *
 * @author RY
 * @since 2020-08-11
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName("t_article")
public class Article implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId
    private Integer id;
    /**
     * 标题
     */
    @TableField("title")
    private String title;

    /**
     * 内容
     */
    @TableField("content")
    private String content;

    /**
     * 作者
     */
    @TableField("author_id")
    private Integer authorId;

    /**
     * 创建时间
     */
    @TableField("create_time")
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField("update_time")
    private LocalDateTime updateTime;

    /**
     * 封面图
     */
    @TableField("image")
    private String image;

    /**
     * 浏览量
     */
    @TableField("visits_num")
    private Integer visitsNum;

    /**
     * 点赞数
     */
    @TableField("thumbup_num")
    private Integer thumbupNum;

    /**
     * 评论数
     */
    @TableField("comment_num")
    private Integer commentNum;

    /**
     * 收藏数
     */
    @TableField("collect_num")
    private Integer collectNum;

    /**
     * 删除状态，0：正常，1：删除
     */
    @TableField("is_delete")
    private Boolean isDelete;

    /**
     * 公开状态，0：公开，1：私密
     */
    @TableField("is_public")
    private Boolean isPublic;


}
