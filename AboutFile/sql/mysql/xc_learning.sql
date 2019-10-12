-- MySQL dump 10.13  Distrib 5.5.62, for Win64 (AMD64)
--
-- Host: localhost    Database: xc_learning
-- ------------------------------------------------------
-- Server version	5.7.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `xc_learning_course`
--

DROP TABLE IF EXISTS `xc_learning_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `xc_learning_course` (
  `id` varchar(32) NOT NULL,
  `course_id` varchar(32) NOT NULL COMMENT '课程id',
  `user_id` varchar(32) NOT NULL COMMENT '用户id',
  `valid` varchar(32) DEFAULT NULL COMMENT '有效性',
  `start_time` datetime DEFAULT NULL,
  `end_time` datetime DEFAULT NULL,
  `status` varchar(32) DEFAULT NULL COMMENT '选课状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `xc_learning_list_unique` (`course_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xc_learning_course`
--

LOCK TABLES `xc_learning_course` WRITE;
/*!40000 ALTER TABLE `xc_learning_course` DISABLE KEYS */;
INSERT INTO `xc_learning_course` VALUES ('402882c76db399eb016db399feb10000','4028e581617f945f01617f9dabc40000','49',NULL,NULL,NULL,'501001'),('402882c76db399eb016db399ff420001','4028e581617f945f01617f9dabc40001','49',NULL,NULL,NULL,'501001');
/*!40000 ALTER TABLE `xc_learning_course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `xc_task_his`
--

DROP TABLE IF EXISTS `xc_task_his`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `xc_task_his` (
  `id` varchar(32) NOT NULL COMMENT '任务id',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  `task_type` varchar(32) DEFAULT NULL COMMENT '任务类型',
  `mq_exchange` varchar(64) DEFAULT NULL COMMENT '交换机名称',
  `mq_routingkey` varchar(64) DEFAULT NULL COMMENT 'routingkey',
  `request_body` varchar(512) DEFAULT NULL COMMENT '任务请求的内容',
  `version` int(10) DEFAULT '0' COMMENT '乐观锁版本号',
  `status` varchar(32) DEFAULT NULL COMMENT '任务状态',
  `errormsg` varchar(512) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `xc_task_his`
--

LOCK TABLES `xc_task_his` WRITE;
/*!40000 ALTER TABLE `xc_task_his` DISABLE KEYS */;
INSERT INTO `xc_task_his` VALUES ('10','2018-04-04 22:58:20','2018-07-20 12:24:10','2018-07-16 12:24:36',NULL,'ex_learning_addchoosecourse','addchoosecourse','{\"userId\":\"49\",\"courseId\":\"4028e581617f945f01617f9dabc40000\"}',NULL,'10201',NULL),('11','2018-07-16 12:28:03','2018-07-20 12:24:10','2018-07-16 12:29:11',NULL,'ex_learning_addchoosecourse','addchoosecourse','{\"userId\":\"49\",\"courseId\":\"4028e581617f945f01617f9dabc40001\"}',NULL,'10201',NULL);
/*!40000 ALTER TABLE `xc_task_his` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'xc_learning'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-10-12 15:16:34
