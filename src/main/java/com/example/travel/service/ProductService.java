package com.example.travel.service;

import com.example.travel.domain.Product;
import com.example.travel.domain.ProductOption;
import com.example.travel.dto.order.OptionCountForm;
import com.example.travel.dto.order.OrderRequest;
import com.example.travel.dto.order.OrderResponse;
import com.example.travel.dto.product.ProductDetailInfoResponse;
import com.example.travel.dto.product.StockRequest;
import com.example.travel.dto.product.StockResponse;
import com.example.travel.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

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

        return OrderResponse.builder()
                .productRepImg(product.getProductRepImgList().get(0).getProductRepImgSrc())
                .optionList(toOptionCountFormList(request.getOptionId(), request.getCount()))
                .totalRegularPrice(000)
                .totalDiscount(000)
                .accountList(product.getUser().getAccountList())
                .user(principal == null ? null : userService.getUserByPrincipal(principal))
                .departDate(request.getSelectedDate())
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

}
