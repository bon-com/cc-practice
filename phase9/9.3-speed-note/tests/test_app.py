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


def test_set_count(client):
    client.post('/api/count/increment')
    client.post('/api/count/increment')
    client.post('/api/count/set', json={'count': 5})
    res = client.get('/api/count')
    assert res.get_json() == {'count': 5}


def test_set_count_to_zero(client):
    client.post('/api/count/increment')
    client.post('/api/count/set', json={'count': 0})
    res = client.get('/api/count')
    assert res.get_json() == {'count': 0}


def test_set_count_invalid(client):
    res = client.post('/api/count/set', json={'count': -1})
    assert res.status_code == 400


def test_date_reset(client, tmp_path, monkeypatch):
    counter_path = str(tmp_path / 'counter.json')
    monkeypatch.setattr(app_module, 'COUNTER_FILE', counter_path)
    with open(counter_path, 'w') as f:
        json.dump({'date': '2020-01-01', 'count': 99}, f)
    res = client.get('/api/count')
    assert res.get_json() == {'count': 0}
