package com.bigthumb.honeytip.integration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.bigthumb.honeytip.domain.Category;
import com.bigthumb.honeytip.domain.User;
import com.bigthumb.honeytip.domain.UserType;
import com.bigthumb.honeytip.repository.CategoryRepository;
import com.bigthumb.honeytip.repository.UserRepository;
import com.bigthumb.honeytip.service.CategoryService;
import com.github.javafaker.Faker;
import java.util.List;
import java.util.Locale;
import javax.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@Transactional
@TestPropertySource("classpath:application-test.properties")
class CategoryServiceTest {

  @Autowired
  CategoryRepository 카테고리저장소;
  @Autowired
  CategoryService 카테고리서비스;
  @Autowired
  UserRepository 사용자저장소;

  static Faker faker;
  static Faker koFaker;

  @BeforeAll
  static void setFaker() {
    faker = new Faker();
    koFaker = new Faker(new Locale("ko"));
  }

  @Test
  @DisplayName("카테고리 추가")
  void addCategory() {
    // given
    User 관리자 = fakeAdmin();
    Category 새카테고리 = Category.builder().name(koFaker.lorem().characters(2, 30)).build();
    카테고리서비스.add(새카테고리, 관리자.getId());

    // when
    List<Category> 전체카테고리 = 카테고리서비스.findAll();

    //then
    assertThat(전체카테고리).contains(새카테고리);
  }

  @Test
  @DisplayName("일반 사용자는 카테고리 추가 불가")
  void onlyAdminCanAddCategory() {
    // given
    User 일반사용자 = fakeMember();
    Category 새카테고리 = Category.builder().name(koFaker.lorem().characters(2, 30)).build();

    // when then throws
    assertThrows(IllegalArgumentException.class,
        () -> 카테고리서비스.add(새카테고리, 일반사용자.getId()));

  }

  @Test
  @DisplayName("중복 카테고리 이름 생성 실패")
  void duplicateCategoryNameFail() {
    // given
    User 관리자 = fakeAdmin();
    Category 카테고리 = Category.builder().name("음식").build();
    카테고리서비스.add(카테고리, 관리자.getId());
    Category 중복이름카테고리 = Category.builder().name("음식").build();

    // when then throws
    assertThrows(IllegalArgumentException.class, () -> 카테고리서비스.add(중복이름카테고리, 관리자.getId()));
  }

  @Test
  @DisplayName("카테고리 이름 길이 제한 실패")
  void categoryNameValidationError() {
    // given
    User 관리자 = fakeAdmin();
    Category 이름짧은카테고리 = Category.builder().name(koFaker.lorem().characters(1)).build();
    Category 이름긴카테고리 = Category.builder().name(koFaker.lorem().characters(31, 50)).build();

    // when then throws
    assertThrows(IllegalStateException.class, () -> 카테고리서비스.add(이름짧은카테고리, 관리자.getId()));
    assertThrows(IllegalStateException.class, () -> 카테고리서비스.add(이름긴카테고리, 관리자.getId()));
  }

  @Test
  @DisplayName("전체카테고리 검색")
  void findAllCategory() {
    // given
    User 사용자 = fakeMember();
    createNCategories(10);

    // when
    List<Category> 전체카테고리 = 카테고리서비스.findAll();

    //then
    assertThat(전체카테고리.size()).isEqualTo(10);
  }

  @Test
  @DisplayName("카테고리 삭제")
  void deleteCategory() {
    // given
    User 관리자 = fakeAdmin();
    Category 새카테고리 = Category.builder().name(koFaker.lorem().characters(2, 30)).build();
    카테고리서비스.add(새카테고리, 관리자.getId());

    // when
    카테고리서비스.deleteCategory(새카테고리.getId(), 관리자.getId());

    //then
    assertThat(카테고리서비스.findAll()).doesNotContain(새카테고리);
  }

  @Test
  @DisplayName("일반 사용자 카테고리 삭제 불가")
  void onlyAdminCanDeleteCategory() {
    // given
    User 일반사용자 = fakeMember();
    Category 새카테고리 = Category.builder().name(koFaker.lorem().characters(2, 30)).build();

    // when then throws
    Assertions.assertThrows(IllegalArgumentException.class,
        () -> 카테고리서비스.add(새카테고리, 일반사용자.getId()));
  }

  void createNCategories(int n) {
    for (int i = 0; i < n; i++) {
      Category 새카테고리 = Category.builder().name(koFaker.lorem().characters(2, 30)).build();
      카테고리저장소.save(새카테고리);
    }
  }

  private User fakeAdmin() {
    User 관리자 = User.builder()
        .username(faker.name().username())
        .nickname(faker.leagueOfLegends().champion())
        .password(faker.lorem().characters(8, 20))
        .type(UserType.ADMIN)
        .build();
    사용자저장소.save(관리자);
    return 관리자;
  }

  private User fakeMember() {
    User 사용자 = User.builder()
        .username(faker.name().username())
        .nickname(faker.leagueOfLegends().champion())
        .password(faker.lorem().characters(8, 20))
        .build();
    사용자저장소.save(사용자);
    return 사용자;
  }
}
