package cn.edu.scujcc.diandian;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    private final Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case UserLab.USER_LOGIN_SUCCESS:
                    loginSuccess();
                    break;
                case UserLab.USER_LOGIN_FAIL:
                    loginFail();
                    break;
                case UserLab.USER_PASSWORD_FAIL:
                    loginPasswordError();
                    break;
            }
        }
    };
    private TextInputLayout username, password;
    private Button loginButton, registerButton;
    private UserLab lab = UserLab.getInstance();
    private MyPreference prefs = MyPreference.getInstance();

    private void loginSuccess() {
        Toast.makeText(LoginActivity.this, "登录成功！欢迎！", Toast.LENGTH_LONG).show();
        //FIXME  用Star来替换，从服务器获取成功的用户名
        prefs.saveUser("Star");
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void loginFail() {
        Toast.makeText(LoginActivity.this, "登录失败！！！", Toast.LENGTH_LONG).show();
    }

    private void loginPasswordError() {
        Toast.makeText(LoginActivity.this, "登录失败！！！", Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        prefs.setup(getApplicationContext());

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener(v -> {
            String u = username.getEditText().getText().toString();
            String p = password.getEditText().getText().toString();
            lab.login(u, p, handler);
        });

        registerButton = findViewById(R.id.register_button);
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
