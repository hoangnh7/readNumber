package com.example.demo.model.mapper;

import com.example.demo.entity.Brand;
import com.example.demo.model.dto.BrandInfo;

public class BrandMapper {
    public static BrandInfo toBrandInfo(Brand brand){
        BrandInfo tmp = new BrandInfo();
        tmp.setId(brand.getId());
        tmp.setName(brand.getName());
        tmp.setThumbnail(brand.getThumbnail());
        return tmp;
    }
}
