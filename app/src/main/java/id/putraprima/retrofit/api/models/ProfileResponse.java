package id.putraprima.retrofit.api.models;

public class ProfileResponse {
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
