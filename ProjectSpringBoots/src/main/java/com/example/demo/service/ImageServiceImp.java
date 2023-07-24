package com.example.demo.service;

import com.example.demo.entity.Image;
import com.example.demo.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ImageServiceImp implements ImageService{
    @Autowired
    private ImageRepository imageRepository;
    @Override
    public void uploadImage(Image image) {
        Image tmp = new Image();
        tmp.setId(image.getId());
        tmp.setType(image.getType());
        tmp.setUploaded_by(image.getUploaded_by());
        tmp.setUploadedAt(image.getUploadedAt());
        tmp.setName(image.getName());
        tmp.setLink(image.getLink());
        tmp.setSize(image.getSize());
        imageRepository.save(tmp);
    }
}
