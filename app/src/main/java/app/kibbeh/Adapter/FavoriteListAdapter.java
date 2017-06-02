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
import java.util.List;

import app.kibbeh.Activity.ProductDetails;
import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;
import app.kibbeh.model.Favorite;

/**
 * Created by archirayan on 20-Oct-16.
 */

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.MyViewHolder> {

    public Context mContext;
    public List<Favorite> favList;
    public Utils utils;
    public Favorite fav;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, price, tvAdd, tvRemove, tvDelete;
        public ImageView Imgfavorite;
        public LinearLayout llView;
        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.tvTitle_FavoriteAdapter);
            price = (TextView) view.findViewById(R.id.tvPrice_FavoriteAdapter);
            Imgfavorite = (ImageView) view.findViewById(R.id.ivFavoriteImage);
            tvAdd = (TextView) view.findViewById(R.id.tvAddFav_FavoriteAdapter);
            tvRemove = (TextView) view.findViewById(R.id.tvRemoveFav_FavoriteAdapter);
            tvDelete = (TextView) view.findViewById(R.id.tvDeleteFav_FavoriteAdapter);
            llView = (LinearLayout)view.findViewById(R.id.adp_fav_linear);
        }

    }

    public FavoriteListAdapter(Context mContext, ArrayList<Favorite> favoritList) {
        this.mContext = mContext;
        this.favList = favoritList;
        utils = new Utils(mContext);
    }

    @Override
    public FavoriteListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_favorite_list, parent, false);

        return new FavoriteListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FavoriteListAdapter.MyViewHolder holder, final int position) {
        fav = favList.get(position);
        holder.title.setText(fav.getName());
        holder.price.setText("$ "+fav.getPrice());
        Glide.with(mContext).load(fav.getImage_url()).error(R.drawable.ic_placeholder).into(holder.Imgfavorite);
        holder.tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new addToCart(position, fav.getId()).execute();
            }
        });

        holder.tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new removeFromCart(position, fav.getId()).execute();
            }
        });


        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Position for remove

                int modPosition = holder.getLayoutPosition();
                new removeFavorite(modPosition, favList.get(position).getId(), favList.get(position).getFav_id()).execute();

            }
        });

        holder.llView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productDetailsIntent = new Intent(mContext, ProductDetails.class);
                productDetailsIntent.putExtra("id", favList.get(position).getId());
                mContext.startActivity(productDetailsIntent);

            }
        });

    }


    @Override
    public int getItemCount() {
        return favList.size();
    }


    private class addToCart extends AsyncTask<String, String, String> {

        ProgressDialog pd;
        String product_id;
        int pos;

        public addToCart(int position, String id) {
            this.pos = position;
            this.product_id = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //http://web-medico.com/web2/kibbeh/api/v1/user/add_product_cart/87/4/56?quantity=1000
//            Parameters:{user_id}/{store_id}/{product_id}
            String Url = Constant.BASE_URL + "user/add_product_cart/" + utils.ReadSharePrefrence(mContext, Constant.UserId) + "/" + utils.ReadSharePrefrence(mContext, Constant.UserStoreId) + "/" + product_id + "?quantity=1000";
            Log.e("URL ADD", "FAV  >> " + Url);
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Log.e("Response", ">>> " + s);
            try {
                JSONObject mainObject = new JSONObject(s);
                if (mainObject.getString("status").equalsIgnoreCase("true")) {
                    Toast.makeText(mContext, "Product Added To Cart Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "" + mainObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private class removeFromCart extends AsyncTask<String, String, String> {

        ProgressDialog pd;
        String product_id;
        int pos;

        public removeFromCart(int position, String id) {
            this.pos = position;
            this.product_id = id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
//            http://web-medico.com/web2/kibbeh/api/v1/user/remove_from_cart/87/5/567
//            Parameters{user_id}/{store_id}/{product_id}


            String Url = Constant.BASE_URL + "user/remove_from_cart/" + utils.ReadSharePrefrence(mContext, Constant.UserId) + "/" + utils.ReadSharePrefrence(mContext, Constant.UserStoreId) + "/" + product_id;
            Log.e("URL REMOVE", "FAV  >> " + Url);
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Log.e("Response", ">>> " + s);
            try {
                JSONObject mainObject = new JSONObject(s);
                if (mainObject.getString("status").equalsIgnoreCase("true")) {
                    Toast.makeText(mContext, "Product Removed From Cart Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "" + mainObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private class deleteFromCart extends AsyncTask<String, String, String> {

        ProgressDialog pd;
        String product_id;
        String favorite_id;
        int pos;

        public deleteFromCart(int position, String id, String fav_id) {
            this.pos = position;
            this.product_id = id;
            this.favorite_id = fav_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
//            http://web-medico.com/web2/kibbeh/api/v1/product/remove_fav/84/30
//            Parameters:{fav_id}/{product_id}
            String Url = Constant.BASE_URL + "product/remove_fav/" + favorite_id + "/" + product_id;
            Log.e("URL ADD", "FAV  >> " + Url);
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Log.e("Response", ">>> " + s);
            try {
                JSONObject mainObject = new JSONObject(s);
                if (mainObject.getString("status").equalsIgnoreCase("true")) {
                    Toast.makeText(mContext, "Product Deleted From Favorite List", Toast.LENGTH_SHORT).show();
                    //remove item from recycler view
                    notifyItemRemoved(pos);
                    notifyItemRangeChanged(pos, favList.size());
                } else {
                    Toast.makeText(mContext, "" + mainObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private class removeFavorite extends AsyncTask<String, String, String> {
        int pos;
        String productid, favId;
        ProgressDialog pd;

        public removeFavorite(int modPosition, String id, String fav_id) {

            this.pos = modPosition;
            this.productid = id;
            this.favId = fav_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String userId = utils.ReadSharePrefrence(mContext, Constant.UserId);
            String Url = Constant.BASE_URL + "product/remove_fav/" + userId + "/" + favId;
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    favList.remove(pos);
                    notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                Toast.makeText(mContext, "something went wrong", Toast.LENGTH_SHORT).show();
            }

            pd.dismiss();

        }


    }
}