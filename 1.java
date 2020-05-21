package main;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Main {
	
	private final static String bucketName = "sunjinliang";//创建的bucket的名字
	//private final static String filePath   = "D:/不可视之禁/大三下/大数据开发实训/5.18第一次课程/hello.txt";
	private final static String accessKey = "53398F8F839C16A82DB3";
	private final static String secretKey = "W0I0NzQ3OEExM0ZBQjI3RTg1NDI5OUQwMkQ0QTc5QTdCMzhFRTVCMkNd";
	private final static String serviceEndpoint = 
	"http://scuts3.depts.bingosoft.net:29999";
	private final static String signingRegion = "";
	
	public static void main(String[] args) throws IOException {
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

	        try {
	        	//建桶
	        	//s3.createBucket(bucketName);
	        	
	        	//上传数据+使用xxxx/前缀对象 - 自动显示成目录的形式
	        	//windows转义符要用‘\\’
	        	//s3.putObject(bucketName,"2020/hello.txt",new File("D:\\不可视之禁\\大三下\\大数据开发实训\\5.18第一次课程\\hello.txt"));
	        	
	        	//下载数据
	        	/*S3Object o = s3.getObject(bucketName,"hello.txt");
	        	S3ObjectInputStream s3is = o.getObjectContent();
	        	FileOutputStream fos = new FileOutputStream(new File("D:\\不可视之禁\\大三下\\大数据开发实训\\5.18第一次课程\\download.txt"));
	        	byte[] read_buf = new byte[64*1024];
	        	int read_len = 0;
	        	while ((read_len = s3is.read(read_buf))>0) {
	        		fos.write(read_buf,0,read_len);
	        	}
	        	fos.close();*/
	        	
	        	//如果不知道bucket中的文件
	        	ListObjectsRequest listObjectsRequest = new ListObjectsRequest();
	        	listObjectsRequest.setBucketName(bucketName);
	        	listObjectsRequest.setDelimiter("/");
	        	ObjectListing o3 = s3.listObjects(listObjectsRequest);//需要什么拿什么
	        	for (String s :o3.getCommonPrefixes()) {
	        		System.out.println("*"+s);
	        	}
	        	
	        	/*ObjectListing o1 = s3.listObjects(bucketName);//全部获取
	        	ObjectListing o2 = s3.listObjects(bucketName,"2020/");//需要什么拿什么
	        	
	        	List<S3ObjectSummary> objects = o2.getObjectSummaries();
	        	for (S3ObjectSummary os : objects) {
	        		System.out.println("*"+os.getKey());
	        	}*/
	        	
	        	
	        	
	        	
	        }catch(AmazonClientException e){
	        	System.err.println(e.toString());
	        	System.exit(1);
	        }

	        System.out.println("Done!");
	    }

}
