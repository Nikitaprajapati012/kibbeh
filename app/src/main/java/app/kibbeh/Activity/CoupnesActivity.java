package app.kibbeh.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;

/**
 * Created by archirayan on 19-Oct-16.
 */
public class CoupnesActivity extends Activity implements View.OnClickListener {
    TextView tvRedeemCode, tvCreditLeft;
    String coupneCodeStr;
    public Utils utils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupens);
        utils = new Utils(CoupnesActivity.this);
        (findViewById(R.id.actionBarBackImage)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        ((TextView) findViewById(R.id.actionBarTitle)).setText("Redeem Coupen");
        init();
    }

    private void init() {
        new getCredit().execute();

        tvRedeemCode = (TextView) findViewById(R.id.tvRedeemCode);
        tvCreditLeft = (TextView) findViewById(R.id.tvCreditLeft);
        tvRedeemCode.setOnClickListener(this);

    }

   private class getCredit extends AsyncTask<String,String,String>
   {

       ProgressDialog pd;
       @Override
       protected void onPreExecute() {
           super.onPreExecute();
           pd = new ProgressDialog(CoupnesActivity.this);
           pd.setMessage("please wait...");
           pd.setCancelable(false);
           pd.show();

       }

       @Override
       protected String doInBackground(String... params) {

           //http://web-medico.com/web2/kibbeh/api/v1/user/checkout/user_credit/99

          String userId = utils.ReadSharePrefrence(CoupnesActivity.this,Constant.UserId);
           String Url = Constant.BASE_URL + "user/checkout/user_credit/" + userId;
           return utils.MakeServiceCall(Url);
       }

       @Override
       protected void onPostExecute(String s) {
           super.onPostExecute(s);
           try {
               JSONObject jsonObject = new JSONObject(s);
               if (jsonObject.getString("status").equalsIgnoreCase("true"))
               {
                   JSONObject jsonObject1 = jsonObject.getJSONObject("user_credit_details");
                   String id = jsonObject1.get("id").toString();
                   String user_id = jsonObject1.get("user_id").toString();
                   String spent = jsonObject1.get("spent").toString();
                   String created_at = jsonObject1.get("created_at").toString();
                   String updated_at = jsonObject1.get("updated_at").toString();
                   String erned = jsonObject1.get("earned").toString();
                   String note = "You have "+erned +" Shopping Credit Left";
                   tvCreditLeft.setText(note);
               }
               else
               {
                   Toast.makeText(CoupnesActivity.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
               }
           } catch (JSONException e) {
               e.printStackTrace();
               Toast.makeText(CoupnesActivity.this,getString(R.string.somethig_went_wrong),Toast.LENGTH_SHORT).show();
           }
           pd.dismiss();
       }
   }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvRedeemCode:
                showChangeEmailPopup();

                break;
        }
    }

    private void showChangeEmailPopup() {
        final Dialog dialog = new Dialog(CoupnesActivity.this);

        dialog.setContentView(R.layout.popup_reddem_coupen);



        final EditText editText = (EditText) dialog.findViewById(R.id.etReedemCoupne);
        Button btnSave  = (Button) dialog.findViewById(R.id.btOkRedeem);
        Button btnCancel = (Button) dialog.findViewById(R.id.btCancelRedeem);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().equalsIgnoreCase("")){
                    coupneCodeStr = editText.getText().toString();
                    new redeemCode().execute();
                    dialog.dismiss();
                }else{
                    Toast.makeText(CoupnesActivity.this, "Please Enter Coupen code", Toast.LENGTH_SHORT).show();
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



    private class redeemCode extends AsyncTask<String, String, String> {
        public ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(CoupnesActivity.this);
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
//            http://web-medico.com/web2/kibbeh/api/v1/user/checkout/redeem_coupon/55?coupon_code=TEST10
            String userId = utils.ReadSharePrefrence(getApplicationContext(), Constant.UserId);

            String Url = Constant.BASE_URL + "user/checkout/redeem_coupon/"+userId+"?coupon_code="+coupneCodeStr;
            Log.e("URL",">>"+Url);
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("RESPOENSE",">> "+s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if(jsonObject.get("success").toString().equalsIgnoreCase("true")){
                    JSONObject obj = jsonObject.getJSONObject("credits");
                    for(int i=0; i < obj.length(); i++){
                        String id = obj.get("id").toString();
                        String user_id = obj.get("user_id").toString();
                        String spent = obj.get("spent").toString();
                        String created_at = obj.get("created_at").toString();
                        String updated_at = obj.get("updated_at").toString();
                        String erned = obj.get("earned").toString();
                        String note = "You have "+erned +" Shopping Credit Left";
                        tvCreditLeft.setText(note);
                    }
                    Toast.makeText(CoupnesActivity.this, "Code Redeemed Successfully.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(CoupnesActivity.this, ""+jsonObject.get("error"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
