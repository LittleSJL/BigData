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
    	//程序启动，同步一次本地目录和S3桶内文件（对比，若没有就上传/多了就删除）
    	//后程序运行时持续监控有无增删改时间
    	SBManager m = new SBManager();
        String propFileName = "D:\\不可视之禁\\大三下\\大数据开发实训\\课程安排\\第一波：S3对象 - 数据如何存\\5.18第一次课程(1-3)"; //要监控的文件目录
        //因为是线程安全的所以可以放入ThreadPool中使用
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
            			System.out.println(event.context()+" "+"创建");
            			m.Upload(event.context()+"");
            		}
            		
            	}
            	if(event.kind() == StandardWatchEventKinds.ENTRY_DELETE) {
            		count++;
            		
            		if(count%2!=0) {
            			System.out.println("count"+count);
            			System.out.println(event.context()+" "+"删除");
            			m.Delete(event.context()+"");
            		}
            			
            	}
            	if(event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
            		count++;
            		
            		if(count%2!=0) {
            			System.out.println("count"+count);
            			System.out.println(event.context()+" "+"修改");
            			m.Modify_(event.context()+"");
            		}
            			
            	}
          }
          key.reset();
      }
    }

}
