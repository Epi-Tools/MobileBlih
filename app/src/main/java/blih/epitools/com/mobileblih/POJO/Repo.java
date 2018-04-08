package blih.epitools.com.mobileblih.POJO;

public class Repo {
    private String email;
    private String token;
    private String name;
    private boolean acl;

    public Repo(String _email, String _token, String _repoName, boolean _acl) {
        email = _email;
        token = _token;
        name = _repoName;
        acl = _acl;
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

    public String getRepoName() {
        return name;
    }

    public void setRepoName(String repoName) {
        this.name = repoName;
    }

    public boolean isAcl() {
        return acl;
    }

    public void setAcl(boolean acl) {
        this.acl = acl;
    }
}
