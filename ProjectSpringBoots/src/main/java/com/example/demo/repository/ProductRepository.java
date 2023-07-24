package com.example.demo.repository;

import com.example.demo.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {

    public List<Product> findAllByOrderByCreatedAtDesc();
    public List<Product> findAllByOrderByTotalSoldDesc();
    public Product findById(String id);

}
