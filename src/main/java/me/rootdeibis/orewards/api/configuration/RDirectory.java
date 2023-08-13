package me.rootdeibis.orewards.api.configuration;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class RDirectory {

    private final FileManager fileManager;
    private final File file;

    private final HashMap<String,RFile> files = new HashMap<>();

    private final String dirPath;
    public RDirectory(FileManager fileManager, String path) {
        this.fileManager = fileManager;
        this.file = new File(fileManager.getInstance().getDataFolder(), path);
        this.dirPath = path;
    }


    public void loadFilesIndirectory() {
        if(this.file.isDirectory() && this.file.exists()) {

            Arrays.stream(Objects.requireNonNull(this.file.listFiles(f -> f.getName().endsWith(".yml")))).forEach(f -> {

                this.files.put(f.getName(), new RFile(f, this.fileManager));

            });

        }
    }

    public RFile use(String path) {
        if(!files.containsKey(path)) {

            this.files.put(path, new RFile(this.dirPath + "/" + path, this.fileManager));



        }
        RFile rFile =this.files.get(path);

        if(!rFile.isLoaded()) rFile.load();

        return rFile;
    }

    public RDirectory createIfNoExists() {
        if(!this.file.exists()) {
            this.file.mkdirs();
        }

        return this;
    }

    public void exportsDefaults(String... f) {
        Arrays.stream(f).forEach(name -> {
            RFile rFile = new RFile(this.dirPath + "/" + name, this.fileManager);
            rFile.setDefaults(name);
            rFile.create();
        });
    }

    public HashMap<String, RFile> getRFiles() {
        return files;
    }

    public boolean exists() {
        return this.file.exists();
    }
}
