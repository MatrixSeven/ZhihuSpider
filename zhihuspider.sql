/*
Navicat MySQL Data Transfer

Source Server         : Seven
Source Server Version : 50624
Source Host           : localhost:3306
Source Database       : zhihuspider

Target Server Type    : MYSQL
Target Server Version : 50624
File Encoding         : 65001

Date: 2017-01-11 17:29:12
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for follower
-- ----------------------------
DROP TABLE IF EXISTS `follower`;
CREATE TABLE `follower` (
  `user_token` varchar(255) DEFAULT NULL,
  `user_token_follower` varchar(255) DEFAULT NULL,
  `updatetime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `user_name` varchar(255) DEFAULT NULL,
  `follower_name` varchar(255) DEFAULT NULL,
  KEY `tokens` (`user_token`,`user_token_follower`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users` (
  `id` int(255) NOT NULL AUTO_INCREMENT COMMENT '默认id',
  `token` varchar(255) NOT NULL,
  `index_url` varchar(255) NOT NULL,
  `isinit` int(1) NOT NULL DEFAULT '0',
  `from_id` int(255) NOT NULL,
  `from_token` varchar(255) NOT NULL,
  `isparser` int(1) DEFAULT '0',
  `updatetime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `users_token_index` (`token`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=326184 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for users_info
-- ----------------------------
DROP TABLE IF EXISTS `users_info`;
CREATE TABLE `users_info` (
  `name` varchar(255) DEFAULT NULL,
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `education` varchar(255) DEFAULT NULL COMMENT '教育',
  `company` varchar(255) DEFAULT NULL COMMENT '公司',
  `job` varchar(255) DEFAULT NULL COMMENT '工作',
  `headline` varchar(255) DEFAULT NULL COMMENT '签名',
  `user_id` varchar(255) DEFAULT NULL COMMENT 'pk主表引用',
  `answer` varchar(255) DEFAULT NULL,
  `question` varchar(255) DEFAULT NULL,
  `article` varchar(255) DEFAULT NULL COMMENT '文章总数',
  `favorite` varchar(255) DEFAULT NULL COMMENT '收藏总数',
  `agree` varchar(255) DEFAULT NULL,
  `thanked` varchar(255) DEFAULT NULL,
  `following` varchar(255) DEFAULT NULL,
  `followers` varchar(255) DEFAULT NULL,
  `topic` varchar(255) DEFAULT NULL,
  `columns` varchar(255) DEFAULT NULL,
  `sex` varchar(255) DEFAULT NULL,
  `updatetime` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `weibo` varchar(255) DEFAULT NULL,
  `token` varchar(255) DEFAULT NULL,
  `index_url` varchar(255) DEFAULT NULL,
  KEY `tokens_users` (`token`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


