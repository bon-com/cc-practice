# Trinity 分析レポート — レガシー認証コード診断と修正計画

> 生成日: 2026-05-21
> 対象: `phase9/9.1-trinity/problem/` 配下の Java 認証コード
> ワークフロー: Plan Mode × Extended Thinking × Sequential（並列 Explore × 3 + Plan agent）

---

## 1. エグゼクティブサマリ

たった 2 ファイル・145 行のレガシー認証コードに、**Critical 10 件 / High 11 件 / Medium 9 件** の重大問題が同居している。本番投入は危険であり、段階的修正が必要。

| 分類 | Critical | High | Medium | 計 |
|---|---|---|---|---|
| セキュリティ | 10 | 6 | 2 | 18 |
| パフォーマンス | 0 | 5 | 4 | 9 |
| 設計 | 0 | 5 | 6 | 11 |
| 合計（重複除外） | **10** | **11** | **9** | **30** |

**主犯 TOP5**:
1. SQL インジェクション（4 箇所）
2. パスワード平文保存・ログ出力（5 箇所）
3. JWT 永久有効＋シークレットハードコード
4. 認可チェック皆無（`getAllUsers`、`resetPassword`）
5. God Class（`AuthService` に 5 責務同居）

---

## 2. 対象コード

| ファイル | 行数 | 役割 |
|---|---|---|
| `problem/AuthService.java` | 96 | login / changePassword / validateToken / getAllUsers / resetPassword |
| `problem/UserRepository.java` | 49 | findAll / findById |

**外部依存**（`problem/` 配下に実体ファイルなし）:
- `DatabaseManager.getConnection()` … DB 接続取得
- `EmailService.send()` … メール送信
- `io.jsonwebtoken.Jwts` … JWT 生成・検証

---

## 3. 優先順位付き問題リスト

### 3.1 Critical（即時対応必須）

| # | 問題 | ファイル:行 | 影響 |
|---|---|---|---|
| C1 | SQL インジェクション（login） | `AuthService.java:21-22` | 認証バイパス、DB 全データ窃取 |
| C2 | SQL インジェクション（changePassword） | `AuthService.java:52-53` | 他人パスワード書き換え |
| C3 | SQL インジェクション（resetPassword） | `AuthService.java:85-86` | アカウント乗っ取り |
| C4 | SQL インジェクション（findById） | `UserRepository.java:31` | 全カラム抽出 |
| C5 | パスワード平文比較・保存（login） | `AuthService.java:21-22` | DB 漏洩 = 全 PW 漏洩 |
| C6 | パスワード平文保存（changePassword） | `AuthService.java:52-53` | 同上 |
| C7 | 仮 PW ハードコード `"temp1234"` ＋ 平文保存 | `AuthService.java:84-86` | 推測可能、全員同じ |
| C8 | パスワードをログ出力（login） | `AuthService.java:18` | ログ漏洩 = PW 漏洩 |
| C9 | 新パスワードをログ出力（changePassword） | `AuthService.java:58` | 同上 |
| C10 | `findAll()` が `username:password` を返す | `UserRepository.java:19` | 認証済みユーザー全員に PW 露出 |

### 3.2 High（次スプリント）

| # | 問題 | ファイル:行 | 影響 |
|---|---|---|---|
| H1 | JWT に有効期限なし | `AuthService.java:33-37` | 盗難トークンが永久に有効 |
| H2 | JWT シークレット `"mySecretKey123"` ハードコード | `AuthService.java:14` | リポジトリ漏洩でトークン偽造可能 |
| H3 | `getAllUsers` に認可チェックなし | `AuthService.java:77-80` | 一般ユーザーでも全件取得 |
| H4 | `resetPassword` にレート制限・メール所有確認なし | `AuthService.java:82-95` | アカウント乗っ取り、メールスパム |
| H5 | `findById` が全カラム（password 含む）を Map 返却 | `UserRepository.java:38-41` | API 経由で PW 露出 |
| H6 | コネクションプール未使用 | 全 DB アクセス箇所 | 接続枯渇、レイテンシ悪化 |
| H7 | `findAll` がページングなしで全件メモリロード | `UserRepository.java:8-26` | ヒープ枯渇 |
| H8 | `executeUpdate` の戻り値未確認 | `AuthService.java:57, 90` | 失敗時に成功と誤認 |
| H9 | `e.printStackTrace()` を本番想定で使用 | `UserRepository.java:22, 44` | ログ管理不能、情報漏洩 |
| H10 | `AuthService` が God Class（5 責務同居） | `AuthService.java` 全体 | テスト不能、変更影響大 |
| H11 | 入力バリデーション皆無 | 全 public メソッド | null・不正入力で異常終了 |

### 3.3 Medium（並行で潰す）

- `catch (Exception e) { return false; }` で例外握りつぶし（`AuthService.java:72-74`）
- DI 不在・`new UserRepository()` をフィールド直書き → 単体テスト不能
- `IUserRepository` 等インターフェース未抽出
- `Map<String,Object>` 戻り型による型安全性の欠如
- SQL 文字列の DRY 違反
- null 返却（`Optional` 未使用）
- ログレベルの不適切（INFO で機密も平文も）
- `ResultSetMetaData` をループ内で利用する非効率
- `findById` の HashMap キャッシュなし

---

## 4. 修正計画（4 Phase）

### 4.1 順序の根拠

```
Phase 1 (止血)
   │  ← SQLi/平文ログ/レスポンス漏洩を API 互換のまま塞ぐ
   ▼
Phase 2 (認証ロジックの正しさ)
   │  ← ハッシュ化・認可・レート制限。dual-read で段階移行
   ▼
Phase 3 (性能・運用堅牢性)
   │  ← プール、ページング、戻り値検証。仕様は変えない
   ▼
Phase 4 (設計改善)
       ← クラス分割、DI、interface 化。Facade で後方互換
```

**逆順だと**: SQLi 残存のままハッシュ化しても `'OR 1=1--` で素通り／性能改善が攻撃スループットを上げる／空 interface に脆弱実装を載せて「分割完了」と誤認、と退行が頻発する。

---

### 4.2 Phase 1: 致命的脆弱性の止血（HOTFIX）

**ゴール**: public API のシグネチャを変えずに、SQLi・平文ログ・パスワード漏洩レスポンスを即座に塞ぐ。

**対応**:
- C1〜C4: 全 SQL を `PreparedStatement` 化
- C8〜C9: パスワードを含むログ出力を削除
- C10: `findAll()` 戻り値から `:password` 除去（型 `List<String>` 維持・中身を username のみに）
- H5: `findById()` の Map から `password` カラムをスキップ
- H1: JWT に `setExpiration(now + 15min)` を付与
- H2: JWT シークレットを環境変数 `AUTH_JWT_SECRET` から読み込み、未設定なら fail-fast
- H9: `e.printStackTrace()` → `logger.severe(msg, e)`

**触るファイル**: `problem/AuthService.java`, `problem/UserRepository.java` のみ

**完了条件**:
- `Statement` 直接使用 / `executeQuery(...+...)` がゼロ件
- `logger.*` 行に `password`/`newPassword` が出現しない
- `findAll()` 戻り値に `:` が含まれない
- `findById()` Map に `password` キーが含まれない
- `AUTH_JWT_SECRET` 未設定で起動失敗

**意図的にやらないこと**: ハッシュ化（dual-read 設計が必要）、クラス分割（API 破壊）、プール導入

---

### 4.3 Phase 2: 認証ロジックの正しさ

**ゴール**: パスワードを保存・比較・送信のいずれでも平文にしない。`getAllUsers`/`resetPassword` の濫用対策。

**対応**:
- C5: BCrypt（Spring Security crypto or jBCrypt）で `passwordEncoder.matches()` に変更
- C6: `changePassword` で `encode()` してから保存
- C7: `resetPassword` の仮 PW を `SecureRandom` 16 文字、ハッシュ化保存、メールには平文 1 回のみ
- **dual-read**: 保存値が `$2a$` 始まりでなければ「legacy 平文比較 → 成功時に encode 書き戻し」
- H3: `getAllUsers(String token)` を追加、JWT クレイムから role 検証、ADMIN のみ通す
- H4: `resetPassword` にレート制限、応答時間を一定化（タイミング攻撃対策）
- H11: 入力バリデーション（username 形式、password 最小長、email 形式）

**触るファイル**:
- `problem/AuthService.java`
- 新規 `problem/PasswordHasher.java`
- 新規 `problem/RateLimiter.java`

**完了条件**: 新規書き込み password が `$2a$` 始まり／USER role JWT で `getAllUsers` 拒否／レート制限発火／legacy 平文ユーザーがログイン後 hashed に置換

---

### 4.4 Phase 3: パフォーマンスと運用堅牢性

**ゴール**: コネクション枯渇・メモリ枯渇・サイレントエラーの排除。仕様は不変。

**対応**:
- H6: HikariCP を `DatabaseManager` に導入。公開 API `getConnection()` は維持
- H7: `findAll(int offset, int limit)` を追加。旧 `findAll()` は `findAll(0, 100)` を呼ぶ後方互換ラッパ
- H8: `executeUpdate` 戻り値 0 → 業務例外（`RecordNotFoundException` 等）
- `UserRepository` をステートレスに保つ（`final`）
- メトリクス: ログイン成功/失敗、resetPassword レート、プール稼働数

**触るファイル**: `AuthService.java`, `UserRepository.java`, `DatabaseManager.java`（新設 or 既存修正）

**完了条件**: 100 並列ログインで `Too many connections` が出ない／`findAll()` が 100 件で打ち切り／`printStackTrace` の grep 件数ゼロ／戻り値 0 が表面化

---

### 4.5 Phase 4: 設計改善（クラス分割・DI・interface）

**ゴール**: God Class 解体。テスト容易性と将来拡張性を確保。**初めて public API を大きく変える**。

**クラス分割**:

```
com.example.auth
  ├─ service/
  │   ├─ LoginService          # login → TokenResponse
  │   ├─ PasswordService       # changePassword / resetPassword
  │   ├─ TokenService          # issue / validate / parseClaims
  │   └─ UserAdminService      # getAllUsers / getUserById（要 ADMIN）
  ├─ repository/
  │   ├─ IUserRepository       # interface
  │   └─ JdbcUserRepository    # 旧 UserRepository 実装
  ├─ domain/
  │   ├─ User                  # password ハッシュ持ち
  │   ├─ UserSummary           # password を持たない一覧用 DTO
  │   └─ Role                  # enum: USER, ADMIN
  ├─ security/
  │   ├─ PasswordHasher        # Phase 2 で導入
  │   └─ RateLimiter           # Phase 2 で導入
  └─ infra/
      ├─ IConnectionProvider   # DatabaseManager のラッパ
      ├─ IEmailSender          # EmailService のラッパ
      └─ IClock                # 時刻抽象化（JWT exp テスト用）
```

**IUserRepository シグネチャ案**:

```java
public interface IUserRepository {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<UserSummary> findById(String userId);
    List<UserSummary> findAll(int offset, int limit);
    int updatePasswordHash(String userId, String hash);
}
```

**Facade 戦略**: 旧 `AuthService` は内部で 4 サービスを保持する Facade として残置。`@Deprecated` を付け、2 リリースサイクル後に撤去。

**完了条件**: `AuthService` 内に `new UserRepository()` がない／モック注入で全サービス単体テストが DB なしで通る／Phase 1〜3 テストが Facade 経由で全件緑

---

## 5. リスクと緩和

| リスク | 緩和策 |
|---|---|
| Phase 1 で JWT シークレット変更 → 全ユーザー強制ログアウト | メンテナンス窓実施、事前告知 |
| Phase 2 dual-read 中に平文 PW が残存 | 移行完了率メトリクス、N 日後に強制リセット |
| Phase 3 プール導入で `getConnection()` セマンティクス変更 | close 規約（プール返却）を JavaDoc 明記 |
| Phase 4 Facade 経由で挙動差 | Phase 1〜3 のテストを Facade 越しに CI 再実行 |

---

## 6. 検証方法（End to End）

### Phase 1
- 静的: `Statement` 直接使用 / `executeQuery(...+...)` / `printStackTrace` がゼロ件
- 動的: SQLi ペイロード 5 種を login に投げ全件 `null` 返却
- ログキャプチャで `password=` 不出現

### Phase 2
- 平文ユーザー → ログイン → DB の password カラム先頭が `$2a$` になることを SQL で確認
- USER role JWT で `getAllUsers(token)` 拒否
- レート制限の閾値超で後続拒否

### Phase 3
- JMeter 等で 100 並列ログインを 1 分継続、プール稼働数が頭打ち
- `findAll()` が 100 件で打ち切り
- 存在しない userId への `changePassword` で業務例外

### Phase 4
- `grep "new UserRepository\|new JdbcUserRepository\|DatabaseManager\.getConnection" problem/service/` がゼロ
- モック注入のみで全サービス単体テストが DB なしで通過
- Phase 1〜3 テストを Facade 経由で再実行し全件緑

---

## 7. 実行時の注意事項

- `DatabaseManager` / `EmailService` は `problem/` 配下に実体ファイルがない。Phase 1 着手前にダミー実装で stub 化するか、ビルド対象外として扱うか、明示的に決める必要がある。
- BCrypt 依存（jBCrypt or Spring Security crypto）の追加は `pom.xml` / `build.gradle` 修正が伴う。ビルドツールの実態確認が前提。
- 練習目的のコードであり、本番投入を想定するなら DB マイグレーションスクリプトと運用手順書（ロールバック含む）の整備が別途必要。

---

## 8. Trinity ワークフロー実施記録

| 段階 | 手段 |
|---|---|
| Phase 1: 理解 | Explore agent × 3（セキュリティ／パフォーマンス／設計）並列実行 |
| Phase 2: 設計 | Plan agent × 1（4 Phase 構成、順序根拠、Facade 戦略を生成） |
| Phase 3: レビュー | 対象 2 ファイルを直接 Read で確認、エージェント結果と完全一致を検証 |
| Phase 4: 計画化 | `~/.claude/plans/` に計画ファイルを保存、本レポートを `output/` に保存 |
| Phase 5: 承認 | ExitPlanMode で承認取得 |
