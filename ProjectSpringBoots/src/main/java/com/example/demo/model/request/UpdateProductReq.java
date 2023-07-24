package com.example.demo.model.request;

import com.example.demo.entity.Category;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateProductReq {
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("brand_id")
    private int brandId;
    @JsonProperty("category_ids")
    private List<Integer>categoryId;
    @JsonProperty("is_available")
    private Boolean isAvailable;
    @JsonProperty("price")
    private long price;
    @JsonProperty("product_images")
    private ArrayList<String> productImages;
}
