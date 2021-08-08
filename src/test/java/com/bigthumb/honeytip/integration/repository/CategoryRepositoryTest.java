package com.bigthumb.honeytip.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.bigthumb.honeytip.domain.Category;
import com.bigthumb.honeytip.repository.CategoryRepository;
import com.github.javafaker.Faker;
import java.util.List;
import java.util.Locale;
import javax.transaction.Transactional;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@Transactional
@TestPropertySource("classpath:application-test.properties")
class CategoryRepositoryTest {

  @Autowired CategoryRepository 카테고리저장소;

  static Faker faker;
  static Faker koFaker;

  @BeforeAll
  static void setFaker() {
    faker = new Faker();
    koFaker = new Faker(new Locale("ko"));
  }

  @Test
  @DisplayName("카테고리 생성")
  void saveCategory() {
    // given
    Category 새카테고리 = Category.builder().name(koFaker.name().title()).build();
    카테고리저장소.save(새카테고리);

    // when
    Category 검색카테고리 = 카테고리저장소.findById(새카테고리.getId());

    //then
    assertThat(검색카테고리).isEqualTo(새카테고리);
  }

  @Test
  @DisplayName("전체 카테고리 검색")
  void findAllCategories() {
    // given
    createNCategories(10);

    // when
    List<Category> 전체카테고리 = 카테고리저장소.findAll();

    //then
    assertThat(전체카테고리.size()).isEqualTo(10);
  }

  @Test
  @DisplayName("이름으로 카테고리 검색")
  void findCategoryByName() {
    // given
    Category 새카테고리 = Category.builder().name("이것은 검색용 카테고리").build();
    카테고리저장소.save(새카테고리);

    // when
    Category 검색카테고리 = 카테고리저장소.findByName(새카테고리.getName());

    //then
    assertThat(검색카테고리).isEqualTo(새카테고리);
  }

  @Test
  @DisplayName("카테고리 삭제")
  void deleteCategory() {
    // given
    createNCategories(9);
    Category 삭제카테고리 = Category.builder().name("이것은 검색용 카테고리").build();
    카테고리저장소.save(삭제카테고리);

    // when
    카테고리저장소.delete(삭제카테고리);
    List<Category> 전체카테고리 = 카테고리저장소.findAll();

    //then
    assertThat(전체카테고리.size()).isEqualTo(9);
    assertThat(전체카테고리).doesNotContain(삭제카테고리);
  }

  void createNCategories(int n) {
    for (int i = 0; i < n; i++) {
      Category 새카테고리 = Category.builder().name(koFaker.name().title()).build();
      카테고리저장소.save(새카테고리);
    }
  }
}
