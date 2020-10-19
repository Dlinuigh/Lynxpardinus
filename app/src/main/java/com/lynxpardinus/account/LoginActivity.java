package com.lynxpardinus.account;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hmf.tasks.TaskExecutors;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.api.hwid.SignInResult;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;
import com.lynxpardinus.R;
import com.lynxpardinus.selfview.MyEditView;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.hwid_signin).setOnClickListener(v -> {
            signIn();
        });
        MyEditView nickname = findViewById(R.id.nickName);
        nickname.setLeadText("昵称");
        MyEditView phone = findViewById(R.id.accountPhone);
        phone.setLeadText("电话");
        MyEditView email = findViewById(R.id.accountEmail);
        email.setLeadText("邮箱");
        MyEditView code = findViewById(R.id.code);
        code.setLeadText("验证码");
        MyEditView password = findViewById(R.id.password);
        password.setLeadText("可选密码");
        MyEditView passwordAgain = findViewById(R.id.passwordAgain);
        passwordAgain.setLeadText("确认密码");
        findViewById(R.id.nosignup).setOnClickListener(v -> {
            finish();
            Toast.makeText(this, "无变化",Toast.LENGTH_LONG).show();
        });
        findViewById(R.id.email).setOnClickListener(v -> {
            AGConnectAuthCredential credential = EmailAuthProvider.credentialWithVerifyCode("email", null, "verify code");
            AGConnectUser agcUser = AGConnectAuth.getInstance().getCurrentUser();
            if (agcUser != null) {
                agcUser.link(credential)
                        .addOnSuccessListener(TaskExecutors.uiThread(), new OnSuccessListener<SignInResult>() {
                            @Override
                            public void onSuccess(SignInResult signInResult) {

                            }
                        })
                        .addOnFailureListener(TaskExecutors.uiThread(), new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {

                            }
                        });
            }
        });

        /*findViewById(R.id.btn_unlink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AGConnectUser agcUser = AGConnectAuth.getInstance().getCurrentUser();
                if (agcUser != null) {
                    agcUser.unlink(AGConnectAuthCredential.Email_Provider);
                }
            }
        });*/
    }
    private void signIn() {
        HuaweiIdAuthParams mAuthParam = new HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setIdToken()
                .setAccessToken()
                .createParams();
        HuaweiIdAuthService mAuthManager = HuaweiIdAuthManager.getService(LoginActivity.this, mAuthParam);
        startActivityForResult(mAuthManager.getSignInIntent(), Constant.REQUEST_SIGN_IN_LOGIN);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constant.REQUEST_SIGN_IN_LOGIN) {
            //login success
            //get user message by parseAuthResultFromIntent
            Task<AuthHuaweiId> authHuaweiIdTask = HuaweiIdAuthManager.parseAuthResultFromIntent(data);
            if (authHuaweiIdTask.isSuccessful()) {
                AuthHuaweiId huaweiAccount = authHuaweiIdTask.getResult();
                Log.i(TAG, huaweiAccount.getDisplayName() + " signIn success ");
                SharedPreferences.Editor editor = getSharedPreferences("userInfo", MODE_PRIVATE).edit();
                editor.putString("accountName", huaweiAccount.getDisplayName());
                editor.putString("accountEmail", huaweiAccount.getEmail());
                editor.apply();
                Log.i(TAG, "AccessToken: " + huaweiAccount.getAccessToken());
                Toast.makeText(this, "退出发生变化",Toast.LENGTH_LONG).show();
                finish();
            } else {
                Log.i(TAG, "signIn failed: " + ((ApiException) authHuaweiIdTask.getException()).getStatusCode());
                SharedPreferences.Editor editor = getSharedPreferences("userInfo", MODE_PRIVATE).edit();
                editor.putString("accountName", "Template");
                editor.putString("accountEmail", "template@temp.com");
                editor.apply();
                Log.i(TAG, "makeup the account");
                Toast.makeText(this, "退出发生变化",Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}