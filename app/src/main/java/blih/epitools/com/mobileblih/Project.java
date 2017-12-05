package blih.epitools.com.mobileblih;

public class Project {
    private String name;
    private String acl = null;

    Project (String text1){
        name = text1;
    }

    Project (String text, String acls)
    {
        name = text;
        acl = acls;
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