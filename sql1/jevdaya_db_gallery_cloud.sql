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
-- Table structure for table `gallery_cloud`
--

DROP TABLE IF EXISTS `gallery_cloud`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `gallery_cloud` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `path` varchar(255) DEFAULT NULL,
  `gallery_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK9pin19ycbvaquhbduh6x9ei3g` (`gallery_id`),
  CONSTRAINT `FK9pin19ycbvaquhbduh6x9ei3g` FOREIGN KEY (`gallery_id`) REFERENCES `gallery` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `gallery_cloud`
--

LOCK TABLES `gallery_cloud` WRITE;
/*!40000 ALTER TABLE `gallery_cloud` DISABLE KEYS */;
INSERT INTO `gallery_cloud` VALUES (1,'https://res.cloudinary.com/ddchyegwp/image/upload/v1775309212/itu53h45nnzybqridspe.jpg',2),(2,'https://res.cloudinary.com/ddchyegwp/image/upload/v1775309243/q2jnm0avirjmcc7nqpuh.jpg',3),(3,'https://res.cloudinary.com/ddchyegwp/image/upload/v1775309733/gma2ajdyttinqv1q7ogf.jpg',4),(4,'https://res.cloudinary.com/ddchyegwp/image/upload/v1775316720/uvoz7uicbfxy3nc5gr4z.png',5),(5,'https://res.cloudinary.com/ddchyegwp/image/upload/v1775317333/pub5egiuxqzncuktiqlb.jpg',6),(6,'https://res.cloudinary.com/ddchyegwp/image/upload/v1775369621/dsqlg7bcdeu9cxp4cmgy.jpg',7);
/*!40000 ALTER TABLE `gallery_cloud` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-04-07 11:44:59
