package com.lynxpardinus.account;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.huawei.agconnect.auth.AGConnectAuth;
import com.huawei.agconnect.auth.AGConnectUser;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.support.hwid.HuaweiIdAuthManager;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParams;
import com.huawei.hms.support.hwid.request.HuaweiIdAuthParamsHelper;
import com.huawei.hms.support.hwid.service.HuaweiIdAuthService;
import com.lynxpardinus.R;

public class LogoutActivity extends AppCompatActivity {
    public static final String TAG = "LogoutActivity";
    private HuaweiIdAuthService mAuthManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }
        HuaweiIdAuthParams mAuthParam = new HuaweiIdAuthParamsHelper(HuaweiIdAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setIdToken()
                .setAccessToken()
                .createParams();
        mAuthManager = HuaweiIdAuthManager.getService(this, mAuthParam);
        findViewById(R.id.logout).setOnClickListener(v -> signOut());
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("accountName");
        editor.remove("accountEmail");
        editor.putBoolean("accountStatus",false);
        editor.apply();
        AGConnectUser agcUserPhone = AGConnectAuth.getInstance().getCurrentUser();
        if (agcUserPhone != null) {
            AGConnectAuth.getInstance().signOut();
        }
        AGConnectUser agcUserEmail = AGConnectAuth.getInstance().getCurrentUser();
        if (agcUserEmail != null) {
            AGConnectAuth.getInstance().signOut();
        }
    }
    private void signOut() {
        Task<Void> signOutTask = mAuthManager.signOut();
        signOutTask.addOnSuccessListener(aVoid -> {
            Log.i(TAG, "signOut Success");
            Toast.makeText(this, "退出发生变化",Toast.LENGTH_LONG).show();
        }).addOnFailureListener(e -> {
            Log.i(TAG, "signOut fail");
        });
    }
}