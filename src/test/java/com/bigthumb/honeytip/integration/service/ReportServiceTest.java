package com.bigthumb.honeytip.integration.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import com.bigthumb.honeytip.service.ReportService;
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
class ReportServiceTest {

  @Autowired
  ReportService 신고서비스;
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
  @DisplayName("신고하기")
  void reportIt() {
    // given
    User 사용자 = fakeMember();
    Category 카테고리 = fakeCategory();
    Tip 팁 = fakeTip(사용자, 카테고리);
    User 신고자 = fakeMember();
    Report 신고 = Report.builder().tip(팁).user(신고자).build();

    // when
    신고서비스.create(신고);

    //then
    assertThat(신고저장소.findById(신고.getId())).isEqualTo(신고);
  }

  @Test
  @DisplayName("전체 신고 검색하기")
  void findAllTests() {
    // given
    User 관리자 = fakeAdmin();
    createNReports(10);

    // when
    List<Report> 전체신고리스트 = 신고서비스.findAll(관리자.getId());

    //then
    assertThat(전체신고리스트.size()).isEqualTo(10);
  }

  @Test
  @DisplayName("일반사용자는 신고 검색 불가")
  void onlyAdminCanFindReport() {
    // given
    User 일반사용자 = fakeMember();

    // when then throws
    assertThrows(IllegalArgumentException.class, () -> 신고서비스.findAll(일반사용자.getId()));
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

    User 관리자 = fakeAdmin();

    // when
    신고서비스.reject(신고, 관리자.getId());

    //then
    assertThat(신고저장소.findById(신고.getId()).getStatus()).isEqualTo(ReportStatus.REJECTED);
  }

  @Test
  @DisplayName("일반 사용자는 신고 반려 불가")
  void onlyAdminCanRejectReport() {
    // given
    User 사용자 = fakeMember();
    Category 카테고리 = fakeCategory();
    Tip 팁 = fakeTip(사용자, 카테고리);
    User 신고자 = fakeMember();
    Report 신고 = Report.builder().tip(팁).user(신고자).build();
    신고저장소.save(신고);

    // when then throws
    assertThrows(IllegalArgumentException.class, () -> 신고서비스.reject(신고, 신고자.getId()));
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

    User 관리자 = fakeAdmin();

    // when
    신고서비스.approve(신고, 관리자.getId());

    //then
    assertThat(신고저장소.findById(신고.getId()).getStatus()).isEqualTo(ReportStatus.APROVED);
  }

  @Test
  @DisplayName("일반 사용자는 신고 승인 불가")
  void onlyAdminCanApproveReport() {
    // given
    User 사용자 = fakeMember();
    Category 카테고리 = fakeCategory();
    Tip 팁 = fakeTip(사용자, 카테고리);
    User 신고자 = fakeMember();
    Report 신고 = Report.builder().tip(팁).user(신고자).build();
    신고저장소.save(신고);

    // when then throws
    assertThrows(IllegalArgumentException.class, () -> 신고서비스.approve(신고, 신고자.getId()));
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
