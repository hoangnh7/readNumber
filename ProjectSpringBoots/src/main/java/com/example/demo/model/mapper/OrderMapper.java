package com.example.demo.model.mapper;

import com.example.demo.entity.Order;
import com.example.demo.entity.Product;
import com.example.demo.model.dto.OrderInfoDto;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderMapper {
    @Autowired
    private ProductRepository productRepository;

    public static OrderInfoDto toOrderInfoDto(Order order){

    	 OrderInfoDto tmp= new OrderInfoDto();
         tmp.setSizeVn(order.getSize());
         tmp.setPromotionValue(order.getPromotionValue());
         tmp.setProductPrice(order.getProductPrice());
//         tmp.setProductImg(order.getProduct().getOnfeetImages().get(0));
         tmp.setId(order.getId());
         tmp.setProductName(order.getProduct().getName());
         long rs = (order.getProductPrice())*(100 - order.getPromotionValue())/100;
         tmp.setTotalPrice(rs * order.getQuantity());

         tmp.setPromotionPrice(rs);
         tmp.setQuantity(order.getQuantity());
         return tmp;
    }
}
