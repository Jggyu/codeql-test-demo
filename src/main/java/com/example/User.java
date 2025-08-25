package com.example;

/**
 * 간단한 사용자 모델 클래스
 */
public class User {
    private String id;
    private String name;
    private String email;
    
    // 생성자
    public User() {}
    
    public User(String id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }
    
    // Getter와 Setter
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}