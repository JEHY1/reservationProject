package com.example.travel.controller.admin;

import com.example.travel.domain.Product;
import com.example.travel.dto.admin.InsertProductRequest;
import com.example.travel.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    // admin_product_insert 상품 등록&수정 페이지
    @GetMapping(value = {"/seller/product/insert", "/seller/product/{productId}/insert"})
    public String newAdminProduct(@PathVariable(required = false) Long productId, Model model) {
        // 상품정보 수정 - 기존 입력 값 불러옴
        if(productId != null) {
            Product productEntity = productService.findProductByProductId(productId);
            model.addAttribute("product", productEntity);
//            List<ProductImg> productImgList = productService.findProductImgByProductId(productId);
//            model.addAttribute("productImgs", productImgList);
        }

        return "admins/admin_product_insert";
    }

    // admin_product_insert 상품 등록&수정 post
//    @PostMapping("/seller/product/create")
//    public String uploadProduct(InsertProductRequest request, RedirectAttributes rttr){
//        Product product = productService.uploadProduct(request);
//        if(request.getProductId() == null) {
//            rttr.addFlashAttribute("msg", "상품이 등록 되었습니다.");
//        }
//        else {
//            rttr.addFlashAttribute("msg", "상품정보가 수정 되었습니다.");
//        }
//        return "redirect:/admin/product/" + product.getProductId() + "/show";
//    }

}
