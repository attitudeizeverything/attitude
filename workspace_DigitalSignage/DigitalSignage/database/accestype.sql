DROP TABLE IF EXISTS `yoho`.`access`;
CREATE TABLE `access` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ACCESS_TYPE` varchar(100) NOT NULL,
  `IS_ACTIVE` int(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8;

