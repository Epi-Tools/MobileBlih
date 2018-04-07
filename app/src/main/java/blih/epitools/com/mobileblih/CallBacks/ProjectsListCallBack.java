package blih.epitools.com.mobileblih.CallBacks;

import blih.epitools.com.mobileblih.Activities.MainActivity;
import blih.epitools.com.mobileblih.POJO.Token;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectsListCallBack implements Callback<Token> {

    MainActivity context;

    public ProjectsListCallBack(MainActivity _context) {
        context = _context;
    }

    @Override
    public void onResponse(Call<Token> call, Response<Token> response) {
        if (response.isSuccessful()) {

        }
    }

    @Override
    public void onFailure(Call<Token> call, Throwable t) {

    }
}

