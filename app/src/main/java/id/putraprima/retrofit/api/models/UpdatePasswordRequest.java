package id.putraprima.retrofit.api.models;

public class UpdatePasswordRequest {
    private String password, password_confirmation, token;

    public UpdatePasswordRequest(String password, String password_confirmation, String token) {
        this.password = password;
        this.password_confirmation = password_confirmation;
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword_confirmation() {
        return password_confirmation;
    }

    public void setPassword_confirmation(String password_confirmation) {
        this.password_confirmation = password_confirmation;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
