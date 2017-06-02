package app.kibbeh.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.kibbeh.Activity.ProductDetails;
import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.Util;
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;
import app.kibbeh.model.Popular;


/**
 * Created by archi on 10/28/2016.
 */

public class PopularListAdaper extends RecyclerView.Adapter<PopularListAdaper.myViewHolder> {

    public ArrayList<Popular> arrayPopular;
    public Context context;
    public Utils utils;

    public PopularListAdaper(Context activity, ArrayList<Popular> arrayPopular) {
        this.arrayPopular = arrayPopular;
        context = activity;
        utils = new Utils(context);

    }

    public class myViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvPrice, tvAddCart, tvRemoveCart;
        public ImageView ivAddRemFav, ivProductImage;
        public LinearLayout linearLayout;

        public myViewHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.adp_popular_tv_title);
            tvPrice = (TextView) itemView.findViewById(R.id.adp_popular_tv_price);
            tvAddCart = (TextView) itemView.findViewById(R.id.adp_popular_tv_add_cart);
            tvRemoveCart = (TextView) itemView.findViewById(R.id.adp_popular_tv_remove_cart);
            ivAddRemFav = (ImageView) itemView.findViewById(R.id.adp_popular_image);
            ivProductImage = (ImageView) itemView.findViewById(R.id.adp_popular_image);
            linearLayout = (LinearLayout)itemView.findViewById(R.id.adp_linear);

        }
    }

    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_popular_list, parent, false);
        return new PopularListAdaper.myViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, final int position) {
        holder.tvName.setText(arrayPopular.get(position).getName());
        holder.tvPrice.setText("$ "+arrayPopular.get(position).getPrice());
        Glide.with(context).load(arrayPopular.get(position).getImage()).into(holder.ivProductImage);

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent productDetailsIntent = new Intent(context, ProductDetails.class);
            productDetailsIntent.putExtra("id", arrayPopular.get(position).getId());
            context.startActivity(productDetailsIntent);



        }
         });

        if (arrayPopular.get(position).getCarstStatus().equalsIgnoreCase("1"))
        {
             holder.tvRemoveCart.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.tvRemoveCart.setVisibility(View.INVISIBLE);
        }


        holder.tvAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AddToCart().execute(arrayPopular.get(position).getId(), String.valueOf(position));
            }
        });

        holder.tvRemoveCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new RemoveCart().execute(arrayPopular.get(position).getId(), String.valueOf(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayPopular.size();
    }

    private class AddToCart extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String position;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("please wait...");
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected String doInBackground(String... params) {

            String productId = params[0];
            position = params[1];
            String userid = utils.ReadSharePrefrence(context, Constant.UserId);
            String storeid = utils.ReadSharePrefrence(context, Constant.UserStoreId);
            String Url = Constant.BASE_URL + "user/add_product_cart/" + userid + "/" + storeid + "/" + productId + "?quantity=1";
            Log.d("AddRujulUrl", Url);
            return utils.MakeServiceCall(Url);

        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);

                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    Toast.makeText(context, "Product added to cart", Toast.LENGTH_SHORT).show();
                    int pos = Integer.parseInt(position);
                    arrayPopular.get(pos).setCarstStatus("1");
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            pd.dismiss();
        }


    }


    private class RemoveCart extends AsyncTask<String, String, String>
    { ProgressDialog pd;
        String pos;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String productId = params[0];
             pos = params[1];


            String userid = utils.ReadSharePrefrence(context, Constant.UserId);
            String storeid = utils.ReadSharePrefrence(context, Constant.UserStoreId);
            String Url = Constant.BASE_URL + "user/remove_from_cart/" + userid + "/" +storeid+"/"+ productId;
            Log.d("RemoveURL",Url);
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                 if (jsonObject.getString("status").equalsIgnoreCase("true"))
                 {
                     int pos1 = Integer.parseInt(pos);
                     Toast.makeText(context, "Product removed from favourite list.", Toast.LENGTH_SHORT).show();
                     arrayPopular.get(pos1).setCarstStatus("0");
                     notifyDataSetChanged();
                 }
                else
                 {
                     Toast.makeText(context,"something went wrong",Toast.LENGTH_SHORT).show();
                 }
            } catch (JSONException e) {
                e.printStackTrace();
            }
    pd.dismiss();

        }
    }
}
