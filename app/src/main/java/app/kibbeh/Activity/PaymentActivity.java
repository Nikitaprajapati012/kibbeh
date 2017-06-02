package app.kibbeh.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import app.kibbeh.Adapter.PaymentCartAdapter;
import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.Util;
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;
import app.kibbeh.model.CreditCard;

/**
 * Created by archi on 11/15/2016.
 */

public class PaymentActivity extends Activity implements View.OnClickListener {
    public TextView tvPaymentHeader;
    public ListView lvCreditCart;
    public Utils utils;
    public EditText etCoupen;
    public ArrayList<CreditCard> arrayListCreditCard;
    public TextView tvPayNow,tvAddNewCard;
    public Button btnCoupenApply;
    public int currentYear,currentMonth;
    public Dialog dialog;
    public ImageView ivBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_cart);
        tvPaymentHeader = (TextView)findViewById(R.id.actionBarCheckOutPayment);
        tvPaymentHeader.setAlpha(1);
        lvCreditCart = (ListView)findViewById(R.id.act_payment_card_listview);
        utils = new Utils(PaymentActivity.this);
        etCoupen = (EditText)findViewById(R.id.act_paymet_cpn_code_et);
        btnCoupenApply = (Button)findViewById(R.id.btn_apply);
        tvAddNewCard = (TextView)findViewById(R.id.act_payment_add_new_card);
        ivBack = (ImageView)findViewById(R.id.actionBarBackImage);
        Calendar now = Calendar.getInstance();
        currentYear = now.get(Calendar.YEAR);
        currentMonth = now.get(Calendar.MONTH)+1;

        if (utils.isConnectingToInternet())
        {
            init();
        }
        else
        {
            Toast.makeText(PaymentActivity.this,getString(R.string.no_internet_connection),Toast.LENGTH_SHORT).show();
        }


    }

    private void init() {

        new getCreditCartList().execute();
        btnCoupenApply.setOnClickListener(this);
        tvAddNewCard.setOnClickListener(this);
        ivBack.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btn_apply:
               String strGetCode = etCoupen.getText().toString().trim();
                if (strGetCode.length() > 3)
                {
                    new GetCoupen(strGetCode).execute();
                }
                else
                {
                    Toast.makeText(PaymentActivity.this,"please enter valid coupen",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.actionBarBackImage:
                onBackPressed();
                finish();
                break;
            case R.id.act_payment_add_new_card:
                dialog = new Dialog(PaymentActivity.this);
                dialog.setContentView(R.layout.dialog_add_new_card);
                dialog.setTitle(getString(R.string.add_new_card));
                final EditText card=(EditText)dialog.findViewById(R.id.edt_card_number);
                final EditText cvv=(EditText)dialog.findViewById(R.id.edt_cvv);
                final EditText mnth=(EditText)dialog.findViewById(R.id.edt_mnth);
                final EditText yr=(EditText)dialog.findViewById(R.id.edt_year);
                Button  save=(Button)dialog.findViewById(R.id.btn_save_card);
                Button cancel = (Button)dialog.findViewById(R.id.dialog_add_new_cancel);
                dialog.show();
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      String cardNumber = card.getText().toString();
                        String cvvNum  = cvv.getText().toString();
                        String strMnth =mnth.getText().toString();
                        String strYr = yr.getText().toString();
                        checkvalidation(cardNumber,cvvNum,strMnth,strYr);
                    }
                });
                break;
        }
    }

    private void checkvalidation(String cardNumber, String cvvNum, String strMnth, String strYr) {
        if (cardNumber.length() > 0 && cvvNum.length() > 0 && strMnth.length()>0 && strYr.length() > 0 ){
            if (cardNumber.length() ==16 &&  cvvNum.length()==3 && strMnth.length() ==2 && strYr.length() ==4){

                if (Integer.parseInt(strYr)>currentYear){
                    new AddToNewCard(cardNumber,cvvNum,strMnth,strYr).execute();

                }else if (Integer.parseInt(strYr)==currentYear){

                    if (Integer.parseInt(strMnth)>currentMonth){
                        new AddToNewCard(cardNumber,cvvNum,strMnth,strYr).execute();

                    }else {
                        Toast.makeText(PaymentActivity.this, "month is expire", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PaymentActivity.this, "card is expire", Toast.LENGTH_SHORT).show();
                }

            }else {
                Toast.makeText(PaymentActivity.this, "Please Enter Valid Details", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(PaymentActivity.this, "Please Enter All details", Toast.LENGTH_SHORT).show();
        }



    }

    private class getCreditCartList extends AsyncTask<String,String,String>
    {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(PaymentActivity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
         //http://web-medico.com/web2/kibbeh/api/v1/user/checkout/payment_method/91
            String UserId = Utils.ReadSharePrefrence(PaymentActivity.this,Constant.UserId);
            String Url = Constant.BASE_URL + "user/checkout/payment_method/"+UserId;
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            arrayListCreditCard = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true"))
                {
                    JSONArray jsonArray = jsonObject.getJSONArray("methods");
                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        CreditCard creditCard = new CreditCard();
                        creditCard.setId(String.valueOf(jsonObject1.getInt("id")));
                        creditCard.setCustmoreId(jsonObject1.getString("customer_id"));
                        creditCard.setCardToken(jsonObject1.getString("card_token"));
                        creditCard.setLastFour(jsonObject1.getString("last_four"));
                        creditCard.setCraete(jsonObject1.getString("created_at"));
                        creditCard.setUpdated(jsonObject1.getString("updated_at"));
                        arrayListCreditCard.add(creditCard);
                    }
                    PaymentCartAdapter paymentCartAdapter = new PaymentCartAdapter(PaymentActivity.this,arrayListCreditCard);
                    lvCreditCart.setAdapter(paymentCartAdapter);
                }
                else
                {
                 Toast.makeText(PaymentActivity.this,jsonObject.getString("msg"),Toast.LENGTH_SHORT);
                }
            }
            catch
                    (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
        }
    }

    private class GetCoupen extends AsyncTask<String,String,String> {
       String code;
        ProgressDialog pd;
        public GetCoupen(String strGetCode) {
        this.code = strGetCode;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(PaymentActivity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
        // http://web-medico.com/web2/kibbeh/api/v1/user/checkout/redeem_coupon/55?coupon_code=TEST10
            String userId = utils.ReadSharePrefrence(getApplicationContext(), Constant.UserId);
            String Url = Constant.BASE_URL + "user/checkout/redeem_coupon/" + userId +"?coupon_code=" + code;
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
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
                        utils.WriteSharePrefrence(PaymentActivity.this,Constant.coupenCredit,erned);
                    }
                    Toast.makeText(PaymentActivity.this, "Code Redeemed Successfully.", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(PaymentActivity.this, ""+jsonObject.get("error"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
        }
    }

    private class AddToNewCard extends AsyncTask<String,String,String>
    {
        ProgressDialog pd;
        String cardNumber,month,year,cvvNumber;
        public AddToNewCard(String cardNumber, String cvvNum, String strMnth, String strYr) {
            this.cardNumber = cardNumber;
            this.month = strMnth;
            this.year = strYr;
            this.cvvNumber =cvvNum;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd =new ProgressDialog(PaymentActivity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected String doInBackground(String... params) {
            //web-medico.com/web2/kibbeh/api/v1/user/checkout/generate_token/99?card_number=4242424242424242&exp_month=12&exp_year=2020&cvc=123*/
            String user_id  = utils.ReadSharePrefrence(PaymentActivity.this, Constant.UserId);
            String Url = Constant.BASE_URL +"user/checkout/generate_token/"+ user_id +"?card_number="+ cardNumber +"&exp_month="+ month +"&exp_year="+ year +"&cvc="+ cvvNumber;
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("status")==true)
                {

                  new getCreditCartList().execute();
                    dialog.dismiss();

                }
            }
            catch (JSONException e) {
                e.printStackTrace();
            }


            pd.dismiss();
        }

    }
}
