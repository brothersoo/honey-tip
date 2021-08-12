package com.bigthumb.honeytip.integration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.bigthumb.honeytip.domain.User;
import com.bigthumb.honeytip.domain.UserType;
import com.bigthumb.honeytip.service.UserService;
import com.github.javafaker.Faker;
import java.util.List;
import java.util.Locale;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@TestPropertySource("classpath:application-test.properties")
public class UserServiceTest {

  @Autowired UserService 사용자서비스;

  static Faker faker;
  static Faker koFaker;

  @BeforeAll
  static void setStaticFaker() {
    faker = new Faker();
    koFaker = new Faker(new Locale("ko"));
  }

  @Test
  @DisplayName("회원가입")
  void signIn() {
    // given
    User 신규회원 = User.builder()
        .username(koFaker.name().username())
        .nickname(faker.leagueOfLegends().champion())
        .password(faker.lorem().characters(8, 20))
        .build();

    // when
    Long 신규회원번호 = 사용자서비스.join(신규회원);

    //then
    assertThat(사용자서비스.searchUserById(신규회원번호)).isEqualTo(신규회원);
  }

  @Test
  @DisplayName("중복 유저명 회원가입 실패")
  void failSignInDueToDuplicateUsername() {
    // given
    String 유저명 = "Honey Tip";
    User 기존회원 = User.builder()
        .username(유저명)
        .nickname(faker.leagueOfLegends().champion())
        .password(faker.lorem().characters(8, 20))
        .build();
    사용자서비스.join(기존회원);
    User 신규회원 = User.builder()
        .username(유저명)
        .nickname(faker.leagueOfLegends().champion())
        .password(faker.lorem().characters(8, 20))
        .build();

    // when then throws
    assertThrows(IllegalStateException.class, () -> 사용자서비스.join(신규회원));
  }

  @Test
  @DisplayName("이름 길이 미달")
  void shortNameLength() {
    // given
    String 너무짧은이름 = koFaker.lorem().characters(2);
    User 신규회원 = User.builder()
        .username(너무짧은이름)
        .nickname(faker.leagueOfLegends().champion())
        .password(faker.lorem().characters(8, 20))
        .build();

    // when then throws
    assertThrows(IllegalStateException.class, () -> 사용자서비스.join(신규회원));
  }

  @Test
  @DisplayName("이름 길이 초과")
  void longNameLength() {
    // given
    String 너무긴이름 = koFaker.lorem().characters(31);
    User 신규회원 = User.builder()
        .username(너무긴이름)
        .nickname(faker.leagueOfLegends().champion())
        .password(faker.lorem().characters(8, 20))
        .build();

    // when then throws
    assertThrows(IllegalStateException.class, () -> 사용자서비스.join(신규회원));
  }

  @Test
  @DisplayName("비밀번호 길이 미달")
  void shortPasswordLength() {
    // given
    String 너무짧은비밀번호 = faker.lorem().characters(7);
    User 신규회원 = User.builder()
        .username(koFaker.name().username())
        .nickname(faker.leagueOfLegends().champion())
        .password(너무짧은비밀번호)
        .build();

    // when then throws
    assertThrows(IllegalStateException.class, () -> 사용자서비스.join(신규회원));
  }

  @Test
  @DisplayName("비밀번호 길이 초과")
  void longPasswordLength() {
    String 너무긴비밀번호 = faker.lorem().characters(24);
    // given
    User 신규회원 = User.builder()
        .username(koFaker.name().username())
        .nickname(faker.leagueOfLegends().champion())
        .password(너무긴비밀번호)
        .build();

    // when then throws
    assertThrows(IllegalStateException.class, () -> 사용자서비스.join(신규회원));
  }

  @Test
  @DisplayName("닉네임 길이 미달")
  void shortNicknameLenght() {
    // given
    String 너무짧은닉네임 = faker.lorem().characters(1);
    User 신규회원 = User.builder()
        .username(koFaker.name().username())
        .nickname(너무짧은닉네임)
        .password(faker.lorem().characters(8, 20))
        .build();

    // when then throws
    assertThrows(IllegalStateException.class, () -> 사용자서비스.join(신규회원));
  }

  @Test
  @DisplayName("닉네임 길이 초과")
  void longNickNameLength() {
    // given
    String 너무긴닉네임 = faker.lorem().characters(17);
    User 신규회원 = User.builder()
        .username(koFaker.name().username())
        .nickname(너무긴닉네임)
        .password(faker.lorem().characters(8, 20))
        .build();

    // when then throws
    assertThrows(IllegalStateException.class, () -> 사용자서비스.join(신규회원));
  }

  @Test
  @DisplayName("닉네임 중복 회원가입 실패")
  void test() {
    // given
    String 닉네임 = "Honey Tip";
    User 기존회원 = User.builder()
        .username(faker.name().username())
        .nickname(닉네임)
        .password(faker.lorem().characters(8, 20))
        .build();
    사용자서비스.join(기존회원);
    User 신규회원 = User.builder()
        .username(faker.name().username())
        .nickname(닉네임)
        .password(faker.lorem().characters(8, 20))
        .build();

    // when then throws
    assertThrows(IllegalStateException.class, () -> 사용자서비스.join(신규회원));
  }

  @Test
  @DisplayName("전체 유저 검색 - 관리자 포함")
  void findAllUser() {
    // given
    User 관리자 = fakeAdmin();
    createNAdmins(3);
    createNMembers(10);

    // when
    List<User> 전체유저리스트 = 사용자서비스.findAllUser(관리자.getId());

    //then
    assertThat(전체유저리스트.size()).isEqualTo(14);
  }

  @Test
  @DisplayName("전체 사용자 검색 - 관리자 미포함")
  void findAllMember() {
    // given
    User 관리자 = fakeAdmin();
    createNAdmins(3);
    createNMembers(10);

    // when
    List<User> 전체사용자리스트 = 사용자서비스.findAllMember(관리자.getId());

    //then
    assertThat(전체사용자리스트.size()).isEqualTo(10);
  }

  @Test
  @DisplayName("닉네임으로 유저 검색 - 관리자 포함")
  void findMemberByNickname() {
    // given

    // when

    //then

  }

  private void createNAdmins(int n) {
    for (int i = 0; i < n; i++) {
      fakeAdmin();
    }
  }

  private void createNMembers(int n) {
    for (int i = 0; i < n; i++) {
      fakeMember();
    }
  }

  private User fakeAdmin() {
    User 관리자 = User.builder()
        .username(koFaker.name().username())
        .nickname(faker.leagueOfLegends().champion())
        .password(faker.lorem().characters(8, 20))
        .type(UserType.ADMIN)
        .build();
    사용자서비스.join(관리자);
    return 관리자;
  }

  private User fakeMember() {
    User 사용자 = User.builder()
        .username(koFaker.name().username())
        .nickname(faker.leagueOfLegends().champion())
        .password(faker.lorem().characters(8, 20))
        .build();
    사용자서비스.join(사용자);
    return 사용자;
  }
}
