package com.mealfit.common.storageService;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;


@Profile("!test")
@Component
public class S3StorageService implements StorageService{

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3StorageService(AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    @Override
    public List<String> uploadMultipartFile(List<MultipartFile> files, String dirName) {

        List<String> savedUrlList = new ArrayList<>();

        for (MultipartFile file : files) {
            Optional<String> savedUrl = uploadOne(file, dirName);
            savedUrl.ifPresent(savedUrlList::add);
        }

        return savedUrlList;
    }

    private Optional<String> uploadOne(MultipartFile file, String dirName) {
        String fileName = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        return Optional.ofNullable(uploadFile(file, dirName + fileName, objectMetadata));
    }


    @Override
    public void deleteOne(String imageUrl) {
        if (!imageUrl.isBlank()) {
            boolean isExistObject = amazonS3Client.doesObjectExist(bucket, imageUrl);
            System.out.println("???????????? url ?????? : " + imageUrl);
            System.out.println("isExistObject : " + isExistObject);

            if (isExistObject) {
                amazonS3Client.deleteObject(bucket, imageUrl);
            }
        }
    }

    private String createFileName(String fileName) {
        // ?????? ?????? ????????? ???, ???????????? ??????????????? ?????? random?????? ????????????.
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) {
        // file ????????? ????????? ????????? ???????????? ?????? ???????????? ????????????,
        // ?????? ????????? ???????????? ???????????? ??? ?????? ?????? ?????? .??? ?????? ????????? ??????
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                  "????????? ????????? ??????(" + fileName + ") ?????????.");
        }
    }

    private String uploadFile(MultipartFile file, String fileName, ObjectMetadata objectMetadata) {
        try (InputStream inputStream = file.getInputStream()) {
            amazonS3Client.putObject(
                  new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            return amazonS3Client.getUrl(bucket, fileName).toString();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "?????? ???????????? ?????????????????????");
        }
    }

}

