package com.tfs.db;

public class DBProps {
    private String driver;
    private String url;
    private String user;
    private String password;
    private String pgp_key;
    
    public DBProps() {
    }
    
    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPgp_key() {
        return pgp_key;
    }

    public void setPgp_key(String pgp_key) {
        this.pgp_key = pgp_key;
    }
}
