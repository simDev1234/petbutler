# 팻집사
![test coverage](.github/badges/jacoco.svg)

## 개요
``````
- 유형 : 개인 프로젝트
- 기간 : 2022.10.17 - 2022.11.20
- 소개 : 경기도 내 거주지 인근 동물 병원들의 위치와 보유 물품을 쉽게 찾을 수 있는 프로그램
- 주요 기술 : 
  1. Spring Security와 JWT를 통한 로그인 / 회원가입 구현
  2. Spring Batch와 Scheduling을 통해 경기도 내 동물 병원, 동물 약국 정보 저장
  3. Redis를 활용하여 동물 병원 조회 내용 캐싱하기
``````

## 구성도 및 ERD
(추가 예정)

### 스택
- SpringBoot
- Gradle
- Spring Security
- JWT
- Java Mail Sender
- Spring Batch
- Scheduling
- Redis
- JPA
- MySQL
- Lombok
- Tymeleaf

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
- [x] 회원 조회 (feat.페이징 처리)
- [x] 회원 상태 변경 

## 카테고리/상품 관리
### 카테고리 관리
- [x] 대분류, 중분류, 소분류별 카테고리 등록
- [x] 대분류, 중분류, 소분류별 카테고리 조회
### 상품관리
- [x] 대분류, 중분류, 소분류별 상품 조회
- [ ] 상품 등록
- [ ] 상품 수정
- [ ] 상품 삭제

## 동물병원
- [x] 스프링 배치, 스케줄러를 통해 경기도 동물 병원 데이터 저장
- [x] 동물병원 조회
- [x] 동물병원에 상품 등록

## 카카오 지도에 동물 병원 및 보유 상품 보여주기
- [x] GeoLocation을 통해 현재 위치 찾기
- [x] 카카오 API를 통해 지도 뷰 올리기
- [ ] Redis를 통해 동물 병원 조회 내용 캐싱하기

| 제공기관           |사용api|
|----------------|---|
| 카카오            |카카오 지도|
| 공공데이터포털 경기도지자체 |경기도 동물 병원|