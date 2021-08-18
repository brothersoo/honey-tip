package com.bigthumb.honeytip.controller;

import com.bigthumb.honeytip.domain.Category;
import com.bigthumb.honeytip.domain.Tip;
import com.bigthumb.honeytip.dto.TipDto;
import com.bigthumb.honeytip.filter.TipFilter;
import com.bigthumb.honeytip.service.CategoryService;
import com.bigthumb.honeytip.service.TipService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
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
  public String newProcess(TipDto tipDto, @AuthenticationPrincipal String writer, BindingResult result) {
    tipService.createTip(tipDto, writer);
    return "redirect:/tip/tips";
  }

  @GetMapping("/{tipId}")
  public String tipDetail(@PathVariable("tipId") Long tipId, Model model) {
    Tip tip = tipService.findById(tipId);
    model.addAttribute("tip", tip);
    return "/tips/tipDetail";
  }
}
