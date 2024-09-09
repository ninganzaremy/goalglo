package com.goalglo.aws;

import com.goalglo.config.SecretConfig;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.util.UUID;

@Service
public class AwsS3Service {

   private final S3Client s3Client;

   private final SecretConfig secretConfig;


   public AwsS3Service(SecretConfig secretConfig) {
      this.secretConfig = secretConfig;

      AwsBasicCredentials awsCredentials = AwsBasicCredentials.create(secretConfig.getAws().getAccessKeyId(), secretConfig.getAws().getSecretAccessKey());
      this.s3Client = S3Client.builder()
         .region(Region.of(secretConfig.getAws().getRegion()))
         .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
         .build();
   }

   /**
    * Uploads a file to S3 and returns the public URL to access it.
    *
    * @param file the file to upload
    * @return the public URL to access the uploaded file
    * @throws IOException if the file cannot be read
    */
   public String uploadFile(MultipartFile file) throws IOException {
      String key = generateFileKey(file);

      // Create a PutObjectRequest with public-read ACL
      PutObjectRequest putObjectRequest = PutObjectRequest.builder()
         .bucket(secretConfig.getAwsBucketName())
         .key(key)
         .contentType(file.getContentType())
         .build();

      // Upload the file to S3
      PutObjectResponse response = s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

      // Return the public URL
      return generatePublicUrl(key);
   }

   /**
    * Deletes a file from S3.
    *
    * @param fileUrl the URL of the file to delete
    */
   public void deleteS3Object(String fileUrl) {
      String key = extractKeyFromUrl(fileUrl);
      DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
         .bucket(secretConfig.getAwsBucketName())
         .key(key)
         .build();
      s3Client.deleteObject(deleteObjectRequest);
   }

   /**
    * Extracts the key from an S3 URL.
    *
    * @param fileUrl the URL of the file
    * @return the key of the file
    */
   private String extractKeyFromUrl(String fileUrl) {
      // Extract the key from the S3 URL
      String[] parts = fileUrl.split("/");
      StringBuilder keyBuilder = new StringBuilder();

      // Start from the "blog-images" part
      boolean startBuilding = false;
      for (String part : parts) {
         if (startBuilding || part.equals("blog-images")) {
            if (!keyBuilder.isEmpty()) {
               keyBuilder.append("/");
            }
            keyBuilder.append(part);
            startBuilding = true;
         }
      }

      return keyBuilder.toString();
   }

   /**
    * Generates a unique file key for storing the file in S3.
    *
    * @param file the file being uploaded
    * @return a unique S3 key
    */
   private String generateFileKey(MultipartFile file) {
      return "blog-images/" + UUID.randomUUID() + "-" + cleanFileName(file.getOriginalFilename());
   }

   /**
    * Generates a public URL that allows permanent access to the file.
    *
    * @param key the key of the S3 object
    * @return the public URL to access the object
    */
   public String generatePublicUrl(String key) {
      return "https://" + secretConfig.getAwsBucketName() + ".s3." + secretConfig.getAws().getRegion() + ".amazonaws.com/" + key;
   }

   /**
    * Cleans up the file name to remove any unsafe characters.
    *
    * @param originalFileName the original file name
    * @return a sanitized file name
    */
   private String cleanFileName(String originalFileName) {
      if (originalFileName == null) {
         return "default-file-name";
      }
      return originalFileName.replaceAll("[^a-zA-Z0-9.-]", "_");
   }
}