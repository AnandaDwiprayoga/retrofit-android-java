package id.putraprima.retrofit.api.models;

public class AppVersion {
    String app;
    String version;

    public AppVersion(String app, String version) {
        this.app = app;
        this.version = version;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
