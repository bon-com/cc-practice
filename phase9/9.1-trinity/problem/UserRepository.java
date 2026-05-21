package com.example.auth;

import java.sql.*;
import java.util.*;

public class UserRepository {

    public List<String> findAll() {
        List<String> users = new ArrayList<>();
        // 全カラム取得（パスワードも含む）
        String sql = "SELECT * FROM users";

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                // パスワードをそのまま文字列に含める
                users.add(rs.getString("username") + ":" + rs.getString("password"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return users;
    }

    public Map<String, Object> findById(String userId) {
        Map<String, Object> user = new HashMap<>();
        // 入力値の検証なし
        String sql = "SELECT * FROM users WHERE id=" + userId;

        try (Connection conn = DatabaseManager.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                ResultSetMetaData meta = rs.getMetaData();
                for (int i = 1; i <= meta.getColumnCount(); i++) {
                    user.put(meta.getColumnName(i), rs.getObject(i));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }
}
