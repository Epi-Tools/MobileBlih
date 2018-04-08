package blih.epitools.com.mobileblih.POJO;

public class RepoACL {
    private String email;
    private String token;
    private String name;

    public RepoACL(String _email, String _token, String _currentProjet) {
        email = _email;
        token = _token;
        name = _currentProjet;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
