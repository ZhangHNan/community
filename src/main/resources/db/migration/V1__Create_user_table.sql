-- auto-generated definition
CREATE TABLE `user` (
  `ID` bigint(11) NOT NULL AUTO_INCREMENT,
  `ACCOUNT_ID` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `NAME` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `TOKEN` varchar(36) COLLATE utf8_unicode_ci DEFAULT NULL,
  `GMT_CREATE` bigint(20) DEFAULT NULL,
  `GMT_MODIFIED` bigint(20) DEFAULT NULL,
  `AVATAR_URL` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;