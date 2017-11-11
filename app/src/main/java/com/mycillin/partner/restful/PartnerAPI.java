package com.mycillin.partner.restful;

import com.mycillin.partner.restful.expertise.ModelRestExpertise;
import com.mycillin.partner.restful.login.ModelRestLogin;
import com.mycillin.partner.restful.profession.ModelRestProfession;
import com.mycillin.partner.restful.register.ModelRestRegister;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface PartnerAPI {

    @POST("login_doctor/")
    Call<ModelRestLogin> doLogin(@Body HashMap<String, String> params);

    @FormUrlEncoded
    @POST("register/")
    Call<ModelRestRegister> doRegister(@Field("email") String email,
                                       @Field("password") String password,
                                       @Field("name") String name,
                                       @Field("ref_id") String referral);

    @GET("list_partner_type/")
    Call<ModelRestProfession> getProfession();

    @GET("list_spesialisasi/")
    Call<ModelRestExpertise> getExpertise();

}
