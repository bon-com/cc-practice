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
| [9.3-speed-note](phase9/9.3-speed-note/) | SpeedNote | タイムド箇条書きメモツール（Python + Flask） |

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

## SpeedNote（9.3-speed-note）

`phase9/9.3-speed-note/` は 60 秒のタイマーで構造化された箇条書きメモを作る Python + Flask 製の Web ツール。  
メモを作成してストックし、Notionで管理することが目的。

### 主な機能

| 機能 | 詳細 |
|---|---|
| 60 秒タイマー | カウントダウン＋プログレスバー。残り 15 秒で赤色警告 |
| 箇条書きエディタ | Enter → 新しい「・」行、Shift+Enter → インデント行。自動高さ調整 |
| TODAY カウンター | 本日のメモ完成数を記録（日付変更で自動リセット） |
| クリップボードコピー | 完成メモをワンクリックでコピー |
| 永続化 | `data/counter.json` でカウントを保存 |

### フォルダ構成

```
9.3-speed-note/
├── app.py              # Flask サーバー（API 3本）
├── requirements.txt    # flask==3.1.0 / pytest==8.3.0
├── start.bat           # 起動 & ブラウザ自動オープン
├── stop.bat            # ポート 5000 のプロセスを停止
├── templates/
│   └── index.html      # ダークテーマ UI（JS 含む）
├── tests/
│   └── test_app.py     # pytest テストスイート（8 ケース）
└── data/
    └── counter.json    # カウント永続化データ（.gitignore 対象）
```

### 起動方法

```
cd phase9/9.3-speed-note
start.bat          # Windows: 自動でブラウザが開く
# または
python app.py      # http://localhost:5000 にアクセス
```

### テスト実行

```
cd phase9/9.3-speed-note
pytest tests/
```

---

## このリポジトリについて

- 各 Phase は独立した演習。順番通りに読む必要はない。
- 詳細な手順・成果物は各フォルダの `README.md` を参照。
- 個人情報や認証情報を含む演習は別途 Private リポジトリで管理。
