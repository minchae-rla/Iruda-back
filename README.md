<div align="center">

# 📅 IRUDA
### 일정 · 프로젝트 관리 웹 애플리케이션

React + TypeScript + Spring Boot 기반의  
캘린더형 일정 관리 및 프로젝트 관리 웹 서비스

<br>

<img src="https://github.com/user-attachments/assets/9a358391-ee4c-4485-a3e3-a6bee06682d6" width="90%" />

<br><br>

<img src="https://img.shields.io/badge/Java-17-orange?style=for-the-badge&logo=openjdk" />
<img src="https://img.shields.io/badge/SpringBoot-3.x-green?style=for-the-badge&logo=springboot" />
<img src="https://img.shields.io/badge/React-Vite-blue?style=for-the-badge&logo=react" />
<img src="https://img.shields.io/badge/TypeScript-3178C6?style=for-the-badge&logo=typescript&logoColor=white" />
<img src="https://img.shields.io/badge/TailwindCSS-06B6D4?style=for-the-badge&logo=tailwindcss&logoColor=white" />
<img src="https://img.shields.io/badge/PostgreSQL-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" />

</div>

---

# 📌 프로젝트 소개

IRUDA는 현대 사회에서 개인 및 팀의 일정 관리를 효율적으로 지원하기 위해 제작한  
**캘린더 기반 일정 관리 웹 애플리케이션**입니다.

기존에는 Java / Spring 기반 웹 개발만 경험했지만,  
개인 학습을 통해 React · TypeScript · TailwindCSS를 학습하며  
프론트엔드와 백엔드를 직접 연결하는 프로젝트를 진행했습니다.

단순 CRUD 구현을 넘어 실제 서비스처럼 동작할 수 있도록  
JWT 인증, 자동 로그인, 카카오 회원가입 및 로그인, 프로젝트별 일정 관리 기능 등을 구현했습니다.

---

# 🚀 주요 기능

## 👤 사용자 인증

- JWT 기반 로그인 / 회원가입
- Access Token 기반 인증 처리
- 자동 로그인 기능 구현
- 카카오 OAuth 회원가입 및 로그인 구현

<br>

## 📅 일정 관리

- 캘린더 기반 일정 조회
- 일정 등록 / 수정 / 삭제 (CRUD)
- 프로젝트별 일정 분류
- 일정 색상 지정 기능
- 알람 여부 설정 기능

<br>

## 🔔 알림 기능

- 일정 시작 전 알림 기능 구현
- 사용자 설정 기반 알람 관리

---

# 🛠 기술 스택

<table>
<tr>
<td align="center"><b>Category</b></td>
<td align="center"><b>Tech Stack</b></td>
</tr>

<tr>
<td align="center">Backend</td>
<td>

- Java 17
- Spring Boot
- Spring Security
- JWT
- JPA

</td>
</tr>

<tr>
<td align="center">Frontend</td>
<td>

- React (Vite)
- TypeScript
- TailwindCSS

</td>
</tr>

<tr>
<td align="center">Database</td>
<td>

- PostgreSQL

</td>
</tr>

<tr>
<td align="center">Version Control</td>
<td>

- Git
- GitHub

</td>
</tr>
</table>

---

# 🧩 프로젝트 구조

```bash
IRUDA
 ┣ backend
 ┃ ┣ domain
 ┃ ┣ auth
 ┃ ┣ config
 ┃ ┣ repository
 ┃ ┗ service
 ┃
 ┣ frontend
 ┃ ┣ components
 ┃ ┣ pages
 ┃ ┣ hooks
 ┃ ┣ api
 ┃ ┗ store
 ┃
 ┗ database
