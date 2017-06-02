package app.kibbeh.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;
import app.kibbeh.model.Product;

/**
 * Created by archirayan on 17-Oct-16.
 */

public class ProductHeadersAdapter extends RecyclerView.Adapter<ProductHeadersAdapter.MyViewHolder> {
    public Context context;
    public ArrayList<Product> array;
    public Utils utils;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name, price, tvRemove, tvAdd;
        public LinearLayout linearLayout;
        public ImageView thumbnail, ivAddFav;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.tvTitle_adapter_home_product);
            price = (TextView) view.findViewById(R.id.tvPrice_adapter_home_product);
            thumbnail = (ImageView) view.findViewById(R.id.adapter_home_image);
            ivAddFav = (ImageView) view.findViewById(R.id.ivAddFave_HomeAdapter);
            tvAdd = (TextView) view.findViewById(R.id.tvAdd_HomeAdapter);
            linearLayout = (LinearLayout) view.findViewById(R.id.adapter_home_product_linear);
            tvRemove = (TextView) view.findViewById(R.id.tvRemove_HomeAdapter);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.ivAddFave_HomeAdapter:
                    break;

                case R.id.tvAdd_HomeAdapter:
                    break;

                case R.id.tvRemove_HomeAdapter:
                    break;

            }
        }
    }


    public ProductHeadersAdapter(Context activity, ArrayList<Product> arrayProduct) {
        this.context = activity;
        this.array = arrayProduct;
        this.utils = new Utils(context);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_home_product, parent, false);

        return new ProductHeadersAdapter.MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Product product = array.get(position);
        holder.name.setText(product.getName());
        holder.price.setText("$ "+product.getPrice());
        Glide.with(context).load(product.getImage()).into(holder.thumbnail);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productDetailsIntent = new Intent(context, ProductDetails.class);
                productDetailsIntent.putExtra("id", array.get(position).getId());
                context.startActivity(productDetailsIntent);
            }
        });
        holder.ivAddFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (product.getIs_Fav().equalsIgnoreCase("1")) {
                    new RemoveFavourite(product.getFav_Id(), position).execute();
                } else {
                    new AddFavourite(product.getId(), position).execute();
                }
            }
        });
//       Is Favourite =1 then red heart or blue heart
        if (product.getIs_Fav().equalsIgnoreCase("1")) {
            holder.ivAddFav.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_red_heart));
        } else {
            holder.ivAddFav.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_blue_heart));
        }

//      If is_added is more than 1 then remove button appears
        if (Integer.parseInt(product.getAdded_Cart()) >= 1) {
            holder.tvRemove.setVisibility(View.VISIBLE);
        } else {
            holder.tvRemove.setVisibility(View.INVISIBLE);
        }

//      Add to cart
        holder.tvAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ProductAddToCart(product.getId(), position).execute();
            }
        });
//       Remove from cart
        holder.tvRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ProductRemoveToCart(product.getId(), position).execute();
            }
        });
    }

    @Override
    public int getItemCount() {
        return array.size();
    }

    private class AddFavourite extends AsyncTask<String, String, String> {

        ProgressDialog pd;
        String productId;
        int pos;

        public AddFavourite(String id, int pos) {
            this.productId = id;
            this.pos = pos;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //http://web-medico.com/web2/kibbeh/api/v1/product/add_fav/84/25
            String userid = utils.ReadSharePrefrence(context, Constant.UserId);
            String Url = Constant.BASE_URL + "product/add_fav/" + userid + "/" + productId;
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("status").equalsIgnoreCase("true")) {
                    Toast.makeText(context, "Product added to favourite list.", Toast.LENGTH_SHORT).show();
                    array.get(pos).setIs_Fav("1");
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

    private class RemoveFavourite extends AsyncTask<String, String, String> {

        public ProgressDialog pd;
        public String productId;
        public int pos;

        public RemoveFavourite(String id, int pos) {
            this.productId = id;
            this.pos = pos;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //http://web-medico.com/web2/kibbeh/api/v1/product/add_fav/84/25
            String userid = utils.ReadSharePrefrence(context, Constant.UserId);
            String Url = Constant.BASE_URL + "product/remove_fav/" + userid + "/" + productId;
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("status").equalsIgnoreCase("true")) {
                    Toast.makeText(context, "Product removed from favourite list.", Toast.LENGTH_SHORT).show();
                    array.get(pos).setIs_Fav("0");
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
    private class ProductAddToCart extends AsyncTask<String, String, String> {

        public ProgressDialog pd;
        public String productId;
        public int pos;

        public ProductAddToCart(String id, int pos) {
            this.productId = id;
            this.pos = pos;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //http://web-medico.com/web2/kibbeh/api/v1/product/add_fav/84/25
            String userid = utils.ReadSharePrefrence(context, Constant.UserId);
            String storeid = utils.ReadSharePrefrence(context, Constant.UserStoreId);
            String Url = Constant.BASE_URL + "user/add_product_cart/"+userid+"/"+storeid+"/"+productId+"?quantity=1";
            Log.d("AddRujulUrl",Url);
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("status").equalsIgnoreCase("true")) {

                    Toast.makeText(context, "Product Added to Cart.", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    array.get(pos).setAdded_Cart("1");

                }
                else
                {

                    Toast.makeText(context, "Something went wrong.", Toast.LENGTH_SHORT).show();

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            pd.dismiss();
        }
    }
    private class ProductRemoveToCart extends AsyncTask<String, String, String> {

        public ProgressDialog pd;
        public String productId;
        public int pos;

        public ProductRemoveToCart(String id, int pos) {
            this.productId = id;
            this.pos = pos;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(context);
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //http://web-medico.com/web2/kibbeh/api/v1/product/add_fav/84/25
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
                JSONObject object = new JSONObject(s);
                if (object.getString("status").equalsIgnoreCase("true")) {
                    Toast.makeText(context, "Product removed from favourite list.", Toast.LENGTH_SHORT).show();
                    array.get(pos).setAdded_Cart("0");
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
}
