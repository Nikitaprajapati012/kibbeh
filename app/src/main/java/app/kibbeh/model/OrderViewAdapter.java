package app.kibbeh.model;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import app.kibbeh.Activity.OrderActivity;
import app.kibbeh.R;

/**
 * Created by Ravi Archi on 12/23/2016.
 */
public class OrderViewAdapter extends ArrayAdapter<OrderDetailsView>{

    int layoutResourceId;
    Context mContext;
    ArrayList<OrderDetailsView> arraydata;
    public OrderViewAdapter(Context orderActivity, int orderview_items_field, ArrayList<OrderDetailsView> detailsarray) {
        super(orderActivity,orderview_items_field,detailsarray);
        this.layoutResourceId = orderview_items_field;
        this.mContext = orderActivity;
        this.arraydata = detailsarray;

    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolderItem viewHolder;
        View v = convertView;
        if(v==null) {
            LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();

            convertView = inflater.inflate(layoutResourceId, parent, false);
            viewHolder = new ViewHolderItem();
            viewHolder.tv_productname = (TextView) convertView.findViewById(R.id.txt_orderview_productName);
            viewHolder.tv_quantity = (TextView) convertView.findViewById(R.id.txt_orderview_quantity);
            viewHolder.tv_price = (TextView) convertView.findViewById(R.id.txt_orderviewPrice);
            viewHolder.img_productImage = (ImageView) convertView.findViewById(R.id.img_orderviewImage);
            convertView.setTag(viewHolder);

        }else {
            viewHolder = (ViewHolderItem) convertView.getTag();
        }
        OrderDetailsView objectItem = arraydata.get(position);

        if(objectItem != null) {

            viewHolder.tv_productname.setText(objectItem.getOrderprodetname());
            viewHolder.tv_quantity.setText(objectItem.getOerderquantity());
            viewHolder.tv_price.setText(objectItem.getOrderprice());
            Glide.with(mContext).load(objectItem.getOrderimage()).into(viewHolder.img_productImage);
        }

        return convertView;

    }
    static class ViewHolderItem{
        TextView tv_productname;
        TextView tv_quantity;
        TextView tv_price;
        ImageView img_productImage;
    }
}
