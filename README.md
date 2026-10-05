# LOS — LA Clippers Fan Platform

LOS 是一个前后端分离的洛杉矶快船球迷平台，采用单仓库、多应用的组织方式。用户站点与管理后台分别拥有独立的前端和后端，并共享 MySQL 业务数据库。

## Applications

| Application | Path | Port | Responsibility |
| --- | --- | --- | --- |
| User web | `apps/web` | 8080 | 官网、会员中心、商城、社区、新闻、球员与赛程 |
| Primary API | `apps/api` | 8081 | 用户侧 REST API、认证、业务逻辑与媒体资源 |
| Admin web | `apps/admin-web` | 3001 | 用户、商品、订单、帖子和评论管理界面 |
| Admin API | `apps/admin-api` | 8082 | 管理员认证与后台管理 REST API |

## Repository Structure

```text
los/
├── apps/
│   ├── web/                 # 用户前端
│   ├── api/                 # 主后端
│   ├── admin-web/           # 管理后台前端
│   └── admin-api/           # 管理后台后端
├── docs/                    # 架构、设计与实施文档
├── infra/database/          # 数据库初始化资源
├── scripts/                 # 安装、启动、构建与结构检查脚本
├── .editorconfig
├── .gitattributes
└── .gitignore
```

## Prerequisites

- Node.js 18 或更高版本
- npm 9 或更高版本
- Java 8
- Maven 3.9
- MySQL 8
- PowerShell 5.1 或 PowerShell 7

## Local Development

1. 启动 MySQL 8。
2. 在 PowerShell 中配置数据库凭据：

```powershell
$env:DB_USERNAME = 'root'
$env:DB_PASSWORD = 'your-password'
```

3. 安装两套前端依赖：

```powershell
./scripts/Install-Dependencies.ps1
```

4. 分别在独立终端启动需要的应用：

```powershell
./scripts/Start-App.ps1 -App api
./scripts/Start-App.ps1 -App web
./scripts/Start-App.ps1 -App admin-api
./scripts/Start-App.ps1 -App admin-web
```

用户站点访问 `http://localhost:8080`，管理后台访问 `http://localhost:3001`。

## Build and Verification

构建两套前端并运行两套后端测试：

```powershell
./scripts/Build-All.ps1
```

检查仓库是否符合目标目录结构：

```powershell
./scripts/Test-ProjectStructure.ps1
```

也可以进入任一应用目录执行其 README 中列出的独立命令。

## Configuration and Security

- 两套后端默认连接 MySQL 数据库 `los`，通过 `DB_USERNAME` 与 `DB_PASSWORD` 读取凭据。
- 本地默认值仅用于开发。生产环境必须通过安全配置注入数据库凭据。
- 生产环境必须替换两套后端配置中的默认 JWT 密钥，不得把真实密钥提交到仓库。
- 用户前端的 `/api` 和 `/uploads` 代理到主后端 `8081`。
- 管理前端的 `/api` 代理到管理后端 `8082`，`/uploads` 代理到主后端 `8081`。

## Documentation

- [System architecture](docs/architecture/system-overview.md)
- [Structure design](docs/superpowers/specs/2026-10-05-enterprise-monorepo-structure-design.md)
- [Migration plan](docs/superpowers/plans/2026-10-05-enterprise-monorepo-structure.md)
