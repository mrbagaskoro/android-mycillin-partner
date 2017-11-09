package com.mycillin.partner.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.mycillin.partner.R;
import com.mycillin.partner.util.Configs;
import com.mycillin.partner.util.DialogHelper;
import com.mycillin.partner.util.ProgressBarHandler;
import com.mycillin.partner.util.SessionManager;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AccountActivity extends AppCompatActivity {

    private final String EXTRA_STATUS_HOUSE_VISIT = "visit_id";
    private final String EXTRA_STATUS_RESERVASI = "reservasi_id";
    private final String EXTRA_STATUS_CONSULTATION = "consul_id";
    private final String EXTRA_STATUS_BPJS = "BPJS_RCV_status";
    @BindView(R.id.accountActivity_ll_manageAccount)
    LinearLayout manageAccount;
    @BindView(R.id.accountActivity_ll_changePassword)
    LinearLayout changePassword;
    @BindView(R.id.accountActivity_ll_signOut)
    LinearLayout signOut;
    @BindView(R.id.accountActivity_ll_termsPrivacyPolicy)
    LinearLayout termsAndPrivacyPolicy;
    @BindView(R.id.accountActivity_toolbar)
    Toolbar toolbar;
    @BindView(R.id.accountActivity_tv_userName)
    TextView tvDocterName;
    @BindView(R.id.accountActivity_tb_houseVisit)
    ToggleButton tbHouseVisit;
    @BindView(R.id.accountActivity_tb_reservation)
    ToggleButton tbreservation;
    @BindView(R.id.accountActivity_tb_consultation)
    ToggleButton tbConsultation;
    @BindView(R.id.accountActivity_tb_bpjs)
    ToggleButton tbBpjs;
    private SessionManager sessionManager;
    private CircleImageView ivAvatar;
    private Handler mHandler;
    private ProgressBarHandler mProgressBarHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.nav_account);
        sessionManager = new SessionManager(this);
        tvDocterName.setText(sessionManager.getUserFullName());

        mHandler = new Handler(Looper.getMainLooper());
        mProgressBarHandler = new ProgressBarHandler(this);

        fillDoctorAvatar();
        manageAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, AccountDetailActivity.class);
                startActivity(intent);
            }
        });
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, UpdatePassActivity.class);
                startActivity(intent);
            }
        });
        termsAndPrivacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccountActivity.this, TermAndPrivacyPolicyActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fillDoctorAvatar() {
        ivAvatar = findViewById(R.id.accountActivity_iv_userAvatar);

//// TODO: 05/11/2017 FROM SERVICE
        Picasso.with(getApplicationContext())
                .load("https://upload.wikimedia.org/wikipedia/commons/thumb/c/ce/Bill_Gates_in_WEF%2C_2007.jpg/220px-Bill_Gates_in_WEF%2C_2007.jpg")
               /* .transform(new RoundedTransformation(80, 0))*/
                .resize(150, 150)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .centerCrop()
                .into(ivAvatar);
    }

    public void showChangePasswordDialog() {
        /*final DialogPlus dialogPlus = DialogPlus.newDialog(AccountActivity.this)
                .setContentHolder(new ViewHolder(R.layout.dialog_change_password_layout))
                .setGravity(Gravity.CENTER)
                .create();
        dialogPlus.show();

        View dialogPlusView = dialogPlus.getHolderView();*/

        final Dialog dialog = new Dialog(AccountActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_change_password_layout);

        final EditText edtxOldPassword = dialog.findViewById(R.id.changePasswordDialog_et_oldPassword);
        final EditText edtxNewPassword = dialog.findViewById(R.id.changePasswordDialog_et_newPassword);
        final EditText edtxConfirmPassword = dialog.findViewById(R.id.changePasswordDialog_et_confirmNewPassword);
        Button btnCancel = dialog.findViewById(R.id.accountActivity_bt_cancelBtn);
        Button btnConfirm = dialog.findViewById(R.id.accountActivity_bt_applyBtn);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean isValid = true;
                if (edtxOldPassword.getText().toString().trim().equals("")) {
                    edtxOldPassword.setError(getString(R.string.loginActivity_passwordWarning));
                    isValid = false;
                }
                if (edtxNewPassword.getText().toString().trim().equals("")) {
                    edtxNewPassword.setError(getString(R.string.loginActivity_passwordWarning));
                    isValid = false;
                }
                if (edtxConfirmPassword.getText().toString().trim().equals("")) {
                    edtxConfirmPassword.setError(getString(R.string.loginActivity_passwordConfirmationWarning));
                    isValid = false;
                }
                if (!edtxConfirmPassword.getText().toString().trim().equals(edtxNewPassword.getText().toString().trim())) {
                    edtxConfirmPassword.setError(getString(R.string.loginActivity_passwordMatchWarning));
                    isValid = false;
                }

                if (isValid) {
                    doChangePassword(edtxOldPassword.getText().toString(), edtxNewPassword.getText().toString());
                }
            }
        });
        dialog.show();
        Window window = dialog.getWindow();
        assert window != null;
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private void doChangePassword(String oldPassWord, String newPassWord) {


    }


    @OnClick(R.id.accountActivity_ll_signOut)
    public void signOutClicked() {
        SessionManager sessionManager = new SessionManager(getApplicationContext());
        sessionManager.logoutUser();
        Intent intent = new Intent(AccountActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @OnCheckedChanged(R.id.accountActivity_tb_houseVisit)
    public void doHomeVisit() {
        boolean isActive = tbHouseVisit.isChecked();
        String value = isActive ? "0" : "1";
        doToggleUpdate(value, EXTRA_STATUS_HOUSE_VISIT);
    }

    @OnCheckedChanged(R.id.accountActivity_tb_consultation)
    public void doConsultation() {
        boolean isActive = tbConsultation.isChecked();
        String value = isActive ? "0" : "1";
        doToggleUpdate(value, EXTRA_STATUS_CONSULTATION);

    }

    @OnCheckedChanged(R.id.accountActivity_tb_reservation)
    public void doReservation() {
        boolean isActive = tbreservation.isChecked();
        String value = isActive ? "0" : "1";
        doToggleUpdate(value, EXTRA_STATUS_RESERVASI);
    }

    @OnCheckedChanged(R.id.accountActivity_tb_bpjs)
    public void doBpjs() {
        boolean isActive = tbBpjs.isChecked();
        String value = isActive ? "0" : "1";
        doToggleUpdate(value, EXTRA_STATUS_BPJS);
    }

    private void doToggleUpdate(final String value, final String status) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mProgressBarHandler.show();
            }
        });
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        Map<String, Object> data = new HashMap<>();
        data.put("user_id", sessionManager.getUserId());
        data.put("status", status);
        data.put("value", value);

        JSONObject jsonObject = new JSONObject(data);

        Log.d("####", "saveAddress: OBJEK " + jsonObject);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, jsonObject.toString());
        Request request = new Request.Builder()
                .url(Configs.URL_REST_CLIENT + "toggle_status_partner/")
                .post(body)
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

                        switch (status) {
                            case EXTRA_STATUS_HOUSE_VISIT:
                                if (value.equals("0")) {
                                    tbHouseVisit.setChecked(false);
                                } else {
                                    tbHouseVisit.setChecked(true);
                                }
                                break;
                            case EXTRA_STATUS_RESERVASI:
                                if (value.equals("0")) {
                                    tbreservation.setChecked(false);
                                } else {
                                    tbreservation.setChecked(true);
                                }
                                break;
                            case EXTRA_STATUS_CONSULTATION:
                                if (value.equals("0")) {
                                    tbConsultation.setChecked(false);
                                } else {
                                    tbConsultation.setChecked(true);
                                }
                                break;
                            case EXTRA_STATUS_BPJS:
                                if (value.equals("0")) {
                                    tbBpjs.setChecked(false);
                                } else {
                                    tbBpjs.setChecked(true);
                                }
                                break;
                        }
                    }
                });
                DialogHelper.showDialog(mHandler, AccountActivity.this, "Warning", "Connection Problem, Please Try Again Later." + e, false);
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) throws IOException {
                final String x = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mProgressBarHandler.hide();
                        DialogHelper.showDialog(mHandler, AccountActivity.this, "Info", EXTRA_STATUS_HOUSE_VISIT + " Updated", false);
                        if (response.isSuccessful()) {
                            switch (status) {
                                case EXTRA_STATUS_HOUSE_VISIT:
                                    if (value.equals("0")) {
                                        tbHouseVisit.setChecked(true);
                                    } else {
                                        tbHouseVisit.setChecked(false);
                                    }
                                    break;
                                case EXTRA_STATUS_RESERVASI:
                                    if (value.equals("0")) {
                                        tbreservation.setChecked(true);
                                    } else {
                                        tbreservation.setChecked(false);
                                    }
                                    break;
                                case EXTRA_STATUS_CONSULTATION:
                                    if (value.equals("0")) {
                                        tbConsultation.setChecked(true);
                                    } else {
                                        tbConsultation.setChecked(false);
                                    }
                                    break;
                                case EXTRA_STATUS_BPJS:
                                    if (value.equals("0")) {
                                        tbBpjs.setChecked(true);
                                    } else {
                                        tbBpjs.setChecked(false);
                                    }
                                    break;
                            }
                        } else {
                            switch (status) {
                                case EXTRA_STATUS_HOUSE_VISIT:
                                    if (value.equals("0")) {
                                        tbHouseVisit.setChecked(false);
                                    } else {
                                        tbHouseVisit.setChecked(true);
                                    }
                                    break;
                                case EXTRA_STATUS_RESERVASI:
                                    if (value.equals("0")) {
                                        tbreservation.setChecked(false);
                                    } else {
                                        tbreservation.setChecked(true);
                                    }
                                    break;
                                case EXTRA_STATUS_CONSULTATION:
                                    if (value.equals("0")) {
                                        tbConsultation.setChecked(false);
                                    } else {
                                        tbConsultation.setChecked(true);
                                    }
                                    break;
                                case EXTRA_STATUS_BPJS:
                                    if (value.equals("0")) {
                                        tbBpjs.setChecked(false);
                                    } else {
                                        tbBpjs.setChecked(true);
                                    }
                                    break;
                            }
                            DialogHelper.showDialog(mHandler, AccountActivity.this, "Warning", "Please Try Again", false);
                        }
                    }
                });
            }
        });
    }
}
