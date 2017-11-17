package com.mycillin.partner.modul.account;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.mycillin.partner.R;
import com.mycillin.partner.util.PartnerAPI;
import com.mycillin.partner.util.Configs;
import com.mycillin.partner.util.DialogHelper;
import com.mycillin.partner.util.ProgressBarHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.register_ET_email)
    EditText edtxEmail;
    @BindView(R.id.register_ET_name)
    EditText edtxName;
    @BindView(R.id.register_ET_password)
    EditText edtxPassword;
    @BindView(R.id.register_ET_confirmpassword)
    EditText edtxConfirmPassword;
    @BindView(R.id.register_ET_phone)
    EditText edtxPhone;

    private Handler mHandler;
    private ProgressBarHandler mProgressBarHandler;
    private PartnerAPI mPartnerApi;

    public RegisterActivity() {
        //empty public constructor
    }

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        mHandler = new Handler(Looper.getMainLooper());
        mProgressBarHandler = new ProgressBarHandler(this);

        setSupportActionBar(toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_register_menu, menu);
        MenuItem menuFinish = menu.findItem(R.id.action_finish);
        menuFinish.setVisible(true);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_finish:
                final String email = edtxEmail.getText().toString().trim();
                final String password = edtxPassword.getText().toString().trim();
                final String name = edtxName.getText().toString().trim();
                String confirmPassword = edtxConfirmPassword.getText().toString();
                final String phoneNumber = edtxPhone.getText().toString().trim();

                if (email.isEmpty()) {
                    edtxEmail.setError("Email Must Be Filled");
                } else if (name.isEmpty()) {
                    edtxPassword.setError("Name Must Be Filled");
                } else if (password.isEmpty()) {
                    edtxPassword.setError("Password Must Be Filled");
                } else if (confirmPassword.isEmpty()) {
                    edtxConfirmPassword.setError("Confirm Password Must Be Filled");
                } else if (phoneNumber.isEmpty()) {
                    edtxPhone.setError("Phone Number Must Be Filled");
                } else if (!password.equals(confirmPassword)) {
                    edtxPassword.setError("Password Not Match");
                } else {
                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("Register")
                            .setMessage("Are you sure to register ?")
                            .setIcon(R.mipmap.ic_launcher)
                            .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    doRegister(email, name, password, phoneNumber);
                                }
                            })
                            .setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            })
                            .show();
                }
                return true;

            case android.R.id.home:
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void doRegister(String email, String name, String password, String phoneNumber) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressBarHandler.show();
            }
        });
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("email", email)
                .add("password", password)
                .add("name", name)
                .add("ref_id", "")
                .build();
        Request request = new Request.Builder()
                .url(Configs.URL_REST_CLIENT + "register_partner/")
                .post(requestBody)
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
                DialogHelper.showDialog(mHandler, RegisterActivity.this, "Warning", "Connection Problem, Please Try Again Later." + e, false);
            }

            @Override
            public void onResponse(@NonNull okhttp3.Call call, @NonNull Response response) throws IOException {
                String result = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                    }
                });
                if (response.isSuccessful()) {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        if (jsonObject.has("result")) {
                            boolean status = jsonObject.getJSONObject("result").getBoolean("status");
                            if (status) {
                                DialogHelper.showDialog(mHandler, RegisterActivity.this, "Info", "Register Successfully, Please Login", true);
                            } else {
                                String message = jsonObject.getJSONObject("result").getString("message");
                                DialogHelper.showDialog(mHandler, RegisterActivity.this, "Warning", message, false);
                            }
                        } else {
                            DialogHelper.showDialog(mHandler, RegisterActivity.this, "Warning", "Please Try Again", false);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String message;
                        if (jsonObject.has("result")) {
                            message = jsonObject.getJSONObject("result").getString("message");
                        } else {
                            message = jsonObject.getString("message");
                        }
                        DialogHelper.showDialog(mHandler, RegisterActivity.this, "Warning", message, false);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}