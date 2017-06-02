package app.kibbeh.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.text.DecimalFormat;
import java.util.ArrayList;

import app.kibbeh.Activity.CheckOutActivity;
import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;
import app.kibbeh.model.CartList;
import app.kibbeh.model.StoreDetails;

/*** Created by archi on 10/29/2016.
 */

public class CartListAdapter extends RecyclerView.Adapter<CartListAdapter.MyViewHolder> implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {
    public ArrayList<CartList> arrayList;
    public Context context;
    public TextView tvTotalPrice, tvAmount;
    public double /*dblTotalAmount,*/ price, amountPerPerson;
    public int total,countPlus;
    public CartList details;
    public String grandTotal, TaxTotal, productPriceTotal, deliveryTotal;

    public CartListAdapter(Context context, ArrayList<CartList> arrylist, TextView tvTotalCount, TextView tvCartAmount) {
        this.arrayList = arrylist;
        this.context = context;
        this.tvTotalPrice = tvTotalCount;
        this.tvAmount = tvCartAmount;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_cart_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        details = arrayList.get(position);
        grandTotal = (Utils.ReadSharePrefrence(context, Constant.GRAND_TOTAL));
        TaxTotal = (Utils.ReadSharePrefrence(context, Constant.ALL_PRODUCT_TAX));
        productPriceTotal = (Utils.ReadSharePrefrence(context, Constant.ALL_PRODUCT_TOTAL));
        deliveryTotal = (Utils.ReadSharePrefrence(context, Constant.ALL_PRODUCT_DELIVERY_CHARGE));
        holder.tvPname.setText(arrayList.get(position).getpName());
        holder.tvprice.setText(arrayList.get(position).getpPrice());
        holder.tvquentty.setText(arrayList.get(position).getpCartTotal());
        Glide.with(context).load(arrayList.get(position).getpImage()).into(holder.ivImage);
        total = Integer.parseInt(arrayList.get(position).getpCartTotal());
        price = Double.valueOf(arrayList.get(position).getpPrice());
        amountPerPerson = (total * price);
        tvTotalPrice.setText("$ " + grandTotal);
        tvAmount.setText("$ " + productPriceTotal);
        holder.tvprice.setText("$ " + String.valueOf(new DecimalFormat("##.##").format(amountPerPerson)));

        // TODO: 5/15/2017 perform quantity up event
        holder.ivqunttyUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int temp = position;
                int total = Integer.parseInt(holder.tvquentty.getText().toString());
                holder.tvquentty.setText(String.valueOf(total + 1));
                total = total + 1;
                Float priceTotal = Float.valueOf((arrayList.get(position).getpPrice()));
                productPriceTotal = String.valueOf(Float.valueOf(productPriceTotal) + priceTotal);
               /* if (dblTotalAmount >= 30) {
                    if (countPlus == 0) {
                        dblTotalAmount = dblTotalAmount + 10;
                    }
                    countPlus++;
                }*/
                String totalAmount = "$" + String.valueOf(new DecimalFormat("##.##").format(Double.parseDouble(productPriceTotal)));
                tvAmount.setText(totalAmount);
                Double productPrice = null;
              //  if (countPlus == 0) {
                    productPrice = Double.parseDouble(arrayList.get(temp).getpProductTotal()) + Double.parseDouble(arrayList.get(temp).getpPrice());
                    String subtotalProduct = "$" + String.valueOf(new DecimalFormat("##.##").format(productPrice));
                    holder.tvprice.setText(subtotalProduct);
               // }
                //  countPlus++;
             grandTotal(arrayList, holder, position,productPriceTotal);
            }
        });

        final int temp = position;
        // TODO: 5/15/2017 perform quantity down event
        holder.ivqunttyDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.tvquentty.getText().toString().equalsIgnoreCase("1")) {
                    Toast.makeText(context, "select atlest 1", Toast.LENGTH_LONG).show();
                } else {
                    int total = Integer.parseInt(holder.tvquentty.getText().toString());
                    holder.tvquentty.setText(String.valueOf(total - 1));
                    total = total - 1;
                    Float priceTotal = Float.valueOf((arrayList.get(position).getpPrice()));
                    productPriceTotal = String.valueOf(Float.valueOf(productPriceTotal) - priceTotal);
                    /*if (dblTotalAmount < 30) {
                        if (countPlus == 0) {
                            dblTotalAmount = dblTotalAmount - 10;
                        }
                        countPlus--;
                    }*/
                    String totalAmount = "$" + String.valueOf(new DecimalFormat("##.##").format(Double.parseDouble(productPriceTotal)));
                    tvAmount.setText(totalAmount);
                    Double productPrice = Double.parseDouble(arrayList.get(temp).getpProductTotal()) - Double.parseDouble(arrayList.get(temp).getpPrice());
                    Log.d("subtotalproduct","DOWN"+productPrice);
                    String subtotalProduct = "$" + String.valueOf(new DecimalFormat("##.##").format(productPrice));
                    holder.tvprice.setText(subtotalProduct);
                    grandTotal(arrayList, holder, position,productPriceTotal);
                }
            }
        });

        tvTotalPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, CheckOutActivity.class);
                context.startActivity(i);
            }
        });
    }

    public void grandTotal(ArrayList<CartList> list, MyViewHolder holder,
                           int position, String productTotal) {

        Double aDouble = Double.valueOf(grandTotal);
        for (int i = 0; i < list.size(); i++) {
            Float priceTotal = Float.valueOf(productTotal);
            Float taxTotal = Float.valueOf(TaxTotal);
            Float deliveryProductTotal = Float.valueOf(deliveryTotal);
           aDouble = Double.valueOf(priceTotal) + taxTotal + deliveryProductTotal;

        }
        String grandTotal = "$" + String.valueOf(new DecimalFormat("##.##").format(aDouble));
        tvTotalPrice.setText(grandTotal);
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_header, parent, false);
        return new RecyclerView.ViewHolder(view) {
        };
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {
        CartList cartList = arrayList.get(position);
        TextView textView = (TextView) holder.itemView.findViewById(R.id.store_name);
        TextView textMinPrice = (TextView) holder.itemView.findViewById(R.id.store_minprice);
        textView.setText(cartList.getpStoreName());
        textMinPrice.setText("$" + cartList.getpStoreMinimumAmount());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    @Override
    public long getHeaderId(int position) {
        return getItem(position).getpStoreName()
                .charAt(0);
    }

    private CartList getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivImage, ivqunttyUp, ivqunttyDown;
        TextView tvPname, tvprice;
        EditText tvquentty;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivImage = (ImageView) itemView.findViewById(R.id.adp_cart_image);
            ivqunttyDown = (ImageView) itemView.findViewById(R.id.adp_cart_iv_count_down);
            ivqunttyUp = (ImageView) itemView.findViewById(R.id.adp_cart_iv_count_up);
            tvPname = (TextView) itemView.findViewById(R.id.adp_cart_name);
            tvprice = (TextView) itemView.findViewById(R.id.adp_tv_cart_total);
            tvquentty = (EditText) itemView.findViewById(R.id.adp_cart_tv_itme_count);
        }
    }
}
