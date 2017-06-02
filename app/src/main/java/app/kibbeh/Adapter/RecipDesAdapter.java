package app.kibbeh.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.kibbeh.Activity.ProductDetails;
import app.kibbeh.Activity.RecipesDetailActivity;
import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;
import app.kibbeh.model.RecipesItems;

/**
 * Created by archi on 11/10/2016.
 */

public class RecipDesAdapter extends BaseAdapter {

   public Context context;
    public ArrayList<RecipesItems> arrayRecItem;
    public LayoutInflater inflater;
    public Utils utils;
    public RecipDesAdapter(RecipesDetailActivity recipesDetailActivity, ArrayList<RecipesItems> arrayRecipeList)
    {
        arrayRecItem = arrayRecipeList;
        context = recipesDetailActivity;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        utils = new Utils(context);
    }

    @Override
    public int getCount() {
        return arrayRecItem.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.adapter_recip_des,null);
        ImageView imageView = (ImageView)convertView.findViewById(R.id.adp_recip_iv);
        TextView tvTitleName = (TextView) convertView.findViewById(R.id.adp_rec_title);
        TextView tvAmount = (TextView)convertView.findViewById(R.id.adp_rec_product_price);
        TextView tvRemoveCart = (TextView)convertView.findViewById(R.id.adp_recip_tv_remove_cart);
        TextView tvAddCart = (TextView)convertView.findViewById(R.id.adp_recip_tv_add_cart);

        tvTitleName.setText(arrayRecItem.get(position).getProductName());
        tvAmount.setText(arrayRecItem.get(position).getProductPrice());
        Glide.with(context).load(arrayRecItem.get(position).getProductImage()).placeholder(R.drawable.ic_placeholder).into(imageView);

        tvAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddCart(arrayRecItem.get(position).getProductId(),position).execute();
            }
        });


        tvRemoveCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new newRemoveCart(arrayRecItem.get(position).getProductId(),position).execute();
            }
        });


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, ProductDetails.class);
                i.putExtra("id",arrayRecItem.get(position).getProductId());
                context.startActivity(i);
            }
        });

        return convertView;
    }

    private class AddCart extends AsyncTask<String,String,String> {
        ProgressDialog pd;
        String productID;
        int pos;
        public AddCart(String productId, int position)  {
         this.productID = productId;
         pos = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String userid = utils.ReadSharePrefrence(context, Constant.UserId);
            String storeid = utils.ReadSharePrefrence(context, Constant.UserStoreId);
            String Url = Constant.BASE_URL + "user/add_product_cart/" + userid + "/" + storeid + "/" + productID + "?quantity=1";
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true"))
                {
                    Toast.makeText(context,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();

                }
                else
                {
                    Toast.makeText(context,jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                }


            }
            catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(context,"something went wrong",Toast.LENGTH_SHORT).show();
            }

            pd.dismiss();
        }
    }

    private class newRemoveCart extends AsyncTask<String,String,String>
    {

        public newRemoveCart(String productId, int position)
        {
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
