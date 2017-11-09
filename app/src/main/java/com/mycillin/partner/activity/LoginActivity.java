package com.mycillin.partner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mycillin.partner.R;
import com.mycillin.partner.restful.PartnerAPI;
import com.mycillin.partner.restful.RestClient;
import com.mycillin.partner.restful.login.ModelRestLogin;
import com.mycillin.partner.util.Configs;
import com.mycillin.partner.util.DataHelper;
import com.mycillin.partner.util.DialogHelper;
import com.mycillin.partner.util.ProgressBarHandler;
import com.mycillin.partner.util.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.loginActivity_rl_loginLandingContainer)
    RelativeLayout loginLandingContainer;
    @BindView(R.id.loginActivity_rl_loginContainer)
    RelativeLayout loginContainer;
    @BindView(R.id.loginActivity_rl_forgotPassContainer)
    RelativeLayout forgotPassContainer;
    @BindView(R.id.loginActivity_bt_showHaveAccBtn)
    Button showHaveAccBtn;
    @BindView(R.id.loginActivity_bt_register)
    Button btnRegister;
    @BindView(R.id.loginActivity_bt_loginBtn)
    Button doLoginBtn;
    @BindView(R.id.loginActivity_et_loginEmail)
    EditText edtxEmail;
    @BindView(R.id.loginActivity_et_loginPassword)
    EditText edtxPassword;
    @BindView(R.id.loginActivity_et_forgotEmail)
    EditText edtxForgotEmail;
    @BindView(R.id.loginActivity_tv_forgotPassword)
    TextView tvForgotPass;
    @BindView(R.id.loginActivity_bt_forgoBtn)
    Button doForgotBtn;

    private PartnerAPI partnerAPI;
    private SessionManager session;
    private Handler mHandler;
    private ProgressBarHandler mProgressBarHandler;
    private boolean doubleBackToExitPressedOnce = false;
    private int MENU_FLAG_LANDING = 1001;
    private int MENU_FLAG_LOGIN = 1003;
    private int MENU_FLAG_FORGET = 1004;
    private int MENU_FLAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        partnerAPI = RestClient.getPartnerRestInterfaceNoToken();
        session = new SessionManager(getApplicationContext());
        MENU_FLAG = MENU_FLAG_LANDING;
        edtxEmail.setText("tommi.asmara3@gmail.com");
        edtxPassword.setText("rahasia");

        mHandler = new Handler(Looper.getMainLooper());
        mProgressBarHandler = new ProgressBarHandler(this);
    }


    @OnClick(R.id.loginActivity_bt_showHaveAccBtn)
    public void haveAccountClick() {
        toLoginView();
    }


    @OnClick(R.id.loginActivity_bt_loginBtn)
    public void loginFunction() {
        String email = edtxEmail.getText().toString().trim();
        String password = edtxPassword.getText().toString().trim();

        if (email.isEmpty()) {
            edtxEmail.setError(getString(R.string.email_error));
        } else if (password.isEmpty()) {
            edtxPassword.setError(getString(R.string.pass_error));
        } else {
            doLogin(email, password);
        }
    }

    @OnClick(R.id.loginActivity_bt_register)
    public void registerFunction() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.loginActivity_tv_forgotPassword)
    public void forgetPassFunction() {
        forgotPassContainer.setVisibility(View.VISIBLE);
        loginContainer.setVisibility(View.GONE);
        MENU_FLAG = MENU_FLAG_FORGET;
    }

    @OnClick(R.id.loginActivity_bt_forgoBtn)
    public void forgetPassBtnFunction() {
        if (edtxForgotEmail.getText().toString().isEmpty()) {
            edtxForgotEmail.setError("Email Must Be Filled");
        } else {
            doForgotPass(edtxForgotEmail.getText().toString());
        }
    }

    private void doForgotPass(String email) {
        mProgressBarHandler.show();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        Map<String, Object> data = new HashMap<>();
        data.put("email", email);
        JSONObject jsonObject = new JSONObject(data);
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Configs.URL_REST_CLIENT + "forgot_password_doctor")
                .post(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .build();

        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NonNull okhttp3.Call call, @NonNull IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                    }
                });
                DialogHelper.showDialog(mHandler, LoginActivity.this, "Warning", "Connection Problem, Please Try Again Later." + e, false);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull okhttp3.Response response) throws IOException {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                    }
                });
                String result = response.body().string();

                Log.d("###", "onResponse: " + result);
                try {
                    JSONObject jsonObject2 = new JSONObject(result);
                    Log.d("###", "onResponse: " + jsonObject2);
                    if (response.isSuccessful()) {
                        JSONObject jsonObject = new JSONObject(result);

                        boolean status = jsonObject.getJSONObject("result").getBoolean("status");
                        if (status) {
                            String message = jsonObject.getJSONObject("result").getString("message");
                            DialogHelper.showDialog(mHandler, LoginActivity.this, "Info", message, false);
                        } else {
                            String message = jsonObject.getJSONObject("result").getString("message");
                            DialogHelper.showDialog(mHandler, LoginActivity.this, "Warning", message, false);
                        }
                    } else {
                        JSONObject jsonObject = new JSONObject(result);
                        String message = jsonObject.getJSONObject("result").getString("message");
                        DialogHelper.showDialog(mHandler, LoginActivity.this, "Warning", message, false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void doLogin(String email, final String password) {
        mProgressBarHandler.show();
        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        partnerAPI.doLogin(params).enqueue(new Callback<ModelRestLogin>() {
            @Override
            public void onResponse(@NonNull Call<ModelRestLogin> call, @NonNull Response<ModelRestLogin> response) {
                ModelRestLogin result = response.body();
                mProgressBarHandler.hide();
                assert result != null;
                if (response.isSuccessful()) {
                    if (!result.getResult().getMessage().contains("invalid login")) {
                        session.createLoginSession(
                                result.getResult().getData().getEmail(),
                                result.getResult().getData().getFullName(),
                                result.getResult().getData().getUserId(),
                                result.getResult().getToken(),
                                "",
                                password
                        );
                        DataHelper.token = result.getResult().getToken();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                } else {
                    mProgressBarHandler.hide();
                    DialogHelper.showDialog(mHandler, LoginActivity.this, "Error", "Invalid Password", false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelRestLogin> call, @NonNull Throwable t) {
                mProgressBarHandler.hide();
                DialogHelper.showDialog(mHandler, LoginActivity.this, "Error", "Connection Problem", false);
            }
        });
    }


    public void toLandingView() {
        loginLandingContainer.setVisibility(View.VISIBLE);
        loginContainer.setVisibility(View.GONE);
        forgotPassContainer.setVisibility(View.GONE);

        MENU_FLAG = MENU_FLAG_LANDING;
    }

    public void toLoginView() {
        loginLandingContainer.setVisibility(View.GONE);
        loginContainer.setVisibility(View.VISIBLE);

        MENU_FLAG = MENU_FLAG_LOGIN;
    }


    @Override
    public void onBackPressed() {
        if (MENU_FLAG == MENU_FLAG_LANDING) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.pressBackAgainToLeave, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            toLandingView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SessionManager session = new SessionManager(getApplicationContext());
        if (session.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
