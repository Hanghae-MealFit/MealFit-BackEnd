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

    @Value("${cloud.aws.s3.fullPath}")
    private String s3FullPath;

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
            System.out.println("지워야할 url 주소 : " + imageUrl);
            System.out.println("isExistObject : " + isExistObject);

            if (isExistObject) {
                amazonS3Client.deleteObject(bucket, imageUrl);
            }
        }
    }

    private String createFileName(String fileName) {
        // 먼저 파일 업로드 시, 파일명을 난수화하기 위해 random으로 돌립니다.
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    private String getFileExtension(String fileName) {
        // file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직이며,
        // 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                  "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }

    private String uploadFile(MultipartFile file, String fileName, ObjectMetadata objectMetadata) {
        try (InputStream inputStream = file.getInputStream()) {
            amazonS3Client.putObject(
                  new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
            return amazonS3Client.getUrl(bucket, fileName).toString();
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패하셨습니다");
        }
    }

}

