-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: jevdaya_db
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `aadhaar_card` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `pan_card` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKob8kqyqqgmefl0aco34akdtpe` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (1,'ITf5PUADaSM0dHQHgv+nSQ==','mahavir@gmail.com','mahavir jain','le6oGx2O1QFqt5THKcs6Mw==','$2a$10$oKnyViM29ZUdXcU.CfKrOe2NNgXJk3SFbLDm28Po.g.2T8eTAr8VK'),(2,'kQQbsCHJeSj2NFgj+UscLw==','komal@gmail.com','demo','6Sg/6s/khCui5q6sEqCIlg==','$2a$10$3ZcTzpdZGwiZjbMbXsoSOOsY8aMU6RcHGmzXu5FakBjfDL5HmLqZi'),(3,'WXu2kbOyiWIrkBVkY04heg==','paras@gmail.com','paras jain','Ic5LU9zneY1LA0wkOFWjmg==','$2a$10$xffYjKA9fz6Mp8vdhq4gg.XEXZ3dzWXuF7qrlnQdVHErCourQRIm6'),(4,'eUuR5IfiCHbzdgbqykqA5Q==','rutuja@gmail.com','rutuja','xzFwJNu1qB/7nL948yXMdg==','$2a$10$KwC54mVWG2Y9FX/wdaqFbu8mXsyilhMnMjN/nHMkpZf9AYo4zYZJO'),(5,'oaflciWXTM+tygFBJsGlpw==','rutujajain@gmail.com','rutuja jain','lV7+UOgq3/c8dOzR5BPMDw==','$2a$10$a5yMsGeYmKquHQ4BZVcG2uONjp4nY.xMco.UWwn8K2ijodYZVliVG');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-26 17:57:13
