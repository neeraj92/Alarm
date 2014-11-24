package com.pojo;

/**
 * Created by chikku on 23/11/14.
 */
public class ApplicationDetails {
    private String applicationName;
    private String packageName;

    public ApplicationDetails (){
        this.applicationName = "No Application";
        this.packageName = "";
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
