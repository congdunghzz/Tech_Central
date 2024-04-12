package com.example.techcentral.service;

import com.example.techcentral.ExceptionHandler.NotFoundException;
import com.example.techcentral.ExceptionHandler.UnAuthorizedException;
import com.example.techcentral.dao.ProductImageRepository;
import com.example.techcentral.dao.ProductRepository;
import com.example.techcentral.models.ProductImage;
import com.example.techcentral.service.imageService.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductImageService {
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductImageRepository productImageRepository;
    @Autowired
    private ImageService imageService;
    public ProductImage addImage(MultipartFile imgFile) throws IOException {
        String fileName = imageService.save(imgFile);
        return  ProductImage.builder().url(imageService.getImageUrl(fileName)).build();
    }
    public List<ProductImage> addImage(List<MultipartFile> imgFile) throws IOException {
        List<ProductImage> productImageList = imgFile.stream().map(img ->
        {
            try {
                String fileName = imageService.save(img);
                return ProductImage.builder().url(imageService.getImageUrl(fileName)).build();
            } catch (IOException e) {
                throw new UnAuthorizedException("Images are not saved, something went wrong !");
            }
        }).toList();

        return productImageList;
    }

    public void deleteImg(String imgUrl) throws IOException {
        try{

            imageService.delete(imgUrl);
            productImageRepository.deleteByUrl(imgUrl);



        }catch (Exception e){
            throw new NotFoundException("the file is not be found");
        }
    }
}
