package com.example.travel.controller.etc;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class EtcController {

    // company_introduction 회사소개 페이지
    @GetMapping("/etc/company/introduction")
    public String showCompanyIntroduction() {
        return "etcs/company_introduction";
    }

    // use_agreement 이용약관 페이지
    @GetMapping("/etc/use/agreement")
    public String showUseAgreement() {
        return "etcs/use_agreement";
    }

    // personal_information 개인정보취급방침 페이지
    @GetMapping("/etc/personal/information")
    public String showPersonalInformation() {
        return "etcs/personal_information";
    }
}
