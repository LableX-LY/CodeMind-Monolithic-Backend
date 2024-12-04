# 数据库初始化
# @auther X_LY。

-- 创建库
CREATE DATABASE IF NOT EXISTS codemind;

-- 切换库
use codemind;

-- 用户表（软删除）
CREATE TABLE IF NOT EXISTS user
(
    id 						BIGINT 																				PRIMARY KEY COMMENT 'id',
    userAccount 	VARCHAR(256) 																	NOT NULL COMMENT '账号',
    userPassword  VARCHAR(512) 																	NOT NULL COMMENT '密码',
    userName 			VARCHAR(256) 																	NULL COMMENT '用户昵称',
    userAvatar 		VARCHAR(1024) 																NULL COMMENT '用户头像',
    userGender 		TINYINT 																			NULL COMMENT '用户性别',
    userBirthday	VARCHAR(256) 																	NULL COMMENT '用户生日',
    userEmail 		VARCHAR(256) 																	NULL COMMENT '用户邮箱',
    userTags 			VARCHAR(1024) 																NULL COMMENT '用户标签',
    userProfile 	VARCHAR(1024) 																NULL COMMENT '用户简介',
    userRole 			VARCHAR(256) 		DEFAULT 'user' 								NOT NULL COMMENT '用户角色,user/admin',
    userStatus 		INT 						DEFAULT 0 										NOT NULL COMMENT '用户状态,0-正常,1-禁用',
    bannedTime		DATETIME																			NULL COMMENT '被Ban时间',
    unbannedTime	DATETIME																			NULL COMMENT '解封时间',
    createTime 		DATETIME 				DEFAULT CURRENT_TIMESTAMP 		NOT NULL COMMENT '创建时间',
    updateTime 		DATETIME 				DEFAULT CURRENT_TIMESTAMP 		NOT NULL COMMENT '更新时间',
    isDelete 			TINYINT 				DEFAULT 0 										NOT NULL COMMENT '是否删除,0-正常,1-删除'
    ) COMMENT '用户表' COLLATE = utf8mb4_unicode_ci;
CREATE INDEX idx_unionId ON user (id);

-- 题目表（软删除）
CREATE TABLE IF NOT EXISTS question
(
    id 									BIGINT 																					PRIMARY KEY COMMENT 'id',
    questionTitle 			VARCHAR(512) 																		NOT NULL COMMENT '题目标题',
    questionContent 		TEXT 																						NOT NULL COMMENT '题目内容',
    questionTags 				VARCHAR(1024) 																	NOT NULL COMMENT '题目标签列表(JSON数组)',
    judgeCase 					TEXT 																						NOT NULL COMMENT '判题用例(JSON数组)',
    judgeConfig 				TEXT 																						NOT NULL COMMENT '判题配置(JSON对象)',
    questionAnswer 			TEXT 																						NOT NULL COMMENT '题目答案',
    questionDifficulty 	INT 						DEFAULT 2												NULL     COMMENT '题目难度,1-简单,2-一般,3-中等,4-难,5-困难',
    submitNum 					INT 						DEFAULT 0 											NOT NULL COMMENT '题目提交数',
    acceptedNum 				INT 						DEFAULT 0 											NOT NULL COMMENT '题目通过数',
    createUser		 			BIGINT 																					NOT NULL COMMENT '题目创建人',
    editUser						BIGINT																					NULL     COMMENT '题目修改人',
    questionStatus 			INT 						DEFAULT 0 											NOT NULL COMMENT '题目状态,0-正常,1-禁用',
    createTime 					DATETIME 				DEFAULT CURRENT_TIMESTAMP 			NOT NULL COMMENT '创建时间',
    updateTime 					DATETIME 				DEFAULT CURRENT_TIMESTAMP 			NOT NULL COMMENT '更新时间',
    isDelete 						TINYINT 				DEFAULT 0 											NOT NULL COMMENT '是否删除,0-正常,1-删除'
) COMMENT '题目表' COLLATE = utf8mb4_unicode_ci;
CREATE INDEX idx_unionId ON question (id);
CREATE INDEX idx_userId ON question (createUser);

-- 题目提交表（软删除）
CREATE TABLE IF NOT EXISTS question_submit
(
    id 								BIGINT 																					PRIMARY KEY COMMENT 'id',
    userId 						BIGINT 																					NOT NULL COMMENT '提交者',
    questionId 				BIGINT 																					NOT NULL COMMENT '题目id',
    questionLanguage 	VARCHAR(128) 		DEFAULT 'java'									NOT NULL COMMENT '题目使用的编程语言',
    questionCode 			TEXT 																						NOT NULL COMMENT '用户代码',
    judgeStatus 			INT 						DEFAULT 0 											NOT NULL COMMENT '判题状态,0-待判题、1-判题中、2-判题成功、3-判题失败',
    judgeInfo 				TEXT 	                                          NULL COMMENT '判题信息(JSON对象)',
    createTime 				DATETIME 				DEFAULT CURRENT_TIMESTAMP 			NOT NULL COMMENT '提交时间',
    updateTime 				DATETIME 				DEFAULT CURRENT_TIMESTAMP 			NOT NULL COMMENT '更新时间',
    isDelete 					TINYINT 				DEFAULT 0 											NOT NULL COMMENT '是否删除,0-正常,1-删除'
    ) COMMENT '题目提交表' COLLATE = utf8mb4_unicode_ci;
CREATE INDEX idx_questionId ON question_submit (questionId);
CREATE INDEX idx_userId ON question_submit (userId);