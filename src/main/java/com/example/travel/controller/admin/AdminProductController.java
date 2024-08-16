package com.example.travel.controller.admin;

import com.example.travel.domain.Product;
import com.example.travel.domain.ProductInfoImg;
import com.example.travel.domain.ProductOption;
import com.example.travel.domain.ProductRepImg;
import com.example.travel.dto.admin.ProductRequest;
import com.example.travel.service.ProductService;
import com.example.travel.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;
    private final UserService userService;

    // admin_product_insert 상품 등록&수정 페이지
    @GetMapping(value = {"/seller/product/insert", "/seller/product/{productId}/insert", "/admin/product/{productId}/insert"})
    public String newAdminProduct(@PathVariable(required = false) Long productId, Principal principal, Model model) {
        model.addAttribute("productId", productId);
        // 상품정보 수정 - 기존 입력 값 불러옴
        if(productId != null) {
            Product productEntity = productService.findProductByProductId(productId);
            model.addAttribute("product", productEntity);
            List<ProductOption> productOptionList = productService.findProductOptionByProductId(productId);
            model.addAttribute("productOptions", productOptionList);
        }
        // 유저 권한 가져옴
        model.addAttribute("auth", userService.getUserByPrincipal(principal).getUserRole().toLowerCase());

        return "admins/admin_product_insert";
    }

    // admin_product_insert 상품 등록&수정 post
    @PostMapping(value = {"/admin/product/create", "/seller/product/create"})
    public String createAdminProduct(ProductRequest request, Principal principal, RedirectAttributes rttr) {
        Product product = productService.insertProduct(request, principal);
        if(request.getProductId() == null) {
            rttr.addFlashAttribute("msg", "상품이 등록 되었습니다.");
        }
        else {
            rttr.addFlashAttribute("msg", "상품정보가 수정 되었습니다.");
        }
        // 유저 권한 가져옴
        String auth = userService.getUserByPrincipal(principal).getUserRole().toLowerCase();

        return "redirect:/" + auth + "/product/" + product.getProductId() + "/show";
    }

    // admin_product_show 상품 보기 페이지
    @GetMapping(value = {"/admin/product/{productId}/show", "/seller/product/{productId}/show"})
    public String showAdminProduct(@PathVariable Long productId, Principal principal, Model model) {
        Product productEntity = productService.findProductByProductId(productId);
        model.addAttribute("product", productEntity);
        List<ProductOption> productOptionList = productService.findProductOptionByProductId(productId);
        model.addAttribute("productOptions", productOptionList);
        List<ProductRepImg> productRepImgList = productService.findProductRepImgByProductId(productId);
        model.addAttribute("productRepImgs", productRepImgList);
        List<ProductInfoImg> productInfoImgList = productService.findProductInfoImgByProductId(productId);
        model.addAttribute("productInfoImgs", productInfoImgList);
        // 유저 권한 가져옴
        model.addAttribute("auth", userService.getUserByPrincipal(principal).getUserRole().toLowerCase());

        return "admins/admin_product_show";
    }

    // admin_product_index 상품 목록 페이지
    @GetMapping(value = {"/admin/product/index", "/seller/product/index"}) // default 페이지, 한 페이지 게시글 수, 정렬기준 컬럼, 정렬순서
    public String indexAdminProduct(Model model, @PageableDefault(page = 0, size = 10, sort = "productId", direction = Sort.Direction.DESC) Pageable pageable,
                                    @RequestParam(required = false) Integer isDel, @RequestParam(defaultValue = "정상") String status, @RequestParam(defaultValue = "") String mainCategory,
                                    @RequestParam(defaultValue = "") String subCategory, @RequestParam(defaultValue = "") String searchKeyword, Principal principal) {
        model.addAttribute("status", status);
        model.addAttribute("mainCategory", mainCategory);
        model.addAttribute("subCategory", subCategory);
        model.addAttribute("searchKeyword", searchKeyword);

        // 유저 권한 가져옴
        model.addAttribute("auth", userService.getUserByPrincipal(principal).getUserRole().toLowerCase());

        Page<Product> productList = productService.productList(status, mainCategory, subCategory, searchKeyword, principal, pageable);
        model.addAttribute("products", productList);

        // 페이징 관련 변수
        int nowPage = productList.getPageable().getPageNumber()+1; // 현재 페이지 (pageable이 갖고 있는 페이지는 0부터이기 때문에 +1)
        int block = (int) Math.ceil(nowPage/5.0); // 페이지 구간 (5페이지 - 1구간)
        int startPage = (block - 1) * 5 + 1; // 블럭에서 보여줄 시작 페이지
        int lastPage = productList.getTotalPages() == 0 ? 1 : productList.getTotalPages(); // 존재하는 마지막 페이지
        int endPage = Math.min(startPage + 4, lastPage); // 블럭에서 보여줄 마지막 페이지
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        // 체크해서 숨김 했을 때 팝업창
        if(isDel != null && isDel == 1) {
            model.addAttribute("msg", "상품이 숨김처리 되었습니다.");
        }
        // 체크 안하고 숨김 눌렀을 때 팝업창
        if(isDel != null && isDel == 0) {
            model.addAttribute("msg", "숨김처리할 상품을 선택해주세요.");
        }

        return "admins/admin_product_index";
    }

    // 상품 숨김
    @GetMapping(value = {"/admin/product/{productId}/delete", "/seller/product/{productId}/delete"})
    public String deleteAdminNotice(@PathVariable Long productId, RedirectAttributes rttr, Integer page, String status, String mainCategory, String subCategory, String searchKeyword, Principal principal) throws UnsupportedEncodingException {
        if(productService.findProductByProductId(productId).getProductStatus().equals("숨김")) {
            rttr.addFlashAttribute("msg", "이미 숨김처리된 상품입니다.");
        }
        else {
            productService.updateHideById(productId);
            rttr.addFlashAttribute("msg", "상품이 숨김처리 되었습니다.");
        }
        // ASCII 아닌 파라미터 percent encoding
        String encodeStatus = URLEncoder.encode(status, StandardCharsets.UTF_8);
        String encodeMainCategory = URLEncoder.encode(mainCategory, StandardCharsets.UTF_8);
        String encodeSubCategory = URLEncoder.encode(subCategory, StandardCharsets.UTF_8);
        String encodeSearchKeyword = URLEncoder.encode(searchKeyword, StandardCharsets.UTF_8);
        // 유저 권한 가져옴
        String auth = userService.getUserByPrincipal(principal).getUserRole().toLowerCase();

        return "redirect:/" + auth + "/product/index?page=" + page + "&status=" + encodeStatus + "&mainCategory=" + encodeMainCategory + "&subCategory=" + encodeSubCategory + "&searchKeyword=" + encodeSearchKeyword;
    }

    // 상품 선택 숨김 - admin_product_index.js 연결
    @PostMapping(value = {"/admin/product/delete", "/seller/product/delete"})
    @ResponseBody
    public String deleteAdminNoticeSelect(@RequestParam(required = false) List<Long> productIds) {
        if(productIds != null) {
            productService.updateHideByIds(productIds);
            return "delete";
        }
        else {
            return "null";
        }
    }

}
