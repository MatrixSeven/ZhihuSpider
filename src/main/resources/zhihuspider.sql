/*
Navicat MySQL Data Transfer

Source Server         : Seven
Source Server Version : 50018
Source Host           : localhost:3307
Source Database       : zhihuspider

Target Server Type    : MYSQL
Target Server Version : 50018
File Encoding         : 65001

Date: 2016-12-11 20:13:42
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for follower
-- ----------------------------
DROP TABLE IF EXISTS `follower`;
CREATE TABLE `follower` (
  `user_token` varchar(255) default NULL,
  `user_token_follower` varchar(255) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(255) NOT NULL auto_increment COMMENT '默认id',
  `token` varchar(255) NOT NULL,
  `index_url` varchar(255) NOT NULL,
  `isinit` int(1) NOT NULL default '0',
  `from_id` int(255) NOT NULL,
  `from_token` varchar(255) NOT NULL,
  `isparser` int(1) default '0',
  PRIMARY KEY  (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for users_info
-- ----------------------------
DROP TABLE IF EXISTS `users_info`;
CREATE TABLE `users_info` (
  `name` varchar(255) default NULL,
  `address` varchar(255) default NULL COMMENT '地址',
  `education` varchar(255) default NULL COMMENT '教育',
  `company` varchar(255) default NULL COMMENT '公司',
  `job` varchar(255) default NULL COMMENT '工作',
  `headline` varchar(255) default NULL COMMENT '签名',
  `user_id` varchar(255) default NULL COMMENT 'pk主表引用',
  `answer` varchar(255) default NULL,
  `question` varchar(255) default NULL,
  `article` varchar(255) default NULL COMMENT '文章总数',
  `favorite` varchar(255) default NULL COMMENT '收藏总数',
  `agree` varchar(255) default NULL,
  `thanked` varchar(255) default NULL,
  `following` varchar(255) default NULL,
  `followers` varchar(255) default NULL,
  `topic` varchar(255) default NULL,
  `columns` varchar(255) default NULL,
  `sex` varchar(255) default NULL,
  `updatetime` timestamp NULL default CURRENT_TIMESTAMP on update CURRENT_TIMESTAMP,
  `weibo` varchar(255) default NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
