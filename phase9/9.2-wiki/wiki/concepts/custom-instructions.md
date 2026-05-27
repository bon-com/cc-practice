---
type: concept
title: "カスタム命令（Custom Instructions）"
tags: [github-copilot, custom-instructions, agent, instructions-md]
created: 2026-05-27
updated: 2026-05-27
status: draft
sources: [raw/GitHub Copilot を極める会.md]
related: [agent-mode-context-tools, coding-agent-and-review]
---

# カスタム命令（Custom Instructions）

## カスタム命令とは

GitHub Copilot に対して「常に守ってほしい前提条件・振る舞い」を事前に与える設定機能。  
**Copilot の性格・前提知識・ルールを定義する仕組み**。

## カスタム命令があると変わること

| カスタム命令なし | カスタム命令あり |
|---|---|
| 毎回ルールを説明する必要がある | 常に同じルールでコード生成される |
| 人によって回答品質がブレる | プロジェクト文脈を理解した回答 |
| プロジェクト固有ルールを守らないことがある | 設定ルールを一貫して遵守 |

## 重要なファイル構成

| ファイル名 | 役割 |
|---|---|
| `AGENTS.md` | AI の最上位ルール。技術スタック固定・暴走防止 |
| `instructions.md`（`.github/instructions/`） | 実装・設計・UI・セキュリティの共通ルール |
| `docs/introduction.md` | アプリの概要・目的・非目的。要件誤解防止 |
| `docs/architecture.md` | 全体アーキテクチャ定義。構成の一貫性維持 |
| `docs/api/README.md` | API 全体の方針・共通仕様 |
| `docs/decisions/README.md` | ADR（設計判断）一覧 |

最低限 **AGENTS.md / instructions.md / docs/introduction.md / docs/architecture.md** の4ファイルがあれば OK。

## xxx.instructions.md の記載形式

```markdown
---
applyTo: '**'
---

# Basic Instructions
- 常に日本語で応答すること

# Tech Stack / Constraints
- Next.js（App Router）+ TypeScript
- 認証は Clerk を使用
- データベースは Azure Cosmos DB

# Commit Message
- コミットメッセージは日本語で記述
```

`applyTo: '**'` で全ファイルに適用。スコープを絞ることも可能。

## 設定方法

VSCode の Copilot チャットで `📎` マーク → 手順を選択 → 新しい命令ファイル → `.github/instructions/` に配置。
