package app.kibbeh.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ResetPasswordActivity extends Activity implements View.OnClickListener {

    public Utils utils;
    public EditText edtNewPswrd,edtConfirmPswrd;
    public Button btnResetPswrd;
    public String strNewpswd,strConfirmPswrd;
    public String userEmail;
    public TextView tvHeader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        ImageView btnback;
        tvHeader = (TextView)findViewById(R.id.actionBarTitle);
        tvHeader.setText(getString(R.string.app_name));
        btnback=(ImageView)findViewById(R.id.actionBarBackImage) ;
        utils = new Utils(ResetPasswordActivity.this);
        edtNewPswrd=(EditText)findViewById(R.id.activity_new_password);
        edtConfirmPswrd=(EditText)findViewById(R.id.activity_confirm_password);
        btnResetPswrd=(Button)findViewById(R.id.activity_reset_pswrd);
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        Intent intent = getIntent();
        userEmail=intent.getExtras().getString("email");
        Log.d("userEmail",userEmail);
        init();
    }

    private void init() {

        btnResetPswrd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_reset_pswrd :
                strNewpswd = edtNewPswrd.getText().toString();
                strConfirmPswrd = edtConfirmPswrd.getText().toString();
               if (strNewpswd.length()<5){
                   if (strNewpswd.equals(strConfirmPswrd)){
                       new resetPassword().execute();
                   }else {
                       Toast.makeText(ResetPasswordActivity.this, "Didn't match your both password", Toast.LENGTH_SHORT).show();
                   }
               }else {
                   Toast.makeText(ResetPasswordActivity.this, "please Enter 5 digit password", Toast.LENGTH_SHORT).show();
               }
        }
    }

    private class resetPassword extends AsyncTask<String,String,String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(ResetPasswordActivity.this);
            pd.setMessage("Loading");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //http://web-medico.com/web2/kibbeh/api/v1/reset-password?email=archiryan12@gmail.com&new_password=archiryan12
            String Url = Constant.BASE_URL + "reset-password?email=" +userEmail+"&new_password="+strNewpswd;
            Log.d("resetUrl",Url);

            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (object.getBoolean("status")==true){
                    Intent loginIntent = new Intent(ResetPasswordActivity.this,Login.class);
                    startActivity(loginIntent);
                }else{
                    Toast.makeText(ResetPasswordActivity.this, object.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(ResetPasswordActivity.this, getString(R.string.somethig_went_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
