package app.kibbeh.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import app.kibbeh.Activity.RecipesDetailActivity;
import app.kibbeh.R;
import app.kibbeh.model.RecipesDetails;

/**
 * Created by Ravi Tamada on 18/05/16.
 */
public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.MyViewHolder> {

    private Context mContext;
    private List<RecipesDetails> recipesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView recipesImage;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.adapter_recipes_title);
            recipesImage = (ImageView) view.findViewById(R.id.adapter_recipes_image);
        }
    }


    public RecipesAdapter(Context mContext, List<RecipesDetails> recipesList) {
        this.mContext = mContext;
        this.recipesList = recipesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_recipes, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        RecipesDetails album = recipesList.get(position);
        final String pos =recipesList.get(position).getId();
        holder.title.setText(album.getName());
        //holder.count.setText(album.getNumOfSongs() + " songs");

        // loading album cover using Glide library
//        holder.thumbnail.setImageDrawable(ContextCompat.getDrawable(mContext, album.getImage()));
        Glide.with(mContext).load(album.getBackground_image()).placeholder(R.drawable.ic_placeholder).into(holder.recipesImage);

    holder.recipesImage.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent i = new Intent(mContext,RecipesDetailActivity.class);
            i.putExtra("id",pos);
            mContext.startActivity(i);
        }
    });


    }


    @Override
    public int getItemCount() {
        return recipesList.size();
    }
}