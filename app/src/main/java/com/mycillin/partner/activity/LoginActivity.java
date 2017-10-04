package com.mycillin.partner.activity;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.mycillin.partner.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.loginActivity_rl_loginLandingContainer)
    RelativeLayout loginLandingContainer;
    @BindView(R.id.loginActivity_rl_loginContainer)
    RelativeLayout loginContainer;

    @BindView(R.id.loginActivity_bt_showHaveAccBtn)
    Button showHaveAccBtn;
    @BindView(R.id.loginActivity_bt_loginBtn)
    Button doLoginBtn;

    private boolean doubleBackToExitPressedOnce = false;
    private int MENU_FLAG_LANDING = 1001;
    private int MENU_FLAG_LOGIN = 1003;
    private int MENU_FLAG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        MENU_FLAG = MENU_FLAG_LANDING;

        showHaveAccBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toLoginView();
            }
        });

        loginFunction();
    }

    public void loginFunction() {
        doLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 13/09/2017 DO LOGIN
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void toLandingView(){
        loginLandingContainer.setVisibility(View.VISIBLE);
        loginContainer.setVisibility(View.GONE);

        MENU_FLAG = MENU_FLAG_LANDING;
    }

    public void toLoginView(){
        loginLandingContainer.setVisibility(View.GONE);
        loginContainer.setVisibility(View.VISIBLE);

        MENU_FLAG = MENU_FLAG_LOGIN;
    }


    @Override
    public void onBackPressed() {
        if(MENU_FLAG == MENU_FLAG_LANDING) {
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
        }
        else {
            toLandingView();
        }
    }
}
