package com.example.demo.controller.admin;

import com.example.demo.entity.Brand;
import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.model.dto.CategoryInfo;
import com.example.demo.model.dto.ProductInfo;
import com.example.demo.model.request.CreateProductReq;
import com.example.demo.model.request.UpdateProductReq;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.BrandService;
import com.example.demo.service.CategoryService;
import com.example.demo.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ManageProductController {
    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private ProductRepository productRepository;
    @GetMapping("/admin/products")
    public String getProductManagePage(Model model ){
    List<Product> products = productService.adminGetListProduct();

    model.addAttribute("products",products);
//        System.out.println(products.get(0).getP);
    List<CategoryInfo>categories = categoryService.getListCategory();
    model.addAttribute("categories",categories);
    List<Brand> brands=brandService.getListBrand();
    model.addAttribute("brands",brands);

    return "admin/product/list.html";
    }
    @GetMapping("/admin/products/create")
    public String createProduct(Model model){

//        System.out.println(products.get(0).getP);
        List<CategoryInfo>categories = categoryService.getListCategory();
        model.addAttribute("categories",categories);

        List<Brand> brands=brandService.getListBrand();
        model.addAttribute("brands",brands);

        return "admin/product/create.html";
    }
    @PostMapping("/api/admin/products")
    public ResponseEntity<?> createProduct( @RequestBody CreateProductReq req) {
        String productId = productService.createProduct(req);
        System.out.println(productId);
        return ResponseEntity.ok(productId);
    }
    @GetMapping("/admin/products/{id}")
    public String getProductDetail(Model model,@PathVariable String id){
        ProductInfo product = productService.getProductById(id);
        List<CategoryInfo> categories = categoryService.getListCategory();
        List<Brand> brands = brandService.getListBrand();
        System.out.println(brands);

        brands.get(0).getId();
        model.addAttribute("product",product);
        model.addAttribute("brands",brands);
        model.addAttribute("categories",categories);
        return "/admin/product/detail.html";
    }
    @DeleteMapping("/api/admin/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable String id){
        productService.deleteProduct(id);
        return ResponseEntity.ok(id);
    }
    @PutMapping("/api/admin/products/{id}")
    public ResponseEntity<?>updateProduct(@PathVariable String id, @RequestBody UpdateProductReq req){
        productService.updateProduct(id,req);
        return ResponseEntity.ok(id);
    }
}
