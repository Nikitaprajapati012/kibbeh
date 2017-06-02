package app.kibbeh.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
 * Created by archirayan on 14-Oct-16.
 */

public class AccountMain extends Activity implements View.OnClickListener {
    public TextView tvHeader, tvChangeEmail, tvChangePassword, tvEmailChange;
    public ImageView ivBack;
    private EditText etFname, etLname, etPhone;
    public Utils utils;
    public Button btSaveInfo;
    private String fNameStr,lNameStr, phoneStr, newEmailStr, newPasswordStr;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        (findViewById(R.id.actionBarBackImage)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.actionBarTitle)).setText("Account");
        utils = new Utils(AccountMain.this);
        tvHeader = (TextView) findViewById(R.id.actionBarTitle);
        tvHeader.setText(R.string.account);
        ivBack = (ImageView) findViewById(R.id.actionBarBackImage);
        etFname = (EditText) findViewById(R.id.et_account_firt_name);
        etLname = (EditText) findViewById(R.id.et_account_last_name);
        etPhone = (EditText) findViewById(R.id.et_account_phone);
        btSaveInfo = (Button) findViewById(R.id.btn_account_save_info);
        tvChangeEmail = (TextView) findViewById(R.id.tvChangeEmail);
        tvChangePassword = (TextView) findViewById(R.id.tvChangePassword);
        tvEmailChange = (TextView) findViewById(R.id.tvEmailChange);

        btSaveInfo.setOnClickListener(this);
        tvChangeEmail.setOnClickListener(this);
        tvChangePassword.setOnClickListener(this);
        if(utils.isConnectingToInternet()){
            new getAccountDetails().execute();
        }else{
            Toast.makeText(AccountMain.this,"No internet connection found, please check your internet connection", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_account_save_info:
                if(!etFname.getText().toString().equalsIgnoreCase("")){
                    if(!etLname.getText().toString().equalsIgnoreCase("")){
                        if(!etPhone.getText().toString().equalsIgnoreCase("")){
                            fNameStr = etFname.getText().toString();
                            lNameStr = etLname.getText().toString();
                            phoneStr = etPhone.getText().toString();
                            new saveAccountDetails().execute();
                        }else{
                            Toast.makeText(getApplicationContext(), "Please Enter Phone Number", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(), "Please Enter Last Name", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Please Enter First Name", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.tvChangeEmail:
                    showChangeEmailPopup();
                break;

            case R.id.tvChangePassword:
                showChangePasswordPopup();
                break;

        }

    }


    private void showChangePasswordPopup() {
        final Dialog dialog = new Dialog(AccountMain.this);

        dialog.setContentView(R.layout.popup_change_password);
        final EditText editText = (EditText) dialog.findViewById(R.id.etNewPassword);
        Button btnSave  = (Button) dialog.findViewById(R.id.btOkPassword);
        Button btnCancel = (Button) dialog.findViewById(R.id.btCancelPassword);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().equalsIgnoreCase("")){
                    newPasswordStr = editText.getText().toString();
                    new changePassword().execute();
                    dialog.dismiss();
                }else{
                    Toast.makeText(AccountMain.this, "Please Enter New Password", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }



    private void showChangeEmailPopup() {
        final Dialog dialog = new Dialog(AccountMain.this);

        dialog.setContentView(R.layout.popup_change_email);

        final EditText editText = (EditText) dialog.findViewById(R.id.etNewEmail);
        Button btnSave  = (Button) dialog.findViewById(R.id.btOk);
        Button btnCancel = (Button) dialog.findViewById(R.id.btCancel);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().equalsIgnoreCase("")){
                    newEmailStr = editText.getText().toString();
                    new changeEmailAddress().execute();
                    dialog.dismiss();
                }else{
                    Toast.makeText(AccountMain.this, "Please Enter New Email Address", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }




    private class getAccountDetails extends AsyncTask<String, String, String> {
        public ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(AccountMain.this);
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //http://web-medico.com/web2/kibbeh/api/v1/user/my_account/acount_detail/87
            String userId = utils.ReadSharePrefrence(getApplicationContext(),Constant.UserId);
            String Url = Constant.BASE_URL + "user/my_account/acount_detail/"+userId ;
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("RESPOENSE",">> "+s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("status") == true) {

                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    int id = jsonObject1.getInt("id");
                    String fName = jsonObject1.getString("first_name");
                    String lName = jsonObject1.getString("last_name");
                    String email = jsonObject1.getString("email");
                    String phone = jsonObject1.getString("phone");
                    String password = jsonObject1.getString("password");
                    etFname.setText(fName);
                    etLname.setText(lName);
                    etPhone.setText(phone);
                    tvEmailChange.setText(email);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
        }
    }


    private class saveAccountDetails extends AsyncTask<String, String, String> {
        public ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(AccountMain.this);
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //http://web-medico.com/web2/kibbeh/api/v1/user/my_account/change_basic_detail/87?first_name=samir&last_name=sanket&phone=222
            String userId = utils.ReadSharePrefrence(getApplicationContext(),Constant.UserId);
            String Url = Constant.BASE_URL + "user/my_account/change_basic_detail/"+userId+"?"+"first_name="+fNameStr+"&last_name="+lNameStr+"&phone="+phoneStr;
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("status") == true) {
                    Toast.makeText(AccountMain.this, "Account Details Updated Successfully.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AccountMain.this, "Account Details not updated, Please try again later.", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
        }
    }


    private class changePassword extends AsyncTask<String, String, String> {
        public ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(AccountMain.this);
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //http://web-medico.com/web2/kibbeh/api/v1/user/my_account/change_password/87/?new_password=abc
            String userId = utils.ReadSharePrefrence(getApplicationContext(),Constant.UserId);
            String Url = Constant.BASE_URL + "user/my_account/change_password/"+userId+"?"+"new_password="+newPasswordStr;
            Log.e("URL PWD",""+Url);
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("Password Response",">>"+s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("status") == true) {
                    Toast.makeText(AccountMain.this, "Password Updated Successfully.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(AccountMain.this, "Password not updated, Please try again later.", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
        }
    }









    private class changeEmailAddress extends AsyncTask<String, String, String> {
        public ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(AccountMain.this);
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //http://web-medico.com/web2/kibbeh/api/v1/user/my_account/change_password/87/?new_password=abc
            String userId = utils.ReadSharePrefrence(getApplicationContext(),Constant.UserId);
            String Url = Constant.BASE_URL + "user/my_account/change_email/"+userId+"?"+"new_email="+newEmailStr;
            Log.e("URL Eml",""+Url);
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("Email Response",">>"+s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("status") == true) {
                    Toast.makeText(AccountMain.this, "Email Updated Successfully.", Toast.LENGTH_SHORT).show();
                    utils.WriteSharePrefrence(getApplicationContext(),Constant.Email,newEmailStr);
                    tvEmailChange.setText(newEmailStr);

                }else{
                    Toast.makeText(AccountMain.this, "Email not updated, Please try again later.", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
        }
    }

}
