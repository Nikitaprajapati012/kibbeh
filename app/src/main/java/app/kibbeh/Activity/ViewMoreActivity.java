package app.kibbeh.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import app.kibbeh.Adapter.ViewMoreProductAdapter;
import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.MakeServiceCall;
import app.kibbeh.Constant.Util;
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;
import app.kibbeh.model.ViewMoreProduct;

public class ViewMoreActivity extends Activity {
    public Utils utils;
    private List<ViewMoreProduct> productList;
    public ArrayList<ViewMoreProduct> arrayMoreProduct;
    public TextView emptyView;
    RecyclerView recyclerView;
    private ViewMoreProductAdapter mAdapter;
    //private AdapterFish mAdapter;
    int dept_id,store_id;
      String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_more);
        emptyView = (TextView)findViewById(R.id.emptyview);

        utils = new Utils(ViewMoreActivity.this);
        (findViewById(R.id.actionBarBackImage)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });
        ((TextView) findViewById(R.id.actionBarTitle)).setText("Products");
        recyclerView = (RecyclerView)findViewById(R.id.recyclerview);
        new AsyncFetch().execute();
        if (getIntent().getExtras().equals(""))
        {
        emptyView.setVisibility(View.VISIBLE);
        }
        else
        {
            dept_id = Integer.parseInt(getIntent().getExtras().getString("department_ID"));
            store_id = Integer.parseInt(getIntent().getExtras().getString("store_ID"));
        }
        user_id = utils.ReadSharePrefrence(ViewMoreActivity.this, Constant.UserId);
    }

    private class AsyncFetch extends AsyncTask<String,String,String>{


        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ViewMoreActivity.this);
            pd.setMessage("please wait...");
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected String doInBackground(String... params) {
            String Url = Constant.BASE_URL + "user/store_departments_more/" + user_id + "/" +store_id + "/" + dept_id ;
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            /*pd.dismiss();*/
            try {
                arrayMoreProduct = new ArrayList<>();
                JSONObject mainObject = new JSONObject(s);
                if (mainObject.getBoolean("status") == true)
                {

                    JSONArray array = mainObject.getJSONArray("data");
                    for (int i = 0;i<array.length();i++){
                        JSONObject dataObject = array.getJSONObject(i);
                        ViewMoreProduct moreProduct = new ViewMoreProduct();
                        moreProduct.setId(String.valueOf(dataObject.getInt("product_id")));
                        moreProduct.setName(dataObject.getString("name"));
                        moreProduct.setImage(dataObject.getString("image_url"));
                        moreProduct.setQty(String.valueOf(dataObject.getInt("quantity")));
                        moreProduct.setPrice(String.valueOf(dataObject.getInt("price")));
                        moreProduct.setIs_Fav(String.valueOf(dataObject.getInt("is_favourite")));
                        moreProduct.setFav_Id(dataObject.getString("favourite_id"));
                        moreProduct.setAdded_Cart(String.valueOf(dataObject.getInt("added_cart")));
                        arrayMoreProduct.add(moreProduct);

                    }


                    if (arrayMoreProduct.size() == 0)
                    {
                        emptyView.setVisibility(View.VISIBLE);
                    }
                    else {
                        mAdapter = new ViewMoreProductAdapter(ViewMoreActivity.this, arrayMoreProduct);
                        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                        llm.setOrientation(LinearLayoutManager.VERTICAL);
                        //RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                        recyclerView.setLayoutManager(llm);
                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                        recyclerView.setAdapter(mAdapter);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();

                emptyView.setVisibility(View.VISIBLE);
            }
           pd.dismiss();
        }
    }
}
