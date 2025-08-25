package com.example;

import java.sql.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 의도적으로 취약점을 포함한 사용자 서비스 클래스
 * CodeQL이 다양한 보안 이슈를 찾아낼 수 있도록 구성
 */
public class VulnerableUserService {

    // 취약점 1: 하드코딩된 데이터베이스 자격증명
    private static final String DB_URL = "jdbc:mysql://localhost:3306/mydb";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "password123!"; // 하드코딩된 패스워드
    
    // 취약점 2: SQL Injection 취약점
    public User getUserById(String userId) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        
        // 직접 문자열 연결로 SQL 쿼리 구성 - SQL Injection 취약
        String query = "SELECT * FROM users WHERE id = '" + userId + "'";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        
        if (rs.next()) {
            User user = new User();
            user.setId(rs.getString("id"));
            user.setName(rs.getString("name"));
            user.setEmail(rs.getString("email"));
            return user;
        }
        
        return null;
    }
    
    // 취약점 3: XSS (Cross-Site Scripting) 취약점
    public void displayUserProfile(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        String username = request.getParameter("username");
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        // 사용자 입력을 직접 HTML에 출력 - XSS 취약
        out.println("<h1>Welcome, " + username + "!</h1>");
        out.println("<p>Your profile: " + request.getParameter("profile") + "</p>");
    }
    
    // 취약점 4: 사용자 입력 기반 SQL 쿼리 (또 다른 SQL Injection)
    public boolean loginUser(String email, String password) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        
        // 동적 쿼리 구성 - SQL Injection 취약
        String query = "SELECT COUNT(*) FROM users WHERE email = '" + email + 
                      "' AND password = '" + password + "'";
        
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        
        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        
        return false;
    }
    
    // 취약점 5: API 키 하드코딩
    private static final String API_KEY = "sk-1234567890abcdef"; // 하드코딩된 API 키
    
    public void callExternalAPI() {
        // API 키를 직접 사용
        String url = "https://api.example.com/data?key=" + API_KEY;
        // API 호출 로직...
    }
}