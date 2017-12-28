package com.mycillin.partner.util;

import com.mycillin.partner.modul.account.model.loginModel.ModelRestLogin;
import com.mycillin.partner.modul.account.model.registerModel.ModelRestRegister;
import com.mycillin.partner.modul.accountProfile.model.expertise.ModelRestExpertise;
import com.mycillin.partner.modul.accountProfile.model.profession.ModelRestProfession;
import com.mycillin.partner.modul.accountProfile.model.serviceType.ModelRestService;
import com.mycillin.partner.modul.home.cancelAdapterList.ModelRestCancelReason;

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

    @POST("list_partner_type/")
    Call<ModelRestProfession> getProfession(@Body HashMap<String, String> params);

    @POST("list_spesialisasi/")
    Call<ModelRestExpertise> getExpertise(@Body HashMap<String, String> params);

    @GET("list_service_type/")
    Call<ModelRestService> getServiceType();

    @GET("list_cancel_reason_partner/")
    Call<ModelRestCancelReason> getCancelReason();
}
