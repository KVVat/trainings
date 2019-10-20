package com.example.udacity.surfconnect;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.FacebookSdk;
import com.facebook.accountkit.AccessToken;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    public static int APP_REQUEST_CODE=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FontHelper.setCustomTypeface(findViewById(R.id.view_root));

        FacebookSdk.sdkInitialize(getApplicationContext());
        Log.d("AppLog", "key:" + FacebookSdk.getApplicationSignature(this));

        AccessToken accessToken = AccountKit.getCurrentAccessToken();
        if(accessToken != null){
            launchAccountActivity();
        }
    }

    private void onLogin(final LoginType loginType){
        final Intent intent = new Intent(this,AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder cb =
        new AccountKitConfiguration.AccountKitConfigurationBuilder(
                loginType,
                AccountKitActivity.ResponseType.TOKEN
        );
        final AccountKitConfiguration config= cb.build();
        intent.putExtra(AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,config);
        startActivityForResult(intent,APP_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == APP_REQUEST_CODE){
            AccountKitLoginResult loginResult = data.getParcelableExtra
                    (AccountKitLoginResult.RESULT_KEY);
            if(loginResult.getError() != null){
                String m = loginResult.getError().getErrorType().getMessage();
                Toast.makeText(this,m,Toast.LENGTH_LONG).show();
            } else {
                launchAccountActivity();
            }
        }
    }

    public void onPhoneLogin(View view){
        onLogin(LoginType.PHONE);
    }
    public void onEmailLogin(View view){
        onLogin(LoginType.EMAIL);
    }
    private void launchAccountActivity() {
        Intent intent = new Intent(this, AccountActivity.class);
        startActivity(intent);
        finish();
    }

}
