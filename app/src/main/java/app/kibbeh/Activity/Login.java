package app.kibbeh.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.Util;
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;


/**
 * Facebook account :
 * Archirayan15@gmail.com
 **/


public class Login extends FragmentActivity implements View.OnClickListener {

    public Button btnLogin;
    public EditText etEmailLogin, etPasseordLogin;
    public String strEmail, strPassword;
    public Utils utils;
    public TextView tvForgrtPassword;
    public TextView tvSignUp;
    public ImageView ivfblogin;
    CallbackManager callbackManager;
    ProgressDialog prgDialog;
    GraphRequest request;
    private URL image = null;
    String fbname, fbemail, fbgender, fbimg, fbid, fbfirstname, fblastname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);

        utils = new Utils(Login.this);
        LoginManager.getInstance().logOut();
        btnLogin = (Button) findViewById(R.id.activity_login_submit);
        etEmailLogin = (EditText) findViewById(R.id.activity_login_email);
        etPasseordLogin = (EditText) findViewById(R.id.activity_login_password);
        tvForgrtPassword = (TextView) findViewById(R.id.activity_login_forgetpassword);
        tvSignUp = (TextView) findViewById(R.id.activity_login_signup);
        ivfblogin = (ImageView) findViewById(R.id.fblogin_button_image);
        init();
    }

    private void init() {
        btnLogin.setOnClickListener(this);
        tvForgrtPassword.setOnClickListener(this);
        tvSignUp.setOnClickListener(this);
        ivfblogin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_login_submit:
                strEmail = etEmailLogin.getText().toString().trim();
                strPassword = etPasseordLogin.getText().toString();
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(strEmail).matches()) {
                    if (strPassword.length() < 3) {
                        Toast.makeText(getApplicationContext(), "please enter valid email id ", Toast.LENGTH_SHORT).show();
                    } else {
                        if (utils.isConnectingToInternet()) {
                            new getLogin().execute();
                        } else {
                            Toast.makeText(getApplicationContext(), "please Check your Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "please enter valid email id ", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.activity_login_forgetpassword:
                Intent i = new Intent(Login.this, ForgetPassword.class);
                startActivity(i);
                break;

            case R.id.activity_login_signup:
                Intent register = new Intent(Login.this, Signup2.class);
                register.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(register);
                break;

            case R.id.fblogin_button_image:

                LoginManager.getInstance().logInWithReadPermissions(Login.this, (Arrays.asList("public_profile", "user_friends", "user_birthday", "user_about_me", "email")));
                LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                            @Override
                            public void onSuccess(LoginResult loginResult) {

                                AccessToken accessToken = loginResult.getAccessToken();
                                Profile profile = Profile.getCurrentProfile();

                                Log.e("Access", "" + accessToken);
                                // Facebook Email address
                                request = GraphRequest.newMeRequest(
                                        loginResult.getAccessToken(),
                                        new GraphRequest.GraphJSONObjectCallback() {
                                            @Override
                                            public void onCompleted(JSONObject object, GraphResponse response) {
                                                Log.e("LoginActivity ", " " + response.toString());
                                                try {
                                                    fbid = object.getString("id");
                                                    fbname = object.getString("name");
                                                    fbemail = object.getString("email");
                                                    fbgender = object.getString("gender");
                                                    // Toast.makeText(getApplicationContext(), fbname, Toast.LENGTH_SHORT).show();
                                                    Log.e("fbname", " " + fbname);
                                                    Log.e("fbemail = ", " " + fbemail);
                                                    Log.e("fbgender = ", " " + fbgender);
                                                    String[] animalsArray = fbname.split(" ");
                                                    // Log.e("Array", Arrays.toString(animalsArray));
                                                    fbfirstname = animalsArray[0];
                                                    fblastname = animalsArray[1];
                                                    Log.e("first_name", fbfirstname);
                                                    Log.e("last_name", fblastname);
                                                    prgDialog = new ProgressDialog(Login.this);
                                                    prgDialog.setCancelable(false);
                                                    new getfbLogin().execute();
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        });

                                Bundle parameters1 = new Bundle();
                                parameters1.putString("fields", "id,name,email,gender, birthday,age_range");
                                request.setParameters(parameters1);
                                request.executeAsync();

                                //  Log.e("name123", " " + FragmentDrawer.name13);

                                System.out.println("accesstoken" + loginResult.getAccessToken().getToken());
                                System.out.println("userid " + loginResult.getAccessToken().getUserId());

                                Log.d("tag", "FF fb onSuccess");
                                try {
                                    image = new URL("https://graph.facebook.com/" + loginResult.getAccessToken().getUserId() + "/picture");
                                    Log.e("fid", "" + loginResult.getAccessToken());
                                    String id = loginResult.getAccessToken().getUserId();
                                    Log.e("URL FACEBOOK", "" + image);
                                    fbimg = String.valueOf(image);
                                    Log.e("fbimg", "" + fbimg);

                                } catch (
                                        MalformedURLException e
                                        ) {
                                    e.printStackTrace();
                                }


                            }

                            @Override
                            public void onCancel() {

                            }

                            @Override
                            public void onError(FacebookException error) {

                            }
                        }
                );


        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private class getLogin extends AsyncTask<String, String, String> {
        public ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Login.this);
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //http://web-medico.com/web2/kibbeh/api/v1/login?email=archiryan12@gmail.com&password=archiryan12
            return utils.MakeServiceCall(Constant.BASE_URL + "login?email=" + strEmail + "&password=" + strPassword);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("jai", "response :" + s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("status") == true) {

                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    int strUserId = jsonObject1.getInt("id");
                    Utils.WriteSharePrefrence(Login.this, Constant.UserId, String.valueOf(strUserId));
                    Utils.WriteSharePrefrence(Login.this, Constant.FirstName, jsonObject1.getString("first_name"));
                    Utils.WriteSharePrefrence(Login.this, Constant.LastName, jsonObject1.getString("last_name"));
                    Utils.WriteSharePrefrence(Login.this, Constant.PhNo, jsonObject1.getString("phone"));
                    Utils.WriteSharePrefrence(Login.this, Constant.Email, jsonObject1.getString("email"));
                    Utils.WriteSharePrefrence(Login.this, Constant.RefralCode, jsonObject1.getString("referral_code"));
                    Utils.WriteSharePrefrence(Login.this, Constant.MemberType, jsonObject1.getString("membership_type"));
                    Utils.WriteSharePrefrence(Login.this, Constant.ZipCode, jsonObject1.getString("zipcode"));
                    Utils.WriteSharePrefrence(Login.this, Constant.UserProPic, jsonObject1.getString("image_url"));
                    Utils.WriteSharePrefrence(Login.this, Constant.UserStoreId, jsonObject1.getString("store_id"));
                    Utils.WriteSharePrefrence(Login.this, Constant.RegId, jsonObject1.getString("register_status"));

                    Intent i = new Intent(Login.this, MainActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                    finish();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
        }
    }

    private class getfbLogin extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(Login.this);
            pd.setMessage("please wait...");
            pd.show();
        }


        @Override
        protected String doInBackground(String... params) {
            // http://web-medico.com/web2/kibbeh/api/v1/user/login_with_facebook/archirayan1511@gmail.com/1822409728016031111/Archirayan1/Infotech1/22201
          /*  HashMap<String, String> map = new HashMap();
            map.put("email", fbemail);
            map.put("fb_token", fbid);
            map.put("first_name", fbfirstname);
            map.put("last_name", fblastname);
            map.put("zipcode", Util.ReadSharePrefrence(Login.this, Constant.ZIPCODE));

            for (Map.Entry<String, String> entry : map.entrySet()) {   //print keys and values
                Log.d("HashMapValues", entry.getKey() + " : " + entry.getValue());
            }*/

            return utils.MakeServiceCall(Constant.BASE_URLF + "login_with_facebook/" + fbemail + "/"
                    + fbid + "/" + fbfirstname + "/" + fblastname + "/" + Util.ReadSharePrefrence(Login.this, Constant.ZIPCODE));
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Log.d("Response", s);

            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("status").equalsIgnoreCase("true")) {
                    JSONObject userObject = object.getJSONObject("data");


                    //  Utils.WriteSharePrefrence(Login.this, Constant.UserId, userObject.getString("ID"));
                    //Utils.WriteSharePrefrence(Login.this, Constant.ISFACEBOOK, "1");
                    Intent in = new Intent(Login.this, MainActivity.class);
                    in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(in);
                    finish();

                } else {
                    Toast.makeText(Login.this, "Hello", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }


    }
}
