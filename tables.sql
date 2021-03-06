CREATE DATABASE IF NOT EXISTS whoami DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;

CREATE TABLE IF NOT EXISTS user (
  uid BIGINT(20) PRIMARY KEY AUTO_INCREMENT NOT NULL,
  email VARCHAR(100) NOT NULL,
  password VARCHAR(50) NOT NULL,
  gender TINYINT(3) NOT NULL COMMENT '0:male,1:female,2:unset',
  birthday LONG NOT NULL COMMENT 'unixtime_10',
  province VARCHAR(20),
  phone VARCHAR(20),
  UNIQUE KEY `unique_index` (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin COMMENT='用户列表';

GRANT ALL PRIVILEGES ON whoami.* TO 'liu'@'localhost' IDENTIFIED BY 'FfcsgN+h4VjM=' WITH GRANT OPTION;
