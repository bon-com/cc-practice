---
type: concept
title: "Agent HQ & Mission Control"
tags: [github-copilot, agent-hq, mission-control, multi-agent]
created: 2026-05-27
updated: 2026-05-27
status: draft
sources: [raw/GitHub Copilot を極める会.md]
related: [github-copilot-modes, llm-models-premium]
---

# Agent HQ & Mission Control

## Agent HQ とは

GitHub Universe 2025 で発表された新構想。  
あらゆる AI エージェントを単一プラットフォームとして統合・管理するための基盤。

**目的**: 現在 AI 活用は多数のツールに分断されているが、GitHub Copilot 一つから全エージェントを呼び出せる環境を実現する。

## 利用可能なエージェント

- Anthropic（Claude）
- OpenAI（GPT系）
- Google（Gemini系）
- Cognition
- xAI

今後、有料 GitHub Copilot サブスクリプションを通じてアクセス可能になる予定。

## Mission Control

複数エージェントにタスクを割り当て・監視・追跡する統合ダッシュボード。

主な機能:
- エージェントごとのタスク割り当て・進捗管理
- ブランチコントロール（エージェント生成コードへの CI 実行タイミング制御）
- エージェントのアクセス・ポリシー管理（ID 機能）
- ワンクリックでマージ競合解決
- Slack / Linear / Azure Boards / Teams などとの連携強化

## 意義

ツール選定で迷わず、GitHub Copilot を選べばすべての AI エージェントが使えるようになる。  
開発体験の中核に AI を据える GitHub の戦略的構想。
