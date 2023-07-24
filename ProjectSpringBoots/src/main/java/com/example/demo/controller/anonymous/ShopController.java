package com.example.demo.controller.anonymous;

import com.example.demo.entity.*;
import com.example.demo.model.dto.CategoryInfo;
import com.example.demo.model.dto.ProductInfo;
import com.example.demo.model.mapper.ProductMapper;
import com.example.demo.model.request.CreateOrderReq;
import com.example.demo.model.request.FilterProductReq;
import com.example.demo.model.request.UpdateUserReq;
import com.example.demo.repository.PromotionRepository;
import com.example.demo.security.CustomUserDetails;
import com.example.demo.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.config.Constant.*;

@Controller
public class ShopController {
    @Autowired
    private ProductService productService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private PromotionService promotionService;
    @Autowired
    private PromotionRepository promotionRepository;
    @GetMapping("/")
    public String getIndex(Model model) {
        List<ProductInfo> products = productService.getListNewProduct();
        List<ProductInfo> bestSellerProducts = productService.getBestSellerProduct();
        System.out.println(products.get(0).getPromotionPrice());
        model.addAttribute("newProducts",products);
        model.addAttribute("bestSellerProducts",bestSellerProducts);

        return "shop/index";
    }
    @GetMapping("/san-pham/{slug}/{id}")
    public String getDetailShop(Model model, @PathVariable String id) {
        ProductInfo product = productService.getProductById(id);
        List<Integer>productSizes = productService.getListProductSize(product.getId());
        Boolean canBuy;
        if (productSizes.size()==0){
            canBuy= false;
        }else {
            canBuy=true;
        }
//        model.addAttribute();
        model.addAttribute("product",product);
        model.addAttribute("canBuy",canBuy);
        model.addAttribute("sizeVn", SIZE_VN);
        model.addAttribute("sizeUs", SIZE_US);
        model.addAttribute("sizeCm", SIZE_CM);
        return "shop/detail";
    }
    @GetMapping("/dat-hang/{slug}/{id}")
    public String getPayment(Model model, @PathVariable String id) {
        ProductInfo product = productService.getProductById(id);
        List<Promotion> promotions = new ArrayList<>();
        promotions = promotionRepository.findAllByIsActiveAndIsPublic(true, true);
        if(promotions.size()>0) {
        	product.setPromotion(promotions.get(0).getDiscountValue());
        
        }
        System.out.println(product.getPromotion());
        List<Integer> productSize = productService.getListProductSize(id);
        model.addAttribute("product",product);
        model.addAttribute("product_id",product.getId().toString().toUpperCase());
        model.addAttribute("availableSizes",productSize);
        for ( Integer size : productSize){
            System.out.println(size);
        }
        System.out.println(""+product.getId().toString());
        return "shop/payment";
    }
    //    @GetMapping("/tin-tuc")
//    public String getNews(Model model) {
//        return "shop/news";
//    }
    public List<ProductInfo> tmp = new ArrayList<>();
    @GetMapping("/san-pham")
    public String getShop(Model model , @RequestParam(required = false) Integer page) {
        if (page==null){
            page = 0;
        } else  {
            page--;
            if (page < 0) {
                page = 0;
            }
        }

        Page<Product> products = productService.getListProduct(page);
//        System.out.println();
        //convert Product to ProductInfo
        List<ProductInfo> rs = new ArrayList<>();
        List<Product> listProducts = products.getContent();
        //listProducts.get(0).getCategories().get(0).
        System.out.println("value:  "+ listProducts.get(0).getCategories().get(0).getId());
        for( Product item: listProducts){
            rs.add(ProductMapper.toProductInfo(item));
        }
        for (ProductInfo rs1 : rs){
            rs1.setPromotionPrice(productService.calPromotionPrice(rs1));
        }
//        System.out.println("gia " +rs.get(0).getPromotionPrice());
//        Long price = products.getContent().get(0).getPrice();
        List<Promotion> promotions = promotionRepository.findAllByIsActiveAndIsPublic(true,true);
        //System.out.println("value : " + promotions.get(0).getDiscountValue());
        //System.out.println(productService.calPromotionPrice(products.getContent().get(0)));
        List<Brand> brands = brandService.getListBrand();
        model.addAttribute("brands",brands);
        ArrayList<Integer> brandIds = new ArrayList<Integer>();

        for (Brand brand : brands) {
            brandIds.add(brand.getId());
        }

        //System.out.println(brands.get(0).getName());
        List<CategoryInfo>categories = categoryService.getListCategory();
        ArrayList<Integer> categoryIds = new ArrayList<Integer>();
        for (CategoryInfo category : categories) {
            categoryIds.add(category.getId());
        }
        model.addAttribute("categoryIds",categoryIds);
        List<ProductInfo>productsFilter = productService.getListNewProduct();
        model.addAttribute("categories",categories);
        model.addAttribute("listProduct",rs);
        FilterProductReq req = new FilterProductReq(brandIds,categoryIds);
        List<ProductInfo> results = productService.filterProducts(req);
        model.addAttribute("productsFilter",results);
        System.out.println("result : "+tmp);
        model.addAttribute("sizeVn", SIZE_VN);

        model.addAttribute("totalPages", products.getTotalPages());
        model.addAttribute("currentPage", ++page);

//        model.addAttribute("listProduct", products.getItems());
        return "shop/shop";
    }
    @GetMapping("/api/tim-kiem")
    public String getSearch(Model model , @RequestParam(required = false) String keyword) throws InterruptedException {
        List<ProductInfo> result = productService.getListProductByName(keyword);
        model.addAttribute("listProduct",result);
        return "shop/search";

    }
    @PostMapping("/api/order")
    public ResponseEntity<?> postorder(@RequestBody CreateOrderReq req){
        User user = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
        Order order = orderService.CreateOrder(req,user);
        return ResponseEntity.ok(order);
    }
    @PostMapping("api/san-pham/loc")
    public ResponseEntity<?> filterProduct(@RequestBody FilterProductReq req){
        List<ProductInfo> products= productService.filterProducts(req);
        tmp = products;
        System.out.println(products);
        System.out.println(tmp);
        return ResponseEntity.ok(products);
    }
}
