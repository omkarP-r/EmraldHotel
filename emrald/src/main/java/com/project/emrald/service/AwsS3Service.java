package com.project.emrald.service;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Builder;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.project.emrald.exception.OurException;

@Service
public class AwsS3Service {
	
	
	public final String bucketName = "hotel-emrald-bucket-images";
			

	@Value("${aws.s3.access.key}")
	private String awsS3AccessKey;
	
	@Value("${aws.s3.secret.key}")
	private String awsS3SeceretKey;
	
	public String saveImageToS3( MultipartFile photo) {
		
		String s3LocationImage = null;
		
		try {
			
			String s2FileName = photo.getOriginalFilename();
			BasicAWSCredentials basicAWSCredentials = new BasicAWSCredentials(awsS3AccessKey, awsS3SeceretKey);
	        AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
	        		.withCredentials(new AWSStaticCredentialsProvider(basicAWSCredentials))
	        		.withRegion(Regions.EU_NORTH_1)
	        		.build();
	        
	        InputStream inputStream = photo.getInputStream();
	        ObjectMetadata metadata = new ObjectMetadata();
	        metadata.setContentType("image/jpeg");
	        
	        PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s2FileName, inputStream, metadata);
	        s3Client.putObject(putObjectRequest);
	        return "https://" + bucketName + ".s3.amazon.com/" + s2FileName;
		
		}
		catch(Exception e) {
			e.printStackTrace();
			throw new OurException("Unable to upload image to S3 Bucket");
		}
		
		
	}
	
}
