package com.example.demo.service;

import com.example.demo.entity.Brand;
import com.example.demo.entity.Product;
import com.example.demo.model.dto.BrandInfo;
import com.example.demo.model.mapper.BrandMapper;
import com.example.demo.repository.BrandRepository;
import com.example.demo.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class BrandServiceImpl implements BrandService {
    @Autowired
    BrandRepository brandRepository;
    @Override
    public List<Brand> getListBrand() {
        List<Brand> brands= brandRepository.findAll();
        return brands;
    }
    @Autowired
    ProductRepository productRepository;
    @Override
    public List<BrandInfo> getProductCount() {
        List<Brand> brands = brandRepository.findAll();
        List<BrandInfo> rs = new ArrayList<>();
        for(Brand brand: brands){
           rs.add(BrandMapper.toBrandInfo(brand));
        }
        for( BrandInfo brandInfo: rs){
            System.out.println(brandInfo.getId());
            brandInfo.setProductCount(brandRepository.getProductCount(1));
        }
//        for(BrandInfo brandInfo: brandInfos){
//            System.out.println(brandInfo.getId());
//            brandInfo.setProductCount(brandRepository.getProductCount(brandInfo.getId()));
////            brandRepository.save(brandInfo);
//        }
//        brandRepository.save()
        return null;
    }
}
