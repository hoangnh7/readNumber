package com.example.demo.repository;

import com.example.demo.entity.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order,Long> {
    @Query(value = "SELECT * FROM `orders` WHERE buyer =?1 ",nativeQuery = true)
     List<Order> findByBuyer(long id);
}
