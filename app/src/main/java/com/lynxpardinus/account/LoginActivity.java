package com.lynxpardinus.account;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper;
import com.huawei.hms.support.hwid.result.AuthHuaweiId;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;
import com.lynxpardinus.R;
import com.lynxpardinus.selfview.MyEditView;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "LoginActivity";
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_login);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        MyEditView nickname = findViewById(R.id.nickName);
        nickname.setLeadText("昵称");
        findViewById(R.id.hwid_signin).setOnClickListener(v -> {
            signIn();
        });
        findViewById(R.id.nosignup).setOnClickListener(v -> {
            finish();
            Toast.makeText(this, "无变化",Toast.LENGTH_LONG).show();
        });
        findViewById(R.id.phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Pintent = new Intent(context, PhoneActivity.class);
                Pintent.putExtra("nickName",nickname.getText().toString());
                startActivity(Pintent);
            }
        });
        findViewById(R.id.email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Eintent = new Intent(context, EmailActivity.class);
                Eintent.putExtra("nickName",nickname.getText().toString());
                startActivity(Eintent);
            }
        });
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
                editor.putBoolean("accountStatus",true);
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
                editor.putBoolean("accountStatus",false);
                editor.apply();
                Log.i(TAG, "makeup the account");
                Toast.makeText(this, "登录失败，下次运行自动启动登录页面",Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}