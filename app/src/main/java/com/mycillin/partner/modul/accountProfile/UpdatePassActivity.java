package com.mycillin.partner.modul.accountProfile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.mycillin.partner.R;
import com.mycillin.partner.util.Configs;
import com.mycillin.partner.util.DialogHelper;
import com.mycillin.partner.util.ProgressBarHandler;
import com.mycillin.partner.util.SessionManager;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UpdatePassActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.updatePassActivity_ET_oldpassword)
    EditText edtxOldPass;
    @BindView(R.id.updatePassActivity_ET_newpassword)
    EditText edtxNewPass;
    @BindView(R.id.updatePassActivity_ET_confirmpassword)
    EditText edtxConfirmPassword;

    private Handler mHandler;
    private ProgressBarHandler mProgressBarHandler;
    private SessionManager sessionManager;

    public UpdatePassActivity() {
        //Empty Constructor
    }

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_update_password);
        ButterKnife.bind(this);
        mHandler = new Handler(Looper.getMainLooper());
        mProgressBarHandler = new ProgressBarHandler(this);
        sessionManager = new SessionManager(getApplicationContext());

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
                final String oldPassword = edtxOldPass.getText().toString().trim();
                final String newPassword = edtxNewPass.getText().toString().trim();
                final String confirmPassword = edtxConfirmPassword.getText().toString().trim();

                if (oldPassword.isEmpty()) {
                    edtxOldPass.setError("Old Password Must Be Filled");
                } else if (newPassword.isEmpty()) {
                    edtxNewPass.setError("New Password Must Be Filled");
                } else if (confirmPassword.isEmpty()) {
                    edtxConfirmPassword.setError("Confirm Password Must Be Filled");
                } else if (!oldPassword.equals(sessionManager.getUserPass())) {
                    DialogHelper.showDialog(mHandler, UpdatePassActivity.this, "Error", "Old Password Not Match", false);
                } else if (!newPassword.equals(confirmPassword)) {
                    edtxConfirmPassword.setText("");
                    edtxConfirmPassword.setError("Password Not Match");
                } else {
                    new AlertDialog.Builder(UpdatePassActivity.this)
                            .setTitle("Register")
                            .setMessage("Are you sure to change password ?")
                            .setIcon(R.mipmap.ic_launcher)
                            .setPositiveButton("Sure", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    doUpdatePass(oldPassword, newPassword);
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
                Intent intent = new Intent(UpdatePassActivity.this, AccountDetailActivity.class);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void doUpdatePass(String oldPassword, String newPassword) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressBarHandler.show();
            }
        });
        OkHttpClient client = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("user_id", sessionManager.getUserId())
                .add("old_password", oldPassword)
                .add("new_password", newPassword)
                .build();
        Request request = new Request.Builder()
                .url(Configs.URL_REST_CLIENT + "change_password_partner/")
                .post(requestBody)
                .addHeader("content-type", "application/json; charset=utf-8")
                .addHeader("Authorization", sessionManager.getUserToken())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                    }
                });
                DialogHelper.showDialog(mHandler, UpdatePassActivity.this, "Error", "Connection problem " + e, false);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                DialogHelper.showDialog(mHandler, UpdatePassActivity.this, "Status", response.body().string(), false);
            }
        });
    }
}
