package com.lynxpardinus.account;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.EmailAuthProvider;
import com.huawei.agconnect.auth.EmailUser;
import com.huawei.agconnect.auth.SignInResult;
import com.huawei.agconnect.auth.VerifyCodeResult;
import com.huawei.agconnect.auth.VerifyCodeSettings;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hmf.tasks.TaskExecutors;
import com.lynxpardinus.R;
import com.lynxpardinus.selfview.MyEditView;

import java.util.Locale;

public class EmailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        MyEditView emailName = findViewById(R.id.EmailName);
        emailName.setLeadText("邮箱");
        MyEditView code = findViewById(R.id.code);
        code.setLeadText("验证码");
        MyEditView Password = findViewById(R.id.password);
        Password.setLeadText("可选密码");
        MyEditView passwordAgain = findViewById(R.id.passwordAgain);
        passwordAgain.setLeadText("确认密码");
        VerifyCodeSettings settings = VerifyCodeSettings.newBuilder()
                .action(VerifyCodeSettings.ACTION_REGISTER_LOGIN)
                .sendInterval(30) //shortest send interval ，30-120s
                .locale(Locale.SIMPLIFIED_CHINESE) //optional,must contain country and language eg:zh_CN
                .build();
        findViewById(R.id.sendEmailCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task<VerifyCodeResult> task = EmailAuthProvider.requestVerifyCode(emailName.getText().toString(), settings);
                task.addOnSuccessListener(TaskExecutors.uiThread(), new OnSuccessListener<VerifyCodeResult>() {
                    @Override
                    public void onSuccess(VerifyCodeResult verifyCodeResult) {
                        //You need to get the verification code from your email
                    }
                }).addOnFailureListener(TaskExecutors.uiThread(), new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(EmailActivity.this, emailName.getText().toString()+"requestVerifyCode fail:" + e, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        findViewById(R.id.regEmail).setOnClickListener(v -> {
            EmailUser emailUser = new EmailUser.Builder()
                    .setEmail(emailName.getText().toString())
                    .setPassword(Password.getText().toString())//optional
                    .setVerifyCode(code.getText().toString())
                    .build();
            AGConnectAuth.getInstance().createUser(emailUser)
                    .addOnSuccessListener(new OnSuccessListener<SignInResult>() {
                        @Override
                        public void onSuccess(SignInResult signInResult) {
                            Toast.makeText(EmailActivity.this, "create user success",Toast.LENGTH_LONG).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(EmailActivity.this, emailName.getText().toString()+"createUser fail:" + e, Toast.LENGTH_SHORT).show();
                        }
                    });

        });

        findViewById(R.id.email).setOnClickListener(v -> {
            AGConnectAuthCredential credential ;
            if (TextUtils.isEmpty(code.getText().toString())) {
                credential = EmailAuthProvider.credentialWithPassword(emailName.getText().toString(), Password.getText().toString());
            } else {
                //If you do not have a password, param password can be null
                credential = EmailAuthProvider.credentialWithVerifyCode(emailName.getText().toString(), Password.getText().toString(), code.getText().toString());
            }
            /*
            AGConnectUser agcUser = AGConnectAuth.getInstance().getCurrentUser();
            if (agcUser != null) {
                agcUser.link(credential)*/
                        AGConnectAuth.getInstance().signIn(credential).addOnSuccessListener(TaskExecutors.uiThread(), signInResult -> {
                    String nickName = getIntent().getStringExtra("nickName");
                    SharedPreferences.Editor editor = getSharedPreferences("userInfo", MODE_PRIVATE).edit();
                    editor.putBoolean("accountStatus",true);
                    if(!nickName.equals("")){
                        editor.putString("accountName", nickName);
                    }
                    editor.putString("accountEmail", emailName.getText().toString());
                    editor.apply();
                    Toast.makeText(EmailActivity.this, "退出发生变化",Toast.LENGTH_LONG).show();
                    finish();
                })
                        .addOnFailureListener(TaskExecutors.uiThread(), e -> {
                            Toast.makeText(EmailActivity.this, "登录失败"+e.toString(), Toast.LENGTH_LONG).show();
                        });
            //}
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
}