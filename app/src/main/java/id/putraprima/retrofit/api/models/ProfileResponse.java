package id.putraprima.retrofit.api.models;

import java.io.Serializable;

public class ProfileResponse implements Serializable {
    private DataProfile data;

    public ProfileResponse(DataProfile data) {
        this.data = data;
    }

    public DataProfile getData() {
        return data;
    }

    public void setData(DataProfile data) {
        this.data = data;
    }
}
