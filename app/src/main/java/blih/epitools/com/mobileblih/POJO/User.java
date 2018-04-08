package blih.epitools.com.mobileblih.POJO;

public class User {
    private static final User ourInstance = new User();

    public static User getInstance() {
        return ourInstance;
    }

    private String email, token;

    private User() {

    }

    public void setEmail(String _email) {
        email = _email;
    }
    public String getToken() {
        return token;
    }

    public String getEmail() {
        return email;
    }

    public void setUserInfos(String _email, String _token) {
        email = _email;
        token = _token;
    }
}
