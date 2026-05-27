---
type: concept
title: "MCP（Model Context Protocol）"
tags: [mcp, github-copilot, agent, protocol]
created: 2026-05-27
updated: 2026-05-27
status: draft
sources: [raw/GitHub Copilot を極める会.md]
related: [mcp-server-setup, agent-mode-context-tools]
---

# MCP（Model Context Protocol）

## MCP とは

生成 AI（GitHub Copilot / Agent）が外部システムの情報や機能を、安全かつ共通ルールで利用するためのプロトコル。

## 登場人物

| 役割 | 具体例 |
|---|---|
| **MCP Client** | GitHub Copilot / VSCode / Copilot Chat・Plan・Agent |
| **MCP** | アクセス範囲・権限・形式を定義するプロトコル |
| **MCP Server** | GitHub / Microsoft Learn など実データ・実機能を提供する側 |

## MCP でできること

- 外部サービスの情報取得（Issues / PR / Docs / チケット）
- 外部サービスへの操作（PR 作成、Issue 更新、ページ作成）
- 権限・スコープを明確にした安全な実行
- 複数サービスを同一ルールで扱う

## MCP がない時 vs ある時

| | MCP なし | MCP あり |
|---|---|---|
| 実装コスト | API ごとに個別実装が必要 | 共通プロトコルで統一 |
| 権限管理 | 曖昧になりがち | 読み取り/書き込み/実行を明確に制御 |
| Agent との連携 | Agent が実務を代行しにくい | Agent が業務を安全に実行できる |

## 代表的な MCP サーバー

- **GitHub × MCP**: Issue を読む、PR を作成・更新、差分を確認
- **Microsoft Learn × MCP**: 最新公式ドキュメントを参照、古い知識を避けた回答

追加手順は [[mcp-server-setup]] 参照。
