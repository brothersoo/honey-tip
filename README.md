# Honey Tip

---

## 개요

꿀팁 CRUD기능을 구현한 간단한 Spring boot 프로젝트입니다.

2021.07.30 ~ ing
<br>
<br>
<br>

## 스펙

**Java 11**\
**Spring boot 2.5.3**\
**Spring security 5.5.1**\
**JPA 2.5.3**\
**QueryDSL 4.4.0**\
**Lombok 1.18.20**\
**Thymeleaf 2.5.3**\
**BootStrap**\
**MySQL**\
**H2 database (For testing)**\
**Gradle 7.1.1**
<br>
<br>
<br>

## 기능

### Tip

1. 팁 리스트 보기
   1. 로그인을 하지 않은 사용자도 팁 리스트를 볼 수 있습니다.
   2. 리스트의 팁 행을 클릭하면 팁 상세 페이지로 들어가집니다.
   3. 팁 리스트에서는 팁 제목, 작성자, 그리고 작성 날짜가 표시됩니다.
   4. Pagination: 한 페이지당 20개의 팁을 확인할 수 있습니다. (TODO)
   5. 경고 누적을 받은 팁을 노출되지 않습니다.(TODO)
2. 팁 작성하기
   1. 로그인한 유저만 팁을 작성할 수 있습니다.
   2. 팁 제목, 내용은 직접 작성할 수 있습니다.
   3. 카테고리는 정해진 카테고리 내에서 선택할 수 있습니다.
   4. 경고 누적을 받은 회원은 팁을 작성할 수 없습니다. (TODO)
3. 팁 상세 내용
   1. 팁의 제목, 작성자, 작성 날짜, 그리고 내용까지 확인할 수 있습니다.
   2. 작성자에게는 팁 수정하기 버튼이 노출되며 작성자가 아닌 다른 사용자에게는 노출되지 않습니다. (TODO)
   3. 작성자에게는 팁 삭제하기 버튼이 노출되며 작성자가 아닌 다른 사용자에게는 노출되지 않습니다. (TODO)
   4. 경고 누적을 받은 회원은 팁 상세 내용을 확인할 수 없습니다. (TODO)
4. 팁 수정하기
   1. 팁의 작성자는 팁의 제목, 내용을 수정할 수 있습니다.
5. 팁 삭제하기
   1. 팁의 작성자는 팁을 삭제할 수 있습니다.
6. 팁 신고하기 (TODO)
   1. 부적절한 내용의 팁일 경우 신고할 수 있습니다.
   2. 신고가 누적될 경우 관리자가 확인 후 팁 삭제 및 회원 추방을 할 수 있습니다.

### User

1. 회원가입
   1. 아이디, 비밀번호, 비밀번호 확인, 닉네임을 입력하여 회원가입할 수 있습니다.
   2. 아이디와 닉네임은 다른 사용자와 중복되어서는 안됩니다.
   3. 아이디는 3자 이상 30자 미만이어야 합니다.
   4. 닉네임은 2자 이상 16자 이하이어야 합니다.
   5. 비밀번호는 8자 이상 23자 이하이어야 합니다.
   6. 조건에 부합하지 않는 경우 경고 표시와 함께 회원가입 페이지에 머무릅니다.
   7. 회원가입에 성공시 팁 리스트 화면으로 이동합니다.
2. 로그인
   1. 아이디, 비밀번호를 입력하여 로그인 할 수 있습니다.
   2. 로그인 실패시 경고 표시와 함께 로그인 페이지에 머무릅니다.
   3. 로그인 성공시 팁 리스트 화면으로 이동합니다.
3. 회원 정보 수정
   1. 아이디를 제외한 비밀번호와 닉네임을 수정할 수 있습니다.
   2. 비밀번호는 재입력 비밀번호와 동일해야 합니다.
   3. 변경할 닉네임은 다른 유저가 사용하지 않는 닉네임이어야 합니다.
4. 회원 탈퇴 (TODO)
   1. 회원 탈퇴 전 회원 탈퇴 의사를 한번 더 묻고 동의 시 회원의 모든 정보와 회원의 모든 작성 팁을 제거합니다.
5. 내가 쓴 팁들
   1. 사용자가 작성한 팁의 리스트를 확인할 수 있습니다.

### Admin (TODO)
1. 회원 관리
    1. 관리자 아이디로 로그인 시 관리자 페이지에 진입할 수 있습니다.
    2. 신고 누적이 된 사용자를 추방 할 수 있습니다.
2. 팁 관리
   1. 팁 신고 리스트를 확인할 수 있습니다.
   2. 신고 누적이 된 팁을 숨김처리 및 삭제할 수 있습니다.
3. 카테고리 관리
   1. 카테고리를 생성 및 삭제 할 수 있습니다.

<br>
<br>
<br>

## Run Server

1. `$ git clone https://github.com/brothersoo/honey-tip.git`
2. `$cd honey-tip`
3. `$ gradle build`
4. `$ java -jar ./build/libs/Honey-tip-0.0.1-SNAPSHOT.jar`
5. Enter `localhost:8080` by your web browser.

<br>
<br>
<br>

## Testing

Test Code는 모두 Spring boot test로 이루어져있고 unit test는 추후 추가될 예정입니다.

Main database는 MySQL을 사용하였고 test database는 H2 database를 사용하였습니다.
<br>
<br>
<br>

## Authentication / authorization

Authentication / authorization에는 Spring Security를 사용하였습니다.
<br>
<br>
<br>

## Coding Style

코딩 형식은 Google Java Style Guide를 따랐습니다.

<br>
<br>
<br>
<br>

### UI
UI는 김영한님의 [실전! 스프링 부트와 JPA 활용1](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-JPA-%ED%99%9C%EC%9A%A9-1) 강의에서 사용한 UI를 참고하였습니다.
