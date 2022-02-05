package com.tfs.util;

import com.tfs.db.DBProps;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utils {
    private Utils() {
    }
    
    public static String getFilesDir() {
        String dir = "";
        try (InputStream is = Utils.class.getResourceAsStream("/resources/files_dir.properties")) {
            Properties props = new Properties();
            props.load(is);
            
            dir = (String) props.get("dir");
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
        return dir;
    }
    
    public static DBProps getDBProperties() {
        try (InputStream is = Utils.class.getResourceAsStream("/resources/connection.properties")) {
            Properties props = new Properties();
            DBProps dbprops = new DBProps();
            
            props.load(is);
            dbprops.setDriver((String) props.get("driver"));
            dbprops.setUrl((String) props.get("url"));
            dbprops.setUser((String) props.get("user"));
            dbprops.setPassword((String) props.get("password"));
            
            return dbprops;
        } catch (FileNotFoundException ex) {
            System.out.println(ex.getMessage());
        } catch (IOException ex) {
            System.out.println(ex);
        }
        return null;
    }
}
