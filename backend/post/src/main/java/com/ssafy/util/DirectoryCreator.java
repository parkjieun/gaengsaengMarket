package com.ssafy.util;

import java.io.File;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DirectoryCreator implements ApplicationRunner{
	@Value("${file.upload.directory}")
	private String directoryPath;
	
	@Autowired
	ServletContext context;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		String rootPath = context.getRealPath(directoryPath);
		File folder = new File(rootPath);
		// 해당 디렉토리가 없을경우 디렉토리를 생성합니다.
		if (!folder.exists()) {
			try{
				folder.mkdirs(); //폴더 생성합니다.
			    
		        } 
		        catch(Exception e){
			    e.getStackTrace();
			}     
		}
	}
	
}
