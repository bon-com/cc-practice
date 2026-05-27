---
type: how-to
title: "Agent モードのコンテキスト・ツール設定"
tags: [github-copilot, agent, context, tools]
created: 2026-05-27
updated: 2026-05-27
status: draft
sources: [raw/GitHub Copilot を極める会.md]
related: [github-copilot-modes, mcp, toolset-creation]
---

# Agent モードのコンテキスト・ツール設定

Agent モードに事前情報を与える3つの手段。

## 1. コンテキスト指定

Agent が参照する情報を事前に指定する。

### ソースコードのコンテキスト

| 指定方法 | 内容 |
|---|---|
| `#codebase` | ワークスペース内の全ファイル・フォルダ・設定を参照 |
| `#file:{FILE_NAME}` | 指定したファイルを参照（複数指定可） |
| `#selection` | アクティブなエディタで選択中のコードを参照 |

### ターミナルのコンテキスト

| 指定方法 | 内容 |
|---|---|
| `#terminalLastCommand` | 最後に実行したコマンドの結果を取得 |
| `#terminalSelection` | ターミナルで選択した内容を取得 |

### Git のコンテキスト

| 指定方法 | 内容 |
|---|---|
| `#git` / `#git_branch` | Git リポジトリの情報（ブランチ等）を取得 |
| `#changes` | 変更されたファイルの情報を取得（git status 相当） |

## 2. ツールの設定

Agent がタスク実行時に使える機能を選択する。ツールには3種類ある。

### 組み込み機能（VSCode 標準）

主要なもの：

| ツール名 | 機能 |
|---|---|
| `edit` | ワークスペース内のファイルを編集 |
| `runCommands` | ターミナルでコマンドを実行 |
| `runTests` | 単体テストを実行 |
| `fetch` | Web ページからメインコンテンツをフェッチ |
| `problems` | ファイルのエラーを確認 |
| `search` | ワークスペース内のファイルを検索 |
| `runSubagent` | タスクを別の分離されたサブエージェントで実行 |
| `todos` | ToDo 項目の管理・追跡 |

### 拡張機能

VSCode 拡張機能の「チャット参加者」をチェックするとツールとして利用可能になる。  
例）GitHub Pull Requests 拡張機能 → PR 関連操作が Agent から使える。

### MCP

MCP サーバーを追加するとその機能がツールとして利用可能になる。  
詳細は [[mcp-server-setup]] 参照。

## 注意事項

- ツールは最大 **128個** まで設定可能
- 上限に達した場合はツールセットで切り替えて管理する → [[toolset-creation]]
