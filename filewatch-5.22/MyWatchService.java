package filewatch;


import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyWatchService {

    public static void main(String[] args) throws Exception {
    	//����������ͬ��һ�α���Ŀ¼��S3Ͱ���ļ����Աȣ���û�о��ϴ�/���˾�ɾ����
    	//���������ʱ�������������ɾ��ʱ��
    	SBManager m = new SBManager();
        String propFileName = "D:\\������֮��\\������\\�����ݿ���ʵѵ\\�γ̰���\\��һ����S3���� - ������δ�\\5.18��һ�ογ�(1-3)"; //Ҫ��ص��ļ�Ŀ¼
        //��Ϊ���̰߳�ȫ�����Կ��Է���ThreadPool��ʹ��
        /*ExecutorService cachedThreadPool = Executors.newFixedThreadPool(1);
        cachedThreadPool.execute(new FileWatchTask(propFileName));*/
        WatchService watchService= FileSystems.getDefault().newWatchService();
        Path path = Paths.get(propFileName);
        path.register(watchService, 
        		StandardWatchEventKinds.ENTRY_CREATE, 
        		StandardWatchEventKinds.ENTRY_DELETE, 
                StandardWatchEventKinds.ENTRY_MODIFY);
        WatchKey key;
        while ((key = watchService.take()) != null) {
        	int count = 0;
        	
        	for (WatchEvent<?> event : key.pollEvents()) {
        		if(event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
        			count++;
            		
            		if(count%2!=0) {
            			System.out.println("count"+count);
            			System.out.println(event.context()+" "+"����");
            			m.Upload(event.context()+"");
            		}
            		
            	}
            	if(event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
            		count++;
            		
            		if(count%2!=0) {
            			System.out.println("count"+count);
            			System.out.println(event.context()+" "+"ɾ��");
            			m.Delete(event.context()+"");
            		}
            			
            	}
            	if(event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
            		count++;
            		
            		if(count%2!=0) {
            			System.out.println("count"+count);
            			System.out.println(event.context()+" "+"�޸�");
            			m.Modify_(event.context()+"");
            		}
            			
            	}
          }
          key.reset();
      }
    }

}
