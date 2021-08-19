package com.bigthumb.honeytip.controller;

import com.bigthumb.honeytip.domain.Category;
import com.bigthumb.honeytip.domain.Tip;
import com.bigthumb.honeytip.dto.TipDto;
import com.bigthumb.honeytip.exception.NoPermissionException;
import com.bigthumb.honeytip.filter.TipFilter;
import com.bigthumb.honeytip.service.CategoryService;
import com.bigthumb.honeytip.service.TipService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/tip")
public class TipController {

  public final TipService tipService;
  private final CategoryService categoryService;

  @GetMapping("/tips")
  public String list(
      Model model,
      TipFilter filter) {
    List<Tip> tips = tipService.findByFilter(filter);
    model.addAttribute("tips", tips);
    return "/tips/tipList";
  }

  @GetMapping("/new")
  public String create(Model model) {
    TipDto tipDto = new TipDto();
    List<Category> categories = categoryService.findAll();
    model.addAttribute("tip", tipDto);
    model.addAttribute("categories", categories);
    return "/tips/createTip";
  }

  @PostMapping("/newProcess")
  public String newProcess(
      @AuthenticationPrincipal String writer,
      TipDto tipDto,
      BindingResult result) {
    tipService.createTip(tipDto, writer);
    return "redirect:/tip/tips";
  }

  @GetMapping("/{tipId}")
  public String tipDetail(
      @PathVariable("tipId") Long tipId,
      Model model) {
    Tip tip = tipService.findById(tipId);
    model.addAttribute("tip", tip);
    return "/tips/tipDetail";
  }

  @GetMapping("/{tipId}/edit")
  public String update(
      @AuthenticationPrincipal String requestUsername,
      @PathVariable("tipId") Long tipId,
      Model model) {
    try {
      Tip tip = tipService.findById(tipId);
      if (!requestUsername.equals(tip.getUser().getUsername())) {
        throw new NoPermissionException("You do not have permission");
      }
      TipDto tipDto = TipDto.builder()
          .title(tip.getTitle())
          .content(tip.getContent())
          .categoryName(tip.getCategory().getName()).build();
      List<Category> categories = categoryService.findAll();
      model.addAttribute("tip", tipDto);
      model.addAttribute("tipId", tip.getId());
      model.addAttribute("categories", categories);
      return "/tips/updateTip";
    } catch (NoPermissionException e) {
      log.error(e.getMessage());
      model.addAttribute("exceptionMessage", e.getMessage());
      return "/error/errorPage";
    }
  }

  @PostMapping("/{tipId}/editProcess")
  public String updateProcess(
      @AuthenticationPrincipal String requestUsername,
      @PathVariable("tipId") Long tipId,
      TipDto tipDto) {
    tipService.updateTip(tipId, requestUsername, tipDto);
    return String.format("redirect:/tip/%d", tipId);
  }

  @PostMapping("/{tipId}/deleteProcess")
  public String deleteProcess(
      @AuthenticationPrincipal String requestUsername,
      @PathVariable("tipId") Long tipId,
      Model model
  ) {
    try {
      tipService.removeById(tipId, requestUsername);
      return "redirect:/tip/tips";
    } catch (NoPermissionException e) {
      log.error(e.getMessage());
      model.addAttribute("exceptionMessage", e.getMessage());
      return "/error/errorPage";
    }
  }
}
