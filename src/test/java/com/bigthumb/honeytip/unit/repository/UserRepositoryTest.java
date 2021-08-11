package com.bigthumb.honeytip.unit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;

import com.bigthumb.honeytip.domain.User;
import com.bigthumb.honeytip.domain.UserType;
import com.bigthumb.honeytip.repository.UserRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ExtendWith(MockitoExtension.class)
@TestPropertySource("classpath:application-test.properties")
public class UserRepositoryTest {

  @Mock private UserRepository 유저저장소;

  static User 일반회원;
  static User 관리자;
  static List<User> 유저리스트;

  @BeforeAll
  static void setUser() {
    일반회원 = User.builder().name("홍길동").nickname("mrHong").email("Mr.Hong@gmail.com")
        .password("SHA-256").build();
    관리자 = User.builder().name("김첨지").nickname("gongYangMi300").email("GYM300@gmail.com")
        .password("RSA").type(
            UserType.ADM).build();
    유저리스트 = new ArrayList<>();
    유저리스트.add(일반회원);
    유저리스트.add(관리자);
  }

  @Test
  @DisplayName("모든 유저 검색")
  void findAllUser() {
    // given
    given(유저저장소.findAll()).willReturn(유저리스트);

    // when
    List<User> 검색유저리스트 = 유저저장소.findAll();

    // then
    assertThat(검색유저리스트.size()).isEqualTo(유저리스트.size());
    assertThat(검색유저리스트).contains(일반회원, 관리자);
  }

  @Test
  @DisplayName("별명으로 이름 검색")
  void test() {
    // given

    // when

    //then

  }

  @Test
  @DisplayName("회원 필수값 없어서 생성 실패")
  void mandatoryValueMissingUser() {
    assertThrows(IllegalArgumentException.class, () -> User.builder().nickname("noName").email("anonymous@gmail.com")
        .password("MysEcrEt").build(), "User name should not be null");

    assertThrows(IllegalArgumentException.class,
        () -> User.builder().name("noNickName").email("onlyName@gmail.com").password("QWERTY").build(),
        "User nickname should not be null");

    assertThrows(IllegalArgumentException.class,
        () -> User.builder().name("noEmail").nickname("lonely").password("password123").build(),
        "User email should not be null");

    assertThrows(IllegalArgumentException.class,
        () -> User.builder().name("noPassword").nickname("pieceOfCake")
            .email("Antoinette@gmail.com").build(),
        "User password should not be null");
  }

  @Test
  @DisplayName("Id로 유저 검색")
  void findUserById() {
    // given
    given(유저저장소.findById(1L)).willReturn(일반회원);

    // when
    User 검색회원 = 유저저장소.findById(1L);

    //then
    assertThat(검색회원).isEqualTo(일반회원);
  }
}
