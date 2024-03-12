package com.example.techcentral.service.imageService;

import com.example.techcentral.ExceptionHandler.NotFoundException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@Service
public class ImageServiceImp implements ImageService {

    @Autowired
    Properties properties;

    @EventListener
    public void init(ApplicationReadyEvent event) {

        try {
            ClassPathResource serviceAccount = new ClassPathResource("firebase.json");

            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
                    .setStorageBucket(properties.getBucketName())
                    .build();

            FirebaseApp.initializeApp(options);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @Override
    public String getImageUrl(String name) {
        return String.format(properties.getImageUrl(), name);
    }

    @Override
    public String save(MultipartFile file) throws IOException {
        Bucket bucket = StorageClient.getInstance().bucket();

        String name = generateFileName(file.getOriginalFilename());

        bucket.create(name, file.getBytes(), file.getContentType());

        return name;
    }



    @Override
    public void delete(String imgUrl) throws IOException {


        Bucket bucket = StorageClient.getInstance().bucket();
        String name = imgUrl.substring(properties.getImageUrl().length()-2);


        if (StringUtils.isEmpty(name)) {
            throw new IOException("invalid file name");
        }

        Blob blob = bucket.get(name);

        if (blob == null) {
            throw new NotFoundException("file not found");
        }

        blob.delete();

    }
}
