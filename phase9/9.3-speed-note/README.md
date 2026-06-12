# Phase 9.3 - SpeedNote

**60 秒のタイマーで構造化された箇条書きメモを作る Python + Flask 製 Web ツール。**  
メモを素早く作成してストックし、Notion で管理することが目的。

---

## SpeedNote とは

| 機能 | 詳細 |
|---|---|
| **60 秒タイマー** | カウントダウン＋プログレスバー。残り 15 秒で赤色警告 |
| **箇条書きエディタ** | Enter → 新しい「・」行、Shift+Enter → インデント行。自動高さ調整 |
| **TODAY カウンター** | 本日のメモ完成数を記録（日付変更で自動リセット） |
| **クリップボードコピー** | 完成メモをワンクリックでコピー |
| **永続化** | `data/counter.json` でカウントを保存 |

---

## この演習でやったこと

TDD（テスト駆動開発）で Flask アプリを実装し、Claude Code の開発フローを体験。

- **実装**: Flask サーバー（API 3本）＋ ダークテーマ UI（JS 含む）
- **テスト**: pytest テストスイート（8 ケース）
- **CI 相当**: `pytest tests/` で全テストがパスする状態を維持

---

## 起動方法

```bash
cd phase9/9.3-speed-note

# Windows（ブラウザが自動で開く）
start.bat

# または直接起動
python app.py
# → http://localhost:5000 にアクセス
```

---

## テスト実行

```bash
cd phase9/9.3-speed-note
pytest tests/
```

---

## フォルダ構成

```
9.3-speed-note/
├── README.md           # このファイル
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
