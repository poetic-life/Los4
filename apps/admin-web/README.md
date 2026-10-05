# Admin Web

## Responsibility

面向运营人员的 Vue 3 管理界面，提供仪表盘以及用户、商品、订单、帖子和评论管理功能。

## Prerequisites

- Node.js 18+
- npm 9+
- 已在 `http://localhost:8082` 启动 Admin API
- 若页面展示上传资源，需在 `http://localhost:8081` 启动 Primary API

## Configuration

开发服务器端口为 `3001`。Vite 将 `/api` 代理到 `http://localhost:8082`，将 `/uploads` 代理到 `http://localhost:8081`。

## Development

```powershell
npm ci
npm run dev
```

## Build and Test

```powershell
npm run build
npm run preview
```

构建产物位于 `dist/`，该目录不进入版本控制。
