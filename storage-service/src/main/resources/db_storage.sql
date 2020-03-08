/*
 Navicat Premium Data Transfer

 Source Server         : 192.168.1.200
 Source Server Type    : MySQL
 Source Server Version : 50729
 Source Host           : 192.168.1.200:3306
 Source Schema         : db_storage

 Target Server Type    : MySQL
 Target Server Version : 50729
 File Encoding         : 65001

 Date: 08/03/2020 12:37:30
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for storage_info
-- ----------------------------
DROP TABLE IF EXISTS `storage_info`;
CREATE TABLE `storage_info`  (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `commodity_code` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `count` int(11) NULL DEFAULT 0,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `commodity_code`(`commodity_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of storage_info
-- ----------------------------
INSERT INTO `storage_info` VALUES (1, 'iphone', 1000);
INSERT INTO `storage_info` VALUES (2, 'xiaomi', 1000);
INSERT INTO `storage_info` VALUES (3, 'huawei', 1000);

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `branch_id` bigint(20) NOT NULL,
  `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `rollback_info` longblob NOT NULL,
  `log_status` int(11) NOT NULL,
  `log_created` datetime(0) NOT NULL,
  `log_modified` datetime(0) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `ux_undo_log`(`xid`, `branch_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
