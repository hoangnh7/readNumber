package com.example.demo.service;

import com.example.demo.entity.Category;
import com.example.demo.entity.Product;
import com.example.demo.entity.ProductSize;
import com.example.demo.entity.Promotion;
import com.example.demo.model.dto.ProductInfo;
import com.example.demo.model.mapper.ProductMapper;
import com.example.demo.model.request.CreateProductReq;
import com.example.demo.model.request.FilterProductReq;
import com.example.demo.model.request.UpdateProductReq;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ProductSizeRepository;
import com.example.demo.repository.PromotionRepository;
import com.github.slugify.Slugify;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
public class ProductServiceImpl implements ProductService{
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private PromotionRepository promotionRepository;
    @Autowired
    private ProductSizeRepository productSizeRepository;
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public List<ProductInfo> getListNewProduct() {
        List<Product> products = productRepository.findAllByOrderByCreatedAtDesc();
        List<ProductInfo> result = new ArrayList<ProductInfo>();
//        calPromotionPrice();
        for (Product product:products){
            result.add(ProductMapper.toProductInfo(product));

            // calPromotionPrice(products1.get(i));
        }
        for (ProductInfo rs: result){
            rs.setPromotionPrice(calPromotionPrice(rs));
        }
        return result;
    }

    @Override
    public List<Product> adminGetListProduct() {
        List<Product> products = productRepository.findAllByOrderByCreatedAtDesc();

        return products;
    }

    @Override
    public List<ProductInfo> getBestSellerProduct() {
        List<Product> products = productRepository.findAllByOrderByTotalSoldDesc();
        List<ProductInfo> result = new ArrayList<ProductInfo>();
        for (Product product: products){
            result.add(ProductMapper.toProductInfo(product));
            // calPromotionPrice(products1.get(i));
        }
        for (ProductInfo rs : result){
            rs.setPromotionPrice(calPromotionPrice(rs));
        }
        return result;
    }

    @Override
    public ProductInfo getProductById(String   id) {
        Product product = productRepository.findById(id);

        // calPromotionPrice(product);
        ProductInfo rs= ProductMapper.toProductInfo(product);
        rs.setPromotionPrice(calPromotionPrice(rs));
        return rs;
    }



    @Override
    public Long calPromotionPrice(ProductInfo product) {
        Long price = product.getPrice();
        List<Promotion> promotions = promotionRepository.findAllByIsActiveAndIsPublic(true,true);
        System.out.println(promotions.get(0));
        Long rs = price * (100 - promotions.get(0).getDiscountValue() )/ 100;
//        product.setPromotionPrice(rs);
//        productRepository.save(product);
        return rs;
    }

    @Override
    public List<Integer> getListProductSize(String id) {
        List<Integer> productSizes = productSizeRepository.findProductSize(id);
        return productSizes;
    }

    @Override
    public Page<Product> getListProduct(int page) {
        Page<Product> products = productRepository.findAll(PageRequest.of(page, 8, Sort.by("name").descending()));

        return products;

    }

    @Override
    public List<ProductInfo> getListProductByName(String keyword) throws InterruptedException {
        FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);
        fullTextEntityManager.createIndexer().startAndWait();

        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory()
                .buildQueryBuilder()
                .forEntity(Product.class)
                .get();
        org.apache.lucene.search.Query query= queryBuilder
                .keyword()
                .wildcard()
                .onField("name")
                .matching("*"+keyword)
                .createQuery();
        org.hibernate.search.jpa.FullTextQuery jpaQuery =fullTextEntityManager.createFullTextQuery(query,Product.class);
        List<Product> products =jpaQuery.getResultList();
        List<ProductInfo> rs = new ArrayList<>();
        for (Product product: products){
            rs.add(ProductMapper.toProductInfo(product));
        }
        for (ProductInfo rs1 : rs){
            rs1.setPromotionPrice(calPromotionPrice(rs1));
        }
        return rs;


    }

    @Override
    public List<ProductInfo> filterProducts(FilterProductReq req) {
        List<Product> products = productRepository.findAllByOrderByCreatedAtDesc();
//        productRepository.getProductCount(1);
        List<Integer>brands = req.getBrandsId();
        System.out.println(brands);
        List<Integer>categories = req.getCategoriesId();
        List<Product> rs = new ArrayList<>();

        for (Product product : products){
            for (int brand : brands){
                if ((product.getBrandId())== brand){
                    rs.add(product);
                }
            }
//            if ((brands!= null ) && (categories!=null)){
//            for (int brand : brands){
//                if ((product.getBrandId()== brand) && (checkCategories(categories))){
//                    rs.add(product);
//                    }
//                }
//            }
//            else if ((brands!= null) ){
//                for (int brand: brands){
//                    if ((product.getBrandId()== brand)){
//                        rs.add(product);
//                        //  productRepository.save(product);
//                    }
//                }
//            }
//            else if (categories!= null){
//                    if (checkCategories(categories)){
//                        rs.add(product);
//                    }
//            }
        }
       // productRepository.save(rs);
        List<ProductInfo> productInfos = new ArrayList<>();
        for (Product product : rs){
            productInfos.add(ProductMapper.toProductInfo(product));
        }
        return productInfos;
    }

    @Override
    public String createProduct(CreateProductReq req) {
        Product product= new Product();
        product.setPrice(req.getPrice());
        product.setDescription(req.getDescription());
        product.setBrandId(req.getBrandId());
        ArrayList<Category> categories = new ArrayList<Category>();
        for (Integer id : req.getCategoryIds()) {
            Category category = new Category();
            category.setId(id);
            categories.add(category);
        }
        product.setCategories(categories);
        product.setProductImages(req.getProductImages());
        product.setName(req.getName());
        product.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        product.setTotalSold(0);
        Slugify slg= new Slugify();
        product.setSlug(slg.slugify(req.getName()));

        String productId = RandomStringUtils.randomAlphanumeric(6);
        product.setId(productId);

        productRepository.save(product);
        return productId;
    }

    @Override
    public Integer getProductCount(Product product) {
        int id = product.getBrandId();
        return null;
    }

    @Override
    public void deleteProduct(String id) {
        Product product = productRepository.findById(id);
        productRepository.delete(product);
    }

    @Override
    public void updateProduct(String id, UpdateProductReq req) {
        Product product= productRepository.findById(id);
        product.setName(req.getName());
        product.setProductImages(req.getProductImages());
        List<Category> categories = new ArrayList<>();
        for (Integer cateId : req.getCategoryId()) {
            Category category = new Category();
            category.setId(cateId);
            categories.add(category);
        }
        product.setCategories(categories);
        product.setDescription(req.getDescription());
        product.setPrice(req.getPrice());
        product.setIsAvailable(req.getIsAvailable());
        product.setBrandId(req.getBrandId());
        productRepository.save(product);

    }

    private Boolean checkCategories(List<Integer> categories){
        boolean check = false;
        List<Category>categoryList= categoryRepository.findAll();
//        if ( categories == null){
//            check = true;
//        }
        for (int i = 0; i<categoryList.size();i++){
            for (int category : categories){
                if (category == categoryList.get(i).getId()){
                    check= true;
                    break;
                }
            }
          }

        return check;
    }
}
