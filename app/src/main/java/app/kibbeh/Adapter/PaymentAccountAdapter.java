package app.kibbeh.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.kibbeh.Activity.PaymentAccountActivity;
import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.MakeServiceCall;
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;
import app.kibbeh.model.CreditCard;

/**
 * Created by archi1 on 11/14/2016.
 */

public class PaymentAccountAdapter extends BaseAdapter {

    public Utils utils;
    public ArrayList<CreditCard> arrayList;
    public Context context;
    public LayoutInflater inflater;


    public PaymentAccountAdapter(Context context, ArrayList<CreditCard> cardArrayList){
        this.context =context;
        this.arrayList = cardArrayList;
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.utils = new Utils(context);
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView=inflater.inflate(R.layout.adapter_payment_account,null);
        TextView tvcard = (TextView)convertView.findViewById(R.id.activity_credi_card_no);
        TextView txtDlt = (TextView)convertView.findViewById(R.id.activity_txt_dlt);
        tvcard.setText("****"+arrayList.get(position).getLastFour());

        //paymentId = arrayList.get(position).getId();
        txtDlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //pos=position;

               new  DeleteCardEntry(position,arrayList.get(position).getId(),arrayList.get(position).getUserId()).execute();
            }
        });
        return convertView;
    }

    private class DeleteCardEntry extends AsyncTask<String,String,String>{
        ProgressDialog pd;
        String userId;
        String paymentId;
        int pos;

        public DeleteCardEntry(int position, String id, String userId) {
            this.pos =position;
            this.userId=userId;
            this.paymentId=id;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //String Url = Constant.BASE_URL +"user/checkout/payment_method/delete/"+userId +"/" + paymentId;
            String Url = Constant.BASE_URL+"user/checkout/payment_method/delete/"+userId +"/"+paymentId;
            Log.d("Vijayyy",Url);
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("status").equalsIgnoreCase("true")){
                    arrayList.remove(pos);
                    notifyDataSetChanged();
                }else {
                    Toast.makeText(context, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
        }
    }


    /*private class DeleteCardEntry extends AsyncTask<String,String,String>{
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Please Wait... ");
            pd.setCancelable(false);
            pd.show();
        }


        @Override
        protected String doInBackground(String... params) {
            userId=utils.ReadSharePrefrence(context,Constant.UserId);
            String Url = Constant.BASE_URL +"user/checkout/payment_method/delete/"+userId +"/" + paymentId;
            return utils.MakeServiceCall(Url);
        }





        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("status").equalsIgnoreCase("true")){
                    arrayList.remove(pos);
                    notifyDataSetChanged();

                    Toast.makeText(context, "Succesfully delete", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
        }
    }*/
}
