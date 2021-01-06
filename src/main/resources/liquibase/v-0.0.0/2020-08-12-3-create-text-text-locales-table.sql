--liquibase formatted SQL

--changeset elya:3
CREATE TABLE IF NOT EXISTS `text_text_locales` (
  `text_id` int(11) NOT NULL,
  `text_locales` varchar(1000) DEFAULT NULL,
  `text_locales_key` int(11) NOT NULL,
  PRIMARY KEY (`text_id`,`text_locales_key`),
  KEY `FKlss8cppfiffhn16s4u5dvo0iw` (`text_locales_key`),
  CONSTRAINT `FK51lwjav7fr1vwef89ra2y78f6` FOREIGN KEY (`text_id`) REFERENCES `texts` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FKlss8cppfiffhn16s4u5dvo0iw` FOREIGN KEY (`text_locales_key`) REFERENCES `locales` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
