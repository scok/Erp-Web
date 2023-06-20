![system_logo](https://github.com/scok/Erp-Web/assets/20298169/93d39029-cd84-4a97-839b-bf6ff18b7780)

KOSMO 웹 기반 ERP 프로젝트
======================

# 1. 프로젝트 소개
## 1.1. 프로젝트 설명
기업에서 자주 사용하는 ERP(전사적 지원 관리)에 대해서 인사, 물류, 재무로 나누어 구현한 웹 기반 ERP 프로젝트 입니다. 

## 1.2. 사용 스킬
<!-- <img alt="이미지 이름" src ="https://img.shields.io/badge/이미지 이름-색상 코드.svg?&style=for-the-badge&logo=이미지 이름&logoColor=로고 색상"/> -->
<img alt="Html" src ="https://img.shields.io/badge/HTML5-E34F26.svg?&style=for-the-badge&logo=HTML5&logoColor=white"/> <img alt="Css" src ="https://img.shields.io/badge/CSS3-1572B6.svg?&style=for-the-badge&logo=CSS3&logoColor=white"/> <img alt="react" src ="https://img.shields.io/badge/react-61DAFB.svg?&style=for-the-badge&logo=react&logoColor=black"/> <img alt="JavaScript" src ="https://img.shields.io/badge/JavaScriipt-F7DF1E.svg?&style=for-the-badge&logo=JavaScript&logoColor=black"/> <img alt="springboot" src ="https://img.shields.io/badge/springboot-6DB33F.svg?&style=for-the-badge&logo=springboot&logoColor=white"/> <img alt="springsecurity" src ="https://img.shields.io/badge/springsecurity-6DB33F.svg?&style=for-the-badge&logo=springsecurity&logoColor=white"/> <img alt="jquery" src ="https://img.shields.io/badge/jquery-0769AD.svg?&style=for-the-badge&logo=jquery&logoColor=white"/> 

## 1.3. 사용 툴
<img alt="intellijidea" src ="https://img.shields.io/badge/intellijidea-000000.svg?&style=for-the-badge&logo=intellijidea&logoColor=white"/> <img alt="mysql" src ="https://img.shields.io/badge/mysql-4479A1.svg?&style=for-the-badge&logo=mysql&logoColor=white"/>

## 1.4 형상 관리
<img alt="git" src ="https://img.shields.io/badge/git-F05032.svg?&style=for-the-badge&logo=git&logoColor=white"/> <img alt="github" src ="https://img.shields.io/badge/github-181717.svg?&style=for-the-badge&logo=github&logoColor=white"/>

## 1.5. 프로그램 기능
### 1.5.1. 인사
	1. 회원가입 / 로그인
    2. 직원 등록 ( 관리자 )
    3. 직원 급여 리스트 ( 관리자 )
    4. 직원 급여 등록 ( 관리자 )
    5. 마이 페이지

### 1.5.2. 물류
	1. 구매 관리
	2. 물류 관리
    3. 생산 관리
    4. 영업 관리

### 1.5.3. 재무
	1. 재무 회계표
    2. 손익 계산서
    3. 거래 명세표
    4. 통합 데이터 분석

****
# 2. 프로그램 구성
## 2.1. 서버

```
포트 : Spring( 8877 ), React ( 3000)
백엔드: JAVA, JavaScript
원격 데이터베이스 : MySQL ( 데이터 : AWS 사용 ) - JPA 사용
```

## 2.2. UI

```
메인 페이지 : React, CSS3
인사, 물류, 재무 페이지 : HTML5, CSS3
```
*****

# 3. 기능 설명
## 3.1 메인 페이지
| 구분  | 부서 | 기능    | 상세 내용                      |
|-----|----|-------|----------------------------|
| 공통  | 공통 | 로그인   | 세션 바인딩 및 로그인               |
|   |  | 물류 차트 | 물류 각 항목에 대한 차트 표시          |
|   |  | 날씨    | 현재 위치에 대한 온도 및 날씨, 습도 등 표시 |
## 3.2 인사
| 구분  | 부서 | 기능 | 상세 내용 |
|-----|----|-----|---|
| 사원  | 인사 | 직원 등록 / 수정 | 직원 등록 및 수정 |
| | | 직원 급여 등록 / 수정 | 직원 급여 항목에 대한 데이터 등록 및 수정 |
| | | 직원 리스트 / 급여 리스트 | 직원 리스트 및 각 급여 항목 확인 |
| | | 마이 페이지 | 로그인 되어있는 직원의 정보 확인 |
## 3.3 물류
| 구분  | 부서 | 기능 | 상세 내용 |
|-----|----|-----|---|
| | 물류 | 구매 관리 | 거래처 조회 <br/>- 구매 거래처 등록, 수정, 삭제, Excel 변환 |
| | | | 상품 조회<br/>- 거래 상품 등록, 수정, 삭제, Excel 변환 |
| | | | 견적서 조회<br/>- 구매 견적서, 등록, 수정, 삭제 |
| | |  | 주문서 조회<br/>- 구매 주문서 수정, 삭제 |
| | | 물류 관리 | 창고 관리<br/>- 상품 적재를 위한 창고 등록 및 창고 별 수량 조회 |
| | | | 입하 관리<br/>- 구매 주문서(입하 대기) 항목 수정 |
| | | | 입고 관리<br/>- 구매 주문서(입고) 항목 상세 보기 |
| | | | 재고 조회<br/>- 창고로 입고 된 자재, 제품 조회 |
| | | | 출하 관리<br/>- 판매 주문서(출하 대기) 항목 수정 |
| | | | 출고 관리<br/>- 판매 주문서(출고) 항목 상세 보기 |
| | | 생산 관리 | 자재 불출<br/>- 생산 공장으로 불출할 자재 조회, 등록 |
| | | | 실적 등록<br/>- 작업 라인별 제품에 대한 생산 실적 등록 |
| | | 영업 관리 | 거래처 조회<br/>- 판매거래처 등록, 수정, 삭제 |
| | | | 견적서 조회<br/>- 판매 견적서 등록, 수정, 삭제 |
| | | | 주문서 조회<br/>- 판매 주문서 등록, 수정, 삭제 |
## 3.4 재무
| 구분  | 부서 | 기능 | 상세 내용 |
|-----|----|-----|---|
| | 재무 | 재무 상태표 | 자산, 부채, 자본 및 각 하위 항목에 대한 연도별/ 분기별 데이터, PDF 변환 기능 |
| | | 손익 계산서 | 매출액, 비용, 영업 이익, 세금 비용, 당기 순이익 등 연도별 / 분기별 데이터, PDF 변환 기능 |
| | | 거래 명세표 | 입고/출고 거래 명세표, 입고/출고 별 거래량 차트, 거래처별 연도/분기 차트 |
| | | 데이터 분석 | 재무상태표, 손익계산서 데이터 차트 |
*****
## 4. 출력 화면
* [ERP프로젝트 최종.pdf](https://github.com/scok/Erp-Web/blob/master/ERP%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8%20%EC%B5%9C%EC%A2%85.pdf)

## ○ 참고문서
* [Chart.js](https://www.chartjs.org/docs/latest/)
* [DataTables](https://datatables.net/)
