package app.kibbeh.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.Utils;
import app.kibbeh.Interface.OnLoseInternet;
import app.kibbeh.R;

/**
 * Created by archirayan on 10-Oct-16.
 */

public class ProductDetails extends Activity implements View.OnClickListener {
    public String id;
    public ImageView upImage, downImage, proImage;
    public TextView countTv, nutriationTv, descriptionTv, titleTv,countTotalTv;
    public Utils utils;
    public Button addToCartBtn;
    public int intialQty;
    public String userId, storeId;
    public Double price;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        userId = Utils.ReadSharePrefrence(this, Constant.UserId);
        storeId = Utils.ReadSharePrefrence(this, Constant.UserStoreId);
        (findViewById(R.id.actionBarBackImage)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        addToCartBtn = (Button) findViewById(R.id.activity_product_details_add_cart);
        utils = new Utils(ProductDetails.this);
        if (getIntent().getExtras() != null) {
            id = getIntent().getExtras().getString("id");
        }
        proImage = (ImageView) findViewById(R.id.activity_product_details_image);
        upImage = (ImageView) findViewById(R.id.activity_product_details_up);
        downImage = (ImageView) findViewById(R.id.activity_product_details_down);
        countTv = (TextView) findViewById(R.id.activity_product_details_count);
        titleTv = (TextView) findViewById(R.id.activity_product_details_title);
        nutriationTv = (TextView) findViewById(R.id.activity_product_details_nutriation);
        descriptionTv = (TextView) findViewById(R.id.activity_product_details_des);
        countTotalTv = (TextView)findViewById(R.id.activity_product_totalprice);
        init();

    }

    private void init() {

        if (utils.isConnectingToInternet()) {
            new getProductDetails().execute();
        }
        else
        {
            Toast.makeText(ProductDetails.this,"NO INTERNET CONNECTION",Toast.LENGTH_LONG).show();
        }
        upImage.setOnClickListener(this);
        downImage.setOnClickListener(this);
        addToCartBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_product_details_up:

                int count = Integer.parseInt(countTv.getText().toString());
                count = count+1;
                countTv.setText(String.valueOf(count));
                double i2 = (price * count);
                countTotalTv.setText("$ "+String.valueOf(new DecimalFormat("##.##").format(i2)));
                break;
            case R.id.activity_product_details_down:
                int count1 = Integer.parseInt(countTv.getText().toString());
                if (count1 > 1)
                {
                    count1 = count1-1;
                    double iDouble = (price * count1);
                    countTotalTv.setText("$ "+String.valueOf(new DecimalFormat("##.##").format(iDouble)));
                    countTv.setText(String.valueOf(count1));
                }
                else
                {
                    Toast.makeText(ProductDetails.this,"Atleast one select",Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.activity_product_details_add_cart:
                int Qty = Integer.parseInt(countTv.getText().toString());
                if (intialQty != Qty) {
                    new addToCart(countTv.getText().toString()).execute();
                }
                else
                {

                }
                break;
        }

    }

    private class getProductDetails extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ProductDetails.this);
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //http://web-medico.com/web2/kibbeh/api/v1/login?email=archiryan12@gmail.com&password=archiryan12
            String Url = Constant.BASE_URL + "product/product_detail/" + id;
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("status") == true) {
                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
                    descriptionTv.setText(Html.fromHtml(jsonObject1.getString("description")));
                    ((TextView) findViewById(R.id.actionBarTitle)).setText(jsonObject1.getString("name"));
                    price = Double.valueOf(jsonObject1.getString("price"));
                    //Toast.makeText(getApplicationContext(),price,Toast.LENGTH_LONG).show();
                    nutriationTv.setText(Html.fromHtml(jsonObject1.getString("nutrition_facts")));
                    int quentity = Integer.parseInt(jsonObject1.getString("quantity"));
                    double i2 = (price * quentity);
                    countTotalTv.setText("$ "+String.valueOf(new DecimalFormat("##.##").format(i2)));
                    countTv.setText(jsonObject1.getString("quantity"));
                    intialQty = Integer.parseInt(jsonObject1.getString("quantity"));
                    Glide.with(ProductDetails.this).load(jsonObject1.getString("image_url")).placeholder(R.drawable.ic_placeholder).into(proImage);
                    titleTv.setText(jsonObject1.getString("name"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();}
    }

    private class addToCart extends AsyncTask<String, String, String> implements OnLoseInternet {

        ProgressDialog pd;
        String qty;
        public addToCart(String qty) {
            this.qty = qty;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(ProductDetails.this);
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            pd.show();
        }
        @Override
        protected String doInBackground(String... params) {
            //http://web-medico.com/web2/kibbeh/api/v1/user/add_product_cart/87/4/56?quantity=1000
            String Url = Constant.BASE_URL + "user/add_product_cart/" + userId + "/" + storeId + "/" + id + "?quantity=" + qty;
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("status") == true) {
                    Toast.makeText(ProductDetails.this, "Product Added sucessfully", Toast.LENGTH_SHORT).show();

//                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");
//                    descriptionTv.setText(Html.fromHtml(jsonObject1.getString("description")));
//                    ((TextView) findViewById(R.id.actionBarTitle)).setText(jsonObject1.getString("name"));
//                    nutriationTv.setText(Html.fromHtml(jsonObject1.getString("nutrition_facts")));
//                    countTv.setText(jsonObject1.getString("quantity"));
//                    intialQty = Integer.parseInt(jsonObject1.getString("quantity"));
//                    Glide.with(ProductDetails.this).load(jsonObject1.getString("image_url")).placeholder(R.drawable.ic_placeholder).into(proImage);
//                    titleTv.setText(jsonObject1.getString("name"));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();}

        @Override
        public void OnLoseInternetConnection(View view) {

        }
    }
}
