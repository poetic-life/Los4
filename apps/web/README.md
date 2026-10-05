# User Web

## Responsibility

面向球迷的 Vue 3 单页应用，提供首页、商城、新闻、球队、赛程、社区、会员中心、订单和私信功能。

## Prerequisites

- Node.js 22+
- npm 10+
- 已在 `http://localhost:8081` 启动 Primary API

## Configuration

开发服务器端口为 `8080`。Vite 将 `/api` 与 `/uploads` 请求代理到 `http://localhost:8081`。

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
