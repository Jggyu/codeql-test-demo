package com.example;

import java.io.*;
import java.nio.file.*;
import javax.servlet.http.HttpServletRequest;

/**
 * 파일 처리 관련 취약점을 포함한 서비스
 */
public class VulnerableFileService {
    
    // 취약점 6: Path Traversal 공격 취약점
    public String readFile(String filename) throws IOException {
        // 사용자 입력을 직접 파일 경로에 사용 - Path Traversal 취약
        String filePath = "/app/data/" + filename;
        
        return new String(Files.readAllBytes(Paths.get(filePath)));
    }
    
    // 취약점 7: 파일 업로드시 경로 검증 없음
    public void uploadFile(HttpServletRequest request, String filename) throws IOException {
        // 파일명 검증 없이 직접 사용
        String uploadPath = "/uploads/" + filename;
        
        try (FileOutputStream fos = new FileOutputStream(uploadPath)) {
            // 파일 저장 로직...
        }
    }
    
    // 취약점 8: Command Injection 취약점
    public String executeSystemCommand(String userInput) throws IOException {
        // 사용자 입력을 직접 시스템 명령어에 포함 - Command Injection 취약
        String command = "ls -la " + userInput;
        
        Process process = Runtime.getRuntime().exec(command);
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }
            
            return result.toString();
        }
    }
    
    // 취약점 9: 리소스 누수 (파일 스트림을 제대로 닫지 않음)
    public String readFileUnsafely(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
        
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line);
        }
        
        // try-with-resources 사용하지 않아 리소스 누수 가능
        return content.toString();
    }
    
    // 취약점 10: 또 다른 Command Injection (배열 형태)
    public void executeCommandArray(String userCommand) throws IOException {
        // 사용자 입력을 명령어 배열에 직접 포함
        String[] commands = {"sh", "-c", userCommand};
        
        Runtime.getRuntime().exec(commands);
    }
}