# Phase 9.2 - Wiki 管理システム

**Claude Code スキルで運用するパーソナルナレッジベース。**  
3つのスキルコマンドでソース素材を取り込み、知識を検索・管理できる。

---

## Wiki スキルとは

| スキル | 役割 |
|---|---|
| **`/wiki-ingest`** | `raw/` のソース素材（md / pdf / 画像 / URL）を読み込み、Wiki ページを自動生成 |
| **`/wiki-query`** | 蓄積した Wiki ページを知識ベースとして質問に回答 |
| **`/wiki-lint`** | リンク切れ・孤立ページ・矛盾・陳腐化を全カテゴリ横断で検出してレポート |

3つを組み合わせることで「**素材を入れる → 知識として使う → 品質を保つ**」サイクルが回る。

---

## この演習でやったこと

Claude Code のスキルシステムを使って個人用ナレッジベースをゼロから構築。

- **対象**: `reference-repos/` 配下の Claude Code 関連ドキュメント類
- **成果**: `concepts/` `how-to/` `learnings/` `qa/` の 4 カテゴリに整理された Wiki ページ群
- **特徴**: Obsidian の Vault として開けるフォーマット（`[[ページ名]]` リンク対応）

---

## 基本的な使い方

```bash
# 1. ソース素材を raw/ に置く（md / pdf / 画像 / URL どれでも可）
cp some-article.md phase9/9.2-wiki/raw/

# 2. Claude Code を起動
claude

# 3. ingest で Wiki ページを自動生成
/wiki-ingest

# 4. 蓄積した知識に質問
/wiki-query "Sequential Thinking MCP の使い方は？"

# 5. 品質チェック
/wiki-lint
```

---

## Obsidian との連携

`9.2-wiki/` を Obsidian の Vault として開くと、`[[リンク]]` 形式のページ間リンクやタグが機能する。  
ファイル名は英語だが、frontmatter の `title:` フィールドに日本語タイトルが入っている。

> **推奨設定**: `Settings → Appearance → Show inline title` を ON にすると  
> ノート上部に日本語タイトルが大きく表示される。

---

## フォルダ構成

```
9.2-wiki/
├── README.md             # このファイル
├── .claude/skills/       # wiki-ingest / wiki-query / wiki-lint スキル
├── raw/                  # インボックス（処理前のソース素材）
├── raw_back/             # アーカイブ（ingest 済み・.gitignore 対象）
└── wiki/                 # 生成された Wiki ページ
    ├── index.md          # 全ページ一覧
    ├── overview.md       # Wiki 全体の概要
    ├── log.md            # ingest 履歴ログ
    ├── concepts/         # 概念・用語解説
    ├── how-to/           # 手順・操作ガイド
    ├── learnings/        # 気づき・体験記録
    └── qa/               # 質問と回答
```
