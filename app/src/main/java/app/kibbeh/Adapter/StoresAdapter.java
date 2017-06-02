package app.kibbeh.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import app.kibbeh.Activity.MainActivity;
import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.Utils;
import app.kibbeh.Fragment.HelpFragment;
import app.kibbeh.R;
import app.kibbeh.model.StoreDetails;

/**
 * Created by archi1 on 11/12/2016.
 */

public class StoresAdapter extends RecyclerView.Adapter<StoresAdapter.MyViewHolder>{

    private Context mContext;
    private List<StoreDetails> storeList;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView title;
        public ImageView storeImage;


        public MyViewHolder(View view) {

            super(view);
            title = (TextView)view.findViewById(R.id.adapter_stores_title);
            storeImage=(ImageView)view.findViewById(R.id.adapter_stores_image);
        }
    }

    public StoresAdapter(Context mContext, List<StoreDetails> storeList){
        this.mContext = mContext;
        this.storeList =storeList;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_stores,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        StoreDetails album = storeList.get(position);
        final String pos = storeList.get(position).getStoreId();
        holder.title.setText(album.getStoreName());
        Glide.with(mContext).load(album.getStoreLogo()).placeholder(R.drawable.ic_placeholder).into(holder.storeImage);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.WriteSharePrefrence(mContext, Constant.StorId,storeList.get(position).getStoreId());
                Utils.WriteSharePrefrence(mContext,Constant.StorLogo,storeList.get(position).getStoreLogo());
                Intent i = new Intent(mContext, MainActivity.class);
                mContext.startActivity(i);

            }
        });


    }

    @Override
    public int getItemCount() {
        return storeList.size();
    }


}
