# Admin API

## Responsibility

独立的 Spring Boot 管理服务，负责管理员认证以及用户、商品、订单、帖子和评论管理接口。

## Prerequisites

- Java 8
- Maven 3.9
- MySQL 8

## Configuration

服务端口为 `8082`，与 Primary API 共用数据库 `los`。使用 `DB_USERNAME` 和 `DB_PASSWORD` 环境变量配置数据库凭据。生产环境必须覆盖默认 JWT 密钥。

## Development

```powershell
$env:DB_USERNAME = 'root'
$env:DB_PASSWORD = 'your-password'
mvn spring-boot:run
```

## Build and Test

```powershell
mvn test
mvn package -DskipTests
```

构建产物位于 `target/`，该目录不进入版本控制。
