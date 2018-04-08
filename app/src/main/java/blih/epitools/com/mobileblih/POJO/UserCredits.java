package blih.epitools.com.mobileblih.POJO;

public class UserCredits {
    private String email;
    private String pwd;

    public UserCredits(String _email, String _pwd) {
        email = _email;
        pwd = _pwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
