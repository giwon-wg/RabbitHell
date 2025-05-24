# RabbitHell

## 프로젝트 세팅 가이드 (팀원용)

### 1. 레포 클론
```bash
git clone https://github.com/giwon-wg/RabbitHell.git
cd RabbitHell
```

---

### 2. `.env` 파일 만들기
```bash
cp backend/.env.example backend/.env
```
→ 환경변수 수정: DB 비밀번호, 시크릿 키 등

---

### 3. 커밋 템플릿 등록 (최초 1회만)
```bash
git config commit.template .gitmessage.txt
```
→ 커밋 시 자동으로 메시지 템플릿 적용

---

### 4. Docker로 실행하기
```bash
docker-compose up --build
```
→ MySQL / Redis / Spring Boot 서버가 동시에 실행됨

---

## 디렉토리 구조
```
/RabbitHell
├── backend/              # Spring Boot 백엔드
├── frontend/             # 프론트엔드
├── docker-compose.yml    # 전체 서비스 실행
├── .gitmessage.txt       # 커밋 템플릿
├── .github/              # CI, PR 템플릿, 이슈 템플릿
└── README.md
```

---

## 사용 기술
- Java 17 / Spring Boot 3
- MySQL / Redis
- Docker / Docker Compose
- GitHub Actions (CI)

---
