package app.kibbeh.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;

/**
 * Created by archi on 10/5/2016.
 */

public class ForgetPassword extends Activity implements View.OnClickListener {
    public Utils utils;
    public Button btnSubmit;
    public EditText edtLoginEmail;
    public String strLoginEmail;
    public TextView tvHeader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ImageView btnback = (ImageView)findViewById(R.id.actionBarBackImage);
        utils = new Utils(ForgetPassword.this);
        edtLoginEmail=(EditText)findViewById(R.id.activity_login_email);
        btnSubmit=(Button)findViewById(R.id.activity_login_submit);
        tvHeader = (TextView)findViewById(R.id.actionBarTitle);
        tvHeader.setText(getString(R.string.app_name));
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


        init();

    }

    private void init() {

        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_login_submit:
               strLoginEmail=edtLoginEmail.getText().toString().trim();
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(strLoginEmail).matches()){
                    new forgetPassword().execute();
                }else {
                    Toast.makeText(ForgetPassword.this, "please Enter valid Email_ID", Toast.LENGTH_SHORT).show();
                }
        }
    }


    private class forgetPassword extends AsyncTask<String,String,String>{

        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd= new ProgressDialog(ForgetPassword.this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

         //   "http://web-medico.com/web2/kibbeh/api/v1/forgot-password?email=archiryan12@gmail.com";
            String Url = Constant.BASE_URL +"forgot-password?email="+ strLoginEmail;
            Log.d("forgetpswd",Url);
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject =new JSONObject(s);
                if (jsonObject.getBoolean("status")==true){
                    //int otpCode = jsonObject.getInt("code");
                     String otpCode = jsonObject.getString("code");
                    Intent otpIntent = new Intent(ForgetPassword.this,OtpActivity.class);

                    otpIntent.putExtra("otp",otpCode);
                    otpIntent.putExtra("useremail",strLoginEmail);

                    startActivity(otpIntent);
                    otpIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);


                }else {
                    Toast.makeText(ForgetPassword.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }
                pd.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ForgetPassword.this, getString(R.string.somethig_went_wrong), Toast.LENGTH_SHORT).show();
            }

        }
    }
}
