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
-- Table structure for table `udesh`
--

DROP TABLE IF EXISTS `udesh`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `udesh` (
  `id` int NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `udesh`
--

LOCK TABLES `udesh` WRITE;
/*!40000 ALTER TABLE `udesh` DISABLE KEYS */;
INSERT INTO `udesh` VALUES (3,'अलग अलग सामाजिक संस्था, हाईस्कूल मंडल इनको गोशाला का महत्व बताना और अधिक जानकारी देना।\"'),(4,'अलग अलग प्रकार की उपयोगिता, गोशालाओं की वजह से होने वाले फायदों को लोगो तक पहुँचाना।'),(5,'गोशालाओं के द्वारा मिलाने वाले उत्पादन और संस्था को मिलने वाले उजीत लाभ करा के देना।'),(6,'गोशाला विशेषज्ञ के मार्गदर्शन में शिविर का आयोजन करना   ।'),(7,'गोशालाओं के उपक्रम बढ़ने के अलग अलग योजनाए शुरू करना।'),(8,'संस्था के जानकारी देने हेतु पुस्तिका प्रकाशित करना।'),(9,'स्थानिक संस्था, सरकारी गैर सरकारी अधिकारिओ से मार्गदर्शन लेना।'),(10,'पशु आरोग्य के बारे में मार्गदर्शन करना।'),(11,'आरोग्य शिविर लगाना।'),(12,'पहलेसे ही अस्तित्व में रहने वाली गोशालों को हर प्रकार की सहायता देना।');
/*!40000 ALTER TABLE `udesh` ENABLE KEYS */;
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
