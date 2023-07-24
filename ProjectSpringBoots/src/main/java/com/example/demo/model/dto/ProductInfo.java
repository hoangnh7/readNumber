package com.example.demo.model.dto;

import com.example.demo.entity.Category;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductInfo {

    private Long promotionPrice;
    private String id;

    private String name;
    private long promotion;

    private String slug;

    private long price;

    private int totalSold;
    private int brandId;

    private String image;
    private String description;

    private Boolean isAvailable;
    private ArrayList<String> onfeetImages;
    private List<Category> categories;


}
