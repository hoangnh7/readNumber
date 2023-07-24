package com.example.demo.entity;

import com.example.demo.model.dto.BrandInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "brand")
@Table(name = "brand")
@NamedNativeQuery(
        name = "countProductByBrand",
        query = "SELECT COUNT(*) FROM `product` WHERE product.brand_id=?1",
        resultClass = Brand.class)
public class Brand {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    @Column(name = "name",length = 255,unique = true)
    private String name;
    @Column(name = "thumbnail",length = 255)
    private String thumbnail;

}
