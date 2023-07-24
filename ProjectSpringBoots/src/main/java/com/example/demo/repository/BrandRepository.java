package com.example.demo.repository;

import com.example.demo.entity.Brand;
import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrandRepository extends JpaRepository<Brand,Integer> {
    @Query(nativeQuery = true, name = "countProductByBrand")
    Integer getProductCount(int ab);
}
