package com.bigthumb.honeytip.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.bigthumb.honeytip.domain.Category;
import com.bigthumb.honeytip.domain.Tip;
import com.bigthumb.honeytip.domain.User;
import com.bigthumb.honeytip.repository.CategoryRepository;
import com.bigthumb.honeytip.repository.TipRepository;
import com.bigthumb.honeytip.repository.UserRepository;
import com.github.javafaker.Faker;
import java.util.List;
import java.util.Locale;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class TipRepositoryTest {

  @Autowired EntityManager em;
  @Autowired TipRepository 팁저장소;
  @Autowired CategoryRepository 카테고리저장소;
  @Autowired UserRepository 사용자저장소;

  static User 사용자;
  static Category 카테고리;

  @Test
  @DisplayName("팁 생성")
  void createTip() {
    // given
    setStaticUserCategory();
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
    setStaticUserCategory();
    Tip 팁 = Tip.builder().title("코딩 꿀팁 대방출").content("테스팅을 생활화합시다^^*").user(사용자).category(카테고리).build();
    팁저장소.save(팁);
    em.flush();

    // when
    Tip 검색된팁 = 팁저장소.findById(팁.getId());

    //then
    assertThat(검색된팁).isEqualTo(팁);
  }

  @Test
  @DisplayName("모든 팁 검색")
  void findAllTips() {
    // given
    setStaticUserCategory();
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
    setStaticUserCategory();
    createNTips(10, 사용자, 카테고리);
    User 김검색 = User.builder()
        .name(faker.name().name())
        .email(faker.internet().emailAddress())
        .nickname("김검색")
        .password(faker.crypto().sha256())
        .build();
    사용자저장소.save(김검색);
    Tip 검색팁1 = Tip.builder()
        .title("검색 잘하는 꿀팁")
        .content(faker.lorem().paragraph())
        .user(사용자)
        .category(카테고리)
        .build();
    Tip 검색팁2 = Tip.builder()
        .title(faker.name().title())
        .content("이것은 검색기능이 정상적으로 작동하는지 확인하기 위해 만들어진 검색용 문장입니다.")
        .user(사용자)
        .category(카테고리)
        .build();
    Tip 검색팁3 = Tip.builder()
        .title(faker.name().title())
        .content(faker.lorem().paragraph())
        .user(김검색)
        .category(카테고리)
        .build();
    팁저장소.save(검색팁1);
    팁저장소.save(검색팁2);
    팁저장소.save(검색팁3);

    // when
    List<Tip> 검색팁리스트 = 팁저장소.findByCondition("검색");

    //then
    assertThat(검색팁리스트).containsSequence(검색팁1, 검색팁2, 검색팁3);
  }

  @Test
  @DisplayName("팁 삭제하기")
  void test() {
    // given
    setStaticUserCategory();
    Tip 팁 = Tip.builder().title("코딩 꿀팁 대방출").content("테스팅을 생활화합시다^^*").user(사용자).category(카테고리).build();

    // when
    팁저장소.remove(팁);

    //then
    assertThat(팁저장소.findAll()).doesNotContain(팁);
  }

  void setStaticUserCategory() {
    Faker faker = new Faker();
    Faker koFaker = new Faker(new Locale("ko"));
    사용자 = User.builder()
        .name(koFaker.name().fullName()).email(faker.internet().emailAddress())
        .nickname(faker.funnyName().name())
        .password(faker.crypto().sha256())
        .build();
    카테고리 = Category.builder()
        .name(koFaker.leagueOfLegends().champion())
        .build();
    사용자저장소.save(사용자);
    카테고리저장소.save(카테고리);
  }

  void createNUsers(int n) {
    Faker faker = new Faker();
    Faker koFaker = new Faker(new Locale("ko"));
    for (int i = 0; i < n; i++) {
      User 새사용자 = User.builder()
          .name(koFaker.name().fullName()).email(faker.internet().emailAddress())
          .nickname(faker.funnyName().name())
          .password(faker.crypto().sha256())
          .build();
      사용자저장소.save(새사용자);
    }
  }

  void createNCategories(int n) {
    Faker koFaker = new Faker(new Locale("ko"));

    for (int i = 0; i < n; i++) {
      Category 새카테고리 = Category.builder()
          .name(koFaker.leagueOfLegends().champion())
          .build();
      카테고리저장소.save(새카테고리);
    }
  }

  void createNTips(int n, User 작성자, Category 팁카테고리) {
    Faker koFaker = new Faker(new Locale("ko"));
    for (int i = 0; i < n; i++) {
      Tip 새팁 = Tip.builder()
          .title(koFaker.name().title())
          .content(koFaker.lorem().paragraph())
          .user(작성자)
          .category(팁카테고리)
          .build();
      팁저장소.save(새팁);
    }
  }
}
