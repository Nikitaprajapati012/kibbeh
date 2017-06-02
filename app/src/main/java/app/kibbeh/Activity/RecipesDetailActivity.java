package app.kibbeh.Activity;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.kibbeh.Adapter.RecipDesAdapter;
import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;
import app.kibbeh.model.RecipesDetails;
import app.kibbeh.model.RecipesItems;

/**
 * Created by archirayan on 08-Nov-16.
 */

public class RecipesDetailActivity extends AppCompatActivity implements View.OnClickListener {
    public String recId;
    public TextView tvTitle;
    public ImageView ivBack;
    public Utils utils;
    public ArrayList<RecipesItems> arrayRecipeList;
    public ImageView ivHeader;
    public TextView tvNameOwner,tvDetail,tvNameValue;
    public RecipDesAdapter recipDesAdapter;
    public ListView lvRecDetail;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipes_detail);
        arrayRecipeList = new ArrayList<>();
        utils = new Utils(RecipesDetailActivity.this);
        tvTitle = (TextView) findViewById(R.id.actionBarTitle);
        ivBack = (ImageView) findViewById(R.id.actionBarBackImage);
        ivHeader = (ImageView) findViewById(R.id.activity_reci_iv_header);
        tvNameOwner = (TextView) findViewById(R.id.activity_reci_tv_owner_name);
        tvNameValue = (TextView) findViewById(R.id.activity_reci_tv_name);
        tvDetail = (TextView)findViewById(R.id.tv_recip_detail);
        lvRecDetail = (ListView)findViewById(R.id.activity_rec_lv);
        if (utils.isConnectingToInternet() == true) {
            init();
        } else {
            Toast.makeText(RecipesDetailActivity.this, "NO INTERNET CONNECTION", Toast.LENGTH_LONG).show();
        }
    }

    private void init() {
        tvTitle.setText(getString(R.string.recipes_detail));
        ivBack.setOnClickListener(this);
        if (getIntent().getExtras() != null) {
            recId = getIntent().getExtras().getString("id");
        }
        new getRecipDetail().execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.actionBarBackImage:
                onBackPressed();
                break;
        }
    }

    private class getRecipDetail extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(RecipesDetailActivity.this);
            pd.setMessage("Please wait");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

            // http://web-medico.com/web2/kibbeh/api/v1/recipe_detail/3
            String url = Constant.BASE_URL + "recipe_detail/" + recId;
           //  String url = Constant.BASE_URL + "recipe_detail/3";
            return utils.MakeServiceCall(url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonData = jsonArray.getJSONObject(i);
                        String name = jsonData.getString("name");
                        String image = jsonData.getString("background_image_url");
                        String owner = jsonData.getString("owner");
                        String descreption = jsonData.getString("description");
                        tvDetail.setText(descreption);
                        tvNameOwner.setText(owner);
                        tvNameValue.setText(name);
                        Glide.with(RecipesDetailActivity.this).load(image).placeholder(R.drawable.ic_placeholder).into(ivHeader);
                        JSONArray jsonArryMini = jsonData.getJSONArray("products");
                        for (int i1 = 0; i1 < jsonArryMini.length(); i1++) {
                            JSONObject jsonObject1 = jsonArryMini.getJSONObject(i1);
                            RecipesItems recipesItems = new RecipesItems();
                            String productId = String.valueOf(jsonObject1.getInt("product_id"));
                            recipesItems.setProductId(productId);
                            String productName = jsonObject1.getString("name");
                            recipesItems.setProductName(productName);
                            String productPrice = String.valueOf(jsonObject1.getInt("price"));
                            recipesItems.setProductPrice(productPrice);
                            String productImage = jsonObject1.getString("image_url");
                            recipesItems.setProductImage(productImage);
                            arrayRecipeList.add(recipesItems);
                        }

                        recipDesAdapter = new RecipDesAdapter(RecipesDetailActivity.this,arrayRecipeList);
                        lvRecDetail.setAdapter(recipDesAdapter);
                    }
                }
            } catch (JSONException e1)
            {
                e1.printStackTrace();
            }
            pd.dismiss();

        }
    }
}



