package com.bigthumb.honeytip.repository;

import static com.bigthumb.honeytip.domain.QReport.report;

import com.bigthumb.honeytip.domain.Report;
import com.bigthumb.honeytip.domain.ReportStatus;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ReportRepository {

  private final EntityManager em;

  public void save(Report report) {
    em.persist(report);
  }

  public Report findById(Long id) {
    return em.find(Report.class, id);
  }

  public List<Report> findAll() {
    JPAQueryFactory query = new JPAQueryFactory(em);
    return query.selectFrom(report).fetch();
  }

  public void reject(Report report) {
    report.changeStatus(ReportStatus.REJ);
  }

  public void approve(Report report) {
    report.changeStatus(ReportStatus.APR);
  }
}
