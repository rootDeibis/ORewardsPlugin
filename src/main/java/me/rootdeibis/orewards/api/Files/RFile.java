package me.rootdeibis.orewards.api.Files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


import org.bukkit.configuration.file.YamlConfiguration;

public class RFile extends YamlConfiguration {
	
	private final File file;
	private final String path;
	private final FileManager fileManager;
	private String defaultFilePath;
	
	
	public RFile(String path, FileManager fm) {
	
		
		this.path = path;
		this.fileManager = fm;
		this.file = new File(this.fileManager.getDataFolder(), this.path);
		
			
	}
	
	public RFile setDefaults(String path) {
		this.defaultFilePath = path;
		
		return this;
	}
	
	private void useDefaults() {
		
		InputStream in = this.fileManager.getClass().getResourceAsStream(
				this.fileManager.getResourcesPath() != null ? this.fileManager.getResourcesPath() + this.path : path
				);
		
		System.out.println(this.fileManager.getResourcesPath() != null ? this.fileManager.getResourcesPath() + this.path : path);
		
		byte[] buffer = new byte[1024];
		int length;
		
		try {
			OutputStream out = new FileOutputStream(this.file);
			
			
			while((length = in.read(buffer)) > 0) {
				out.write(buffer,0,length);
			}
			
			in.close();
			out.close();
			
		} catch (Exception e) {
	
			e.printStackTrace();
		}
		
		
		
		
		
		
		
	}
	
	public RFile create() {
		try {
			if(!this.fileManager.getDataFolder().exists()) this.fileManager.getDataFolder().mkdir();
			if(!this.file.exists()) {
				
				this.file.getParentFile().mkdirs();
				this.file.createNewFile();	
				
				if(this.defaultFilePath != null) {
					
					this.useDefaults();
					
				}
				
				
			} 
			
			
			this.load(this.file);
			
			
			

		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return this;
	}
	
	public RFile save() {
		try {
			
			this.save(this.file);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}
	
	public RFile reload() {
		try {
			this.load(this.file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return this;
	}

	public File getFile() {
		return file;
	}
}
