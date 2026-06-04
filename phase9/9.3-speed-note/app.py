import json
import os
from datetime import date
from flask import Flask, jsonify, render_template, request

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


@app.route('/api/count/set', methods=['POST'])
def set_count():
    value = request.get_json(silent=True, force=True) or {}
    count = value.get('count', 0)
    if not isinstance(count, int) or count < 0:
        return jsonify({'error': 'invalid value'}), 400
    data = _load_counter()
    data['count'] = count
    _save_counter(data)
    return jsonify({'count': data['count']})


if __name__ == '__main__':
    app.run(host='127.0.0.1', port=5000, debug=False)
