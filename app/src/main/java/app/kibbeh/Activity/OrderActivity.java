package app.kibbeh.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.kibbeh.Adapter.OrderHistoryAdapter;
import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.Util;
import app.kibbeh.R;
import app.kibbeh.model.OrderDetailsView;
import app.kibbeh.model.OrderHistory;
import app.kibbeh.model.OrderViewAdapter;

/**
 * Created by archi on 11/15/2016.
 */

public class OrderActivity extends Activity implements View.OnClickListener {
    public ImageView ivBack;
    public TextView tvOrder;
    public String Shopper_id,Order_id;
    public Util  utils;
    public String orderid;
    ArrayList<String> arrayorderid;
    OrderViewAdapter adapter;
    ArrayList<OrderDetailsView> arrayOrderDetailView;
    public ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        utils = new Util();
        setContentView(R.layout.act_order_list);
       // Shopper_id = utils.ReadSharePrefrence(OrderActivity.this, Constant);
        mListView = (ListView)findViewById(R.id.act_order_list_lv);
        tvOrder = (TextView)findViewById(R.id.actionBarCheckOutOrder);
        tvOrder.setAlpha(1);
        ivBack = (ImageView)findViewById(R.id.actionBarBackImage);
        init();
        new getOrderHistory().execute();
    }
    private class getOrderHistory extends AsyncTask<String, String,  ArrayList<OrderDetailsView>> {
        public ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // orderHistoryList = new ArrayList<>();
            pd = new ProgressDialog(OrderActivity.this);
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected  ArrayList<OrderDetailsView> doInBackground(String... params) {
//            http://web-medico.com/web2/kibbeh/api/v1/user/my_account/order_history/55
            String userId = utils.ReadSharePrefrence(getApplicationContext(), Constant.UserId);
            //String orderId = utils.ReadSharePrefrence(getApplicationContext(), Constant.OrderId);
            arrayorderid = new ArrayList<>();
            String Url1 = Constant.BASE_URL + "user/my_account/order_history/"+userId ;
            String oder_id = utils.MakeServiceCall(Url1);
            try {
                JSONObject jsonObject = new JSONObject(oder_id);
                if (jsonObject.getBoolean("status") == true) {
                    JSONArray arry = jsonObject.getJSONArray("orders");
                    if (arry.length() > 0) {
                        for (int i = 0; i < arry.length(); i++) {
                            JSONObject jsonObject1 = arry.getJSONObject(i);
                            OrderHistory orderHistory = new OrderHistory();
                            orderHistory.setOrder_id(jsonObject1.getString("order_id"));
                            orderid = orderHistory.getOrder_id();
                            arrayorderid.add(orderid);

                           // utils.WriteSharePrefrence(OrderActivity.this, Constant.OrderId, jsonObject1.getString("order_id"));
                        }
                    }else{
                        Toast.makeText(OrderActivity.this, "No Order Found.", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int k = 0; k<arrayorderid.size(); k++)
            {
                String id = arrayorderid.get(k);
                String Url = Constant.BASE_URL + "user/checkout/invoice/"+userId+"/"+id ;
                String orderresult = utils.MakeServiceCall(Url);
                Log.e("URL",">> "+Url);
                try {
                    arrayOrderDetailView = new ArrayList<OrderDetailsView>();
                    JSONObject jsonObject = new JSONObject(orderresult);
                    if (jsonObject.getBoolean("status") == true) {
                        JSONArray arry = jsonObject.getJSONArray("product_details");
                        if(arry.length() > 0) {
                            for (int i = 0; i < arry.length(); i++) {
                                JSONObject jsonObject1 = arry.getJSONObject(i);
                                String OrderImage = jsonObject1.getString("product_image_url");
                                String Order_ProdectName =jsonObject1.getString("product_name");
                                String Oder_Quantity = jsonObject1.getString("quantity");
                                String Order_Price = jsonObject1.getString("price");

                                arrayOrderDetailView.add(new OrderDetailsView(OrderImage,Order_ProdectName,Oder_Quantity,Order_Price));
                            }

                        }else{
                            Toast.makeText(OrderActivity.this, "No Order Found.", Toast.LENGTH_SHORT).show();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return arrayOrderDetailView;
        }

        @Override
        protected void onPostExecute( ArrayList<OrderDetailsView> detailsarray) {
            super.onPostExecute(detailsarray);
            Log.e("RESPOENSE",">> "+detailsarray);

            adapter = new OrderViewAdapter(OrderActivity.this,R.layout.orderview_items_field,detailsarray);
            mListView.setAdapter(adapter);
            pd.dismiss();


        }
    }
     private void init()
    {
        ivBack.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.actionBarBackImage:
                onBackPressed();
                finish();
                break;
        }


    }
}
