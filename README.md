# cc-practice

Claude Code の機能を実践的に学ぶ練習リポジトリ。
各 Phase フォルダに詳細な README と成果物が入っている。

---

## Phase 一覧

### Phase 9 - Claude Code 上級ワークフロー

| フォルダ | テーマ | 概要 |
|---|---|---|
| [9.1-trinity](phase9/9.1-trinity/README.md) | Trinity ワークフロー | Plan Mode × Extended Thinking × Sequential Thinking MCP |
| [9.2-wiki](phase9/9.2-wiki/) | Wiki 作成・管理 | Claude スキルで運用するパーソナルナレッジベース |

---

## Wiki システム（9.2-wiki）

`phase9/9.2-wiki/` は Claude Code のスキルで運用するパーソナル Wiki。
Obsidian と連携して閲覧・編集できる。

### フォルダ構成

```
9.2-wiki/
├── .claude/skills/   # Wiki 管理スキル
├── raw/              # インボックス（処理前のソース素材を置く）
├── raw_back/         # アーカイブ（ingest 済み・.gitignore 対象）
└── wiki/             # 生成された Wiki ページ
    ├── concepts/     # 概念・用語
    ├── how-to/       # 手順・操作ガイド
    ├── learnings/    # 気づき・体験記録
    └── qa/           # 質問と回答
```

### スキル一覧

| コマンド | 用途 |
|---|---|
| `/wiki-ingest [source]` | `raw/` のソース素材を読み込んで Wiki ページを生成 |
| `/wiki-query "<質問>"` | Wiki を知識ベースとして質問に回答 |
| `/wiki-lint` | リンク切れ・孤立ページ・矛盾を検出してレポート |

### 基本的な使い方

1. `raw/` にソースファイル（md / pdf / 画像）を置く
2. `/wiki-ingest` を実行 → キーテイクアウェイを確認 → Wiki ページが自動生成
3. `/wiki-query "知りたいこと"` で蓄積した知識を検索
4. 定期的に `/wiki-lint` で品質チェック

### Obsidian との連携

`9.2-wiki/` を Obsidian の Vault として開くと、`[[リンク]]` 形式のページ間リンクやタグが機能する。
ファイル名は英語だが、frontmatter の `title:` フィールドに日本語タイトルが入っている。

> **推奨設定**: Obsidian の `Settings → Appearance → Show inline title` を ON にすると
> ノート上部に日本語タイトルが大きく表示される。

---

## このリポジトリについて

- 各 Phase は独立した演習。順番通りに読む必要はない。
- 詳細な手順・成果物は各フォルダの `README.md` を参照。
- 個人情報や認証情報を含む演習は別途 Private リポジトリで管理。
