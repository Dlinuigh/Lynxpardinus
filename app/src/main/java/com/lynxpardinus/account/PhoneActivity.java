package com.lynxpardinus.account;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectAuthCredential;
import com.huawei.agconnect.auth.PhoneAuthProvider;
import com.huawei.agconnect.auth.PhoneUser;
import com.huawei.agconnect.auth.VerifyCodeResult;
import com.huawei.agconnect.auth.VerifyCodeSettings;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hmf.tasks.TaskExecutors;
import com.lynxpardinus.R;
import com.lynxpardinus.selfview.MyEditView;

import java.util.Locale;

public class PhoneActivity extends AppCompatActivity {

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.activity_phone);
        MyEditView Phone = findViewById(R.id.PhoneName);
        Phone.setLeadText("电话");
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
        findViewById(R.id.sendPhoneCode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task<VerifyCodeResult> task = PhoneAuthProvider.requestVerifyCode("+86", Phone.getText().toString(), settings);
                task.addOnSuccessListener(TaskExecutors.uiThread(), new OnSuccessListener<VerifyCodeResult>() {
                    @Override
                    public void onSuccess(VerifyCodeResult verifyCodeResult) {
                        //You need to get the verification code from your phone
                    }
                }).addOnFailureListener(TaskExecutors.uiThread(), new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(PhoneActivity.this, Phone.getText().toString()+"requestVerifyCode fail:" + e, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        findViewById(R.id.phone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*AGConnectAuthCredential credential = PhoneAuthProvider.credentialWithVerifyCode(
                        "+86",
                        Phone.getText().toString(),
                        Password.getText().toString(), // password, can be null
                        );*/
                AGConnectAuthCredential credential;
                if (TextUtils.isEmpty(code.getText().toString())) {
                    credential = PhoneAuthProvider.credentialWithPassword("+86", Phone.getText().toString(), Password.getText().toString());
                } else {
                    //If you do not have a password, param password can be null
                    credential = PhoneAuthProvider.credentialWithVerifyCode("+86", Phone.getText().toString(), Password.getText().toString(), code.getText().toString());
                }
                /*AGConnectUser agcUser = AGConnectAuth.getInstance().getCurrentUser();
                if (agcUser != null) {
                    agcUser.link(credential)*/
                            AGConnectAuth.getInstance().signIn(credential)
                                    .addOnSuccessListener(TaskExecutors.uiThread(), signInResult -> {
                                        String nickName = getIntent().getStringExtra("nickName");
                                        SharedPreferences.Editor editor = getSharedPreferences("userInfo", MODE_PRIVATE).edit();
                                        editor.putBoolean("accountStatus",true);
                                        if(!nickName.equals("")){
                                            editor.putString("accountName", nickName);
                                        }
                                        editor.putString("accountEmail", Phone.getText().toString());
                                        editor.apply();
                                        Toast.makeText(PhoneActivity.this, "退出发生变化",Toast.LENGTH_LONG).show();
                                        PhoneActivity.this.finish();
                                    })
                            .addOnFailureListener(TaskExecutors.uiThread(), e -> Toast.makeText(context, "失败"+e.toString(),Toast.LENGTH_LONG).show());
                }
            //}
        });
        findViewById(R.id.regPhone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneUser phoneUser = new PhoneUser.Builder()
                        .setCountryCode("+86")
                        .setPhoneNumber(Phone.getText().toString())
                        .setPassword(Password.getText().toString())//optional
                        .setVerifyCode(code.getText().toString())
                        .build();
                AGConnectAuth.getInstance().createUser(phoneUser)
                        .addOnSuccessListener(signInResult -> Toast.makeText(PhoneActivity.this, "success", Toast.LENGTH_SHORT).show())
                        .addOnFailureListener(e -> Toast.makeText(PhoneActivity.this, "createUser fail:" + e, Toast.LENGTH_SHORT).show());
            }
        });
        /*findViewById(R.id.btn_unlink).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AGConnectUser agcUser = AGConnectAuth.getInstance().getCurrentUser();
                if (agcUser != null) {
                    agcUser.unlink(AGConnectAuthCredential.Phone_Provider);
                }
            }
        });*/
    }
}