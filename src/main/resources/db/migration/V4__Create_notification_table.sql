CREATE TABLE `notification` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `notifier` bigint(20) NOT NULL,
  `receiver` bigint(20) NOT NULL,
  `outerId` bigint(20) NOT NULL,
  `type` int(11) NOT NULL,
  `gmt_create` bigint(20) NOT NULL,
  `status` int(11) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;