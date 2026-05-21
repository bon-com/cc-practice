# Phase 9.1 - Trinity ワークフロー実践

**Plan Mode × Extended Thinking × Sequential Thinking MCP** の3つを組み合わせた「Trinity」ワークフローの実践演習。

---

## Trinity とは

| 要素 | 役割 |
|---|---|
| **Plan Mode** (`/plan`) | 実行前に計画を立て、承認を得てから動く。暴走防止。 |
| **Extended Thinking** (`Alt+T`) | Claude が内部で長時間推論してから回答する。複雑な問題に強い。 |
| **Sequential Thinking MCP** | 思考を連鎖ステップに分解して逐次実行する MCP サーバー。推論の透明性が上がる。 |

3つが揃うことで「**考えてから計画し、透明に推論して実行する**」最強構成になる。

---

## Sequential Thinking MCP とは

`@modelcontextprotocol/server-sequential-thinking` は、Claude の思考プロセスを明示的なステップに分解して実行する MCP サーバー。

**通常の推論との違い：**
- 通常：Claude が内部でまとめて考えて結果だけ返す
- Sequential Thinking：「ステップ1→2→3...」と段階的に思考が展開され、途中で前のステップを振り返って修正もできる

**Opus と組み合わせると強い理由：**  
Opus はもともと推論能力が高いが、Sequential Thinking MCP を使うことで複雑な問題を構造的に分解できる。Plan Mode と合わせると「設計→確認→実行」の流れが明確になる。

---

## この演習でやったこと

レガシーJava認証コード（`problem/` フォルダ）を Trinity ワークフローで分析。

- **対象**: `AuthService.java`（96行）、`UserRepository.java`（49行）
- **成果**: Critical 10件 / High 11件 / Medium 9件 の問題を発見、4 Phase の修正計画を生成
- **出力**: [`output/trinity-analysis.md`](output/trinity-analysis.md)

---

## 実行手順（再現方法）

```bash
# 1. Claude Code 起動
claude

# 2. モデルを Opus に切り替え
/model opus

# 3. Plan Mode に入る
/plan

# 4. Alt+T（Mac: Option+T）で Thinking ON

# 5. 以下のプロンプトを投げる
problem/ フォルダのJavaコードを分析してください。
セキュリティ問題・パフォーマンス問題・設計上の問題を
優先順位付きでリストアップし、
修正計画をPhase分けで提案してください。
```

---

## フォルダ構成

```
9.1-trinity/
├── README.md         # このファイル
├── CLAUDE.md         # Claude Code 向け手順書
├── problem/          # 分析対象のレガシーコード
│   ├── AuthService.java
│   └── UserRepository.java
└── output/
    └── trinity-analysis.md   # Trinity による分析結果
```
