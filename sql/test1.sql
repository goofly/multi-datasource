/*
Navicat MySQL Data Transfer

Source Server         : docker-mysql
Source Server Version : 50724
Source Host           : localhost:3306
Source Database       : test1

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2019-01-23 17:25:27
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
  `id` int(10) NOT NULL,
  `userName` varchar(10) DEFAULT NULL,
  `password` varchar(10) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of tb_user
-- ----------------------------
INSERT INTO `tb_user` VALUES ('1', '张三', '3333');
INSERT INTO `tb_user` VALUES ('2', '李四', '3333');
INSERT INTO `tb_user` VALUES ('112', '张思', '3333');
INSERT INTO `tb_user` VALUES ('116', '王六', '116');
INSERT INTO `tb_user` VALUES ('1019', '王10', '1019');
INSERT INTO `tb_user` VALUES ('1157', '王7', '117');
INSERT INTO `tb_user` VALUES ('1334', '王1334', '1334');
INSERT INTO `tb_user` VALUES ('3334', '王3334', '3334');
