package app.kibbeh.Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
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

import app.kibbeh.Adapter.PaymentAccountAdapter;
import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;
import app.kibbeh.model.CreditCard;

public class PaymentAccountActivity extends AppCompatActivity {
    public Utils utils;
    public int currentYear,currentMonth;
    public  Dialog dialog;
    ListView listPayment;
    ArrayList<CreditCard> cardArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_account);
        utils = new Utils(PaymentAccountActivity.this);
        Calendar now = Calendar.getInstance();
        currentYear = now.get(Calendar.YEAR);
        currentMonth = now.get(Calendar.MONTH)+1;
        TextView txtHeader = (TextView)findViewById(R.id.actionBarTitle);
        txtHeader.setText(R.string.paymentmethod);
        ImageView imgBack = (ImageView)findViewById(R.id.actionBarBackImage);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        listPayment=(ListView)findViewById(R.id.activity_payment_card_list);

        init();


        ImageView addCard = (ImageView)findViewById(R.id.ivAddButton);
        addCard.setVisibility(View.VISIBLE);

        addCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(PaymentAccountActivity.this);
                dialog.setContentView(R.layout.dialog_add_new_card);
                dialog.setTitle("Add New Credit Card");
                final EditText edtCardNumber = (EditText)dialog.findViewById(R.id.edt_card_number);
                final EditText edtCvvNumber = (EditText)dialog.findViewById(R.id.edt_cvv);
                final EditText edtMonth = (EditText)dialog.findViewById(R.id.edt_mnth);
                final EditText edtYear = (EditText)dialog.findViewById(R.id.edt_year);
                Button btnCancel = (Button)dialog.findViewById(R.id.dialog_add_new_cancel);
                Button btnSaveCard = (Button)dialog.findViewById(R.id.btn_save_card);

                dialog.show();

                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                btnSaveCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String cardNumber = edtCardNumber.getText().toString();
                        String cvvNumber = edtCvvNumber.getText().toString();
                        String strMnth = edtMonth.getText().toString();
                        String strYr = edtYear.getText().toString();
                        checkvalidation(cardNumber,cvvNumber,strMnth,strYr);
                    }
                });
            }

        });
    }

    private void checkvalidation(String cardNumber, String cvvNumber, String strMnth, String strYr) {

        if (cardNumber.length() > 0 && cvvNumber.length() > 0 && strMnth.length()>0 && strYr.length() > 0 ){
            if (cardNumber.length() ==16 &&  cvvNumber.length()==3 && strMnth.length() ==2 && strYr.length() ==4){

                if (Integer.parseInt(strYr)>currentYear){
                    new AddToNewCard(cardNumber,cvvNumber,strMnth,strYr).execute();

                }else if (Integer.parseInt(strYr)==currentYear){

                    if (Integer.parseInt(strMnth)>currentMonth){
                        new AddToNewCard(cardNumber,cvvNumber,strMnth,strYr).execute();

                    }else {
                        Toast.makeText(PaymentAccountActivity.this, "month is expire", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(PaymentAccountActivity.this, "card is expire", Toast.LENGTH_SHORT).show();
                }

            }else {
                Toast.makeText(PaymentAccountActivity.this, "Please Enter Valid Details", Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(PaymentAccountActivity.this, "Please Enter All details", Toast.LENGTH_SHORT).show();
        }


    }

    private void init() {

        new getData().execute();
    }

    private class getData extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(PaymentAccountActivity.this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String userid = Utils.ReadSharePrefrence(PaymentAccountActivity.this,Constant.UserId);
            String Url = Constant.BASE_URL +"user/checkout/payment_method/" +userid;
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            cardArrayList= new ArrayList<>();
            try {
                JSONObject mainobject = new JSONObject(s);
                if (mainobject.getString("status").equalsIgnoreCase("true"))
                {
                    JSONArray array = mainobject.getJSONArray("methods");
                    for (int i = 0;i<array.length();i++){
                        JSONObject dataobject = array.getJSONObject(i);
                        CreditCard creditCard = new CreditCard();
                        creditCard.setId(String.valueOf(dataobject.getInt("id")));
                        creditCard.setUserId(dataobject.getString("user_id"));
                        creditCard.setCustmoreId(dataobject.getString("customer_id"));
                        creditCard.setCardToken(dataobject.getString("card_token"));
                        creditCard.setLastFour(dataobject.getString("last_four"));
                        creditCard.setCraete(dataobject.getString("created_at"));
                        creditCard.setUpdated(dataobject.getString("updated_at"));
                        cardArrayList.add(creditCard);
                    }

                    PaymentAccountAdapter paymentAccountAdapter = new PaymentAccountAdapter(PaymentAccountActivity.this,cardArrayList);
                    listPayment.setAdapter(paymentAccountAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
        }
    }

    private class AddToNewCard extends AsyncTask<String,String,String> {

        ProgressDialog pd;
        String cardNumber,month,year,cvvNumber;


        public AddToNewCard(String cardNumber, String cvvNumber, String strMnth, String strYr) {
                this.cardNumber=cardNumber;
                this.cvvNumber=cvvNumber;
                this.month=strMnth;
                this.year=strYr;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(PaymentAccountActivity.this);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String userID = utils.ReadSharePrefrence(PaymentAccountActivity.this,Constant.UserId);
            String Url = Constant.BASE_URL +"user/checkout/generate_token/"+ userID +"?card_number="+ cardNumber +"&exp_month="+ month +"&exp_year="+ year +"&cvc="+ cvvNumber;
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try{
                JSONObject object = new JSONObject(s);
                if (object.getString("status").equalsIgnoreCase("true")){
                    new getData().execute();
                    dialog.dismiss();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
        }
    }
}
