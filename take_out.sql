/*
 Navicat Premium Data Transfer

 Source Server         : MySQL55
 Source Server Type    : MySQL
 Source Server Version : 80021
 Source Host           : localhost:3306
 Source Schema         : take_out

 Target Server Type    : MySQL
 Target Server Version : 80021
 File Encoding         : 65001

 Date: 09/07/2022 13:36:49
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for address_book
-- ----------------------------
DROP TABLE IF EXISTS `address_book`;
CREATE TABLE `address_book`  (
  `id` bigint(0) NOT NULL COMMENT '主键',
  `user_id` bigint(0) NOT NULL COMMENT '用户id',
  `consignee` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '收货人',
  `sex` tinyint(0) NOT NULL COMMENT '性别 0 女 1 男',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '手机号',
  `province_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省级区划编号',
  `province_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省级名称',
  `city_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '市级区划编号',
  `city_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '市级名称',
  `district_code` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区级区划编号',
  `district_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区级名称',
  `detail` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详细地址',
  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签',
  `is_default` tinyint(1) NOT NULL DEFAULT 0 COMMENT '默认 0 否 1是',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  `create_user` bigint(0) NOT NULL COMMENT '创建人',
  `update_user` bigint(0) NOT NULL COMMENT '修改人',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '地址管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of address_book
-- ----------------------------

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` bigint(0) NOT NULL COMMENT '主键',
  `type` int(0) NULL DEFAULT NULL COMMENT '类型   1 菜品分类 2 套餐分类',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '分类名称',
  `sort` int(0) NOT NULL DEFAULT 0 COMMENT '顺序',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  `create_user` bigint(0) NOT NULL COMMENT '创建人',
  `update_user` bigint(0) NOT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_category_name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '菜品及套餐分类' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1523221549476327425, 1, '家常菜', 9, '2022-05-08 16:41:32', '2022-05-08 17:38:00', 1, 1);
INSERT INTO `category` VALUES (1523221580623228930, 1, '盖饭', 8, '2022-05-08 16:41:40', '2022-05-08 17:38:08', 1, 1);
INSERT INTO `category` VALUES (1523221607961702401, 1, '特色菜', 7, '2022-05-08 16:41:46', '2022-05-08 17:38:16', 1, 1);
INSERT INTO `category` VALUES (1523221632653570050, 1, '干锅', 6, '2022-05-08 16:41:52', '2022-05-08 17:38:22', 1, 1);
INSERT INTO `category` VALUES (1523221667613093889, 1, '家常菜系列', 5, '2022-05-08 16:42:00', '2022-05-08 16:42:00', 1, 1);
INSERT INTO `category` VALUES (1523221692862803970, 1, '汤菜', 4, '2022-05-08 16:42:06', '2022-05-08 17:38:34', 1, 1);
INSERT INTO `category` VALUES (1523223881484185602, 1, '素菜系', 3, '2022-05-08 16:50:48', '2022-05-08 17:38:46', 1, 1);
INSERT INTO `category` VALUES (1523224049252151297, 2, '套餐', 2, '2022-05-08 16:51:28', '2022-05-08 17:39:08', 1, 1);
INSERT INTO `category` VALUES (1523224111688560641, 1, '米饭', 8, '2022-05-08 16:51:43', '2022-05-08 16:51:43', 1, 1);
INSERT INTO `category` VALUES (1523224134312636417, 1, '饮料', 1, '2022-05-08 16:51:48', '2022-05-08 17:39:13', 1, 1);

-- ----------------------------
-- Table structure for dish
-- ----------------------------
DROP TABLE IF EXISTS `dish`;
CREATE TABLE `dish`  (
  `id` bigint(0) NOT NULL COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '菜品名称',
  `category_id` bigint(0) NOT NULL COMMENT '菜品分类id',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '菜品价格',
  `code` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '商品码',
  `image` varchar(200) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '图片',
  `description` varchar(400) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '描述信息',
  `status` int(0) NOT NULL DEFAULT 1 COMMENT '0 停售 1 起售',
  `sort` int(0) NOT NULL DEFAULT 0 COMMENT '顺序',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  `create_user` bigint(0) NOT NULL COMMENT '创建人',
  `update_user` bigint(0) NOT NULL COMMENT '修改人',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_dish_name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '菜品管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dish
-- ----------------------------
INSERT INTO `dish` VALUES (1523227188311207938, '三鲜肉炒莲白(米饭单点)', 1523221549476327425, 1488.00, '', '29e866c2-5755-4e38-b78d-1477e3a5dc135.jpg', '原料：猪肉，白菜', 1, 0, '2022-06-19 14:24:07', '2022-06-19 14:24:07', 1, 1, 0);
INSERT INTO `dish` VALUES (1523227597964685314, '土豆炒肉丝（米饭单点）', 1523221549476327425, 1388.00, '', '3e35c86a-9c74-45bd-9955-0e9cc6cb36160.jpg', '原料：土豆，猪肉', 1, 0, '2022-06-19 14:24:44', '2022-06-19 14:24:44', 1, 1, 0);
INSERT INTO `dish` VALUES (1523227696245616641, '农家炒腊肉（米饭单点）', 1523221549476327425, 1688.00, '', 'df4c80db-80b6-455b-baa5-a4008b91baa86.jpg', '原料：腊肉', 1, 0, '2022-06-19 14:23:55', '2022-06-19 14:23:55', 1, 1, 0);
INSERT INTO `dish` VALUES (1523229046501126146, '干烧鲫鱼（米饭单点）', 1523221549476327425, 1898.00, '', '165ab715-8d70-4cb7-a166-6131675f35497.jpg', '原料：鲫鱼', 1, 0, '2022-06-19 14:23:21', '2022-06-19 14:23:21', 1, 1, 0);
INSERT INTO `dish` VALUES (1523229349560561666, '小炒肉（米饭单点）', 1523221549476327425, 1380.00, '', 'aeabc455-7ac1-413e-8605-e7d664cc7e252.jpg', '原料：猪肉', 1, 0, '2022-06-19 14:24:31', '2022-06-19 14:24:31', 1, 1, 0);
INSERT INTO `dish` VALUES (1523229489490931714, '尖椒鸡（米饭单点）', 1523221549476327425, 1398.00, '', 'c69aed1f-2553-43d4-9e4b-be0166f222752.jpg', '原料：辣椒，鸡肉', 1, 0, '2022-06-19 14:23:02', '2022-06-19 14:23:02', 1, 1, 0);
INSERT INTO `dish` VALUES (1523229659871948801, '回锅肉（米饭单点）', 1523221549476327425, 1388.00, '', 'd5deb5ee-f5d5-4fd9-abe3-339d6f147103d.jpg', '原料：猪肉', 1, 0, '2022-06-19 14:22:43', '2022-06-19 14:22:43', 1, 1, 0);
INSERT INTO `dish` VALUES (1523229839321051137, '鱼香肉丝（米饭单点）', 1523221549476327425, 1398.00, '', 'b262a692-72a8-4b79-871c-6c3ac167b8444.jpg', '原料：胡罗卜、笋、木耳、猪肉', 1, 0, '2022-06-19 14:21:32', '2022-06-19 14:21:32', 1, 1, 0);
INSERT INTO `dish` VALUES (1523230006640226306, '土豆肉丝+米饭', 1523221580623228930, 1500.00, '', 'b260bd1a-660d-473b-8274-476ac2e61a5e1.jpg', '原料：大米、土豆、猪肉', 1, 0, '2022-06-19 14:21:00', '2022-06-19 14:21:00', 1, 1, 0);
INSERT INTO `dish` VALUES (1523230157148631041, '盐煎肉+米饭', 1523221580623228930, 1500.00, '', '5ad601be-80a0-4127-9bbf-1cd1521e76bae.jpg', '原料：大米、猪肉、食盐', 1, 0, '2022-06-19 14:20:48', '2022-06-19 14:20:48', 1, 1, 0);
INSERT INTO `dish` VALUES (1523230305597632513, '回锅肉+米饭', 1523221580623228930, 1500.00, '', '87157560-d49f-4794-8de4-ef1a26a380007.jpg', '原料：大米、猪肉', 1, 0, '2022-06-19 14:20:34', '2022-06-19 14:20:34', 1, 1, 0);
INSERT INTO `dish` VALUES (1523230460363255810, '豆干肉丝+米饭', 1523221580623228930, 1500.00, '', '748e219d-7fe2-4634-9214-b80678b0de25d.jpg', '原料：大米、豆干、猪肉', 1, 0, '2022-06-19 14:15:41', '2022-06-19 14:15:41', 1, 1, 0);
INSERT INTO `dish` VALUES (1523230566508507138, '鱼香肉丝+米饭', 1523221580623228930, 1500.00, '', '74a09de6-58fc-4565-81c5-5c4ddcec8b6fc.jpg', '原料：大米、猪肉', 1, 0, '2022-06-19 14:21:45', '2022-06-19 14:21:45', 1, 1, 0);
INSERT INTO `dish` VALUES (1523230706002669569, '老盐菜炒回锅肉（米饭单点）', 1523221607961702401, 1398.00, '', 'e16bdd95-2dbd-4cc5-b258-7055353bde5a1.jpg', '原料：猪肉', 1, 0, '2022-06-19 14:14:49', '2022-06-19 14:14:49', 1, 1, 0);
INSERT INTO `dish` VALUES (1523230887100133378, '剁椒鲫鱼（米饭单点）', 1523221607961702401, 2198.00, '', '860c01a5-707f-48d8-a451-a8b2078b6b85a.jpg', '原料：鲫鱼', 1, 0, '2022-06-19 14:14:37', '2022-06-19 14:14:37', 1, 1, 0);
INSERT INTO `dish` VALUES (1523231074837180418, '泡椒儿肠（米饭单点）', 1523221607961702401, 1698.00, '', '0086a90b-0a7e-45ec-8c0b-10285d3bffb19.jpg', '原料：猪小肠', 1, 0, '2022-06-19 14:13:31', '2022-06-19 14:13:31', 1, 1, 0);
INSERT INTO `dish` VALUES (1523231271495512066, '干锅鸡+米饭', 1523221632653570050, 1980.00, '', 'e0ebca04-ab15-46d4-acc9-d6b1f9de7a4b2.jpg', '原料：鸡肉', 1, 0, '2022-06-19 14:19:42', '2022-06-19 14:19:42', 1, 1, 0);
INSERT INTO `dish` VALUES (1523231432867164162, '豆花+油碟+米饭', 1523221667613093889, 980.00, '', '168782c6-7bda-4187-958e-bcc118a421456.jpg', '原料：豆腐脑', 1, 0, '2022-06-19 14:11:32', '2022-06-19 14:11:32', 1, 1, 0);
INSERT INTO `dish` VALUES (1523231582276661250, '干烧鲫鱼（不含米饭）', 1523221667613093889, 1898.00, '', '741b54fa-241f-4deb-8cd6-66b6a597e358d.jpg', '原料：鲫鱼', 1, 0, '2022-06-19 14:14:14', '2022-06-19 14:14:14', 1, 1, 0);
INSERT INTO `dish` VALUES (1523231728100028417, '红烧肉（不含米饭）', 1523221667613093889, 1498.00, '', 'd71472dc-d897-4a41-8b77-0c8cd2a0a68cd.jpg', '原料：土豆、猪肉', 1, 0, '2022-06-19 14:18:42', '2022-06-19 14:18:42', 1, 1, 0);
INSERT INTO `dish` VALUES (1523231842168320002, '土豆烧鸡（不含米饭）', 1523221667613093889, 2000.00, '', '14d802e3-8998-4211-9b89-e93b828668f11.jpg', '原料：土豆、鸡肉', 1, 0, '2022-06-19 14:18:20', '2022-06-19 14:18:20', 1, 1, 0);
INSERT INTO `dish` VALUES (1523231978755829761, '农家炒香肠（不含米饭）', 1523221667613093889, 1698.00, '', 'fbdbf963-0901-4a00-be21-6cc937e24c67d.jpg', '原料：香肠', 1, 0, '2022-06-19 14:11:18', '2022-06-19 14:11:18', 1, 1, 0);
INSERT INTO `dish` VALUES (1523232102403911681, '三线莲白（不含米饭）', 1523221667613093889, 1398.00, '', '685f187f-3956-4cc2-b360-2be6b6d9997c4.jpg', '原料：大白菜', 1, 0, '2022-06-19 14:18:03', '2022-06-19 14:18:03', 1, 1, 0);
INSERT INTO `dish` VALUES (1523232185912504321, '紫菜蛋花汤（不含米饭）', 1523221692862803970, 880.00, '', '1e52f26c-82ea-4d1c-b5d8-42ffe2c3a46b4.jpg', '原料：紫菜、鸡蛋', 1, 0, '2022-06-19 14:08:47', '2022-06-19 14:08:47', 1, 1, 0);
INSERT INTO `dish` VALUES (1523232325142425601, '酸菜粉丝汤（不含米饭）', 1523221692862803970, 780.00, '', '06c223e0-3e65-443e-8271-9ffe2357e12b0.jpg', '原料：粉丝、酸菜', 1, 0, '2022-06-19 14:08:17', '2022-06-19 14:08:17', 1, 1, 0);
INSERT INTO `dish` VALUES (1523232409104003074, '肉片汤（不含米饭）', 1523221692862803970, 2200.00, '', '8e920254-d9f5-44de-bef0-1f10254ff30ff.jpg', '原料：猪肉', 1, 0, '2022-06-19 14:08:07', '2022-06-19 14:08:07', 1, 1, 0);
INSERT INTO `dish` VALUES (1523232518197850114, '番茄鸡蛋汤（不含米饭）', 1523221692862803970, 880.00, '', '374ce401-cf55-4764-9bc5-4567477c75a25.jpg', '原料：番茄、 鸡蛋', 1, 0, '2022-06-19 14:05:51', '2022-06-19 14:05:51', 1, 1, 0);
INSERT INTO `dish` VALUES (1523232652012924930, '红烧茄子（不含米饭）', 1523223881484185602, 856.00, '', '21646dbb-dfcc-4027-9895-bca4492ddeae6.jpg', '原料：茄子', 1, 0, '2022-06-19 14:16:27', '2022-06-19 14:16:27', 1, 1, 0);
INSERT INTO `dish` VALUES (1523232750734258177, '炒胡豆（不含米饭）', 1523223881484185602, 788.00, '', 'da315456-20f9-4cef-9438-cd804e74ebeec.jpg', '原料：胡豆', 1, 0, '2022-06-19 14:06:59', '2022-06-19 14:06:59', 1, 1, 0);
INSERT INTO `dish` VALUES (1523232846934814722, '干煸土豆丝（不含米饭）', 1523223881484185602, 1500.00, '', '507e35d9-068f-4161-9bb9-b232dd7ddd2be.jpg', '原料：土豆', 1, 0, '2022-06-19 14:07:10', '2022-06-19 14:07:10', 1, 1, 0);
INSERT INTO `dish` VALUES (1523233237814587394, '红苕稀饭', 1523224111688560641, 168.00, '', '95f1bf72-0e72-4db0-8b83-1802daea52287.jpg', '原料：大米', 1, 0, '2022-06-19 14:07:19', '2022-06-19 14:07:19', 1, 1, 0);
INSERT INTO `dish` VALUES (1523233316415844354, '香肠炒饭', 1523224111688560641, 998.00, '', '9e251dec-834c-4096-b105-b64ce69ed854d.jpg', '原料：大米、香肠', 1, 0, '2022-06-19 14:07:41', '2022-06-19 14:07:41', 1, 1, 0);
INSERT INTO `dish` VALUES (1523233498566078465, '鸡蛋炒饭', 1523224111688560641, 688.00, '', '7129c01e-dcc6-4c7a-9822-bc7c47a663d54.jpg', '原料：大米、鸡蛋', 1, 0, '2022-06-19 14:07:54', '2022-06-19 14:07:54', 1, 1, 0);
INSERT INTO `dish` VALUES (1523233618137296897, '珍珠米饭', 1523224111688560641, 100.00, '', '57234cb1-f2ab-451b-ac11-c0e0002def2b8.jpg', '原料：大米', 1, 0, '2022-06-19 02:47:24', '2022-06-19 02:47:24', 1, 1, 0);
INSERT INTO `dish` VALUES (1523233739667255298, '怡宝', 1523224134312636417, 200.00, '', '8bbf6a9b-601a-447b-baba-c500b5df57625.jpg', '原料：水', 1, 0, '2022-06-19 14:04:00', '2022-06-19 14:04:00', 1, 1, 0);

-- ----------------------------
-- Table structure for dish_flavor
-- ----------------------------
DROP TABLE IF EXISTS `dish_flavor`;
CREATE TABLE `dish_flavor`  (
  `id` bigint(0) NOT NULL COMMENT '主键',
  `dish_id` bigint(0) NOT NULL COMMENT '菜品',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '口味名称',
  `value` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '口味数据list',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  `create_user` bigint(0) NOT NULL COMMENT '创建人',
  `update_user` bigint(0) NOT NULL COMMENT '修改人',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '菜品口味关系表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of dish_flavor
-- ----------------------------
INSERT INTO `dish_flavor` VALUES (1397849417888346113, 1397849417854791681, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]', '2021-05-27 09:37:27', '2021-05-27 09:37:27', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1397849936421761025, 1397849936404983809, '忌口', '[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]', '2021-05-27 09:39:30', '2021-05-27 09:39:30', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1397849936438538241, 1397849936404983809, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]', '2021-05-27 09:39:30', '2021-05-27 09:39:30', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1397850630734262274, 1397850630700707841, '忌口', '[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]', '2021-05-27 09:42:16', '2021-05-27 09:42:16', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1397850630755233794, 1397850630700707841, '辣度', '[\"微辣\",\"中辣\",\"重辣\"]', '2021-05-27 09:42:16', '2021-05-27 09:42:16', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1397853423486414850, 1397853423461249026, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]', '2021-05-27 09:53:22', '2021-05-27 09:53:22', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1397854133632413697, 1397854133603053569, '温度', '[\"热饮\",\"常温\",\"去冰\",\"少冰\",\"多冰\"]', '2021-05-27 09:56:11', '2021-05-27 09:56:11', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1397855742303186946, 1397855742273826817, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]', '2021-05-27 10:02:35', '2021-05-27 10:02:35', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1397855906497605633, 1397855906468245506, '忌口', '[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]', '2021-05-27 10:03:14', '2021-05-27 10:03:14', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1397856190573621250, 1397856190540066818, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]', '2021-05-27 10:04:21', '2021-05-27 10:04:21', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1397859056709316609, 1397859056684150785, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]', '2021-05-27 10:15:45', '2021-05-27 10:15:45', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1397859277837217794, 1397859277812051969, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]', '2021-05-27 10:16:37', '2021-05-27 10:16:37', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1397859487502086146, 1397859487476920321, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]', '2021-05-27 10:17:27', '2021-05-27 10:17:27', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1397859757061615618, 1397859757036449794, '甜味', '[\"无糖\",\"少糖\",\"半躺\",\"多糖\",\"全糖\"]', '2021-05-27 10:18:32', '2021-05-27 10:18:32', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1397861135754506242, 1397861135733534722, '甜味', '[\"无糖\",\"少糖\",\"半躺\",\"多糖\",\"全糖\"]', '2021-05-27 10:24:00', '2021-05-27 10:24:00', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1397861370035744769, 1397861370010578945, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]', '2021-05-27 10:24:56', '2021-05-27 10:24:56', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1397861898467717121, 1397861898438356993, '忌口', '[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]', '2021-05-27 10:27:02', '2021-05-27 10:27:02', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1398089545865015297, 1398089545676271617, '温度', '[\"热饮\",\"常温\",\"去冰\",\"少冰\",\"多冰\"]', '2021-05-28 01:31:38', '2021-05-28 01:31:38', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1398089782323097601, 1398089782285348866, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]', '2021-05-28 01:32:34', '2021-05-28 01:32:34', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1398090003262255106, 1398090003228700673, '忌口', '[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]', '2021-05-28 01:33:27', '2021-05-28 01:33:27', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1398090264554811394, 1398090264517062657, '忌口', '[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]', '2021-05-28 01:34:29', '2021-05-28 01:34:29', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1398090455399837698, 1398090455324340225, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]', '2021-05-28 01:35:14', '2021-05-28 01:35:14', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1398090685449023490, 1398090685419663362, '温度', '[\"热饮\",\"常温\",\"去冰\",\"少冰\",\"多冰\"]', '2021-05-28 01:36:09', '2021-05-28 01:36:09', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1398090825358422017, 1398090825329061889, '忌口', '[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]', '2021-05-28 01:36:43', '2021-05-28 01:36:43', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1398091007051476993, 1398091007017922561, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]', '2021-05-28 01:37:26', '2021-05-28 01:37:26', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1398091296164851713, 1398091296131297281, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]', '2021-05-28 01:38:35', '2021-05-28 01:38:35', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1398091546531246081, 1398091546480914433, '忌口', '[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]', '2021-05-28 01:39:35', '2021-05-28 01:39:35', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1398091729809747969, 1398091729788776450, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]', '2021-05-28 01:40:18', '2021-05-28 01:40:18', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1398091889499484161, 1398091889449152513, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]', '2021-05-28 01:40:56', '2021-05-28 01:40:56', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1398092095179763713, 1398092095142014978, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]', '2021-05-28 01:41:45', '2021-05-28 01:41:45', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1398092283877306370, 1398092283847946241, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]', '2021-05-28 01:42:30', '2021-05-28 01:42:30', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1398094018939236354, 1398094018893099009, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]', '2021-05-28 01:49:24', '2021-05-28 01:49:24', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1398094391494094850, 1398094391456346113, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]', '2021-05-28 01:50:53', '2021-05-28 01:50:53', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1399574026165727233, 1399305325713600514, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]', '2021-06-01 03:50:25', '2021-06-01 03:50:25', 1399309715396669441, 1399309715396669441, 0);
INSERT INTO `dish_flavor` VALUES (1523231271495512067, 1523231271495512066, '辣度', '[\"不辣\",\"微辣\",\"中辣\",\"重辣\"]', '2022-06-19 14:19:42', '2022-06-19 14:19:42', 1, 1, 0);
INSERT INTO `dish_flavor` VALUES (1523231582276661251, 1523231582276661250, '忌口', '[\"不要葱\",\"不要蒜\",\"不要香菜\",\"不要辣\"]', '2022-06-19 14:14:14', '2022-06-19 14:14:14', 1, 1, 0);

-- ----------------------------
-- Table structure for employee
-- ----------------------------
DROP TABLE IF EXISTS `employee`;
CREATE TABLE `employee`  (
  `id` bigint(0) NOT NULL COMMENT '主键',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '姓名',
  `username` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '用户名',
  `password` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '密码',
  `phone` varchar(11) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '手机号',
  `sex` varchar(2) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '性别',
  `id_number` varchar(18) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '身份证号',
  `status` int(0) NOT NULL DEFAULT 1 COMMENT '状态 0:禁用，1:正常',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  `create_user` bigint(0) NOT NULL COMMENT '创建人',
  `update_user` bigint(0) NOT NULL COMMENT '修改人',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_username`(`username`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '员工信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of employee
-- ----------------------------
INSERT INTO `employee` VALUES (1, '管理员', 'admin', 'e10adc3949ba59abbe56e057f20f883e', '13812312312', '1', '110101199001010047', 1, '2021-05-06 17:20:07', '2022-05-05 07:30:15', 1, 1);

-- ----------------------------
-- Table structure for order_detail
-- ----------------------------
DROP TABLE IF EXISTS `order_detail`;
CREATE TABLE `order_detail`  (
  `id` bigint(0) NOT NULL COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '名字',
  `image` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '图片',
  `order_id` bigint(0) NOT NULL COMMENT '订单id',
  `dish_id` bigint(0) NULL DEFAULT NULL COMMENT '菜品id',
  `setmeal_id` bigint(0) NULL DEFAULT NULL COMMENT '套餐id',
  `dish_flavor` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '口味',
  `number` int(0) NOT NULL DEFAULT 1 COMMENT '数量',
  `amount` decimal(10, 2) NOT NULL COMMENT '金额',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '订单明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of order_detail
-- ----------------------------

-- ----------------------------
-- Table structure for orders
-- ----------------------------
DROP TABLE IF EXISTS `orders`;
CREATE TABLE `orders`  (
  `id` bigint(0) NOT NULL COMMENT '主键',
  `number` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '订单号',
  `status` int(0) NOT NULL DEFAULT 1 COMMENT '订单状态 1待付款，2待派送，3已派送，4已完成，5已取消',
  `user_id` bigint(0) NOT NULL COMMENT '下单用户',
  `address_book_id` bigint(0) NOT NULL COMMENT '地址id',
  `order_time` datetime(0) NOT NULL COMMENT '下单时间',
  `checkout_time` datetime(0) NOT NULL COMMENT '结账时间',
  `pay_method` int(0) NOT NULL DEFAULT 1 COMMENT '支付方式 1微信,2支付宝',
  `amount` decimal(10, 2) NOT NULL COMMENT '实收金额',
  `remark` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '备注',
  `phone` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `address` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  `consignee` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '订单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of orders
-- ----------------------------

-- ----------------------------
-- Table structure for setmeal
-- ----------------------------
DROP TABLE IF EXISTS `setmeal`;
CREATE TABLE `setmeal`  (
  `id` bigint(0) NOT NULL COMMENT '主键',
  `category_id` bigint(0) NOT NULL COMMENT '菜品分类id',
  `name` varchar(64) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '套餐名称',
  `price` decimal(10, 2) NOT NULL COMMENT '套餐价格',
  `status` int(0) NULL DEFAULT NULL COMMENT '状态 0:停用 1:启用',
  `code` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '编码',
  `description` varchar(512) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '描述信息',
  `image` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '图片',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  `create_user` bigint(0) NOT NULL COMMENT '创建人',
  `update_user` bigint(0) NOT NULL COMMENT '修改人',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_setmeal_name`(`name`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '套餐' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of setmeal
-- ----------------------------
INSERT INTO `setmeal` VALUES (1523233943468486658, 1523224049252151297, '农家腊肉+小菜+米饭', 1998.00, 1, '', '原料：大米、青菜、腊肉', '6c0e74e0-d11b-49a7-ad15-83d61e10611bc.jpg', '2022-06-19 14:29:33', '2022-06-19 14:29:33', 1, 1, 0);
INSERT INTO `setmeal` VALUES (1523234378937905154, 1523224049252151297, '农家小炒肉+小菜+米饭', 1900.00, 1, '', '原料：大米、猪肉', '433cb039-d471-49b6-8615-3263a44c68b52.jpg', '2022-06-19 14:29:05', '2022-06-19 14:29:05', 1, 1, 0);
INSERT INTO `setmeal` VALUES (1523234624120139777, 1523224049252151297, '尖椒鸡+小菜+米饭', 1988.00, 1, '', '原料：大米、辣椒、青菜、鸡肉', '7fe7b356-1374-4575-be48-cc36d40a24488.jpg', '2022-06-19 14:29:17', '2022-06-19 14:29:17', 1, 1, 0);

-- ----------------------------
-- Table structure for setmeal_dish
-- ----------------------------
DROP TABLE IF EXISTS `setmeal_dish`;
CREATE TABLE `setmeal_dish`  (
  `id` bigint(0) NOT NULL COMMENT '主键',
  `setmeal_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '套餐id ',
  `dish_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL COMMENT '菜品id',
  `name` varchar(32) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '菜品名称 （冗余字段）',
  `price` decimal(10, 2) NULL DEFAULT NULL COMMENT '菜品原价（冗余字段）',
  `copies` int(0) NOT NULL COMMENT '份数',
  `sort` int(0) NOT NULL DEFAULT 0 COMMENT '排序',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `update_time` datetime(0) NOT NULL COMMENT '更新时间',
  `create_user` bigint(0) NOT NULL COMMENT '创建人',
  `update_user` bigint(0) NOT NULL COMMENT '修改人',
  `is_deleted` int(0) NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '套餐菜品关系' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of setmeal_dish
-- ----------------------------

-- ----------------------------
-- Table structure for shopping_cart
-- ----------------------------
DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart`  (
  `id` bigint(0) NOT NULL COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '名称',
  `image` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '图片',
  `user_id` bigint(0) NOT NULL COMMENT '主键',
  `dish_id` bigint(0) NULL DEFAULT NULL COMMENT '菜品id',
  `setmeal_id` bigint(0) NULL DEFAULT NULL COMMENT '套餐id',
  `dish_flavor` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '口味',
  `number` int(0) NOT NULL DEFAULT 1 COMMENT '数量',
  `amount` decimal(10, 2) NOT NULL COMMENT '金额',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '购物车' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of shopping_cart
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint(0) NOT NULL COMMENT '主键',
  `name` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '姓名',
  `phone` varchar(100) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '手机号',
  `sex` varchar(2) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '性别',
  `id_number` varchar(18) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '身份证号',
  `avatar_url` varchar(500) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '头像',
  `status` int(0) NULL DEFAULT 1 COMMENT '状态 0:禁用，1:正常',
  `create_time` datetime(0) NOT NULL COMMENT '注册时间',
  `openid` varchar(90) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '微信openid',
  `nick_name` varchar(150) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '微信昵称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin COMMENT = '用户信息' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
