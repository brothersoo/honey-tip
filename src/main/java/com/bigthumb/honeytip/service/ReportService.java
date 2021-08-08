package com.bigthumb.honeytip.service;

import com.bigthumb.honeytip.domain.Report;
import com.bigthumb.honeytip.domain.User;
import com.bigthumb.honeytip.domain.UserType;
import com.bigthumb.honeytip.repository.ReportRepository;
import com.bigthumb.honeytip.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportService {

  private final ReportRepository reportRepository;
  private final UserRepository userRepository;

  public Long create(Report report) {
    reportRepository.save(report);
    return report.getId();
  }

  public List<Report> findAll(Long requestUserId) {
    User requestUser = userRepository.findById(requestUserId);
    if (!requestUser.getType().equals(UserType.ADM)) {
      throw new IllegalArgumentException("This user has no permission");
    }
    return reportRepository.findAll();
  }

  public void reject(Report report, Long requestUserId) {
    User requestUser = userRepository.findById(requestUserId);
    if (!requestUser.getType().equals(UserType.ADM)) {
      throw new IllegalArgumentException("This user has no permission");
    }
    reportRepository.reject(report);
  }

  public void approve(Report report, Long requestUserId) {
    User requestUser = userRepository.findById(requestUserId);
    if (!requestUser.getType().equals(UserType.ADM)) {
      throw new IllegalArgumentException("This user has no permission");
    }
    reportRepository.approve(report);
  }
}
