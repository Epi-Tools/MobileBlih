package blih.epitools.com.mobileblih.POJO;

public class UserACL {
    private String name;
    private String acl;

    public UserACL(String _name, String _acl) {
        name = _name;
        acl = _acl;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAcl() {
        return acl;
    }

    public void setAcl(String acl) {
        this.acl = acl;
    }
}
