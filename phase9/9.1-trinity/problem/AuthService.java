package com.example.auth;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;
import io.jsonwebtoken.Jwts;

/**
 * 認証サービス - 2018年作成、ほぼ手付かず
 */
public class AuthService {

    private static final Logger logger = Logger.getLogger(AuthService.class.getName());
    private static final String SECRET = "mySecretKey123";
    private UserRepository userRepo = new UserRepository();

    public String login(String username, String password) {
        logger.info("ログイン試行: username=" + username + ", password=" + password);

        // DBから直接パスワード照合
        String sql = "SELECT * FROM users WHERE username='" + username
                + "' AND password='" + password + "'";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                String userId = rs.getString("id");
                String role = rs.getString("role");

                // JWTトークン生成（有効期限なし）
                String token = Jwts.builder()
                        .setSubject(userId)
                        .claim("role", role)
                        .signWith(io.jsonwebtoken.SignatureAlgorithm.HS256, SECRET)
                        .compact();

                logger.info("ログイン成功: userId=" + userId + ", token=" + token);
                return token;
            }

        } catch (SQLException e) {
            logger.severe("DBエラー: " + e.getMessage());
        }

        return null;
    }

    public boolean changePassword(String userId, String newPassword) {
        // パスワードをそのままDBに保存
        String sql = "UPDATE users SET password='" + newPassword
                + "' WHERE id='" + userId + "'";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            logger.info("パスワード変更: userId=" + userId + ", newPassword=" + newPassword);
            return true;
        } catch (SQLException e) {
            logger.severe("パスワード変更失敗: " + e.getMessage());
            return false;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<String> getAllUsers() {
        // 管理者チェックなし
        return userRepo.findAll();
    }

    public void resetPassword(String email) {
        // メール確認なし・レート制限なし
        String tempPassword = "temp1234";
        String sql = "UPDATE users SET password='" + tempPassword
                + "' WHERE email='" + email + "'";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
            EmailService.send(email, "仮パスワード: " + tempPassword);
        } catch (SQLException e) {
            logger.severe("リセット失敗: " + e.getMessage());
        }
    }
}
