---
type: concept
title: "GitHub Copilot のモード（5種類）"
tags: [github-copilot, agent, modes]
created: 2026-05-27
updated: 2026-05-27
status: draft
sources: [raw/GitHub Copilot を極める会.md]
related: [agent-hq, llm-models-premium, agent-mode-context-tools]
---

# GitHub Copilot のモード（5種類）

GitHub Copilot には用途別に5つのモードがある。

## モード一覧

| モード名 | 主な用途 | 特徴 |
|---|---|---|
| **Ask** | 質問・理解 | 自然言語で質問し、コードの意味・使い方・エラー原因や対策を確認 |
| **Edit** | コード編集 | 既存コードに対して追加・修正・削除を具体的に指示 |
| **Plan** | 設計・整理 | 実行前にタスク分解・手順設計・全体像の整理を行う |
| **Agent** | 自律的な作業実行 | 抽象的な指示を分解して実行し、ファイル編集やコマンド実行まで対応 |
| **AIAgentExpert** | Agent 設計・拡張 | カスタム Agent を作成・設定し、特定分野に特化した動作を実現 |

## 使い方の指針

- 「何が起きているか知りたい」→ **Ask**
- 「このコードをこう直して」→ **Edit**
- 「実装前に全体計画を立てたい」→ **Plan**
- 「ええ感じにやっといて」→ **Agent**
- Agent をカスタマイズして専門化したい → **AIAgentExpert**

Agent モードが最も強力で、コンテキスト・ツール・MCP を事前設定することで自律的に動作する。
詳細は [[agent-mode-context-tools]] 参照。
