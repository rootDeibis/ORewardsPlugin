package me.rootdeibis.orewards.api.configuration;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;

public class RFile extends YamlConfiguration {
	
	private final File file;
	private final String path;
	private final FileManager fileManager;
	private String defaultFilePath;

	private boolean loaded = false;
	
	
	public RFile(String path, FileManager fm) {
	
		
		this.path = path;
		this.fileManager = fm;
		this.file = new File(this.fileManager.getDataFolder(), this.path);
		
			
	}

	public RFile(File file, FileManager fm) {
		this.path = file.getPath();
		this.fileManager = fm;
		this.file = file;
	}
	
	public RFile setDefaults(String path) {
		this.defaultFilePath = path;
		
		return this;
	}

	@Override
	public String getString(String path) {
		return super.getString(path) != null ? super.getString(path) : "";
	}

	private void useDefaults() {
		
		InputStream in = this.fileManager.getClass().getResourceAsStream(
				this.fileManager.getResourcesPath() != null ? this.fileManager.getResourcesPath() + this.defaultFilePath : defaultFilePath
				);
		

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
			this.loaded = true;
			
			
			

		
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

	public boolean isLoaded() {
		return loaded;
	}

	public RFile reload() {
		try {
			this.load(this.file);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return this;
	}

	public void load() {
		try {
			this.load(this.file);
			this.loaded = true;
		} catch (IOException | InvalidConfigurationException e) {
			throw new RuntimeException(e);
		}

	}

	public File getFile() {
		return file;
	}
}
