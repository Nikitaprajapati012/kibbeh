package app.kibbeh.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.Util;
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;

import static android.util.Patterns.EMAIL_ADDRESS;

/**
 * Created by archirayan on 24-Aug-16.
 */
public class Signup2 extends Activity implements View.OnClickListener {

    public Button btnsignupsubmit,btnLogin;
    public TextView tvAvailable;
    public EditText etFirstName, etLastName, etEmail, etPassword,etPhone;
    public String strFirstName, strLastName, strEmail, strPaswword,strPhone;
    public Utils utils;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        utils = new Utils(this);

        tvAvailable = (TextView) findViewById(R.id.tv_available_check);
        btnLogin = (Button) findViewById(R.id.btn_login_signup);
        btnsignupsubmit = (Button) findViewById(R.id.activity_signup_submit);
        etFirstName = (EditText) findViewById(R.id.activity_signup_fullname);
        etLastName = (EditText) findViewById(R.id.activity_signup_email);
        etEmail = (EditText) findViewById(R.id.activity_signup_username);
        etPassword = (EditText) findViewById(R.id.activity_signup_password);
        etPhone = (EditText) findViewById(R.id.activity_signup_phone_no);
        btnLogin.setOnClickListener(this);
        btnsignupsubmit.setOnClickListener(this);

       //init();

    }

    private void init() {
        if (getIntent().getExtras() != null) {
            String data = getIntent().getExtras().getString("Data");
            if (data.equalsIgnoreCase("")) {
                String strAvilable = getString(R.string.available);
                tvAvailable.setText(strAvilable);
            } else {
                String strAvilable = getString(R.string.available) + " " + data;
                tvAvailable.setText(strAvilable);
            }
//            String strAvilable = getString(R.string.available);
//            tvAvailable.setText(strAvilable);
        }



    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login_signup:
                /*
                checkField();*/
                Intent i = new Intent(Signup2.this, Login.class);
                startActivity(i);
                break;

            case R.id.activity_signup_submit:

                strFirstName = etFirstName.getText().toString();
                strLastName = etLastName.getText().toString();
                strEmail = etEmail.getText().toString();
                strPaswword = etPassword.getText().toString();
                strPhone = etPhone.getText().toString();
                checkField();
                break;

        }
    }

    private void checkField() {
        if (strFirstName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "please enter FirstName", Toast.LENGTH_SHORT).show();
        } else if (strLastName.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please enter Lastname", Toast.LENGTH_SHORT).show();
        } else if (strPaswword.length() < 5) {
            Toast.makeText(getApplicationContext(), "please enter atleast 6 diogit password", Toast.LENGTH_SHORT).show();
        } else if (strEmail.isEmpty()) {
            Toast.makeText(getApplicationContext(), "please enter valid email ", Toast.LENGTH_SHORT).show();
        }else if (strPhone.isEmpty()) {
            Toast.makeText(getApplicationContext(), "please enter valid Phone Number ", Toast.LENGTH_SHORT).show();
        }else {
            new register().execute();
        }
    }

    private class register extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Signup2.this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
           // String data = Constant.BASE_URL + "register?email=" + strEmail + "&phone=" + strPhone + "&password" + strPaswword + "&first_name" + strFirstName + "&last_name" + strLastName + "&zipcode" + Util.ReadSharePrefrence(Signup2.this, Constant.ZIPCODE);
          //  Log.e("hello",data);
            return utils.MakeServiceCall(Constant.BASE_URL + "register?email=" + strEmail + "&phone=" + strPhone + "&password=" + strPaswword + "&first_name=" + strFirstName + "&last_name=" + strLastName + "&zipcode=" + Util.ReadSharePrefrence(Signup2.this, Constant.ZIPCODE));
            //http://web-medico.com/web2/kibbeh/api/v1/register?email=sanjay053@gmail.com&phone=7878319818&password=archiryan12&first_name=samir&last_name=lakhani&zipcode=1223
        }


        @Override
        protected void onPostExecute(String s)
        {

            super.onPostExecute(s);
            pd.dismiss();
            Log.d("Response", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true"))
                {
                    Intent i = new Intent(Signup2.this, Login.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }
                else
                {
                    Toast.makeText(Signup2.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e)
            {
                e.printStackTrace();
            }

        }
    }
}



