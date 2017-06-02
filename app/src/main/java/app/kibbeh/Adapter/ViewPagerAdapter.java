package app.kibbeh.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;

import app.kibbeh.R;

/**
 * Created by archi on 10/6/2016.
 */

public class ViewPagerAdapter extends PagerAdapter {

    private Context mContext;
    private String[] mResources;

    public ViewPagerAdapter(FragmentActivity activity, String[] mImageResources) {
        mContext = activity;
        mResources = mImageResources;
    }

    @Override
    public int getCount() {
        return mResources.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.pageviever_pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);
//        imageView.setImageResource(mResources[position]);
        Glide.with(mContext).load(mResources[position]).placeholder(R.drawable.ic_placeholder).into(imageView);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);

    }
}
