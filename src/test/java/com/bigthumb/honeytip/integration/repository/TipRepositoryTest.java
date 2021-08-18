package com.bigthumb.honeytip.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.bigthumb.honeytip.domain.Category;
import com.bigthumb.honeytip.domain.Tip;
import com.bigthumb.honeytip.domain.TipStatus;
import com.bigthumb.honeytip.domain.User;
import com.bigthumb.honeytip.domain.UserType;
import com.bigthumb.honeytip.filter.TipFilter;
import com.bigthumb.honeytip.repository.CategoryRepository;
import com.bigthumb.honeytip.repository.TipRepository;
import com.bigthumb.honeytip.repository.UserRepository;
import com.github.javafaker.Faker;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
class TipRepositoryTest {

  @Autowired TipRepository 팁저장소;
  @Autowired CategoryRepository 카테고리저장소;
  @Autowired UserRepository 사용자저장소;

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
    Tip 팁 = Tip.builder().title("코딩 꿀팁 대방출").content("테스팅을 생활화합시다^^*").user(사용자).category(카테고리).build();

    // when
    팁저장소.save(팁);

    //then
    assertThat(팁저장소.findAll()).contains(팁);
  }

  @Test
  @DisplayName("Id로 팁 검색")
  void findTipById() {
    // given
    User 사용자 = fakeMember();
    Category 카테고리 = fakeCategory();
    Tip 팁 = Tip.builder().title("코딩 꿀팁 대방출").content("테스팅을 생활화합시다^^*").user(사용자).category(카테고리).build();
    팁저장소.save(팁);

    // when
    Tip 검색된팁 = 팁저장소.findOne(팁.getId());

    //then
    assertThat(검색된팁).isEqualTo(팁);
  }

  @Test
  @DisplayName("모든 팁 검색")
  void findAllTips() {
    // given
    User 사용자 = fakeMember();
    Category 카테고리 = fakeCategory();
    createNTips(5, 사용자, 카테고리);

    // when
    List<Tip> 검색팁리스트 = 팁저장소.findAll();

    //then
    assertThat(검색팁리스트.size()).isEqualTo(5);
  }

  @Test
  @DisplayName("팁 조건부 검색")
  void findTipsConditionally() {
    Faker faker = new Faker();

    // given
    User 사용자 = fakeMember();
    Category 카테고리 = fakeCategory();
    Category 검색카테고리 = Category.builder()
        .name("검색")
        .build();
    카테고리저장소.save(검색카테고리);
    createNTips(10, 사용자, 카테고리);
    User 김검색 = User.builder()
        .username(faker.name().username())
        .nickname("김검색")
        .password(faker.crypto().sha256())
        .build();
    사용자저장소.save(김검색);
    Tip 제목검색팁 = Tip.builder()
        .title("검색 잘하는 꿀팁")
        .content(faker.lorem().paragraph())
        .user(사용자)
        .category(카테고리)
        .build();
    Tip 내용검색팁 = Tip.builder()
        .title(faker.name().title())
        .content("이것은 검색기능이 정상적으로 작동하는지 확인하기 위해 만들어진 검색용 문장입니다.")
        .user(사용자)
        .category(카테고리)
        .build();
    Tip 닉네임검색팁 = Tip.builder()
        .title(faker.name().title())
        .content(faker.lorem().paragraph())
        .user(김검색)
        .category(카테고리)
        .build();
    Tip 카테고리검색팁 = Tip.builder()
        .title(faker.name().title())
        .content(faker.lorem().paragraph())
        .user(사용자)
        .category(검색카테고리)
        .build();
    팁저장소.save(제목검색팁);
    팁저장소.save(내용검색팁);
    팁저장소.save(닉네임검색팁);
    팁저장소.save(카테고리검색팁);

    TipFilter 작성자닉네임필터 = TipFilter.builder().nickname("검색").build();
    TipFilter 제목필터 = TipFilter.builder().title("검색").build();
    TipFilter 내용필터 = TipFilter.builder().content("검색").build();
    TipFilter 카테고리필터 = TipFilter.builder().category("검색").build();
    TipFilter 작성일필터 = TipFilter.builder()
        .createdAtFrom(LocalDate.now().minusDays(1))
        .createdAtTo(LocalDate.now().plusDays(1))
        .build();


    // when
    List<Tip> 닉네임필터_팁리스트 = 팁저장소.findByFilter(작성자닉네임필터);
    List<Tip> 제목필터_팁리스트 = 팁저장소.findByFilter(제목필터);
    List<Tip> 내용필터_팁리스트 = 팁저장소.findByFilter(내용필터);
    List<Tip> 카테고리필터_팁리스트 = 팁저장소.findByFilter(카테고리필터);
    List<Tip> 작성일필터_팁리스트 = 팁저장소.findByFilter(작성일필터);

    //then
    assertThat(닉네임필터_팁리스트).contains(닉네임검색팁);
    assertThat(닉네임필터_팁리스트.size()).isEqualTo(1);
    assertThat(제목필터_팁리스트).contains(제목검색팁);
    assertThat(제목필터_팁리스트.size()).isEqualTo(1);
    assertThat(내용필터_팁리스트).contains(내용검색팁);
    assertThat(내용필터_팁리스트.size()).isEqualTo(1);
    assertThat(카테고리필터_팁리스트).contains(카테고리검색팁);
    assertThat(카테고리필터_팁리스트.size()).isEqualTo(1);
    assertThat(작성일필터_팁리스트).contains(닉네임검색팁, 제목검색팁, 내용검색팁, 카테고리검색팁);
    assertThat(작성일필터_팁리스트.size()).isEqualTo(4 + 10);
  }

  @Test
  @DisplayName("신고된, 숨김처리된, 삭제된 팁은 보여지지 않는다")
  void contain_RPORTED_n_exclude_HIDDEN_REMVOVED() {
    // given
    User 사용자 = fakeMember();
    Category 카테고리 = fakeCategory();
    Tip 신고팁 = Tip.builder().title("바보녀석").content("신고하지 말아주세요ㅠㅠ").user(사용자).category(카테고리).status(TipStatus.REPORTED).build();
    Tip 숨긴팁 = Tip.builder().title("부끄러워").content("숨겨버려야겠다...").user(사용자).category(카테고리).status(TipStatus.HIDDEN).build();
    Tip 삭제팁 = Tip.builder().title("이유없어").content("왜 썼지? 지워야겠다").user(사용자).category(카테고리).status(TipStatus.REMOVED).build();
    팁저장소.save(신고팁);
    팁저장소.save(숨긴팁);
    팁저장소.save(삭제팁);

    // when
    TipFilter 빈필터 = TipFilter.builder().build();
    List<Tip> 전체팁리스트 = 팁저장소.findByFilter(빈필터);

    //then
    assertThat(전체팁리스트).contains(신고팁).doesNotContain(숨긴팁, 삭제팁);
  }

  @Test
  @DisplayName("팁 제거하기 - db에는 RMV status로 존재")
  void removeTip() {
    // given
    User 사용자 = fakeMember();
    Category 카테고리 = fakeCategory();
    Tip 팁 = Tip.builder().title("코딩 꿀팁 대방출").content("테스팅을 생활화합시다^^*").user(사용자).category(카테고리).build();
    팁저장소.save(팁);

    // when
    팁저장소.remove(팁);

    //then
    assertThat(팁저장소.findOne(팁.getId()).getStatus()).isEqualTo(TipStatus.REMOVED);
  }

  @Test
  @DisplayName("팁 삭제하기 - db에서도 삭제")
  void deleteTip() {
    // given
    User 사용자 = fakeMember();
    Category 카테고리 = fakeCategory();
    Tip 팁 = Tip.builder().title("코딩 꿀팁 대방출").content("테스팅을 생활화합시다^^*").user(사용자).category(카테고리).build();

    // when
    팁저장소.delete(팁);

    //then
    assertThat(팁저장소.findAll()).doesNotContain(팁);
  }

  @Test
  @DisplayName("팁 숨기기")
  void hideTip() {
    // given
    User 사용자 = fakeMember();
    Category 카테고리 = fakeCategory();
    Tip 팁 = Tip.builder().title("코딩 꿀팁 대방출").content("테스팅을 생활화합시다^^*").user(사용자).category(카테고리).build();
    팁저장소.save(팁);

    // when
    팁저장소.hide(팁);

    //then
    assertThat(팁저장소.findOne(팁.getId()).getStatus()).isEqualTo(TipStatus.HIDDEN);
  }

  void createNTips(int n, User 작성자, Category 팁카테고리) {
    Faker koFaker = new Faker(new Locale("ko"));
    for (int i = 0; i < n; i++) {
      Tip 새팁 = Tip.builder()
          .title(faker.name().title())
          .content(String.join("", faker.lorem().sentences(3)))
          .user(작성자)
          .category(팁카테고리)
          .build();
      팁저장소.save(새팁);
    }
  }

  User fakeAdmin() {
    User 관리자 = User.builder()
        .username(faker.name().username())
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
