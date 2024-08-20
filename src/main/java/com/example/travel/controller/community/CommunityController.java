package com.example.travel.controller.community;

import com.example.travel.service.NoticeService;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
@RequiredArgsConstructor
public class CommunityController {
    private final NoticeService noticeService;

    @GetMapping("/community/noticeList")
    public String noticeListPage(Model model, @PageableDefault(page = 0, size = 10, sort = "noticeSubmitDate", direction = Sort.Direction.DESC) Pageable pageable){

        model.addAttribute("noticePage", noticeService.findNoticeWithPage(pageable));

        return "/community/noticeList";
    }

    @GetMapping("/community/notice/{noticeId}")
    public String noticePage(Model model, @PathVariable long noticeId){

        model.addAttribute("notice", noticeService.addNoticeViewsAndGet(noticeId));

        return "/community/notice";
    }
}
