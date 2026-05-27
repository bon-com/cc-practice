---
type: how-to
title: "MCP サーバーの追加手順（GUI / 手動）"
tags: [mcp, github-copilot, setup, vscode]
created: 2026-05-27
updated: 2026-05-27
status: draft
sources: [raw/GitHub Copilot を極める会.md]
related: [mcp, agent-mode-context-tools]
---

# MCP サーバーの追加手順（GUI / 手動）

## GUI から追加（Marketplace）

1. VSCode の拡張機能パネル → 左下「MCP サーバー」→ **Enable MCP Servers Marketplace** をクリック
2. 追加したい MCP サーバーを選択してインストール
3. 画面左下に追加されたことを確認

## 手動追加（設定ファイル直接編集）

### 設定ファイルを開く

VSCode 上部の検索窓で `MCP: Open User Configuration` と入力して開く。

### リモート MCP サーバーの追加フォーマット

```json
{
  "servers": {
    "<MCP_SERVER_NAME>": {
      "type": "http",
      "url": "<MCP_SERVER_URL>"
    }
  },
  "inputs": []
}
```

### ローカル MCP サーバーの追加フォーマット

```json
{
  "servers": {
    "<MCP_SERVER_NAME>": {
      "type": "stdio",
      "command": "npx",
      "args": [
        "-y",
        "@modelcontextprotocol/server-filesystem",
        "/work"
      ]
    }
  },
  "inputs": []
}
```

### シークレットを含む場合

```json
{
  "servers": {
    "<MCP_SERVER_NAME>": {
      "type": "npx",
      "args": ["-y", "@azure/mcp@latest", "server", "start"],
      "env": {
        "API_KEY": "${input:my_api_key}"
      }
    }
  },
  "inputs": []
}
```

`${input:変数名}` 形式でシークレットを参照できる（設定ファイルに直書きしない）。

## 具体例：filesystem MCP サーバーの追加

```bash
npm install -g @modelcontextprotocol/server-filesystem
```

設定ファイルに以下を追記：

```json
"filesystem": {
  "type": "stdio",
  "command": "npx",
  "args": [
    "-y",
    "@modelcontextprotocol/server-filesystem",
    "/work"
  ]
}
```

追加後、画面左下の MCP Server 一覧に `filesystem` が表示されれば成功。

## 追加した MCP のツールセットへの組み込み

複数の MCP を組み合わせて使う場合はツールセットに登録しておくと切り替えが楽。  
→ [[toolset-creation]]
