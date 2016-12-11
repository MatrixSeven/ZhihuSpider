package com.spider.entity;
//=======================================================
//		          .----.
//		       _.'__    `.
//		   .--(^)(^^)---/!\
//		 .' @          /!!!\
//		 :         ,    !!!!
//		  `-..__.-' _.-\!!!/
//		        `;_:    `"'
//		      .'"""""`.
//		     /,  ya ,\\
//		    //狗神保佑\\
//		    `-._______.-'
//		    ___`. | .'___
//		   (______|______)
//=======================================================

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * [Zhihu]https://www.zhihu.com/people/Sweets07
 * [Github]https://github.com/MatrixSeven
  Created by seven on 2016/11/30.
 */
@Data
@NoArgsConstructor
public class UserInfo {
    private String weibo;
    /**
     * 姓名
     */
    private String name;
    /**
     * 住址
     */
    private String address;
    /**
     * 行业
     */
    private String business;
    /**
     * 教育水平
     */
    private String education;
    /**
     * 公司
     */
    private String company;
    /**
     * 工作
     */
    private String job;
    /**
     * 签名
     */
    private String headline;
    /**
     * 系统ID
     */
    private String id;

    /**
     * 简介
     */
    private String content;
    /**
     * 问题总数
     */
    private String answer;
    /**
     * 回答总数
     */
    private String question;
    /**
     * 文章总数
     */
    private String article;
    /**
     * 收藏总数
     */
    private String favorite;
    /**
     * 得赞总数
     */
    private String agree;
    /**
     * 公共编辑
     */
    private String edit;
    /**
     * 被感谢总数
     */
    private String thanked;

    /**
     * 关注数
     */
    private String following;
    /**
     * 粉丝数
     */
    private String followers;
    /**
     * 关注话题数
     */
    private String topic;
    /**
     * 关注专栏数
     */
    private String columns;
    /**
     * 性别
     */
    private String sex;


}
