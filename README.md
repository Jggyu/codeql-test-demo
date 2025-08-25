# CodeQL 보안 분석 데모

이 프로젝트는 CodeQL의 보안 취약점 탐지 기능을 테스트하기 위해 **의도적으로 취약점을 포함**한 Java 애플리케이션입니다.

⚠️ **경고**: 이 코드는 교육 목적으로만 작성되었습니다. 실제 애플리케이션에서는 절대 사용하지 마세요!

## 포함된 취약점들

### 🔴 보안 취약점

1. **SQL Injection (CWE-89)**
   - `VulnerableUserService.getUserById()`
   - `VulnerableUserService.loginUser()`
   - 사용자 입력을 직접 SQL 쿼리에 연결

2. **Cross-Site Scripting - XSS (CWE-79)**
   - `VulnerableUserService.displayUserProfile()`
   - 사용자 입력을 HTML에 직접 출력

3. **Hard-coded Credentials (CWE-798)**
   - 데이터베이스 패스워드, API 키가 소스코드에 하드코딩

4. **Path Traversal (CWE-22)**
   - `VulnerableFileService.readFile()`
   - 사용자 입력으로 파일 경로 조작 가능

5. **Command Injection (CWE-78)**
   - `VulnerableFileService.executeSystemCommand()`
   - 사용자 입력을 시스템 명령어에 직접 포함

6. **Resource Leak (CWE-404)**
   - `VulnerableFileService.readFileUnsafely()`
   - 파일 스트림을 제대로 닫지 않음

## CodeQL 분석 실행하기

### 1. GitHub Actions (자동)
이 레포지토리에서는 다음 상황에서 자동으로 CodeQL 분석이 실행됩니다:
- `main` 브랜치에 push할 때
- Pull Request 생성시
- 매주 월요일 정기 스캔

### 2. 로컬에서 실행

```bash
# 1. CodeQL CLI 다운로드 및 설치
# https://github.com/github/codeql-action/releases

# 2. CodeQL 데이터베이스 생성
codeql database create codeql-demo-db \\
  --language=java \\
  --source-root=. \\
  --command="mvn clean compile -DskipTests"

# 3. 보안 분석 실행
codeql database analyze codeql-demo-db \\
  --format=sarif-latest \\
  --output=results.sarif \\
  java-security-and-quality

# 4. 결과 확인
codeql bqrs decode codeql-demo-db/results/*.bqrs --format=csv
```

### 3. VS Code에서 실행

1. CodeQL extension 설치
2. 이 레포지토리를 클론
3. CodeQL workspace로 데이터베이스 추가
4. 쿼리 실행

## 예상 결과

CodeQL 분석을 실행하면 다음과 같은 이슈들이 발견될 것입니다:

- **High Severity**: SQL Injection, Command Injection
- **Medium Severity**: XSS, Path Traversal  
- **Low/Info**: Hard-coded credentials, Resource leaks

## 수정된 안전한 코드 예시

```java
// ❌ 취약한 코드 (SQL Injection)
String query = "SELECT * FROM users WHERE id = '" + userId + "'";

// ✅ 안전한 코드 (Prepared Statement)
String query = "SELECT * FROM users WHERE id = ?";
PreparedStatement stmt = conn.prepareStatement(query);
stmt.setString(1, userId);
```

```java
// ❌ 취약한 코드 (XSS)
out.println("<h1>Welcome, " + username + "!</h1>");

// ✅ 안전한 코드 (HTML Escape)
out.println("<h1>Welcome, " + StringEscapeUtils.escapeHtml4(username) + "!</h1>");
```

## 실습해보기

1. 이 레포지토리를 fork하세요
2. Actions 탭에서 CodeQL workflow가 실행되는 것을 확인하세요
3. Security 탭에서 발견된 취약점들을 확인하세요
4. 취약점을 수정하고 다시 분석해보세요

## 참고 자료

- [CodeQL 공식 문서](https://codeql.github.com/docs/)
- [GitHub Advanced Security](https://docs.github.com/en/code-security)
- [OWASP Top 10](https://owasp.org/www-project-top-ten/)

---

📚 **학습 목적으로만 사용하세요!** 실제 애플리케이션에서는 이런 취약점들을 피해야 합니다.