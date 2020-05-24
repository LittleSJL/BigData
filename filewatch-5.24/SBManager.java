package file_synchronize;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.UploadPartRequest;

public class SBManager {
	private final static String bucketName = "sunjinliang";//创建的bucket的名字
	private final static String filePath   = "D:\\不可视之禁\\大三下\\大数据开发实训\\课程安排\\第一波：S3对象 - 数据如何存\\5.18第一次课程(1-3)\\";
	private final static String accessKey = "53398F8F839C16A82DB3";
	private final static String secretKey = "W0I0NzQ3OEExM0ZBQjI3RTg1NDI5OUQwMkQ0QTc5QTdCMzhFRTVCMkNd";
	private final static String serviceEndpoint = "http://scuts3.depts.bingosoft.net:29999";
	private final static String signingRegion = "";
	
	public void SBmanager()
	{

	}
	
	public void Upload(String filename)
	{
        try {
        	final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
            final ClientConfiguration ccfg = new ClientConfiguration().
                    withUseExpectContinue(false);

            final EndpointConfiguration endpoint = new EndpointConfiguration(serviceEndpoint, signingRegion);

            final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withClientConfiguration(ccfg)
                    .withEndpointConfiguration(endpoint)
                    .withPathStyleAccessEnabled(true)
                    .build();
        	//上传数据+使用xxxx/前缀对象 - 自动显示成目录的形式
        	//windows转义符要用‘\\’
        	s3.putObject(bucketName,filename,new File(filePath+filename));


        }catch(AmazonClientException e){
        	System.err.println(e.toString());
        	System.exit(1);
        }
        System.out.println("成功上传"+filename+" 文件");
    }
	
	public void Delete(String filename)
	{
        try {
        	final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
            final ClientConfiguration ccfg = new ClientConfiguration().
                    withUseExpectContinue(false);

            final EndpointConfiguration endpoint = new EndpointConfiguration(serviceEndpoint, signingRegion);

            final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withClientConfiguration(ccfg)
                    .withEndpointConfiguration(endpoint)
                    .withPathStyleAccessEnabled(true)
                    .build();
        	//上传数据+使用xxxx/前缀对象 - 自动显示成目录的形式
        	//windows转义符要用‘\\’
        	s3.deleteObject(bucketName, filename);


        }catch(AmazonClientException e){
        	System.err.println(e.toString());
        	System.exit(1);
        }

        System.out.println("成功删除"+filename+" 文件");
	}

	public void Modify_(String filename) {
        try {
        	final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
            final ClientConfiguration ccfg = new ClientConfiguration().
                    withUseExpectContinue(false);

            final EndpointConfiguration endpoint = new EndpointConfiguration(serviceEndpoint, signingRegion);

            final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withClientConfiguration(ccfg)
                    .withEndpointConfiguration(endpoint)
                    .withPathStyleAccessEnabled(true)
                    .build();
        	//上传数据+使用xxxx/前缀对象 - 自动显示成目录的形式
        	//windows转义符要用‘\\’
        	s3.deleteObject(bucketName, filename);
        	s3.putObject(bucketName,filename,new File(filePath+filename));

        }catch(AmazonClientException e){
        	System.err.println(e.toString());
        	System.exit(1);
        }
        System.out.println("成功修改"+filename+" 文件");
	}
	
	public List<String> get_bucket_file() {
        try {
        	final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
            final ClientConfiguration ccfg = new ClientConfiguration().
                    withUseExpectContinue(false);

            final EndpointConfiguration endpoint = new EndpointConfiguration(serviceEndpoint, signingRegion);

            final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withClientConfiguration(ccfg)
                    .withEndpointConfiguration(endpoint)
                    .withPathStyleAccessEnabled(true)
                    .build();
        	
        	List<String> filelist = new ArrayList<String>();
        	ObjectListing o1 = s3.listObjects(bucketName);//全部获取
        	
        	List<S3ObjectSummary> objects = o1.getObjectSummaries();
        	for (S3ObjectSummary os : objects) {
        		//System.out.println("*"+os.getKey());
        		filelist.add(""+os.getKey());
        	}
        	return filelist;	
        }catch(AmazonClientException e){
        	System.err.println(e.toString());
        	System.exit(1);
        }

        System.out.println("Done!");
		return null;
	}
	
	public void first_synchronize(List<String> fileList_disk,List<String> fileList_bucket) {
        try {
        	System.out.println("程序启动，对比本地磁盘和S3桶，首次同步文件中...");
            
            for (int i=0;i<fileList_disk.size();i++) {
            	String disk_item = fileList_disk.get(i);
            	if(!fileList_bucket.contains(disk_item)) {
            		Upload_part(disk_item);
            	}
            }
            
            for (int i=0;i<fileList_bucket.size();i++) {
            	String bucket_item = ""+fileList_bucket.get(i);
            	if(!fileList_disk.contains(bucket_item)) {
            		Delete(bucket_item);
            	}
            }
            	
            System.out.println("同步完成！");
            System.out.println("进入本地目录监听模式...");
        	
        }catch(AmazonClientException e){
        	System.err.println(e.toString());
        	System.exit(1);
        }
	}
	
	public void Upload_part(String filename)
	{
		final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
        final ClientConfiguration ccfg = new ClientConfiguration().
                withUseExpectContinue(false);

        final EndpointConfiguration endpoint = new EndpointConfiguration(serviceEndpoint, signingRegion);

        final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withClientConfiguration(ccfg)
                .withEndpointConfiguration(endpoint)
                .withPathStyleAccessEnabled(true)
                .build();
        
        long partSize = 5<<20;
        
        String keyName = Paths.get(filePath+filename).getFileName().toString();//上传/下载的文件名字？ 3
		
		ArrayList<PartETag> partETags = new ArrayList<PartETag>();
		File file = new File(filePath+filename);
		long contentLength = file.length();
		String uploadId = null;
		
        try {
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

