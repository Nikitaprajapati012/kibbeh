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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.kibbeh.Adapter.OrderHistoryAdapter;
import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;
import app.kibbeh.model.OrderHistory;


/**
 * Created by archirayan on 19-Oct-16.
 */
public class OrderHistoryActivity extends Activity {
    RecyclerView rvOrderHistory;
    ArrayList<OrderHistory> orderHistoryList;
    OrderHistoryAdapter adapterOrderHistory;
    public Utils utils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        (findViewById(R.id.actionBarBackImage)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });
        ((TextView) findViewById(R.id.actionBarTitle)).setText("Order History");
        rvOrderHistory = (RecyclerView) findViewById(R.id.rvOrderHistory_Activity_OrderHistory);

        utils = new Utils(OrderHistoryActivity.this);

        if(utils.isConnectingToInternet()){
            new getOrderHistory().execute();
        }else{
            Toast.makeText(OrderHistoryActivity.this,"No internet connection found, please check your internet connection", Toast.LENGTH_SHORT).show();
        }


    }


    private class getOrderHistory extends AsyncTask<String, String, String> {
        public ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            orderHistoryList = new ArrayList<>();
            pd = new ProgressDialog(OrderHistoryActivity.this);
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
//            http://web-medico.com/web2/kibbeh/api/v1/user/my_account/order_history/55
            String userId = utils.ReadSharePrefrence(getApplicationContext(), Constant.UserId);
            String Url = Constant.BASE_URL + "user/my_account/order_history/"+userId ;
//            String Url = Constant.BASE_URL + "user/my_account/order_history/55" ;
            Log.e("URL",">> "+Url);
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("RESPOENSE",">> "+s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("status") == true) {
                    JSONArray arry = jsonObject.getJSONArray("orders");
                    if(arry.length() > 0) {
                        for (int i = 0; i < arry.length(); i++) {
                            JSONObject jsonObject1 = arry.getJSONObject(i);
                            OrderHistory orderHistory = new OrderHistory();
                            orderHistory.setOrder_id(jsonObject1.getString("order_id"));
                            utils.WriteSharePrefrence(OrderHistoryActivity.this, Constant.OrderId, jsonObject1.getString("order_id"));
                            orderHistory.setDate(jsonObject1.getString("date"));
                            orderHistory.setTotal_products(jsonObject1.getString("total_products"));
                            orderHistory.setTotal_amount(jsonObject1.getString("total_amount"));
                            orderHistory.setStatus(jsonObject1.getString("status"));
                            orderHistoryList.add(orderHistory);
                        }
                        adapterOrderHistory = new OrderHistoryAdapter(OrderHistoryActivity.this,orderHistoryList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(OrderHistoryActivity.this);
                        rvOrderHistory.setLayoutManager(mLayoutManager);
                        rvOrderHistory.setItemAnimator(new DefaultItemAnimator());
                        rvOrderHistory.setAdapter(adapterOrderHistory);
                        adapterOrderHistory.notifyDataSetChanged();
                    }else{
                        Toast.makeText(OrderHistoryActivity.this, "No Order Found.", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
