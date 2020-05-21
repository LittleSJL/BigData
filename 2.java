package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.ListVersionsRequest;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3VersionSummary;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.model.VersionListing;

public class Main {
	private final static String bucketName = "sunjinliang";
	//private final static String keyName   = "download.txt";//回滚才会用到的名字 1+2
	private final static String accessKey = "53398F8F839C16A82DB3";
	private final static String secretKey = "W0I0NzQ3OEExM0ZBQjI3RTg1NDI5OUQwMkQ0QTc5QTdCMzhFRTVCMkNd";
	private final static String serviceEndpoint = "http://scuts3.depts.bingosoft.net:29999";
	
	private final static String signingRegion = "";
	
	public static void main(String[] args) throws IOException {
		final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
		final ClientConfiguration ccfg = new ClientConfiguration().withUseExpectContinue(true);

		final EndpointConfiguration endpoint = new EndpointConfiguration(serviceEndpoint, signingRegion);

		long partSize = 5<<20;
		
		final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withClientConfiguration(ccfg)
                .withEndpointConfiguration(endpoint)
                .withPathStyleAccessEnabled(true)
                .build();
		
		String keyName = Paths.get("").getFileName().toString();//上传/下载的文件名字？ 3
		
		ArrayList<PartETag> partETags = new ArrayList<PartETag>();
		File file = new File("");
		long contentLength = file.length();
		String uploadId = null;
		
		try {
			//一个对象得历史版本的查询
			/*ListVersionsRequest listVersionsRequest = new ListVersionsRequest()
					.withBucketName(bucketName)
					.withMaxResults(200);
			SimpleDateFormat dateFormate = new SimpleDateFormat("yyyy/mm/dd HH:mm:ss");
			VersionListing versionListing = s3.listVersions(listVersionsRequest);
            int numVersions = 1;
            System.out.println("Key\tLastModified\tSize\tVersizeId");
            while (true) {
                for (S3VersionSummary objectSummary : versionListing.getVersionSummaries()) {
                	if (!objectSummary.getKey().equals(keyName)) {
                		continue;
                	}
                	System.out.format("'revision #:%d'\t'%s'\t%d\t'%s'\n", 
                			numVersions, 
                			dateFormate.format(objectSummary.getLastModified()),
                			objectSummary.getSize(),
                			objectSummary.getVersionId()
                			);
                    numVersions++;
                }
                if (versionListing.isTruncated()) {
                    versionListing = s3.listNextBatchOfVersions(versionListing);
                } else {
                    break;
                }
            }*/
			
			//恢复一个误删对象的某一早期版本
			/*String versionId = "hfUBGOEhcIc95MUNESjUbTCHYxfBjo0";
			String keyName = "download.txt";
			GetObjectRequest getObjectRequest = new GetObjectRequest(bucketName, keyName, versionId);
			System.out.format("Retrieving %s from S3 bucket %s...\n", keyName, bucketName);
			
			S3Object o = s3.getObject(getObjectRequest);
			S3ObjectInputStream s3is = o.getObjectContent();
			
			final SimpleDateFormat dateFormate=new SimpleDateFormat("yyyy-mm-dd_HH-mm-ss");
			final String filePath = Paths.get("D:\\",
					String.format("%s_%s", dateFormate.format(o.getObjectMetadata().getLastModified()),
 					keyName)).toString();
			
			final File file = new File(filePath);
			FileOutputStream fos = new FileOutputStream(file);
			byte[] read_buf = new byte[64*1024];
			int read_len = 0;
			while((read_len = s3is.read(read_buf))>0) {
				fos.write(read_buf,0,read_len);
			}
			fos.close();
			
			System.out.format("Save %s to %s\n", keyName, filePath);*/
			
			//分块上传
			// Step 1: Initialize.
			InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, keyName);
			uploadId = s3.initiateMultipartUpload(initRequest).getUploadId();
			System.out.format("Created upload ID was %s\n", uploadId);

			// Step 2: Upload parts.
			long filePosition = 0;
			for (int i = 1; filePosition < contentLength; i++) {
				// Last part can be less than 5 MB. Adjust part size.
				partSize = Math.min(partSize, contentLength - filePosition);

				// Create request to upload a part.
				UploadPartRequest uploadRequest = new UploadPartRequest()
						.withBucketName(bucketName)
						.withKey(keyName)
						.withUploadId(uploadId)
						.withPartNumber(i)
						.withFileOffset(filePosition)
						.withFile(file)
						.withPartSize(partSize);

				// Upload part and add response to our list.
				System.out.format("Uploading part %d\n", i);
				partETags.add(s3.uploadPart(uploadRequest).getPartETag());

				filePosition += partSize;
			}

			// Step 3: Complete.
			System.out.println("Completing upload");
			CompleteMultipartUploadRequest compRequest = 
					new CompleteMultipartUploadRequest(bucketName, keyName, uploadId, partETags);

			s3.completeMultipartUpload(compRequest);
		} catch (Exception e) {
			System.err.println(e.toString());
			if (uploadId != null && !uploadId.isEmpty()) {
				// Cancel when error occurred
				System.out.println("Aborting upload");
				s3.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, keyName, uploadId));
			}
			System.exit(1);
		}
		System.out.println("Done!");
	}
}
