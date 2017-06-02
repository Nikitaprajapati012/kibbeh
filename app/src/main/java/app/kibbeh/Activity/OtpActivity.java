package app.kibbeh.Activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.PublicKey;

import app.kibbeh.Constant.Utils;
import app.kibbeh.R;

public class OtpActivity extends Activity implements View.OnClickListener {

    public Utils utils;
    public Button btnOptSubmit;
    public EditText edtOtpnumber;
    public String otpCode;
    public String otp,userEmail;
    public TextView tvHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        tvHeader = (TextView)findViewById(R.id.actionBarTitle);
        tvHeader.setText(getString(R.string.app_name));


        utils = new Utils(OtpActivity.this);
        ImageView btnback;
        btnback=(ImageView)findViewById(R.id.actionBarBackImage) ;
        edtOtpnumber = (EditText)findViewById(R.id.activity_otp_code);
        btnOptSubmit = (Button)findViewById(R.id.activity_btn_submit);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        Intent otpIntent = getIntent();
        userEmail=otpIntent.getExtras().getString("useremail");
        otp = otpIntent.getExtras().getString("otp");
        Log.d("otp",otp);


        init();
    }

    private void init() {
        btnOptSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.activity_btn_submit:
                otpCode=edtOtpnumber.getText().toString();
                Log.d("otpUser",otpCode);
                if (otpCode.equals(otp)){

                    Intent intent = new Intent(OtpActivity.this,ResetPasswordActivity.class);
                    intent.putExtra("email",userEmail);
                    startActivity(intent);
                    Log.d("email",userEmail);
                }
                else
                {
                    Toast.makeText(this, "Something went Wrong", Toast.LENGTH_SHORT).show();
                }



        }

    }
}
