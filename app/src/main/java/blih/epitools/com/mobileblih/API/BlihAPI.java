package blih.epitools.com.mobileblih.API;

import blih.epitools.com.mobileblih.POJO.User;
import blih.epitools.com.mobileblih.POJO.UserCredits;
import blih.epitools.com.mobileblih.POJO.UserToken;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface BlihAPI {


    @POST("/api/auth")
    Call<UserToken> authUser(@Body UserCredits user);

    @POST("/api/repo/list")
    Call<ResponseBody> repoList(@Body User user);

    @POST("api/repo/create")
    Call<UserToken> createRepo(@Body String email, String token, String repoName, boolean acl);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://blih-preprod.cleverapps.io")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
