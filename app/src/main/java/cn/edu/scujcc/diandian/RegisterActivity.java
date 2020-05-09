package cn.edu.scujcc.diandian;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

public class RegisterActivity extends AppCompatActivity {
    private final static String TAG = "DianDian";
    private TextInputLayout birthdayInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        birthdayInput = findViewById(R.id.r_birthday);
        birthdayInput.setEndIconOnClickListener(v -> {
            //弹出日历框
            Log.d(TAG, "生日被点击了");
            //builder 可理解为装修工
            MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
            //告诉builder我们想要的效果。
            builder.setTitleText(R.string.birthday_title);
            //告诉完了 开工,pick就是我们想得到的东西
            MaterialDatePicker picker = builder.build();
            picker.show(getSupportFragmentManager(), picker.toString());
            //s即为用户选中的日期
            picker.addOnPositiveButtonClickListener(s -> {
                Log.d(TAG, "标题是：" + picker.getHeaderText());
                Log.d(TAG, "日期结果是：" + s);
                birthdayInput.getEditText().setText(picker.getHeaderText());
            });
        });
    }
}
