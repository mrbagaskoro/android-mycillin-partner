package com.mycillin.partner.restful;

import com.mycillin.partner.restful.login.ModelRestLogin;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface PartnerAPI {

    @POST("api/login_doctor")
    Call<ModelRestLogin> doLogin(@Body HashMap<String, String> params);

}
