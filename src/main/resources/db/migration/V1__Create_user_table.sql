-- auto-generated definition
CREATE TABLE `user` (
  `ID` INT(11) NOT NULL AUTO_INCREMENT,
  `ACCOUNT_ID` VARCHAR(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `NAME` VARCHAR(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `TOKEN` VARCHAR(36) COLLATE utf8_unicode_ci DEFAULT NULL,
  `GMT_CREATE` BIGINT(20) DEFAULT NULL,
  `GMT_MODIFIED` BIGINT(20) DEFAULT NULL,
  `avatar_url` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=INNODB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
