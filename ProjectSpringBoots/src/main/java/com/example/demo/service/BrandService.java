package com.example.demo.service;

import com.example.demo.entity.Brand;
import com.example.demo.entity.Product;
import com.example.demo.model.dto.BrandInfo;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Service
public interface BrandService {
    public List<Brand> getListBrand();
    public List<BrandInfo> getProductCount();
}
