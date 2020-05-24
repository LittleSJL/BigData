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
        String propFileName = "D:\\不可视之禁\\大三下\\大数据开发实训\\课程安排\\第一波：S3对象 - 数据如何存\\5.18第一次课程(1-3)"; //要监控的文件目录
        
        //启动时，先获取本地目录内所有文件，再获取S3桶内文件
        List<File> fileList_disk = readFile(propFileName);
        List<String> fileList_bucket = m.get_bucket_file();
        List<String> fileList_disk_aft = new ArrayList<String>();
        
        for (int i=0;i<fileList_disk.size();i++) {
        	String disk_item = ""+fileList_disk.get(i).getName();
        	fileList_disk_aft.add(disk_item);
        }
        
        //进行监听前的第一次同步
        m.first_synchronize(fileList_disk_aft, fileList_bucket);
        
        WatchService watchService= FileSystems.getDefault().newWatchService();
        Path path = Paths.get(propFileName);
        path.register(watchService, 
        		StandardWatchEventKinds.ENTRY_CREATE, 
        		StandardWatchEventKinds.ENTRY_DELETE, 
                StandardWatchEventKinds.ENTRY_MODIFY);
        WatchKey key = null;
        
        //开启监听
        synchronize(key,watchService,m);
    }
    
    private static List<File> readFile(String fileDir) {
    	List<File> fileList = new ArrayList<File>();
    	File file = new File(fileDir);
        File[] files = file.listFiles();// 获取目录下的所有文件或文件夹
        if (files == null) {// 如果目录为空，直接退出
            return null;
        }
        // 遍历，目录下的所有文件
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

