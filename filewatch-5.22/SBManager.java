package filewatch;

import java.io.File;

import com.amazonaws.AmazonClientException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;

public class SBManager {
	private final static String bucketName = "sunjinliang";//������bucket������
	private final static String filePath   = "D:\\������֮��\\������\\�����ݿ���ʵѵ\\�γ̰���\\��һ����S3���� - ������δ�\\5.18��һ�ογ�(1-3)\\";
	private final static String accessKey = "53398F8F839C16A82DB3";
	private final static String secretKey = "W0I0NzQ3OEExM0ZBQjI3RTg1NDI5OUQwMkQ0QTc5QTdCMzhFRTVCMkNd";
	private final static String serviceEndpoint = "http://scuts3.depts.bingosoft.net:29999";
	private final static String signingRegion = "";
	
	public void SBmanager()
	{
		
	}
	
	public void Upload(String filename)
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

        try {
        	//�ϴ�����+ʹ��xxxx/ǰ׺���� - �Զ���ʾ��Ŀ¼����ʽ
        	//windowsת���Ҫ�á�\\��
        	s3.putObject(bucketName,filename,new File(filePath+filename));


        }catch(AmazonClientException e){
        	System.err.println(e.toString());
        	System.exit(1);
        }

        System.out.println("�ɹ��ϴ�"+filename+" �ļ�");
    }
	
	public void Delete(String filename)
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

        try {
        	//�ϴ�����+ʹ��xxxx/ǰ׺���� - �Զ���ʾ��Ŀ¼����ʽ
        	//windowsת���Ҫ�á�\\��
        	s3.deleteObject(bucketName, filename);


        }catch(AmazonClientException e){
        	System.err.println(e.toString());
        	System.exit(1);
        }

        System.out.println("�ɹ�ɾ��"+filename+" �ļ�");
	}

	public void Modify_(String filename) {
		// TODO Auto-generated method stub
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
        	//�ϴ�����+ʹ��xxxx/ǰ׺���� - �Զ���ʾ��Ŀ¼����ʽ
        	//windowsת���Ҫ�á�\\��
        	s3.deleteObject(bucketName, filename);


        }catch(AmazonClientException e){
        	System.err.println(e.toString());
        	System.exit(1);
        }
        
        try {
        	//�ϴ�����+ʹ��xxxx/ǰ׺���� - �Զ���ʾ��Ŀ¼����ʽ
        	//windowsת���Ҫ�á�\\��
        	s3.putObject(bucketName,filename,new File(filePath+filename));


        }catch(AmazonClientException e){
        	System.err.println(e.toString());
        	System.exit(1);
        }
        System.out.println("�ɹ��޸�"+filename+" �ļ�");
	}
}
