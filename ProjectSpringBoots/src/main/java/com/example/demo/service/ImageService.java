package com.example.demo.service;

import com.example.demo.entity.Image;
import org.springframework.stereotype.Service;

@Service
public interface ImageService {
    public void uploadImage(Image image);
}
