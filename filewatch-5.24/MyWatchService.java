package file_synchronize;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.ArrayList;
import java.util.List;


public class MyWatchService {

    public static void main(String[] args) throws Exception {
    	SBManager m = new SBManager();
        String propFileName = "D:\\������֮��\\������\\�����ݿ���ʵѵ\\�γ̰���\\��һ����S3���� - ������δ�\\5.18��һ�ογ�(1-3)"; //Ҫ��ص��ļ�Ŀ¼
        
        //����ʱ���Ȼ�ȡ����Ŀ¼�������ļ����ٻ�ȡS3Ͱ���ļ�
        List<File> fileList_disk = readFile(propFileName);
        List<String> fileList_bucket = m.get_bucket_file();
        List<String> fileList_disk_aft = new ArrayList<String>();
        
        for (int i=0;i<fileList_disk.size();i++) {
        	String disk_item = ""+fileList_disk.get(i).getName();
        	fileList_disk_aft.add(disk_item);
        }
        
        //���м���ǰ�ĵ�һ��ͬ��
        m.first_synchronize(fileList_disk_aft, fileList_bucket);
        
        WatchService watchService= FileSystems.getDefault().newWatchService();
        Path path = Paths.get(propFileName);
        path.register(watchService, 
        		StandardWatchEventKinds.ENTRY_CREATE, 
        		StandardWatchEventKinds.ENTRY_DELETE, 
                StandardWatchEventKinds.ENTRY_MODIFY);
        WatchKey key = null;
        
        //��������
        synchronize(key,watchService,m);
    }
    
    private static List<File> readFile(String fileDir) {
    	List<File> fileList = new ArrayList<File>();
    	File file = new File(fileDir);
        File[] files = file.listFiles();// ��ȡĿ¼�µ������ļ����ļ���
        if (files == null) {// ���Ŀ¼Ϊ�գ�ֱ���˳�
            return null;
        }
        // ������Ŀ¼�µ������ļ�
        for (File f : files) {
            if (f.isFile()) {
                fileList.add(f);
            } else if (f.isDirectory()) {
                System.out.println(f.getAbsolutePath());
                readFile(f.getAbsolutePath());
            }
        }
		return fileList;
    }
    
    private static void synchronize(WatchKey key,WatchService watchService,SBManager m) throws InterruptedException {
    	while ((key = watchService.take()) != null) {
        	int count = 0;
        	
        	for (WatchEvent<?> event : key.pollEvents()) {
        		if(event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
        			count++;
            		
            		if(count%2!=0) {
            			m.Upload_part(event.context()+"");
            		}
            	}
            	if(event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
            		count++;
            		
            		if(count%2!=0) {
            			m.Delete(event.context()+"");
            		}
            			
            	}
            	if(event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
            		count++;
            		
            		if(count%2!=0) {
            			m.Modify_(event.context()+"");
            		}	
            	}
          }
          key.reset();
      }
    }
}

