package project.test.com.myapplication;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/**
 * Created by 子云心 http://blog.csdn.net/lyz_zyx
 */
public class AuthenticatorActivity extends AppCompatActivity {
    // TYPE必须与account_preferences.xml中的TYPE保持一致
    public static final String ACCOUNT_TYPE = "project.test.com.myapplication.account.type";
    private AccountManager mAccountManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticator);

        mAccountManager = (AccountManager)getSystemService(ACCOUNT_SERVICE);
        // 获取系统帐户列表中是否存在我们的帐户，用TYPE做为标识
        Account[] accounts = mAccountManager.getAccountsByType(ACCOUNT_TYPE);
        if (accounts.length > 0) {
            Toast.makeText(this, "已添加当前登录的帐户", Toast.LENGTH_SHORT).show();
            finish();
        }

        Button btnAddAccount = (Button)findViewById(R.id.btn_add_account);
        btnAddAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Account account = new Account(getString(R.string.app_name), ACCOUNT_TYPE);
                mAccountManager.addAccountExplicitly(account, null, null);                          // 帐户密码和信息这里用null演示
                // 自动同步
                Bundle bundle = new Bundle();
                ContentResolver.setIsSyncable(account, AccountProvider.AUTHORITY, 1);
                ContentResolver.setSyncAutomatically(account, AccountProvider.AUTHORITY, true);
                ContentResolver.addPeriodicSync(account, AccountProvider.AUTHORITY, bundle, 30);    // 间隔时间为30秒

                // 手动同步
                // ContentResolver.requestSync(account, AccountProvider.AUTHORITY, bundle);

                finish();
            }
        });
    }
}
