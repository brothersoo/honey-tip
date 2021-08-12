package com.bigthumb.honeytip.integration.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.bigthumb.honeytip.domain.Category;
import com.bigthumb.honeytip.domain.Report;
import com.bigthumb.honeytip.domain.ReportStatus;
import com.bigthumb.honeytip.domain.Tip;
import com.bigthumb.honeytip.domain.User;
import com.bigthumb.honeytip.domain.UserType;
import com.bigthumb.honeytip.repository.CategoryRepository;
import com.bigthumb.honeytip.repository.ReportRepository;
import com.bigthumb.honeytip.repository.TipRepository;
import com.bigthumb.honeytip.repository.UserRepository;
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
public class ReportReporitoryTest {

  @Autowired
  ReportRepository 신고저장소;
  @Autowired
  UserRepository 사용자저장소;
  @Autowired
  CategoryRepository 카테고리저장소;
  @Autowired
  TipRepository 팁저장소;

  static Faker faker;
  static Faker koFaker;

  @BeforeAll
  static void setFaker() {
    faker = new Faker();
    koFaker = new Faker(new Locale("ko"));
  }

  @Test
  @DisplayName("신고 저장")
  void saveReport() {
    // given
    User 사용자 = fakeMember();
    Category 카테고리 = fakeCategory();
    Tip 팁 = fakeTip(사용자, 카테고리);
    User 신고자 = fakeMember();
    Report 신고 = Report.builder().tip(팁).user(신고자).build();
    신고저장소.save(신고);

    // when
    Report 검색신고 = 신고저장소.findById(신고.getId());

    //then
    assertThat(검색신고).isEqualTo(신고);
  }

  @Test
  @DisplayName("전체 신고 검색")
  void findAllReports() {
    // given
    createNReports(10);

    // when
    List<Report> 전체신고 = 신고저장소.findAll();

    //then
    assertThat(전체신고.size()).isEqualTo(10);
  }

  @Test
  @DisplayName("신고 반려")
  void rejectReport() {
    // given
    User 사용자 = fakeMember();
    Category 카테고리 = fakeCategory();
    Tip 팁 = fakeTip(사용자, 카테고리);
    User 신고자 = fakeMember();
    Report 신고 = Report.builder().tip(팁).user(신고자).build();
    신고저장소.save(신고);

    // when
    신고저장소.reject(신고);

    //then
    assertThat(신고저장소.findById(신고.getId()).getStatus()).isEqualTo(ReportStatus.REJECTED);
  }

  @Test
  @DisplayName("신고 승인")
  void approveReport() {
    // given
    User 사용자 = fakeMember();
    Category 카테고리 = fakeCategory();
    Tip 팁 = fakeTip(사용자, 카테고리);
    User 신고자 = fakeMember();
    Report 신고 = Report.builder().tip(팁).user(신고자).build();
    신고저장소.save(신고);

    // when
    신고저장소.approve(신고);

    //then
    assertThat(신고저장소.findById(신고.getId()).getStatus()).isEqualTo(ReportStatus.APROVED);
  }

  User fakeAdmin() {
    User 관리자 = User.builder()
        .username(koFaker.name().fullName())
        .nickname(faker.leagueOfLegends().champion())
        .password(faker.crypto().sha256())
        .type(UserType.ADMIN)
        .build();
    사용자저장소.save(관리자);
    return 관리자;
  }

  User fakeMember() {
    User 사용자 = User.builder()
        .username(faker.name().fullName())
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

  Tip fakeTip(User 작성자, Category 카테고리) {
    Tip 팁 = Tip.builder()
        .user(작성자)
        .category(카테고리)
        .title(koFaker.name().title())
        .content(String.join("", koFaker.lorem().sentences(3)))
        .build();
    팁저장소.save(팁);
    return 팁;
  }

  void createNReports(int n) {
    for (int i = 0; i < n; i++) {
      User 사용자 = fakeMember();
      Category 카테고리 = fakeCategory();
      Tip 팁 = fakeTip(사용자, 카테고리);
      User 신고자 = fakeMember();
      Report 신고 = Report.builder().tip(팁).user(신고자).build();
      신고저장소.save(신고);
    }
  }
}
