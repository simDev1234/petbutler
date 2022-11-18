# 팻집사
![test coverage](.github/badges/jacoco.svg)

## 개요
``````
- 유형 : 개인 프로젝트
- 기간 : 2022.10.17 - 2022.11.20
- 소개 : (경기도) 인근 동물 병원, 동물 약국 위치 정보 및 보유 물품 정보 조회 프로그램 
- 주요 기능 : 
  - 회원 
    - 로그인(JWT토큰 인증)
    - 이메일을 통한 회원가입
  - 주문 
    - 
- 주요 기술 : Spring Security, MySQL, JPA, Spring Batch, Redis, JWT
``````

## 구성도 및 ERD
(추가 예정)

##  포커스

```bash
1. 스프링 시큐러티와 JWT를 통한 로그인 / 회원가입 구현

2. GPS, OpenAPI를 활용하여 경기도 내 동물 병원, 동물 약국 정보 찾기

3. Redis를 활용하여 동물 병원, 동물 약국 정보 조회 내용 캐싱하기
```

### 스택
- SpringBoot
- Gradle
- JUnit5
- Mockito
- Validation
- Spring Security
- Java Mail Sender
- Tymeleaf
- MySQL
- Redis
- Lombok
- JPA
- JWT

## 주요 기능
## 회원
### 공통
- [x] 이메일 인증을 통한 회원가입
- [x] 스프링 시큐러티와 JWT를 통한 로그인 인증
### 일반회원
- [x] 회원 정보 조회
- [x] 회원 정보 수정
- [x] 회원 탈퇴
- [x] 반려동물 다수 등록(feat. 파일 업로드)
- [x] 반려동물 정보 수정
- [x] 반려동물 정보 삭제
### 관리자
- [x] 회원 관리
- [x] 카테고리 관리
- [x] 상품 관리
- [x] 페이징 처리 (카테고리, 상품 조회 시)

## 동물병원/약국  
### 동물병원/약국 저장
- [x] 스프링 배치, 스케줄러를 통해 경기도 동물 병원 데이터 저장 
- [ ] 스프링 배치, 스케줄러를 통해 경기도 동물 약국 데이터 저장
### 동물병원/약국 조회
- [ ] GeoLocation을 통해 현재 위치 찾기
- [ ] 카카오 API를 통해 지도 뷰 올리기
- [ ] Redis를 통해 동물 병원, 동물 약국 정보 조회 내용 캐싱하기

|제공기관|사용api|
|---|---|
|카카오|카카오 지도|
|경기도지자체|경기도 동물 병원, 동물 약국|