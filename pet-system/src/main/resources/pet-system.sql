/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Host           : localhost:3306
Source Database       : pet-system

Target Server Type    : MYSQL
File Encoding         : utf8mb4


Date: 2023-11-11 10:41:41
*/
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS=0;

DROP DATABASE IF EXISTS `pet-system`;
CREATE DATABASE `pet-system`;
USE `pet-system`;


-- ===================  管理端  =================== --

-- -----------------------------
-- 员工信息管理表
-- -----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '姓名',
  `username` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '用户名',
  `password` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '密码',
  `phone` varchar(11) COLLATE utf8_bin NOT NULL COMMENT '手机号',
  `sex` varchar(2) COLLATE utf8_bin NOT NULL COMMENT '性别',
  `email` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '邮箱',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '状态 0:禁用，1:正常',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_user` bigint(20) NOT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='员工信息';
insert into `employee` values ('1', '管理员', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '13812312312', '1', '123@qq.com', '1', '2023-05-06 17:20:07', '2023-05-10 02:24:09', '1', '1');
insert into `employee` values ('2', '员工', 'bella', 'e10adc3949ba59abbe56e057f20f883e', '17776714284', '2', '12345@qq.com', '1', '2023-11-06 17:20:07', '2023-11-10 02:24:09', '1', '1');


-- -----------------------------
-- 分类信息管理表
-- -----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `type` int(11) DEFAULT NULL COMMENT '类型   1 商品分类 2 套餐分类',
  `name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '分类名称',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '顺序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_user` bigint(20) NOT NULL COMMENT '修改人',
  `is_deleted` int(11) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_category_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='分类信息管理';


-- -----------------------------
-- 商品信息管理表
-- -----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '商品名称',
  `category_id` bigint(20) NOT NULL COMMENT '商品分类id',
  `price` decimal(10,2) DEFAULT NULL COMMENT '商品价格',
  `code` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '商品码',
  `image` varchar(200) COLLATE utf8_bin NOT NULL COMMENT '图片',
  `description` varchar(400) COLLATE utf8_bin DEFAULT NULL COMMENT '描述信息',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '0 停售 1 起售',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '顺序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_user` bigint(20) NOT NULL COMMENT '修改人',
  `is_deleted` int(11) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_pet_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='商品管理';


-- ----------------------------
-- 商品类型表
-- ----------------------------
DROP TABLE IF EXISTS `product_type`;
CREATE TABLE `product_type` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `product_id` bigint(20) NOT NULL COMMENT '商品',
  `name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '类型名称',
  `value` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '类型数据list',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_user` bigint(20) NOT NULL COMMENT '修改人',
  `is_deleted` int(11) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='商品类型关系表';


-- -----------------------------
-- 套餐信息管理表
-- -----------------------------
DROP TABLE IF EXISTS `setmeal`;
CREATE TABLE `setmeal` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `category_id` bigint(20) NOT NULL COMMENT '商品分类id',
  `name` varchar(64) COLLATE utf8_bin NOT NULL COMMENT '套餐名称',
  `price` decimal(10,2) NOT NULL COMMENT '套餐价格',
  `status` int(11) DEFAULT NULL COMMENT '状态 0:停用 1:启用',
  `code` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '编码',
  `description` varchar(512) COLLATE utf8_bin DEFAULT NULL COMMENT '描述信息',
  `image` varchar(255) COLLATE utf8_bin DEFAULT NULL COMMENT '图片',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_user` bigint(20) NOT NULL COMMENT '修改人',
  `is_deleted` int(11) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_setmeal_name` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='套餐';


-- -----------------------------
-- 商品-套餐信息管理表
-- -----------------------------
DROP TABLE IF EXISTS `setmeal_product`;
CREATE TABLE `setmeal_product` (
  `id` bigint(20) not null comment '主键',
  `setmeal_id` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '套餐id',
  `product_id` varchar(32) COLLATE utf8_bin NOT NULL COMMENT '商品id',
  `name` varchar(32) COLLATE utf8_bin DEFAULT NULL COMMENT '商品名称 （冗余字段）',
  `price` decimal(10,2) DEFAULT NULL COMMENT '商品原价（冗余字段）',
  `copies` int(11) NOT NULL COMMENT '数量',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` bigint(20) NOT NULL COMMENT  '创建人',
  `update_user` bigint(20) NOT NULL COMMENT  '修改人',
  `is_deleted` int(11) NOT NULL DEFAULT '0' COMMENT '是否删除',
 PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='套餐商品关系';



-- ===================  用户端  =================== --

-- -----------------------------
-- 用户信息管理表
-- -----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '姓名',
  `email` varchar(18) COLLATE utf8_bin NOT NULL COMMENT '邮箱',
  `sex` varchar(2) COLLATE utf8_bin DEFAULT NULL COMMENT '性别',
  `phone` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(500) COLLATE utf8_bin DEFAULT NULL COMMENT '头像',
  `status` int(11) DEFAULT '0' COMMENT '状态 0:禁用，1:正常',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户信息';


-- -----------------------------
-- 购物车信息管理表
-- -----------------------------
DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '名称',
  `image` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '图片',
  `user_id` bigint(20) NOT NULL COMMENT '主键',
  `product_id` bigint(20) DEFAULT NULL COMMENT '商品id',
  `setmeal_id` bigint(20) DEFAULT NULL COMMENT '套餐id',
  `product_type` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '商品类型',
  `number` int(11) NOT NULL DEFAULT '1' COMMENT '数量',
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='购物车';


-- -----------------------------
-- 订单信息管理表
-- -----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `number` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '订单号',
  `status` int(11) NOT NULL DEFAULT '1' COMMENT '订单状态 1待付款，2待派送，3已派送，4已完成，5已取消',
  `user_id` bigint(20) NOT NULL COMMENT '下单用户',
  `address_book_id` bigint(20) NOT NULL COMMENT '地址id',
  `order_time` datetime NOT NULL COMMENT '下单时间',
  `checkout_time` datetime NOT NULL COMMENT '结账时间',
  `pay_method` int(11) NOT NULL DEFAULT '1' COMMENT '支付方式 1微信,2支付宝',
  `amount` decimal(10,2) NOT NULL COMMENT '实收金额',
  `remark` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '备注',
  `phone` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `address` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `user_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `consignee` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单表';


-- -----------------------------
-- 订单明细管理表
-- -----------------------------
DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `name` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '名字',
  `image` varchar(100) COLLATE utf8_bin DEFAULT NULL COMMENT '图片',
  `order_id` bigint(20) NOT NULL COMMENT '订单id',
  `product_id` bigint(20) DEFAULT NULL COMMENT '商品id',
  `setmeal_id` bigint(20) DEFAULT NULL COMMENT '套餐id',
  `product_type` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '类型',
  `number` int(11) NOT NULL DEFAULT '1' COMMENT '数量',
  `amount` decimal(10,2) NOT NULL COMMENT '金额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='订单明细表';


-- -----------------------------
-- 地址管理表
-- -----------------------------
DROP TABLE IF EXISTS `address_book`;
CREATE TABLE `address_book` (
  `id` bigint(20) NOT NULL COMMENT '主键',
  `user_id` bigint(20) NOT NULL COMMENT '用户id',
  `consignee` varchar(50) COLLATE utf8_bin NOT NULL COMMENT '收货人',
  `sex` tinyint(4) NOT NULL COMMENT '性别 0 女 1 男',
  `phone` varchar(11) COLLATE utf8_bin NOT NULL COMMENT '手机号',
  `province_code` varchar(12) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '省级区划编号',
  `province_name` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '省级名称',
  `city_code` varchar(12) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '市级区划编号',
  `city_name` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '市级名称',
  `district_code` varchar(12) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '区级区划编号',
  `district_name` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '区级名称',
  `detail` varchar(200) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '详细地址',
  `label` varchar(100) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '标签',
  `is_default` tinyint(1) NOT NULL DEFAULT '0' COMMENT '默认 0 否 1是',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `create_user` bigint(20) NOT NULL COMMENT '创建人',
  `update_user` bigint(20) NOT NULL COMMENT '修改人',
  `is_deleted` int(11) NOT NULL DEFAULT '0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='地址管理';

INSERT INTO `address_book` VALUES ('1417414526093082626', '1417012167126876162', '小明', '1', '13812345678', null, null, null, null, null, null, '昌平区金燕龙办公楼', '公司', '1', '2021-07-20 17:22:12', '2021-07-20 17:26:33', '1417012167126876162', '1417012167126876162', '0');
INSERT INTO `address_book` VALUES ('1417414926166769666', '1417012167126876162', '小李', '1', '13512345678', null, null, null, null, null, null, '测试', '家', '0', '2021-07-20 17:23:47', '2021-07-20 17:23:47', '1417012167126876162', '1417012167126876162', '0');

