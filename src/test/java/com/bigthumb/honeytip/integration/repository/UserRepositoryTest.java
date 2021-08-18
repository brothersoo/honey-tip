package com.bigthumb.honeytip.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.bigthumb.honeytip.domain.User;
import com.bigthumb.honeytip.domain.UserStatus;
import com.bigthumb.honeytip.repository.UserRepository;
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
class UserRepositoryTest {

  @Autowired UserRepository 사용자저장소;

  static Faker faker;
  static Faker koFaker;

  @BeforeAll
  static void setFaker() {
    faker = new Faker();
    koFaker = new Faker(new Locale("ko"));
  }

  @Test
  @DisplayName("유저 저장")
  void saveUser() {
    // given
    User 사용자 = User.builder()
        .username(koFaker.name().username())
        .password(faker.crypto().sha256())
        .nickname(koFaker.funnyName().name())
        .build();
    사용자저장소.save(사용자);

    // when
    User 검색사용자 = 사용자저장소.findById(사용자.getId());

    //then
    assertThat(검색사용자).isEqualTo(사용자);
  }

  @Test
  @DisplayName("전체 사용자 검색")
  void retrieveAllUser() {
    // given
    createNUsers(10);

    // when
    List<User> 전체사용자리스트 = 사용자저장소.findAll();

    //then
    assertThat(전체사용자리스트.size()).isEqualTo(10);
  }

  @Test
  @DisplayName("닉네임으로 사용자 찾기")
  void findUserByNickname() {
    // given
    createNUsers(10);
    User 김검색 = User.builder()
        .username(koFaker.name().username())
        .password(faker.crypto().sha256())
        .nickname("검색")
        .build();
    사용자저장소.save(김검색);

    // when
    User 검색사용자 = 사용자저장소.findByNickname("검색");

    //then
    assertThat(검색사용자).isEqualTo(김검색);
  }

  @Test
  @DisplayName("회원 탈퇴")
  void removeUser() {
    // given
    User 사용자 = User.builder()
        .username(koFaker.name().username())
        .password(faker.crypto().sha256())
        .nickname(koFaker.funnyName().name())
        .build();
    사용자저장소.save(사용자);

    // when
    사용자저장소.remove(사용자);

    //then
    assertThat(사용자저장소.findById(사용자.getId()).getStatus()).isEqualTo(UserStatus.QUIT);
  }

  @Test
  @DisplayName("회원 강퇴")
  void banUser() {
    // given
    User 사용자 = User.builder()
        .username(koFaker.name().username())
        .password(faker.crypto().sha256())
        .nickname(koFaker.funnyName().name())
        .build();
    사용자저장소.save(사용자);

    // when
    사용자저장소.ban(사용자);

    //then
    assertThat(사용자저장소.findById(사용자.getId()).getStatus()).isEqualTo(UserStatus.BANNED);
  }

  @Test
  @DisplayName("회원 삭제")
  void deleteUser() {
    // given
    User 사용자 = User.builder()
        .username(koFaker.name().username())
        .password(faker.crypto().sha256())
        .nickname(koFaker.funnyName().name())
        .build();
    사용자저장소.save(사용자);

    // when
    사용자저장소.delete(사용자);

    //then
    assertThat(사용자저장소.findAll()).doesNotContain(사용자);
  }

  void createNUsers(int n) {
    for (int i = 0; i < n; i++) {
      User 사용자 = User.builder()
          .username(koFaker.name().username())
          .password(faker.crypto().sha256())
          .nickname(koFaker.funnyName().name())
          .build();
      사용자저장소.save(사용자);
    }
  }
}
