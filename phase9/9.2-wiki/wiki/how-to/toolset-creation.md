---
type: how-to
title: "ツールセットの作成・切り替え"
tags: [github-copilot, toolset, agent, vscode]
created: 2026-05-27
updated: 2026-05-27
status: draft
sources: [raw/GitHub Copilot を極める会.md]
related: [agent-mode-context-tools, mcp-server-setup]
---

# ツールセットの作成・切り替え

## ツールセットとは

Agent が使えるツールの集合体を定義したもの。  
**ツールは最大 128個** までしか設定できないため、プロジェクトや用途ごとにグループ化して切り替える仕組み。

## 作成手順

1. VSCode 右上の歯車マーク → **ツールセット** を開く
2. **新しいツールセットファイル作成** を選択
3. 以下の形式で記述する：

```json
{
  "sample-toolset": {
    "tools": ["filesystem", "microsoftdocs/mcp"],
    "description": "sample toolset has two MCP servers.",
    "icon": "tools"
  }
}
```

### 各フィールドの説明

| フィールド | 内容 |
|---|---|
| `toolSetName` | ツールセットの名前 |
| `tools` | 含めるツールのリスト（組み込み/拡張機能/MCP名） |
| `description` | ツールセットの説明 |
| `icon` | アイコン（[codicons](https://microsoft.github.io/vscode-codicons/) から選択） |

## 切り替え方法

VSCode 右下のツールアイコンをクリック → 作成したツールセットを選択。

## 活用例

| ツールセット名 | 用途 | 含むツール |
|---|---|---|
| `web-dev` | Web 開発 | filesystem, microsoftdocs/mcp, github |
| `review` | コードレビュー | github, problems, usages |
| `test` | テスト実行 | runTests, testFailure, problems |
