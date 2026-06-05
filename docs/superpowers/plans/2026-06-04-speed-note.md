# SpeedNote Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Python + Flask でローカル起動するタイムド箇条書きメモツールを作る。バッチ起動でブラウザが開き、1分タイマーと箇条書き入力補助を持つ。

**Architecture:** Flask が `localhost:5000` でサーブ。UI は単一 HTML ファイル（CSS・JS インライン）。カウンターは `data/counter.json` に保存し日付ベースでリセット。

**Tech Stack:** Python 3.x, Flask 3.x, pytest, vanilla JS（CDN なし）

---

## ファイル構成

```
cc-practice/phase9/9.3-speed-note/
├── app.py                  # Flask サーバー（3エンドポイント）
├── templates/
│   └── index.html          # 単一 UI ファイル（CSS・JS インライン）
├── tests/
│   └── test_app.py         # Flask API のユニットテスト
├── data/                   # .gitignore で除外
│   └── counter.json        # 実行時に自動生成
├── start.bat               # サーバー起動 + ブラウザ自動オープン
├── requirements.txt
└── .gitignore
```

---

## Task 1: プロジェクト初期化

**Files:**
- Create: `cc-practice/phase9/9.3-speed-note/requirements.txt`
- Create: `cc-practice/phase9/9.3-speed-note/.gitignore`

- [ ] **Step 1: フォルダを作る**

```
cc-practice/phase9/9.3-speed-note/
cc-practice/phase9/9.3-speed-note/templates/
cc-practice/phase9/9.3-speed-note/tests/
cc-practice/phase9/9.3-speed-note/data/
```

- [ ] **Step 2: requirements.txt を作る**

```
flask==3.1.0
pytest==8.3.0
```

- [ ] **Step 3: .gitignore を作る**

```
__pycache__/
*.pyc
data/
```

- [ ] **Step 4: 依存パッケージをインストールする**

```
pip install flask pytest
```

- [ ] **Step 5: コミット**

```bash
git add cc-practice/phase9/9.3-speed-note/requirements.txt cc-practice/phase9/9.3-speed-note/.gitignore
git commit -m "feat: add SpeedNote project scaffold"
```

---

## Task 2: Flask バックエンド（TDD）

**Files:**
- Create: `cc-practice/phase9/9.3-speed-note/tests/test_app.py`
- Create: `cc-practice/phase9/9.3-speed-note/app.py`

- [ ] **Step 1: テストを書く**

`tests/test_app.py`:
```python
import json
import pytest
import sys
import os

sys.path.insert(0, os.path.dirname(os.path.dirname(__file__)))
import app as app_module


@pytest.fixture
def client(tmp_path, monkeypatch):
    monkeypatch.setattr(app_module, 'COUNTER_FILE', str(tmp_path / 'counter.json'))
    monkeypatch.setattr(app_module, 'DATA_DIR', str(tmp_path))
    app_module.app.config['TESTING'] = True
    with app_module.app.test_client() as c:
        yield c


def test_index(client):
    res = client.get('/')
    assert res.status_code == 200


def test_get_count_initial(client):
    res = client.get('/api/count')
    assert res.status_code == 200
    assert res.get_json() == {'count': 0}


def test_increment_count(client):
    client.post('/api/count/increment')
    res = client.get('/api/count')
    assert res.get_json() == {'count': 1}


def test_increment_multiple(client):
    client.post('/api/count/increment')
    client.post('/api/count/increment')
    client.post('/api/count/increment')
    res = client.get('/api/count')
    assert res.get_json() == {'count': 3}


def test_date_reset(client, tmp_path, monkeypatch):
    # 昨日の日付でカウンターファイルを書いておく
    counter_path = str(tmp_path / 'counter.json')
    monkeypatch.setattr(app_module, 'COUNTER_FILE', counter_path)
    with open(counter_path, 'w') as f:
        json.dump({'date': '2020-01-01', 'count': 99}, f)
    res = client.get('/api/count')
    assert res.get_json() == {'count': 0}
```

- [ ] **Step 2: テストを実行して FAIL を確認する**

```
cd cc-practice/phase9/9.3-speed-note
pytest tests/test_app.py -v
```

期待: `ModuleNotFoundError: No module named 'app'` や `ImportError`

- [ ] **Step 3: app.py を実装する**

`app.py`:
```python
import json
import os
from datetime import date
from flask import Flask, jsonify, render_template

app = Flask(__name__)

DATA_DIR = os.path.join(os.path.dirname(__file__), 'data')
COUNTER_FILE = os.path.join(DATA_DIR, 'counter.json')


def _load_counter():
    if not os.path.exists(COUNTER_FILE):
        return {'date': str(date.today()), 'count': 0}
    with open(COUNTER_FILE, 'r', encoding='utf-8') as f:
        data = json.load(f)
    if data.get('date') != str(date.today()):
        return {'date': str(date.today()), 'count': 0}
    return data


def _save_counter(data):
    os.makedirs(DATA_DIR, exist_ok=True)
    with open(COUNTER_FILE, 'w', encoding='utf-8') as f:
        json.dump(data, f, ensure_ascii=False)


@app.route('/')
def index():
    return render_template('index.html')


@app.route('/api/count')
def get_count():
    return jsonify({'count': _load_counter()['count']})


@app.route('/api/count/increment', methods=['POST'])
def increment_count():
    data = _load_counter()
    data['count'] += 1
    _save_counter(data)
    return jsonify({'count': data['count']})


if __name__ == '__main__':
    app.run(host='127.0.0.1', port=5000, debug=False)
```

- [ ] **Step 4: テストを実行して PASS を確認する**

```
pytest tests/test_app.py -v
```

期待:
```
test_index PASSED
test_get_count_initial PASSED
test_increment_count PASSED
test_increment_multiple PASSED
test_date_reset PASSED
```

- [ ] **Step 5: コミット**

```bash
git add cc-practice/phase9/9.3-speed-note/app.py cc-practice/phase9/9.3-speed-note/tests/test_app.py
git commit -m "feat: add Flask API with counter endpoints"
```

---

## Task 3: UI スケルトン + CSS

**Files:**
- Create: `cc-practice/phase9/9.3-speed-note/templates/index.html`

- [ ] **Step 1: index.html を作る（CSS・HTML のみ、JS なし）**

`templates/index.html`:
```html
<!DOCTYPE html>
<html lang="ja">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>SpeedNote</title>
  <style>
    *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

    body {
      background: #1a1a2e;
      color: #eee;
      font-family: 'Segoe UI', sans-serif;
      min-height: 100vh;
      display: flex;
      align-items: center;
      justify-content: center;
      padding: 24px;
    }

    .container {
      width: 100%;
      max-width: 680px;
    }

    header {
      text-align: center;
      margin-bottom: 20px;
    }

    header h1 {
      font-size: 15px;
      font-weight: bold;
      color: #a0a0c0;
      letter-spacing: 4px;
    }

    .main {
      display: flex;
      gap: 14px;
    }

    /* 左：入力エリア */
    .input-area {
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 12px;
    }

    .field label {
      display: block;
      font-size: 10px;
      color: #888;
      letter-spacing: 1px;
      margin-bottom: 4px;
    }

    #line-hint {
      color: #4ec9b0;
      margin-left: 8px;
    }

    #title {
      width: 100%;
      background: transparent;
      border: none;
      border-bottom: 2px solid #4ec9b0;
      padding: 6px 4px;
      color: #fff;
      font-size: 14px;
      outline: none;
      font-family: inherit;
    }

    #title::placeholder { color: #444; }

    #memo {
      width: 100%;
      background: #0f0f1f;
      border: 1px solid #2a2a4a;
      border-radius: 6px;
      padding: 10px 12px;
      color: #d4d4d4;
      font-size: 13px;
      line-height: 1.9;
      resize: none;
      height: 160px;
      outline: none;
      font-family: inherit;
    }

    #memo:focus { border-color: #3a3a6a; }
    #memo[readonly] { color: #666; cursor: default; }

    .buttons {
      display: flex;
      gap: 8px;
    }

    .buttons button {
      flex: 1;
      border: none;
      border-radius: 6px;
      padding: 10px;
      font-size: 13px;
      cursor: pointer;
      letter-spacing: 0.5px;
      font-family: inherit;
      transition: opacity 0.15s;
    }

    .buttons button:hover { opacity: 0.85; }

    #btn-copy {
      background: #4ec9b0;
      color: #0f0f1f;
      font-weight: bold;
    }

    #btn-clear {
      background: #1e1e3a;
      border: 1px solid #3a3a5a;
      color: #888;
    }

    /* 右：サイドバー */
    .sidebar {
      width: 90px;
      display: flex;
      flex-direction: column;
      gap: 10px;
    }

    .stat-card {
      background: #0f0f1f;
      border: 1px solid #2a2a4a;
      border-radius: 10px;
      padding: 12px 8px;
      text-align: center;
    }

    .stat-label {
      font-size: 9px;
      color: #888;
      letter-spacing: 1px;
      margin-bottom: 6px;
    }

    .stat-value {
      font-size: 26px;
      font-weight: bold;
      font-variant-numeric: tabular-nums;
      letter-spacing: 1px;
    }

    .stat-sub {
      font-size: 8px;
      color: #555;
      margin-top: 4px;
    }

    #timer-display { color: #4ec9b0; }
    #lines-display { color: #dcdcaa; font-size: 24px; }
    #today-display { color: #c586c0; font-size: 24px; }

    .progress-bar {
      height: 4px;
      background: #1e1e3a;
      border-radius: 2px;
      margin-top: 8px;
      overflow: hidden;
    }

    .progress-fill {
      height: 100%;
      width: 100%;
      background: #4ec9b0;
      border-radius: 2px;
      transition: width 1s linear;
    }

    /* タイマー残り15秒：赤点滅 */
    #timer-card.urgent {
      border-color: #e94560;
      background: #2a0a0a;
    }

    #timer-card.urgent #timer-display { color: #e94560; }
    #timer-card.urgent .progress-fill { background: #e94560; }
    #timer-card.urgent .stat-label { color: #e94560; }

    @keyframes blink {
      0%, 100% { opacity: 1; }
      50% { opacity: 0.4; }
    }

    #timer-card.urgent { animation: blink 0.8s ease-in-out infinite; }

    /* ダイアログ */
    #dialog-overlay {
      position: fixed;
      inset: 0;
      background: rgba(0, 0, 0, 0.6);
      display: flex;
      align-items: center;
      justify-content: center;
      z-index: 100;
    }

    #dialog-overlay.hidden { display: none; }

    .dialog {
      background: #1e1e3a;
      border: 1px solid #4ec9b0;
      border-radius: 12px;
      padding: 28px 32px;
      text-align: center;
      min-width: 200px;
    }

    .dialog-icon { font-size: 28px; margin-bottom: 8px; }

    .dialog-title {
      font-weight: bold;
      color: #4ec9b0;
      font-size: 15px;
      margin-bottom: 4px;
    }

    .dialog-sub {
      font-size: 12px;
      color: #888;
      margin-bottom: 18px;
    }

    #btn-ok {
      background: #4ec9b0;
      border: none;
      border-radius: 6px;
      padding: 8px 28px;
      color: #0f0f1f;
      font-weight: bold;
      cursor: pointer;
      font-size: 13px;
    }

    #btn-ok:hover { opacity: 0.85; }
  </style>
</head>
<body>
  <div class="container">
    <header>
      <h1>SPEED NOTE</h1>
    </header>

    <div class="main">
      <!-- 左：入力エリア -->
      <div class="input-area">
        <div class="field">
          <label for="title">TITLE</label>
          <input type="text" id="title" placeholder="テーマを入力してください">
        </div>

        <div class="field">
          <label for="memo">MEMO <span id="line-hint">0行 / 目安4〜6行</span></label>
          <textarea id="memo" placeholder="ここに考えを書く"></textarea>
        </div>

        <div class="buttons">
          <button id="btn-copy">📋 コピー</button>
          <button id="btn-clear">🗑 クリア</button>
        </div>
      </div>

      <!-- 右：サイドバー -->
      <div class="sidebar">
        <div class="stat-card" id="timer-card">
          <div class="stat-label">TIME</div>
          <div class="stat-value" id="timer-display">01:00</div>
          <div class="progress-bar">
            <div class="progress-fill" id="progress-fill"></div>
          </div>
          <div class="stat-sub">残り時間</div>
        </div>

        <div class="stat-card">
          <div class="stat-label">LINES</div>
          <div class="stat-value" id="lines-display">0</div>
          <div class="stat-sub">目安 4〜6</div>
        </div>

        <div class="stat-card">
          <div class="stat-label">TODAY</div>
          <div class="stat-value" id="today-display">0</div>
          <div class="stat-sub">枚目</div>
        </div>
      </div>
    </div>
  </div>

  <!-- 完了ダイアログ -->
  <div id="dialog-overlay" class="hidden">
    <div class="dialog">
      <div class="dialog-icon">⏱</div>
      <div class="dialog-title">1分経過しました</div>
      <div class="dialog-sub">メモが完成したよ！</div>
      <button id="btn-ok">OK</button>
    </div>
  </div>

  <script>
    // JS は次のタスクで追加する
  </script>
</body>
</html>
```

- [ ] **Step 2: Flask でページが表示されるか確認する**

```
cd cc-practice/phase9/9.3-speed-note
python app.py
```

ブラウザで `http://localhost:5000` を開く。
期待: タイトル「SPEED NOTE」、入力エリア、サイドバーが表示される（まだ動かない）

- [ ] **Step 3: サーバーを止める（Ctrl+C）**

- [ ] **Step 4: コミット**

```bash
git add cc-practice/phase9/9.3-speed-note/templates/index.html
git commit -m "feat: add SpeedNote UI skeleton with CSS"
```

---

## Task 4: 箇条書き JS

**Files:**
- Modify: `cc-practice/phase9/9.3-speed-note/templates/index.html` の `<script>` タグ内

- [ ] **Step 1: `<script>` の中身を以下で置き換える**

```javascript
const INDENT = '　'; // 全角スペース（・の幅に合わせるインデント）

const memo = document.getElementById('memo');

// 初期状態：・を先頭に
memo.value = '・';
memo.selectionStart = memo.selectionEnd = 1;

function onMemoChange() {
  updateLines();
}

memo.addEventListener('input', onMemoChange);

memo.addEventListener('keydown', function (e) {
  if (memo.hasAttribute('readonly')) return;

  const val = this.value;
  const pos = this.selectionStart;

  if (e.key === 'Enter' && !e.shiftKey) {
    e.preventDefault();
    const lineStart = val.lastIndexOf('\n', pos - 1) + 1;
    const currentLine = val.substring(lineStart, pos);
    if (currentLine === '・') return; // 空の箇条書き行では何もしない
    const newVal = val.substring(0, pos) + '\n・' + val.substring(this.selectionEnd);
    this.value = newVal;
    this.selectionStart = this.selectionEnd = pos + 2; // \n + ・
    onMemoChange();
    return;
  }

  if (e.key === 'Enter' && e.shiftKey) {
    e.preventDefault();
    const newVal = val.substring(0, pos) + '\n' + INDENT + val.substring(this.selectionEnd);
    this.value = newVal;
    this.selectionStart = this.selectionEnd = pos + 1 + INDENT.length;
    onMemoChange();
    return;
  }

  if (e.key === 'Backspace') {
    const lineStart = val.lastIndexOf('\n', pos - 1) + 1;
    const currentLine = val.substring(lineStart, pos);
    if ((currentLine === '・' || currentLine === INDENT) && lineStart > 0) {
      e.preventDefault();
      const newVal = val.substring(0, lineStart - 1) + val.substring(pos);
      this.value = newVal;
      this.selectionStart = this.selectionEnd = lineStart - 1;
      onMemoChange();
    }
  }
});

function updateLines() {
  const lines = memo.value.split('\n').filter(l => l.trim() !== '');
  document.getElementById('lines-display').textContent = lines.length;
}
```

- [ ] **Step 2: 手動で動作を確認する**

```
python app.py
```

`http://localhost:5000` を開いて以下を確認:
- メモエリアに最初から「・」がある
- 文字を入力してEnterで「・」が次の行に追加される
- Shift+Enterで全角スペースインデントの改行になる
- 「・」だけの行でBackspaceすると行が消えて前の行に戻る
- 行数カウント（LINES）が増減する

- [ ] **Step 3: サーバーを止める（Ctrl+C）**

- [ ] **Step 4: コミット**

```bash
git add cc-practice/phase9/9.3-speed-note/templates/index.html
git commit -m "feat: add bullet-point keyboard behavior to memo area"
```

---

## Task 5: タイマー JS

**Files:**
- Modify: `cc-practice/phase9/9.3-speed-note/templates/index.html` の `<script>` タグ内

- [ ] **Step 1: Task 4 の `<script>` に以下を追記する（既存コードの下に追加）**

```javascript
let timerState = 'idle';   // 'idle' | 'running' | 'done'
let timerInterval = null;
let remainingSeconds = 60;
let timerExpired = false;

function startTimer() {
  if (timerState !== 'idle') return;
  timerState = 'running';
  timerInterval = setInterval(tick, 1000);
}

function tick() {
  remainingSeconds--;
  updateTimerDisplay();

  if (remainingSeconds <= 15) {
    document.getElementById('timer-card').classList.add('urgent');
  }

  if (remainingSeconds <= 0) {
    clearInterval(timerInterval);
    timerState = 'done';
    timerExpired = true;
    lockMemo();
    showDialog();
  }
}

function updateTimerDisplay() {
  const mins = Math.floor(remainingSeconds / 60).toString().padStart(2, '0');
  const secs = (remainingSeconds % 60).toString().padStart(2, '0');
  document.getElementById('timer-display').textContent = `${mins}:${secs}`;
  const pct = (remainingSeconds / 60) * 100;
  document.getElementById('progress-fill').style.width = `${pct}%`;
}

function lockMemo() {
  memo.setAttribute('readonly', '');
}

function showDialog() {
  document.getElementById('dialog-overlay').classList.remove('hidden');
}
```

- [ ] **Step 2: `onMemoChange` 関数を以下に更新する（既存の関数を置き換える）**

```javascript
function onMemoChange() {
  updateLines();
  startTimer(); // 最初の入力でタイマースタート
}
```

- [ ] **Step 3: 手動で動作を確認する**

```
python app.py
```

`http://localhost:5000` を開いて以下を確認:
- メモエリアに文字を入力するとタイマーが 01:00 からカウントダウンを開始する
- 残り15秒になるとタイマーエリアが赤く点滅する
- 0秒になるとメモエリアが入力不可になり、ダイアログが表示される（ただし OK ボタンはまだ機能しない）

- [ ] **Step 4: サーバーを止める（Ctrl+C）**

- [ ] **Step 5: コミット**

```bash
git add cc-practice/phase9/9.3-speed-note/templates/index.html
git commit -m "feat: add 60-second timer with urgent blink at 15 seconds"
```

---

## Task 6: ダイアログ・コピー・クリア・API 連携

**Files:**
- Modify: `cc-practice/phase9/9.3-speed-note/templates/index.html` の `<script>` タグ内

- [ ] **Step 1: `<script>` に以下を追記する（既存コードの下に追加）**

```javascript
// ダイアログ OK ボタン
document.getElementById('btn-ok').addEventListener('click', function () {
  document.getElementById('dialog-overlay').classList.add('hidden');
});

// コピーボタン
document.getElementById('btn-copy').addEventListener('click', async function () {
  const text = memo.value;
  try {
    await navigator.clipboard.writeText(text);
  } catch {
    // フォールバック（clipboard API が使えない環境用）
    memo.removeAttribute('readonly');
    memo.select();
    document.execCommand('copy');
    if (timerState === 'done') memo.setAttribute('readonly', '');
  }
  this.textContent = '✅ コピー済';
  setTimeout(() => { this.textContent = '📋 コピー'; }, 2000);
});

// クリアボタン
document.getElementById('btn-clear').addEventListener('click', async function () {
  if (timerExpired) {
    try {
      await fetch('/api/count/increment', { method: 'POST' });
      const res = await fetch('/api/count');
      const data = await res.json();
      document.getElementById('today-display').textContent = data.count;
    } catch {
      // サーバーとの通信失敗は無視（カウント更新スキップ）
    }
  }
  resetAll();
});

function resetAll() {
  document.getElementById('title').value = '';
  memo.value = '・';
  memo.removeAttribute('readonly');
  memo.selectionStart = memo.selectionEnd = 1;
  memo.focus();

  clearInterval(timerInterval);
  timerInterval = null;
  remainingSeconds = 60;
  timerState = 'idle';
  timerExpired = false;

  document.getElementById('timer-card').classList.remove('urgent');
  updateTimerDisplay();
  updateLines();
}

// ページロード時に TODAY カウントを取得
(async function loadTodayCount() {
  try {
    const res = await fetch('/api/count');
    const data = await res.json();
    document.getElementById('today-display').textContent = data.count;
  } catch {
    // 取得失敗時は 0 のまま
  }
})();
```

- [ ] **Step 2: エンドツーエンドで動作を確認する**

```
python app.py
```

`http://localhost:5000` を開いて以下のフローを通して確認する:

1. タイトルにテキストを入力する
2. メモに箇条書きを3〜4行書く
3. タイマーが動き出していることを確認
4. 残り15秒で赤点滅することを確認（タイマーを 60→15 秒に短縮したい場合は `remainingSeconds = 15` に一時変更してテスト）
5. 0秒でダイアログが出ることを確認
6. OK でダイアログが閉じ、メモが入力不可のままであることを確認
7. コピーボタンでメモ内容がクリップボードにコピーされることを確認（メモ帳に貼り付けて確認）
8. クリアボタンでリセットされ TODAY が +1 になることを確認

- [ ] **Step 3: サーバーを止める（Ctrl+C）**

- [ ] **Step 4: コミット**

```bash
git add cc-practice/phase9/9.3-speed-note/templates/index.html
git commit -m "feat: add dialog, copy, clear with API counter integration"
```

---

## Task 7: start.bat + README

**Files:**
- Create: `cc-practice/phase9/9.3-speed-note/start.bat`
- Modify: `cc-practice/README.md`（Phase インデックスに追記）

- [ ] **Step 1: start.bat を作る**

`start.bat`:
```batch
@echo off
cd /d %~dp0
echo SpeedNote を起動中...
start /B python app.py
timeout /t 2 /nobreak >nul
start http://localhost:5000
echo ブラウザを開きました。サーバーを止めるには Ctrl+C を押してください。
```

- [ ] **Step 2: start.bat をダブルクリックして動作確認する**

期待:
- コマンドプロンプトが開き「SpeedNote を起動中...」と表示される
- 2秒後にブラウザが `http://localhost:5000` を開く
- SpeedNote の UI が表示される

- [ ] **Step 3: cc-practice/README.md の Phase テーブルに追記する**

既存のテーブルに以下の行を追加:

```markdown
| phase9/9.3-speed-note | SpeedNote — タイムド箇条書きメモツール（Python + Flask） |
```

- [ ] **Step 4: コミット**

```bash
git add cc-practice/phase9/9.3-speed-note/start.bat cc-practice/README.md
git commit -m "feat: add start.bat launcher and update README index"
```

---

## 完了チェックリスト

- [ ] `pytest tests/test_app.py -v` が全テスト PASS
- [ ] `start.bat` でブラウザが開く
- [ ] 箇条書き（Enter / Shift+Enter / Backspace）が期待通り動く
- [ ] タイマーが入力開始でスタートし、残り15秒で赤点滅する
- [ ] 0秒でメモがロックされダイアログが出る
- [ ] コピーボタンでクリップボードにコピーされる
- [ ] クリアで全リセット、TODAY カウントが +1 される
- [ ] `data/` フォルダが `.gitignore` で除外されている
