package com.mycillin.partner.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.mycillin.partner.R;
import com.mycillin.partner.util.SessionManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountActivity extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.nav_account);

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
                showChangePasswordDialog();
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


}
