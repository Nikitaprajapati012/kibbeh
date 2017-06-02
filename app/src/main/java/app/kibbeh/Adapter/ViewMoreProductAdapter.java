package app.kibbeh.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
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
import app.kibbeh.Constant.MakeServiceCall;
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;
import app.kibbeh.model.Product;
import app.kibbeh.model.ViewMoreProduct;

import static android.R.attr.onClick;
import static android.R.attr.track;

/**
 * Created by archi on 11/9/2016.
 */

public class ViewMoreProductAdapter extends RecyclerView.Adapter<ViewMoreProductAdapter.MyViewHolder> {
    public ArrayList<ViewMoreProduct> arrayMoreProduct;
    public Context context;
    public Utils utils;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price, tvRemove, tvAdd;
        public LinearLayout linearLayout;
        public ImageView thumbnail, ivAddFav;
        public MyViewHolder(View view) {
            super(view);

            name = (TextView) view.findViewById(R.id.tvTitle_adapter_home_product);
            price = (TextView) view.findViewById(R.id.tvPrice_adapter_home_product);
            thumbnail = (ImageView) view.findViewById(R.id.imageView8);
            ivAddFav = (ImageView) view.findViewById(R.id.ivAddFave_HomeAdapter);
            tvAdd = (TextView) view.findViewById(R.id.tvAdd_HomeAdapter);
            linearLayout = (LinearLayout) view.findViewById(R.id.adapter_home_product_linear);
            tvRemove = (TextView) view.findViewById(R.id.tvRemove_HomeAdapter);
        }

    }

    public ViewMoreProductAdapter(Context context,ArrayList<ViewMoreProduct> arrayMoreProduct) {
        this.arrayMoreProduct = arrayMoreProduct;
        this.context=context;
        this.utils = new Utils(context);

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_view_more_product,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        final ViewMoreProduct moreProduct= arrayMoreProduct.get(position);
        holder.name.setText(moreProduct.getName());
        holder.price.setText("$" +moreProduct.getPrice());
        Glide.with(context).load(moreProduct.getImage()).into(holder.thumbnail);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productDescriptionIntent = new Intent(context, ProductDetails.class);
                productDescriptionIntent.putExtra("id",arrayMoreProduct.get(position).getId());
                context.startActivity(productDescriptionIntent);
            }
        });

        holder.ivAddFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (moreProduct.getIs_Fav().equalsIgnoreCase("1")){
                    new RemoveFavourite(moreProduct.getFav_Id(),position,holder.ivAddFav).execute();
                }else {
                    new AddFavourite(moreProduct.getId(), position,holder.ivAddFav).execute();

                }
            }
        });



        if (moreProduct.getIs_Fav().equalsIgnoreCase("1"))
        {
            holder.ivAddFav.setImageDrawable(ContextCompat.getDrawable(context,R.drawable.ic_red_heart));
        }else {
            holder.ivAddFav.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_blue_heart));
        }



        if (Integer.parseInt(moreProduct.getAdded_Cart()) >= 1){
            holder.tvRemove.setVisibility(View.VISIBLE);
        }else {
            holder.tvRemove.setVisibility(View.INVISIBLE);
        }

        holder.tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ProductAddToCart(moreProduct.getId(), position).execute();
            }
        });




        holder.tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ProductRemoveToCart(moreProduct.getId(), position).execute();
            }
        });



    }

    @Override
    public int getItemCount() {
        return arrayMoreProduct.size();
    }

    private class AddFavourite extends AsyncTask<String, String, String>{
        ProgressDialog pd;
        String moreProductId;
        int pos;
        ImageView ivFavId;
        public AddFavourite(String id, int pos, ImageView ivAddFav) {

            this.moreProductId = id;
            this.pos = pos;
            this.ivFavId = ivAddFav;
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

            String userid = utils.ReadSharePrefrence(context,Constant.UserId);
            String Url = Constant.BASE_URL + "product/add_fav/" + userid + "/" + moreProductId;
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                Log.d("Respond",s);
                if (object.getString("status").equalsIgnoreCase("true")){
                    Toast.makeText(context, "Product added to favourite list.", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    arrayMoreProduct.get(pos).setIs_Fav("1");


                } else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
        }
    }


    private class RemoveFavourite extends AsyncTask<String, String, String>{

        public ProgressDialog pd;
        String moreProductId;
        public int pos;


        public RemoveFavourite(String id, int pos, ImageView ivAddFav) {
            this.moreProductId = id;
            this.pos = pos;
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
            String userid = utils.ReadSharePrefrence(context, Constant.UserId);
            String Url = Constant.BASE_URL +"product/remove_fav/" + userid + "/" + moreProductId;
            return utils.MakeServiceCall(Url);
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("status").equalsIgnoreCase("true")){
                    Toast.makeText(context,object.getString("msg"), Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    arrayMoreProduct.get(pos).setIs_Fav("0");
                    notifyDataSetChanged();

                }else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
        }
    }



    private class ProductAddToCart extends AsyncTask<String, String, String>{
        public ProgressDialog pd;
        public String moreproductId;
        public int pos;

        public ProductAddToCart(String id, int pos) {

            this.moreproductId = id;
            this.pos = pos;
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
            String userid = utils.ReadSharePrefrence(context,Constant.UserId);
            String storeid = utils.ReadSharePrefrence(context,Constant.UserStoreId);
            String Url =   Constant.BASE_URL + "user/add_product_cart/"+userid+"/"+storeid+"/"+moreproductId+"?quantity=1";
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("status").equalsIgnoreCase("true")){
                    Toast.makeText(context, "Product Added To Cart", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    arrayMoreProduct.get(pos).setAdded_Cart("1");
                    notifyDataSetChanged();
                }
                else
                {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
        }
    }

    private class ProductRemoveToCart extends AsyncTask<String, String, String> {

        public ProgressDialog pd;
        public String moreproductId;
        public int pos;
        public ProductRemoveToCart(String id, int position) {

            this.moreproductId = id;
            this.pos = position;
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
            String userid = utils.ReadSharePrefrence(context,Constant.UserId);
            String storeid = utils.ReadSharePrefrence(context, Constant.UserStoreId);
            String Url = Constant.BASE_URL +"user/remove_from_cart/" + userid + "/"+storeid+"/"+moreproductId;

            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);

                if (object.getString("status").equalsIgnoreCase("true")){
                    Toast.makeText(context, "Product removed from cart", Toast.LENGTH_SHORT).show();
                    arrayMoreProduct.get(pos).setAdded_Cart("0");
                    notifyDataSetChanged();
                }
                else {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
        }
    }
}
