/*
Navicat MySQL Data Transfer

Source Server         : docker-mysql
Source Server Version : 50724
Source Host           : localhost:3306
Source Database       : test

Target Server Type    : MYSQL
Target Server Version : 50724
File Encoding         : 65001

Date: 2019-01-23 17:25:16
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
INSERT INTO `tb_user` VALUES ('1', '张三', '111');
INSERT INTO `tb_user` VALUES ('2', '李四', '222');
INSERT INTO `tb_user` VALUES ('112', '张思', '112');
INSERT INTO `tb_user` VALUES ('116', '王六', '116');
INSERT INTO `tb_user` VALUES ('119', '王9', '119');
INSERT INTO `tb_user` VALUES ('1019', '王10', '1019');
INSERT INTO `tb_user` VALUES ('1157', '王7', '117');
INSERT INTO `tb_user` VALUES ('1158', '王8', '118');
INSERT INTO `tb_user` VALUES ('1212', '王12', '1212');
INSERT INTO `tb_user` VALUES ('1333', '王1333', '1333');
INSERT INTO `tb_user` VALUES ('3321', '小新', '3632');
INSERT INTO `tb_user` VALUES ('3334', '王3334', '3334');
INSERT INTO `tb_user` VALUES ('78490', '小事务', '313632');
INSERT INTO `tb_user` VALUES ('89411', '小事务', '313632');
