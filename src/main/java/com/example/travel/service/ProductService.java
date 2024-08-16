package com.example.travel.service;

import com.example.travel.domain.*;
import com.example.travel.dto.admin.ProductOptionRequest;
import com.example.travel.dto.admin.ProductRequest;
import com.example.travel.dto.order.OptionCountForm;
import com.example.travel.dto.order.OrderRequest;
import com.example.travel.dto.order.OrderResponse;
import com.example.travel.dto.product.ProductDetailInfoResponse;
import com.example.travel.dto.product.SearchProductResponse;
import com.example.travel.dto.product.StockRequest;
import com.example.travel.dto.product.StockResponse;
import com.example.travel.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.swing.text.html.Option;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    @Value("${app.upload.dir}")
    private String uploadDir;
    private final ProductRepository productRepository;
    private final ProductRepImgRepository productRepImgRepository;
    private final ProductInfoImgRepository productInfoImgRepository;
    private final ProductOptionRepository productOptionRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final UserService userService;

    public ProductOption findProductOptionByProductOptionId(long productOptionId){
        return productOptionRepository.findById(productOptionId)
                .orElseThrow(() -> new IllegalArgumentException("not found productOption"));
    }

    public Product findProductByProductId(long productId){
        return productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("not found product"));
    }

    public ProductDetailInfoResponse getProductDetailPageInfo(long productId){
        return ProductDetailInfoResponse.builder()
                .product(findProductByProductId(productId))
                .today(LocalDate.now().toString().replace("-", ""))
                .build();
    }

    public StockResponse getStock(StockRequest request){
        LocalDateTime startDate = LocalDateTime.of(request.getYear(), request.getMonth(), request.getDay(),0,0,0);
        LocalDateTime endDate = LocalDateTime.of(request.getYear(), request.getMonth(), request.getDay(),23,59,59);
        Long stock = orderDetailRepository.findSumOfTravelerCount(startDate, endDate, request.getProductId());
        stock = (long)findProductByProductId(request.getProductId()).getProductMaxCount() - (stock != null ? stock : 0);

        return StockResponse.builder()
                .productId(request.getProductId())
                .date(startDate.toString())
                .count(stock)
                .build();
    }

    public OrderResponse orderResponse(OrderRequest request, Principal principal){
        Product product = findProductByProductId(request.getProductId());

        int totalRegularPrice = 0;
        int totalDiscountPrice = 0;
        List<OptionCountForm> optionList = toOptionCountFormList(request.getOptionId(), request.getCount());
        for(OptionCountForm option : optionList){
            totalRegularPrice += option.getProductOption().getProductOptionRegularPrice() * option.getCount();
            totalDiscountPrice += (option.getProductOption().getProductOptionDiscountPrice() == null ? option.getProductOption().getProductOptionRegularPrice() : option.getProductOption().getProductOptionDiscountPrice()) * option.getCount();
            System.err.println("totalDiscountPrice : " + totalDiscountPrice);
        }

        return OrderResponse.builder()
                .productId(product.getProductId())
                .productRepImg(product.getProductRepImgList().get(0).getProductRepImgSrc())
                .productTitle(product.getProductTitle())
                .optionList(optionList)
                .totalRegularPrice(totalRegularPrice)
                .totalDiscount(totalRegularPrice - totalDiscountPrice)
                .accountList(product.getUser().getAccountList())
                .user(principal == null ? null : userService.getUserByPrincipal(principal))
                .departDate(request.getSelectedDate().substring(0, 4) + "-" + request.getSelectedDate().substring(4,6) + "-" + request.getSelectedDate().substring(6, 8))
                .build();
    }

    public List<OptionCountForm> toOptionCountFormList(List<Long> optionIdList, List<Integer> countList){
        List<OptionCountForm> list = new ArrayList<>();

        int index = 0;
        for(long id : optionIdList){
            list.add(OptionCountForm.builder()
                    .productOption(findProductOptionByProductOptionId(id))
                    .count(countList.get(index++))
                    .build());
        }

        return list;
    }

    public List<Product> findProductByUserId(long userId){
        return productRepository.findAllByUserUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("not found product"));
    }

    public List<Product> findProductByPrincipal(Principal principal){
        return findProductByUserId(userService.getUserId(principal));
    }

    // productId로 List<ProductOption> 가져오기
    public List<ProductOption> findProductOptionByProductId(long productId){
        return productOptionRepository.findByProductProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("not found options"));
    }

    // productId로 List<ProductRepImg> 가져오기
    public List<ProductRepImg> findProductRepImgByProductId(long productId){
        return productRepImgRepository.findByProductProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("not found imgs"));
    }

    // productId로 List<ProductInfoImg> 가져오기
    public List<ProductInfoImg> findProductInfoImgByProductId(long productId){
        return productInfoImgRepository.findByProductProductId(productId)
                .orElseThrow(() -> new IllegalArgumentException("not found imgs"));
    }

    //상품 등록&수정
    @Transactional
    public Product insertProduct(ProductRequest request, Principal principal){

        Product product = null;
        List<ProductOptionRequest> list = request.getProductOptions();

        if(request.getProductId() != null) {
            // 상품정보 수정
            product = findProductByProductId(request.getProductId()).updateProduct(request);

            // 상품옵션 수정
            list.forEach(productOption -> {
                findProductOptionByProductOptionId(productOption.getProductOptionId()).updateProductOption(productOption);
            });
        }

        else {
            // 상품정보 등록
            product = productRepository.save(Product.builder()
                    .productStatus(request.getProductStatus())
                    .productRegionMainCategory(request.getProductRegionMainCategory())
                    .productRegionSubCategory(request.getProductRegionSubCategory())
                    .productTitle(request.getProductTitle())
                    .productStartDate(LocalDateTime.parse(request.getProductStartDate() + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .productEndDate(LocalDateTime.parse(request.getProductEndDate() + " 00:00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .productRegularPrice(request.getProductRegularPrice())
                    .productDiscountPrice(request.getProductDiscountPrice() != null ? request.getProductDiscountPrice() : null)
                    .productMaxCount(request.getProductMaxCount())
                    .productInfo(request.getProductInfo())
                    .user(userService.getUserByPrincipal(principal))
                    .productTravelDays(request.getProductTravelDays())
                    .build());

            // 상품옵션 등록
            for (ProductOptionRequest productOption : list) {
                productOptionRepository.save(ProductOption.builder()
                        .product(product)
                        .productOptionAgeRange(productOption.getProductOptionAgeRange())
                        .productOptionRegularPrice(productOption.getProductOptionRegularPrice())
                        .productOptionDiscountPrice(productOption.getProductOptionDiscountPrice() != null ? productOption.getProductOptionDiscountPrice() : null)
                        .build());
            }
        }

        // productRepImg 등록할 경우 저장
        if(!request.getProductRepImg().get(0).isEmpty()) {
            // 상품정보 수정 시 새로 업로드 하는 파일이 있으면 DB 에서 기존 productRepImg 삭제
            if(request.getProductId() != null) {
                findProductRepImgByProductId(request.getProductId()).forEach(productRepImg -> productRepImgRepository.deleteById(productRepImg.getProductRepImgId()));
            }
            int imgNum = 1;
            for(MultipartFile img : request.getProductRepImg()) {
                //상품 등록시 대표이미지로 설정한 이미지파일 이름
                String originalFileName = img.getOriginalFilename();
                //파일 확장자 추출
                int extensionIndex = originalFileName.lastIndexOf(".");
                String extension = originalFileName.substring(extensionIndex);

                //파일 업로드 (application.properties 에 저장한 경로/images/productRep/)
                fileUpload(img, product.getProductId(), extension, "productRep", "RepImg", imgNum);

                productRepImgRepository.save(ProductRepImg.builder()
                        .product(product)
                        .productRepImgSrc("product" + product.getProductId() + "RepImg" + imgNum++ + extension)
                        .build());
            }

        }

        // productInfoImg 등록할 경우 저장
        if(!request.getProductInfoImg().get(0).isEmpty()) {
            // 상품정보 수정 시 새로 업로드 하는 파일이 있으면 DB 에서 기존 productInfoImg 삭제
            if(request.getProductId() != null) {
                findProductInfoImgByProductId(request.getProductId()).forEach(productInfoImg -> productInfoImgRepository.deleteById(productInfoImg.getProductInfoImgId()));
            }

            //상세 이미지 파일들 업로드 및 db 저장
            int imgNum = 1;
            for(MultipartFile img : request.getProductInfoImg()) {
                String originalFileName = img.getOriginalFilename();
                int extensionIndex = originalFileName.lastIndexOf(".");
                String extension = originalFileName.substring(extensionIndex);

                //파일 업로드 (application.properties 에 저장한 경로/images/productInfo/)
                fileUpload(img, product.getProductId(), extension, "productInfo", "InfoImg", imgNum);

                productInfoImgRepository.save(ProductInfoImg.builder()
                        .product(product)
                        .productInfoImgSrc("product" + product.getProductId() + "InfoImg" + imgNum++ + extension)
                        .build());

            }
        }
        return product;
    }

    //파일 업로드(파일, 상품 아이디, 파일 확장자, 추가 경로, 파일 이름, 파일 번호)
    public void fileUpload(MultipartFile multipartFile, long productId, String extension, String dir, String fileNick, Integer num) {

        //경로 만들기
        Path copyOfLocation = Paths.get(uploadDir + File.separator + dir + File.separator + "product" + productId + fileNick + (num == null ? "" : num) + extension);
        try {
            // inputStream 사용
            // copyOfLocation 저장위치
            // 기존 파일이 존재할 경우 덮어쓰기
            Files.copy(multipartFile.getInputStream(), copyOfLocation, StandardCopyOption.REPLACE_EXISTING);

        } catch (IOException e) {
            throw new IllegalArgumentException("Could not store file : " + multipartFile.getOriginalFilename());
        }
    }

    // 관리자페이지에서 상품 조회
    public Page<Product> productList(String status, String mainCategory, String subCategory, String searchKeyword, Principal principal, Pageable pageable) {
        Page<Product> productList = null;
        User userEntity = userService.getUserByPrincipal(principal);
        // userRole "ADMIN"일 때
        if(userEntity.getUserRole().equals("ADMIN")) {
            // 검색 안했을 때
            if(searchKeyword == null || searchKeyword.equals("")) {
                // 전체상품 조회
                if(status.equals("전체")) {
                    // 대분류, 중분류 모두 선택 안했을 때
                    if(mainCategory.equals("") && subCategory.equals("")) {
                        productList = productRepository.findAll(pageable);
                    }
                    // 대분류만 선택 했을 때
                    else if(subCategory.equals("")) {
                        productList = productRepository.findByProductRegionMainCategory(mainCategory, pageable)
                                .orElseThrow(() -> new IllegalArgumentException("not found product"));
                    }
                    // 대분류, 중분류 모두 선택 했을 때
                    else {
                        productList = productRepository.findByProductRegionMainCategoryAndProductRegionSubCategory(mainCategory, subCategory, pageable)
                                .orElseThrow(() -> new IllegalArgumentException("not found product"));
                    }
                }
                // 정상상품, 품절상품, 숨김상품 조회
                else {
                    // 대분류, 중분류 모두 선택 안했을 때
                    if(mainCategory.equals("") && subCategory.equals("")) {
                        productList = productRepository.findByProductStatus(status, pageable)
                                .orElseThrow(() -> new IllegalArgumentException("not found product"));
                    }
                    // 대분류만 선택 했을 때
                    else if(subCategory.equals("")) {
                        productList = productRepository.findByProductStatusAndProductRegionMainCategory(status, mainCategory, pageable)
                                .orElseThrow(() -> new IllegalArgumentException("not found product"));
                    }
                    // 대분류, 중분류 모두 선택 했을 때
                    else {
                        productList = productRepository.findByProductStatusAndProductRegionMainCategoryAndProductRegionSubCategory(status, mainCategory, subCategory, pageable)
                                .orElseThrow(() -> new IllegalArgumentException("not found product"));
                    }
                }
            }
            // searchKeyword 로 검색 했을 때
            else {
                // 전체상품 조회
                if(status.equals("전체")) {
                    // 대분류, 중분류 모두 선택 안했을 때
                    if(mainCategory.equals("") && subCategory.equals("")) {
                        productList = productRepository.findByProductTitleContaining(searchKeyword, pageable)
                                .orElseThrow(() -> new IllegalArgumentException("not found product"));
                    }
                    // 대분류만 선택 했을 때
                    else if(subCategory.equals("")) {
                        productList = productRepository.findByProductRegionMainCategoryAndProductTitleContaining(mainCategory, searchKeyword, pageable)
                                .orElseThrow(() -> new IllegalArgumentException("not found product"));
                    }
                    // 대분류, 중분류 모두 선택 했을 때
                    else {
                        productList = productRepository.findByProductRegionMainCategoryAndProductRegionSubCategoryAndProductTitleContaining(mainCategory, subCategory, searchKeyword, pageable)
                                .orElseThrow(() -> new IllegalArgumentException("not found product"));
                    }
                }
                // 정상상품, 품절상품, 숨김상품 조회
                else {
                    if(mainCategory.equals("") && subCategory.equals("")) {
                        // 대분류, 중분류 모두 선택 안했을 때
                        productList = productRepository.findByProductStatusAndProductTitleContaining(status, searchKeyword, pageable)
                                .orElseThrow(() -> new IllegalArgumentException("not found product"));
                    }
                    // 대분류만 선택 했을 때
                    else if(subCategory.equals("")) {
                        productList = productRepository.findByProductStatusAndProductRegionMainCategoryAndProductTitleContaining(status, mainCategory, searchKeyword, pageable)
                                .orElseThrow(() -> new IllegalArgumentException("not found product"));
                    }
                    // 대분류, 중분류 모두 선택 했을 때
                    else {
                        productList = productRepository.findByProductStatusAndProductRegionMainCategoryAndProductRegionSubCategoryAndProductTitleContaining(status, mainCategory, subCategory, searchKeyword, pageable)
                                .orElseThrow(() -> new IllegalArgumentException("not found product"));
                    }
                }
            }
        }
        // userRole "SELLER"일 때
        else if(userEntity.getUserRole().equals("SELLER")) {
            // 검색 안했을 때
            if(searchKeyword == null || searchKeyword.equals("")) {
                // 전체상품 조회
                if(status.equals("전체")) {
                    // 대분류, 중분류 모두 선택 안했을 때
                    if(mainCategory.equals("") && subCategory.equals("")) {
                        productList = productRepository.findByUser(userEntity, pageable)
                                .orElseThrow(() -> new IllegalArgumentException("not found product"));
                    }
                    // 대분류만 선택 했을 때
                    else if(subCategory.equals("")) {
                        productList = productRepository.findByProductRegionMainCategoryAndUser(mainCategory, userEntity, pageable)
                                .orElseThrow(() -> new IllegalArgumentException("not found product"));
                    }
                    // 대분류, 중분류 모두 선택 했을 때
                    else {
                        productList = productRepository.findByProductRegionMainCategoryAndProductRegionSubCategoryAndUser(mainCategory, subCategory, userEntity, pageable)
                                .orElseThrow(() -> new IllegalArgumentException("not found product"));
                    }
                }
                // 정상상품, 품절상품, 숨김상품 조회
                else {
                    // 대분류, 중분류 모두 선택 안했을 때
                    if(mainCategory.equals("") && subCategory.equals("")) {
                        productList = productRepository.findByProductStatusAndUser(status, userEntity, pageable)
                                .orElseThrow(() -> new IllegalArgumentException("not found product"));
                    }
                    // 대분류만 선택 했을 때
                    else if(subCategory.equals("")) {
                        productList = productRepository.findByProductStatusAndProductRegionMainCategoryAndUser(status, mainCategory, userEntity, pageable)
                                .orElseThrow(() -> new IllegalArgumentException("not found product"));
                    }
                    // 대분류, 중분류 모두 선택 했을 때
                    else {
                        productList = productRepository.findByProductStatusAndProductRegionMainCategoryAndProductRegionSubCategoryAndUser(status, mainCategory, subCategory, userEntity, pageable)
                                .orElseThrow(() -> new IllegalArgumentException("not found product"));
                    }
                }
            }
            // searchKeyword 로 검색 했을 때
            else {
                // 전체상품 조회
                if(status.equals("전체")) {
                    // 대분류, 중분류 모두 선택 안했을 때
                    if(mainCategory.equals("") && subCategory.equals("")) {
                        productList = productRepository.findByProductTitleContainingAndUser(searchKeyword, userEntity, pageable)
                                .orElseThrow(() -> new IllegalArgumentException("not found product"));
                    }
                    // 대분류만 선택 했을 때
                    else if(subCategory.equals("")) {
                        productList = productRepository.findByProductRegionMainCategoryAndProductTitleContainingAndUser(mainCategory, searchKeyword, userEntity, pageable)
                                .orElseThrow(() -> new IllegalArgumentException("not found product"));
                    }
                    // 대분류, 중분류 모두 선택 했을 때
                    else {
                        productList = productRepository.findByProductRegionMainCategoryAndProductRegionSubCategoryAndProductTitleContainingAndUser(mainCategory, subCategory, searchKeyword, userEntity, pageable)
                                .orElseThrow(() -> new IllegalArgumentException("not found product"));
                    }
                }
                // 정상상품, 품절상품, 숨김상품 조회
                else {
                    if(mainCategory.equals("") && subCategory.equals("")) {
                        // 대분류, 중분류 모두 선택 안했을 때
                        productList = productRepository.findByProductStatusAndProductTitleContainingAndUser(status, searchKeyword, userEntity, pageable)
                                .orElseThrow(() -> new IllegalArgumentException("not found product"));
                    }
                    // 대분류만 선택 했을 때
                    else if(subCategory.equals("")) {
                        productList = productRepository.findByProductStatusAndProductRegionMainCategoryAndProductTitleContainingAndUser(status, mainCategory, searchKeyword, userEntity, pageable)
                                .orElseThrow(() -> new IllegalArgumentException("not found product"));
                    }
                    // 대분류, 중분류 모두 선택 했을 때
                    else {
                        productList = productRepository.findByProductStatusAndProductRegionMainCategoryAndProductRegionSubCategoryAndProductTitleContainingAndUser(status, mainCategory, subCategory, searchKeyword, userEntity, pageable)
                                .orElseThrow(() -> new IllegalArgumentException("not found product"));
                    }
                }
            }
        }

        return productList;
    }

    // productId로 Product 숨김처리
    @Transactional
    public void updateHideById(Long productId) {
        findProductByProductId(productId).updateStatus("숨김");
    }

    // productIds 리스트로 Product 숨김처리
    @Transactional
    public void updateHideByIds(List<Long> productIds) {
        for (Long productId : productIds) {
            // productId로 Product 숨김처리
            findProductByProductId(productId).updateStatus("숨김");
        }
    }

    public SearchProductResponse searchProduct(String mainCategory, String subCategory, String searchText, Pageable pageable){
        List<Product> productList = null;
        List<Product> subList = null;
        System.err.println(mainCategory);
        System.err.println(subCategory);
        System.err.println(searchText);

        if(searchText != null){
            productList = findProductByProductNameContaining(searchText);
        }
        else if(mainCategory != null){
            productList = findProductByProductMainCategory(mainCategory);
        }
        else{
            productList = findProductByProductSubCategory(subCategory);
        }

        subList = productList.subList(pageable.getPageNumber() * 12, Math.min(productList.size(), (pageable.getPageNumber() + 1) * 12));

        return SearchProductResponse.builder()
                .productList(subList)
                .totalElements(productList.size())
                .totalPages(productList.size() / 12 + productList.size() % 12 == 0 ? 0 : 1)
                .build();
    }

    public List<Product> findProductByProductNameContaining(String searchText){
        return productRepository.findAllByProductTitleContaining(searchText)
                .orElseThrow(() -> new IllegalArgumentException("not found product"));
    }

    public List<Product> findProductByProductMainCategory(String mainCategory){
        return productRepository.findAllByProductRegionMainCategory(mainCategory)
                .orElseThrow(() -> new IllegalArgumentException("not found product"));
    }

    public List<Product> findProductByProductSubCategory(String subCategory){
        return productRepository.findAllByProductRegionSubCategory(subCategory)
                .orElseThrow(() -> new IllegalArgumentException("not found product"));
    }

    public List<Product> newProduct() {
        return productRepository.newProduct()
                .orElseThrow(() -> new IllegalArgumentException("not found product"));
    }
}
