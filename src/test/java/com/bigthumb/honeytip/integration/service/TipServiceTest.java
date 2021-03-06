package com.bigthumb.honeytip.integration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.bigthumb.honeytip.domain.Category;
import com.bigthumb.honeytip.domain.Tip;
import com.bigthumb.honeytip.domain.TipStatus;
import com.bigthumb.honeytip.domain.User;
import com.bigthumb.honeytip.domain.UserType;
import com.bigthumb.honeytip.dto.TipDto;
import com.bigthumb.honeytip.repository.CategoryRepository;
import com.bigthumb.honeytip.repository.UserRepository;
import com.bigthumb.honeytip.service.TipService;
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
@TestPropertySource(("classpath:application-test.properties"))
class TipServiceTest {

  @Autowired TipService 팁서비스;
  @Autowired UserRepository 사용자저장소;
  @Autowired CategoryRepository 카테고리저장소;

  static Faker faker;
  static Faker koFaker;

  @BeforeAll
  static void setStaticFaker() {
    faker = new Faker();
    koFaker = new Faker(new Locale("ko"));
  }

  @Test
  @DisplayName("팁 생성")
  void createTip() {
    // given
    User 사용자 = fakeMember();
    Category 카테고리 = fakeCategory();
    TipDto 팁데이터 = TipDto.builder()
        .title(koFaker.lorem().sentence(10))
        .content(String.join("", koFaker.lorem().sentences(3)))
        .categoryName(카테고리.getName())
        .build();

    // when
    Long 팁아이디 = 팁서비스.createTip(팁데이터, 사용자.getUsername());
    //then
    assertThat(팁서비스.findById(팁아이디).getTitle()).isEqualTo(팁데이터.getTitle());
    assertThat(팁서비스.findById(팁아이디).getContent()).isEqualTo(팁데이터.getContent());
    assertThat(팁서비스.findById(팁아이디).getCategory()).isEqualTo(카테고리);
    assertThat(팁서비스.findById(팁아이디).getUser()).isEqualTo(사용자);
  }

  @Test
  @DisplayName("[Admin] 전체 팁 리스트 불러오기")
  void adminGetAllTips() {
    // given
    User 사용자 = fakeMember();
    Category 카테고리 = fakeCategory();
    createNTips(10, 사용자, 카테고리);

    // when
    List<Tip> 전체팁리스트 = 팁서비스.findAll();

    //then
    assertThat(전체팁리스트.size()).isEqualTo(10);
  }

  // TODO 팁 수정 DTO 구현 후 수정
  /*
  @Test
  @DisplayName("팁 수정하기")
  void updateTip() {
    // given
    User 사용자 = fakeMember();
    Category 기존카테고리 = fakeCategory();
    Category 변경할카테고리 = Category.builder().name("멋진카테고리").build();
    카테고리저장소.save(변경할카테고리);
    TipDto 팁데이터 = TipDto.builder()
        .title(koFaker.lorem().sentence(10))
        .content(String.join("", koFaker.lorem().sentences(3)))
        .categoryName(기존카테고리.getName())
        .build();
    팁서비스.createTip(팁데이터, 사용자.getUsername());
    String 변경할제목 = "Title has changed well!";
    String 변경할내용 = "Content has changed well!";
    String 변경할카테고리이름 = 변경할카테고리.getName();

    // when
    팁서비스.updateTip(팁.getId(), 사용자.getId(), 변경할제목, 변경할내용, 변경할카테고리이름);

    //then
    assertThat(팁서비스.findById(팁.getId()).getTitle()).isEqualTo(변경할제목);
    assertThat(팁서비스.findById(팁.getId()).getContent()).isEqualTo(변경할내용);
    assertThat(팁서비스.findById(팁.getId()).getCategory().getName()).isEqualTo(변경할카테고리이름);
  }

  @Test
  @DisplayName("작성자가 아닌 사용자는 수정 불가")
  void onlyWriterCanUpdateTip() {
    // given
    User 사용자 = fakeMember();
    Category 카테고리 = fakeCategory();
    Tip 팁 = Tip.builder().title("코딩 꿀팁 대방출").content("테스팅을 생활화합시다^^*").user(사용자).category(카테고리).build();
    팁서비스.createTip(팁);
    User 다른사용자 = fakeMember();

    // when then
    assertThrows(IllegalArgumentException.class,
        () -> 팁서비스.updateTip(팁.getId(), 다른사용자.getId(), "어쩌구", "저쩌구", 카테고리.getName()));
  }

  @Test
  @DisplayName("없는 카테고리 이름으로 수정 불가")
  void notExistingCategoryNameError() {
    // given
    User 사용자 = fakeMember();
    Category 카테고리 = fakeCategory();
    Tip 팁 = Tip.builder().title("코딩 꿀팁 대방출").content("테스팅을 생활화합시다^^*").user(사용자).category(카테고리).build();
    팁서비스.createTip(팁);

    // when then
    assertThrows(IllegalArgumentException.class,
        () -> 팁서비스.updateTip(팁.getId(), 사용자.getId(), "어쩌구", "저쩌구", "이러쿵"));
  }
  */

  /* TODO 팁 숨기기 기능 구현 후 수정
  @Test
  @DisplayName("팁 숨기기")
  void hideTip() {
    // given
    User 사용자 = fakeMember();
    Category 카테고리 = fakeCategory();
    Tip 팁 = Tip.builder().title("코딩 꿀팁 대방출").content("테스팅을 생활화합시다^^*").user(사용자).category(카테고리).build();
    팁서비스.createTip(팁);

    // when
    팁서비스.hideById(팁.getId(), 사용자.getId());

    //then
    assertThat(팁서비스.findById(팁.getId()).getStatus()).isEqualTo(TipStatus.HIDDEN);
  }
  */

  /* TODO 팁 제거 기능 구현 후 수정
  @Test
  @DisplayName("팁 작성자가 아닌 다른 사용자가 팁 제거 실패")
  void onlyWriterCanHideTip() {
    // given
    User 사용자 = fakeMember();
    Category 카테고리 = fakeCategory();
    Tip 팁 = Tip.builder().title("코딩 꿀팁 대방출").content("테스팅을 생활화합시다^^*").user(사용자).category(카테고리).build();
    팁서비스.createTip(팁);
    User 다른사용자 = fakeMember();

    // when then
    assertThrows(IllegalArgumentException.class, () -> 팁서비스.hideById(팁.getId(), 다른사용자.getId()));
  }

  @Test
  @DisplayName("팁 제거")
  void removeTip() {
    // given
    User 사용자 = fakeMember();
    Category 카테고리 = fakeCategory();
    Tip 팁 = Tip.builder().title("코딩 꿀팁 대방출").content("테스팅을 생활화합시다^^*").user(사용자).category(카테고리).build();
    팁서비스.createTip(팁);

    // when
    팁서비스.removeById(팁.getId(), 사용자.getId());

    //then
    assertThat(팁서비스.findById(팁.getId()).getStatus()).isEqualTo(TipStatus.REMOVED);
  }

  @Test
  @DisplayName("팁 작성자가 아닌 다른 사용자가 팁 제거 실패")
  void onlyWriterCanRemoveTip() {
    // given
    User 사용자 = fakeMember();
    Category 카테고리 = fakeCategory();
    Tip 팁 = Tip.builder().title("코딩 꿀팁 대방출").content("테스팅을 생활화합시다^^*").user(사용자).category(카테고리).build();
    팁서비스.createTip(팁);
    User 다른사용자 = fakeMember();

    // when then
    assertThrows(IllegalArgumentException.class, () -> 팁서비스.removeById(팁.getId(), 다른사용자.getId()));
  }
   */

  /* TODO 팁 삭제하기 기능 구현 후 수정
  @Test
  @DisplayName("[Admin] 팁 삭제하기")
  void deleteTip() {
    // given
    User 관리자 = fakeAdmin();
    User 사용자 = fakeMember();
    Category 카테고리 = fakeCategory();
    Tip 팁 = Tip.builder().title("코딩 꿀팁 대방출").content("테스팅을 생활화합시다^^*").user(사용자).category(카테고리).build();
    팁서비스.createTip(팁);

    // when
    팁서비스.deleteById(팁.getId(), 관리자.getId());

    //then
    assertThat(팁서비스.findAll()).doesNotContain(팁);
  }

  @Test
  @DisplayName("[Admin] 일반 사용자는 팁 삭제 불가")
  void memberHasNoPermissionOfDelete() {
    // given
    User 사용자 = fakeMember();
    Category 카테고리 = fakeCategory();
    Tip 팁 = Tip.builder().title("코딩 꿀팁 대방출").content("테스팅을 생활화합시다^^*").user(사용자).category(카테고리).build();
    팁서비스.createTip(팁);

    // when then
    assertThrows(IllegalArgumentException.class, () -> 팁서비스.deleteById(팁.getId(), 사용자.getId()));
  }
   */

  void createNTips(int n, User 작성자, Category 팁카테고리) {
    for (int i = 0; i < n; i++) {
      TipDto 팁데이터 = TipDto.builder()
          .title(koFaker.name().title())
          .content(koFaker.lorem().paragraph())
          .categoryName(팁카테고리.getName())
          .build();
      팁서비스.createTip(팁데이터, 작성자.getUsername());
    }
  }

  User fakeAdmin() {
    User 관리자 = User.builder()
        .username(faker.name().fullName())
        .nickname(faker.leagueOfLegends().champion())
        .password(faker.crypto().sha256())
        .type(UserType.ADMIN)
        .build();
    사용자저장소.save(관리자);
    return 관리자;
  }

  User fakeMember() {
    User 사용자 = User.builder()
        .username(faker.name().username())
        .nickname(faker.leagueOfLegends().champion())
        .password(faker.crypto().sha256())
        .build();
    사용자저장소.save(사용자);
    return 사용자;
  }

  Category fakeCategory() {
    Category 카테고리 = Category.builder()
        .name(koFaker.leagueOfLegends().champion())
        .build();
    카테고리저장소.save(카테고리);
    return 카테고리;
  }
}
