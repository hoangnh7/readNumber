package com.example.demo.model.mapper;


import com.example.demo.entity.Product;
import com.example.demo.model.dto.ProductInfo;
import com.example.demo.repository.PromotionRepository;
import com.example.demo.service.ProductService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductMapper {
    @Autowired
    private ProductService productService;
    @Autowired
    private static PromotionRepository promotionRepository;

    public static ProductInfo toProductInfo(Product product){

        ProductInfo tmp = new ProductInfo();
        tmp.setId(product.getId());
        tmp.setName(product.getName());
        tmp.setSlug(product.getSlug());
        tmp.setPrice(product.getPrice());
        tmp.setTotalSold(product.getTotalSold());
        tmp.setImage(product.getProductImages().get(0));
        tmp.setIsAvailable(product.getIsAvailable());
        tmp.setDescription(product.getDescription());
        tmp.setOnfeetImages(product.getOnfeetImages());
        tmp.setBrandId(product.getBrandId());
        tmp.setCategories(product.getCategories());
        return tmp;
    }
}
