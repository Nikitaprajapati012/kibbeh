package app.kibbeh.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.Util;
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;

/**
 * Created by archirayan on 24-Aug-16.
 */
public class PinCodeActivity extends Activity implements View.OnClickListener {
    public EditText pincodeEdt;
    public Button getStartedBtn;
    public Utils utils;
    public String pincode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_pincode);
        utils = new Utils(PinCodeActivity.this);
        getStartedBtn = (Button) findViewById(R.id.activity_signup_pincode_get_started);
        pincodeEdt = (EditText) findViewById(R.id.activity_signup_fullname);
        getStartedBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_signup_pincode_get_started:
                if (utils.isConnectingToInternet()) {
                    pincode = pincodeEdt.getText().toString();
                    Util.WriteSharePrefrence(PinCodeActivity.this,Constant.ZIPCODE,pincode);
                    if (pincode.length() < 5) {
                        Toast.makeText(getApplicationContext(), "please enter Atleast 6 character pin", Toast.LENGTH_SHORT).show();
                    } else {
                        new getData().execute(pincode);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                }


                break;

        }
    }

    private class getData extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(PinCodeActivity.this);
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String pincode = params[0];
           /* HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("zipcode", pincode);*/

            Utils utils = new Utils(PinCodeActivity.this);
            //http://web-medico.com/web2/kibbeh/api/v1/get_started?zipcode=22201
            return utils.MakeServiceCall(Constant.BASE_URL + "get_started?zipcode=" + pincode);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            String data = null;
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("status") == true) {

                    data = jsonObject.getString("data");
                    Utils.WriteSharePrefrence(PinCodeActivity.this, Constant.ZipCode, data);
                    Intent i = new Intent(PinCodeActivity.this, Signup2.class);
                    i.putExtra("Data", data);
                    startActivity(i);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            pd.dismiss();
        }

    }
}
