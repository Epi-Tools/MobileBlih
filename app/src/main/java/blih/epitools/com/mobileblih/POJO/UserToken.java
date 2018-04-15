package blih.epitools.com.mobileblih.POJO;

public class UserToken {
    private String token;
    private String err;
    private ProjectBody body;
    private ProjectBody _body;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ProjectBody getBody() {
        return body;
    }

    public void setBody(ProjectBody body) {
        this.body = body;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public ProjectBody get_body() {
        return _body;
    }

    public void set_body(ProjectBody _body) {
        this._body = _body;
    }
}
