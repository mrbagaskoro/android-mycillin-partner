package com.mycillin.partner.activity;

import android.content.DialogInterface;
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
import com.mycillin.partner.restful.PartnerAPI;
import com.mycillin.partner.restful.RestClient;
import com.mycillin.partner.restful.register.ModelRestRegister;
import com.mycillin.partner.util.DialogHelper;
import com.mycillin.partner.util.ProgressBarHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        mPartnerApi = RestClient.getPartnerRestInterfaceNoToken();

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
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void doRegister(String email, String name, String password, String phoneNumber) {
        mProgressBarHandler.show();
/*
        HashMap<String, String> params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
        params.put("name", name);
        params.put("ref_id", "");
        params.put("telephone", phoneNumber);
*/
        mPartnerApi.doRegister(email, password, name, "", phoneNumber).enqueue(new Callback<ModelRestRegister>() {
            @Override
            public void onResponse(@NonNull Call<ModelRestRegister> call, @NonNull Response<ModelRestRegister> response) {
                mProgressBarHandler.hide();
                if (response.isSuccessful()) {
                    ModelRestRegister modelRestRegister = response.body();

                    assert modelRestRegister != null;
                    if (modelRestRegister.getResult().isStatus()) {
                        DialogHelper.showDialog(mHandler, RegisterActivity.this, "Info", "Register Successfully, Please Login", true);
                    }
                } else {
                    ModelRestRegister modelRestRegister = response.body();
                    assert modelRestRegister != null;
                    String errorMessage = modelRestRegister.getResult().getMessage();
                    DialogHelper.showDialog(mHandler, RegisterActivity.this, "Warning", errorMessage, false);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ModelRestRegister> call, @NonNull Throwable t) {
                DialogHelper.showDialog(mHandler, RegisterActivity.this, "Warning", "Connection Problem, Please Try Again Later.", false);
            }
        });

    }
}
