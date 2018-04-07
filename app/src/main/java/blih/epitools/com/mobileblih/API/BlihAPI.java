package blih.epitools.com.mobileblih.API;

import blih.epitools.com.mobileblih.POJO.Token;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface BlihAPI {


    @POST("/api/auth")
    Call<Token> authUser(String username, String pwd);


    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://blih-preprod.cleverapps.io")
            .build();
}
