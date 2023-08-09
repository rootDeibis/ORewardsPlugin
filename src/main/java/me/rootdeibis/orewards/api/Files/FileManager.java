package me.rootdeibis.orewards.api.Files;

import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;


public class FileManager {
	
	private final Plugin instance;
	private final HashMap<String, RFile> FilesCache = new HashMap<>();
	private final HashMap<String, RDirectory> directoriesCache = new HashMap<>();

	private String resourcesPath;
	
	public FileManager(Plugin main) {
		this.instance = main;
		
	}	
	
	public FileManager setResourcesPath(String path) {
		

		
		String resolvedPath = path.replace(".", "/");
		this.resourcesPath = path.startsWith("/") ? resolvedPath : "/" + resolvedPath ;

		return this;
	}
	
	public String getResourcesPath() {
		return resourcesPath;
	}
	
	public RFile use(String path) {
		if(!FilesCache.containsKey(path)) {
			RFile file = new RFile(path, this);
			
			FilesCache.put(path, file);
		}
		
		
		
		return FilesCache.get(path);
	}

	public RDirectory dir(String path) {
		if(!directoriesCache.containsKey(path)) {
			directoriesCache.put(path, new RDirectory(this, path));
		}

		return directoriesCache.get(path);
	}
	
	public Plugin getInstance() {
		return instance;
	}

	public File getDataFolder() {
		return instance.getDataFolder();
	}

	public void reload() {
		this.FilesCache.values().forEach(RFile::reload);
	}


}
