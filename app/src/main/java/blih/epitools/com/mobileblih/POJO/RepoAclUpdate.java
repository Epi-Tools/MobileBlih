package blih.epitools.com.mobileblih.POJO;

public class RepoAclUpdate {
    private String email;
    private String token;
    private String name;
    private String repoName;
    private String menu_acl;

    public RepoAclUpdate(String email, String token, String name, String repoName, String acl) {
        this.email = email;
        this.token = token;
        this.name = name;
        this.repoName = repoName;
        this.menu_acl = acl;
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

    public String getRepoName() {
        return repoName;
    }

    public void setRepoName(String repoName) {
        this.repoName = repoName;
    }

    public String getAcl() {
        return menu_acl;
    }

    public void setAcl(String acl) {
        this.menu_acl = acl;
    }
}
