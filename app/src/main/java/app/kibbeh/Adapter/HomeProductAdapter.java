package app.kibbeh.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;

import app.kibbeh.model.Product;

/**
 * Created by archirayan on 08-Oct-16.
 */

public class HomeProductAdapter extends BaseAdapter  {
    public LayoutInflater inflater;
    Context context;
    ArrayList<Product> array;

    public HomeProductAdapter(Context context, ArrayList<Product> array) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.array = array;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return null;
    }

//    @Override
//    public View getHeaderView(final int position, View convertView, ViewGroup parent) {
//        HeaderViewHolder holder;
//        if (convertView == null) {
//            holder = new HeaderViewHolder();
//            convertView = inflater.inflate(R.layout.adapter_home_product_header, parent, false);
//            holder.text = (TextView) convertView.findViewById(R.id.adapter_home_product_header_tv);
//            holder.viewmoreTv = (TextView) convertView.findViewById(R.id.adapter_home_product_header_view_tv);
//            convertView.setTag(holder);
//        } else {
//            holder = (HeaderViewHolder) convertView.getTag();
//        }
//        //set header text as first char in name
////        String headerText = "" + array.get(position).getBrand().subSequence(0, 1).charAt(0);
//        holder.text.setText(array.get(position).getMainCat());
//        holder.viewmoreTv.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context, "" + array.get(position).getMainCat(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        return convertView;
//    }
//
//    @Override
//    public long getHeaderId(int position) {
//        return array.get(position).getMainCat().subSequence(0, 1).charAt(0);
//    }
//
//    @Override
//    public int getCount() {
//        return array.size();
//    }
//
//    @Override
//    public Object getItem(int position) {
//        return array.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(final int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//
//        if (convertView == null) {
//            holder = new ViewHolder();
//            convertView = inflater.inflate(R.layout.adapter_home_product, parent, false);
//            holder.titleText = (TextView) convertView.findViewById(R.id.tvTitle_adapter_home_product);
//            holder.priceText = (TextView) convertView.findViewById(R.id.tvPrice_adapter_home_product);
//            holder.productImage = (ImageView) convertView.findViewById(R.id.imageView8);
//            holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.adapter_home_product_linear);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        holder.titleText.setText(array.get(position).getName());
//        holder.priceText.setText("$" + array.get(position).getPrice());
//        Glide.with(context).load(array.get(position).getImage()).placeholder(R.drawable.ic_placeholder).fitCenter().into(holder.productImage);
////        holder.titleText.setText(array.get(position).getName());
//        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent productDetailsIntent = new Intent(context, ProductDetails.class);
//                productDetailsIntent.putExtra("id", array.get(position).getId());
//                context.startActivity(productDetailsIntent);
//            }
//        });
//
//
//        return convertView;
//    }
//
//    class HeaderViewHolder {
//        TextView text, viewmoreTv;
//    }
//
//    class ViewHolder {
//        TextView titleText, priceText;
//        LinearLayout linearLayout;
//        ImageView productImage;
//    }
}
