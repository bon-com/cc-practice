---
type: how-to
title: "コーディングエージェント（Issue 割り当て）& PR コードレビュー"
tags: [github-copilot, coding-agent, issue, pull-request, code-review]
created: 2026-05-27
updated: 2026-05-27
status: draft
sources: [raw/GitHub Copilot を極める会.md]
related: [github-copilot-modes, custom-instructions]
---

# コーディングエージェント（Issue 割り当て）& PR コードレビュー

## コーディングエージェントとは

Issue に書かれたやりたいことを Copilot が解釈し、反復的に実装する機能。

特徴:
- 抽象的な内容を小さなステップに分割して反復実行
- エラーも自動的に検知・解消
- 実装完了後、Pull Request を自動作成（スクリーンショット付き）

## Issue への割り当て手順

1. GitHub で `Issues` → `New issue` を作成
2. Issue のタイトルと内容（背景・実装要件・完了条件）を記載
3. **Assignees** の箇所で **GitHub Copilot** を選択
4. `Create` をクリック

割り当て後、Issue に 👀 マークが付き `[WIP] Work in Progress` が表示されれば Copilot が作業中。

## 実装完了後

- PR が自動作成される
- PR にはコードの変更差分、スクリーンショット（PC・モバイル）が自動付与される
- 内容を確認して `Merge` すれば完了

## コードレビュー（Copilot as Reviewer）

PR のレビュアーに Copilot を設定すると自動レビューコメントを生成する。

指摘内容:
- セキュリティリスク
- バグの可能性
- 冗長な処理
- ベストプラクティス違反

### 設定手順

1. PR を作成する
2. **Reviewers** に **GitHub Copilot** を追加
3. Copilot がレビューコメントを自動生成するのを待つ

## ポイント

- Issue の内容が詳細であるほど実装精度が上がる
- [[custom-instructions]] を設定しておくとブレなく実装される
- コミットメッセージも AI で自動生成できる（最近の主流）
