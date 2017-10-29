package com.mycillin.partner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mycillin.partner.R;
import com.mycillin.partner.restful.PartnerAPI;
import com.mycillin.partner.restful.RestClient;
import com.mycillin.partner.restful.login.ModelRestLogin;
import com.mycillin.partner.util.DataHelper;
import com.mycillin.partner.util.SessionManager;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.loginActivity_rl_loginLandingContainer)
    RelativeLayout loginLandingContainer;
    @BindView(R.id.loginActivity_rl_loginContainer)
    RelativeLayout loginContainer;
    @BindView(R.id.loginActivity_bt_showHaveAccBtn)
    Button showHaveAccBtn;
    @BindView(R.id.loginActivity_bt_loginBtn)
    Button doLoginBtn;
    @BindView(R.id.loginActivity_et_loginEmail)
    EditText edtxEmail;
    @BindView(R.id.loginActivity_et_loginPassword)
    EditText edtxPassword;
    private PartnerAPI partnerAPI;
    private SessionManager session;
    private boolean doubleBackToExitPressedOnce = false;
    private int MENU_FLAG_LANDING = 1001;
    private int MENU_FLAG_LOGIN = 1003;
    private int MENU_FLAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        partnerAPI = RestClient.getPartnerRestInterfaceToken();
        session = new SessionManager(getApplicationContext());
        MENU_FLAG = MENU_FLAG_LANDING;
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

    private void doLogin(String email, String password) {


        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);

        partnerAPI.doLogin(params).enqueue(new Callback<ModelRestLogin>() {
            @Override
            public void onResponse(Call<ModelRestLogin> call, Response<ModelRestLogin> response) {
                if (response.isSuccessful()) {
                    ModelRestLogin result = response.body();
                    assert result != null;
                    if (!result.getResult().getMessage().contains("invalid login")) {
                        session.createLoginSession(
                                result.getResult().getData().getEmail(),
                                result.getResult().getData().getFullName(),
                                result.getResult().getData().getUserId(),
                                result.getResult().getToken(),
                                ""
                        );
                        DataHelper.token = result.getResult().getToken();
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Snackbar.make(getWindow().getDecorView().getRootView(), "Password Salah", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(getWindow().getDecorView().getRootView(), response.code(), Snackbar.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<ModelRestLogin> call, Throwable t) {
                Snackbar.make(getWindow().getDecorView().getRootView(), t.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }


    public void toLandingView() {
        loginLandingContainer.setVisibility(View.VISIBLE);
        loginContainer.setVisibility(View.GONE);

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
