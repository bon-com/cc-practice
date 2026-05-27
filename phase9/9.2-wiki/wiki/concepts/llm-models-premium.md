---
type: concept
title: "Copilot で使える LLM モデル & プレミアムリクエスト"
tags: [github-copilot, llm, models, premium-request]
created: 2026-05-27
updated: 2026-05-27
status: draft
sources: [raw/GitHub Copilot を極める会.md]
related: [github-copilot-modes, agent-hq]
---

# Copilot で使える LLM モデル & プレミアムリクエスト

## 主要モデル一覧と使い所

| モデル名 | 提供会社 | 主な使いどころ |
|---|---|---|
| **Auto** | GitHub（内部制御） | タスクに応じて最適なモデルを自動選択。迷ったらこれ |
| **GPT-4.1** | OpenAI | 高精度なコード理解・複雑な設計レビュー・難度高めのリファクタ |
| **GPT-4o** | OpenAI | 高速＋高品質のバランス型。日常的な実装・レビュー・設計相談 |
| **Claude Haiku 4.5** | Anthropic | 超高速・低コスト。コメント生成、軽い修正、簡単な質問 |
| **Claude Opus 4.5** | Anthropic | 大規模コードベース理解、設計・仕様レビュー、品質重視 |
| **Claude Sonnet 4** | Anthropic | バランス型。リファクタ、PR レビュー、構造整理 |
| **Claude Sonnet 4.5** | Anthropic | Sonnet 強化版。複数ファイル編集・設計補助 |
| **Gemini 2.5 Pro** | Google | 大規模コンテキスト理解、全文解析、ドキュメント＋コード混在 |
| **GPT-5.1-Codex** | OpenAI | 高精度コード生成・修正。実装〜修正の主戦力 |
| **GPT-5.2** | OpenAI | 最新・最上位。Agent モード、Plan ＋ 実装の一気通貫 |

記事著者は GPT-5.2 や Codex シリーズを主に使用。Codex が最も使い心地が良いとのこと。

## プレミアムリクエストとは

Copilot の高度機能を使うときに消費する「使い切りポイント」。

| 要素 | 説明 |
|---|---|
| **プレミアムリクエスト** | 高度機能を使うための消費ポイント |
| **いつ消費される？** | チャット・エージェント・高負荷モデルの応答時 |
| **いつリセット？** | 毎月 1 日 |
| **使い切ったら？** | 基本機能は使えるが、優先権や高度機能は制限される |

モデル名の横にある `×1` `×3` 等の表記がプレミアムリクエストの消費倍率。  
高度なモデルほど消費倍率が高い。
