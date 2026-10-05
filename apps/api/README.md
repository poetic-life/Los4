# Primary API

## Responsibility

主业务 Spring Boot 服务，负责用户认证、商品、新闻、球员、赛程、购物车、收藏、地址、订单、社区、私信和反馈接口，并在 `/uploads` 提供媒体资源。

## Prerequisites

- Java 8
- Maven 3.9
- MySQL 8

## Configuration

服务端口为 `8081`，数据库为 `los`。使用 `DB_USERNAME` 和 `DB_PASSWORD` 环境变量配置数据库凭据。生产环境必须覆盖默认 JWT 密钥。

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
