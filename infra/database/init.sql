-- ============================================================
-- LA Clippers 球迷站 (los) 数据库初始化脚本
-- 说明：表结构由 JPA (ddl-auto: update) 自动创建，
--       种子数据由 DataInitializer 在启动时自动写入。
--       本脚本仅负责创建数据库与专用账号。
-- 用法：mysql -u root -p < init.sql
-- ============================================================

-- 创建数据库（字符集 utf8mb4，兼容中文与 emoji）
CREATE DATABASE IF NOT EXISTS los
  DEFAULT CHARACTER SET utf8mb4
  DEFAULT COLLATE utf8mb4_general_ci;

-- 创建专用账号（可选，按需修改用户名/密码）
-- 生产环境请修改下面的密码，并同步到环境变量 DB_USERNAME / DB_PASSWORD
CREATE USER IF NOT EXISTS 'los'@'localhost' IDENTIFIED BY 'los123456';
GRANT ALL PRIVILEGES ON los.* TO 'los'@'localhost';
FLUSH PRIVILEGES;