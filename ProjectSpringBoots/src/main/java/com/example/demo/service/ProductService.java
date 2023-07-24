package com.example.demo.service;

import com.example.demo.entity.Post;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductSize;
import com.example.demo.model.dto.ProductInfo;
import com.example.demo.model.request.CreateProductReq;
import com.example.demo.model.request.FilterProductReq;
import com.example.demo.model.request.UpdateProductReq;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {
    public List<ProductInfo> getListNewProduct();
    public List<Product> adminGetListProduct();
    public List<ProductInfo> getBestSellerProduct();
    public ProductInfo getProductById(String id);
    public Long calPromotionPrice(ProductInfo productInfo);
    public List<Integer>getListProductSize(String id);
    public Page<Product>getListProduct(int page);
    public List<ProductInfo> getListProductByName(String keyword) throws InterruptedException;
    public List<ProductInfo> filterProducts(FilterProductReq req);
    public String createProduct(CreateProductReq req);
    public Integer getProductCount(Product product);
    public void deleteProduct(String id);
    public void updateProduct(String id, UpdateProductReq req);
}
