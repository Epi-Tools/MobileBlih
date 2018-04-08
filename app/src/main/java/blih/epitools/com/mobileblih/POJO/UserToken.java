package blih.epitools.com.mobileblih.POJO;

public class UserToken {
    private String token;
    private String err;
    private ProjectListBody body;
    private ProjectListBody _body;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ProjectListBody getBody() {
        return body;
    }

    public void setBody(ProjectListBody body) {
        this.body = body;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public ProjectListBody get_body() {
        return _body;
    }

    public void set_body(ProjectListBody _body) {
        this._body = _body;
    }
}
