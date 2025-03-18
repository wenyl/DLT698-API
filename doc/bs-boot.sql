/*
 Navicat Premium Data Transfer

 Source Server         : 采后
 Source Server Type    : MySQL
 Source Server Version : 50717
 Source Host           : ch.ythxdny.com:43206
 Source Schema         : bs-boot

 Target Server Type    : MySQL
 Target Server Version : 50717
 File Encoding         : 65001

 Date: 08/04/2024 13:47:02
*/
CREATE DATABASE `bs-boot` /*!40100 DEFAULT CHARACTER SET utf8mb4 */;
USE `bs-boot`;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'ID',
  `parent_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '上级ID',
  `is_leaf` tinyint(1) DEFAULT 0 COMMENT '是否有下级，0-否，1-是',
  `dept_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '部门名称',
  `dept_order` int(11) DEFAULT 0 COMMENT '排序',
  `dept_desc` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '描述信息',
  `dept_category` tinyint(10) NOT NULL DEFAULT 1 COMMENT '机构类别 1公司，2部门，3项目组',
  `address` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '地址',
  `del_flag` tinyint(1) DEFAULT 0 COMMENT '删除状态（0-正常，1-已删除）',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建日期',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新日期',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '组织机构' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('1731858842567655424', NULL, 0, '昆明理工大学', 1, '一所好学校', 1, '呈贡', 0, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_resource
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `parent_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '父资源id',
  `resource_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '资源名称(菜单名、按钮名)',
  `menu_url` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'vue菜单访问路径',
  `menu_path` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单路径(包含父菜单地址)',
  `menu_component` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT 'vue菜单组件',
  `redirect` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '一级菜单跳转地址',
  `resource_type` int(11) DEFAULT NULL COMMENT '资源类型(0:菜单; 1:按钮;)',
  `perms` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '资源权限编码',
  `sort_no` double(8, 2) DEFAULT NULL COMMENT '资源排序',
  `menu_icon` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '菜单图标',
  `is_route` tinyint(1) DEFAULT 1 COMMENT '是否路由菜单: 0:不是  1:是（默认值1）',
  `is_leaf` tinyint(1) DEFAULT NULL COMMENT '是否叶子节点:    1:是   0:不是',
  `description` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '描述',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  `del_flag` int(1) DEFAULT 0 COMMENT '删除状态 0正常 1已删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '资源权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_resource
-- ----------------------------
INSERT INTO `sys_resource` VALUES ('1748355623381299202', NULL, '系统管理', '/system', '/system', 'Layout', '', 0, '', 0.00, '', 0, 0, '', NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_resource` VALUES ('1762750274775195650', NULL, '首页', '/home', '/home', 'Layout', '/home/index', 0, NULL, 0.00, NULL, 0, 1, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_resource` VALUES ('1764529312481071106', '1762750274775195650', '首页', 'index', '/home/index', '@/views/home/Index.vue', '', 0, NULL, 0.00, NULL, 1, 1, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_resource` VALUES ('1764895863071715330', '1748355623381299202', '用户管理', 'sysUser', '/system/sysUser', '@/views/system/sysUser/SysUserList.vue', NULL, 0, NULL, 0.00, NULL, 1, 0, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_resource` VALUES ('1764896148200501250', '1748355623381299202', '组织管理', 'sysDept', '/system/sysDept', '@/views/system/sysDept/SysDeptList.vue', NULL, 0, NULL, 0.00, NULL, 1, 1, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_resource` VALUES ('1764896990496436225', '1748355623381299202', '角色管理', 'sysRole', '/system/sysRole', '@/views/system/sysRole/SysRoleList.vue', NULL, 0, NULL, 0.00, NULL, 1, 1, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_resource` VALUES ('1764897091826626562', '1748355623381299202', '资源管理', 'sysResource', '/system/sysResource', '@/views/system/sysResource/SysResourceList.vue', NULL, 0, NULL, 0.00, NULL, 1, 1, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_resource` VALUES ('1764927108640026626', '1764895863071715330', '新增', '', NULL, '', '', 1, 'sysUser:save', 0.00, '', 0, 1, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_resource` VALUES ('1764927452510040065', '1764895863071715330', '修改', '', NULL, '', '', 1, 'sysUser:edit', 0.00, '', 0, 1, NULL, NULL, NULL, NULL, NULL, 0);
INSERT INTO `sys_resource` VALUES ('1764927575159877633', '1764895863071715330', '删除', '', NULL, '', '', 1, 'sysUser:delete', 0.00, '', 0, 1, NULL, NULL, NULL, NULL, NULL, 0);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `role_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色名称',
  `role_code` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '角色编码',
  `role_desc` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '描述',
  `del_flag` tinyint(1) DEFAULT 0 COMMENT '删除状态（0-正常，1-已删除）',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1731858842567655425', '管理员', 'admin', '系统管理员账号，具有所有权限', 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role` VALUES ('1773242177646587906', '游客', 'vistor', '游客权限', 0, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_resource`;
CREATE TABLE `sys_role_resource`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `role_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '角色id',
  `sys_resource_id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '系统资源ID',
  `is_half_checked` tinyint(1) DEFAULT NULL COMMENT '是否半选中',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '角色权限表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_resource
-- ----------------------------
INSERT INTO `sys_role_resource` VALUES ('1773242059828588546', '1731858842567655425', '1748355623381299202', 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_resource` VALUES ('1773242059828588547', '1731858842567655425', '1764895863071715330', 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_resource` VALUES ('1773242059828588548', '1731858842567655425', '1764927108640026626', 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_resource` VALUES ('1773242059828588549', '1731858842567655425', '1764927452510040065', 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_resource` VALUES ('1773242059828588550', '1731858842567655425', '1764927575159877633', 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_resource` VALUES ('1773242059828588551', '1731858842567655425', '1764896148200501250', 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_resource` VALUES ('1773242059828588552', '1731858842567655425', '1764896990496436225', 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_resource` VALUES ('1773242059828588553', '1731858842567655425', '1764897091826626562', 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_resource` VALUES ('1773242059828588554', '1731858842567655425', '1762750274775195650', 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_resource` VALUES ('1773242059828588555', '1731858842567655425', '1764529312481071106', 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_resource` VALUES ('1773242466227286018', '1773242177646587906', '1762750274775195650', 0, NULL, NULL, NULL, NULL);
INSERT INTO `sys_role_resource` VALUES ('1773242466227286019', '1773242177646587906', '1764529312481071106', 0, NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主键id',
  `username` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '登录账号',
  `realname` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '真实姓名',
  `password` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '密码',
  `avatar` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '头像',
  `email` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '邮件',
  `phone` varchar(45) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '电话',
  `dept_id` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '所属组织ID',
  `status` tinyint(1) DEFAULT 0 COMMENT '账号状态(0-正常,1-冻结)',
  `del_flag` tinyint(1) DEFAULT 0 COMMENT '删除状态(0-正常,1-已删除)',
  `duties` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '职务',
  `create_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(0) DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(0) DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '系统用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1742083042713985026', '1', '222', '1', '2', '2282204743@qq.com', '2', '2', 0, 0, '2', NULL, NULL, NULL, NULL);
INSERT INTO `sys_user` VALUES ('1742091154732802050', '2', '2', '2', '2', '2', '2', '2', 0, 0, '2', NULL, NULL, NULL, NULL);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '主键ID',
  `user_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '用户ID',
  `role_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1767752034837291009', '1742091154732802050', '1731858842567655425');
INSERT INTO `sys_user_role` VALUES ('1767752094727757826', '1742083042713985026', '1731858842567655425');

SET FOREIGN_KEY_CHECKS = 1;
