package main;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration.Rule;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CopyObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;

public class Main {
	private final static String bucketName = "sunjinliang";
	private final static String accessKey = "53398F8F839C16A82DB3";
	private final static String secretKey = "W0I0NzQ3OEExM0ZBQjI3RTg1NDI5OUQwMkQ0QTc5QTdCMzhFRTVCMkNd";
	private final static String serviceEndpoint = "http://scuts3.depts.bingosoft.net:29999";
	private final static String signingRegion = "";
	
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws IOException {
		final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey, secretKey);
		final ClientConfiguration ccfg = new ClientConfiguration().withUseExpectContinue(false);

		final EndpointConfiguration endpoint = new EndpointConfiguration(serviceEndpoint, signingRegion);

		final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(credentials)).withClientConfiguration(ccfg)
				.withEndpointConfiguration(endpoint).withPathStyleAccessEnabled(true).build();

		//运行时修改权限
		/*try {
			s3.setObjectAcl(bucketName, "index.html", CannedAccessControlList.PublicRead);
			s3.setObjectAcl(bucketName, "timg.jpg", CannedAccessControlList.PublicRead);
		} catch (AmazonClientException e) {
			System.err.println(e.toString());
			System.exit(1);
		}*/
		
		//运行时修改属性
		/*ObjectMetadata om = new ObjectMetadata();
		om.setContentType("text/html");

		CopyObjectRequest req = new CopyObjectRequest(bucketName, "index.html", bucketName, "index.html");
		req.setMetadataDirective("REPLACE");
		req.setNewObjectMetadata(om);

		try {
			s3.copyObject(req);
		} catch (AmazonClientException e) {
			System.err.println(e.toString());
			System.exit(1);
		}*/
		
		//文件夹权限控制
		/*String policyText = new String(Files.readAllBytes(Paths.get("D:\\不可视之禁\\大三下\\大数据开发实训\\5.18第二次课\\travel(个人网站)\\strategy.txt")), Charset.forName("utf-8")).trim();
		policyText = policyText.replace("yourbucket", bucketName);

		try {
			s3.setBucketPolicy(bucketName, policyText);
		} catch (AmazonClientException e) {
			System.err.println(e.toString());
			System.exit(1);
		}*/
		
		//设置文件定期删除时间
		Rule rule = new Rule();
		rule.setId("logrule");
		rule.setPrefix("logs/");
		rule.setExpirationInDays(1);
		rule.setStatus(BucketLifecycleConfiguration.ENABLED);
		BucketLifecycleConfiguration conf = new BucketLifecycleConfiguration().withRules(rule);

		try {
			s3.setBucketLifecycleConfiguration(bucketName, conf);
		} catch (AmazonClientException e) {
			System.err.println(e.toString());
			System.exit(1);
		}

		System.out.println("Done!");
	}
}
