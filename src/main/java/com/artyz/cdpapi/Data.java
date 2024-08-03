package com.artyz.cdpapi;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Data {

    private static File file;
    private static FileConfiguration fcon;
    private static CDPAPI main;

    public Data(CDPAPI main){
        this.main = main;

        file = new File(main.getDataFolder(),"CountdownData.yml");
        if(!file.getParentFile().exists()){
            file.getParentFile().mkdirs();
        }
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        fcon = YamlConfiguration.loadConfiguration(file);
        reloadConfigdata();
    }

    public static void reloadConfigdata(){
        save();
    }

    public static FileConfiguration getConfig(){
        return fcon;
    }

    public static void reload(){
        fcon = fcon = YamlConfiguration.loadConfiguration(file);
        reloadConfigdata();
        save();
    }

    public static void save(){
        try {
            fcon.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
