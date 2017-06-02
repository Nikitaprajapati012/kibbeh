package app.kibbeh.Adapter;

import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import app.kibbeh.model.CartList;


/**
 * Adapter holding a list of animal names of type String. Note that each item must be unique.
 */
public abstract class AnimalsAdapter<VH extends RecyclerView.ViewHolder>
    extends RecyclerView.Adapter<VH> {
  private ArrayList<CartList> items = new ArrayList<>();

  public AnimalsAdapter() {
    setHasStableIds(true);
  }

  public void add(CartList object) {
    items.add(object);
    notifyDataSetChanged();
  }

  public void add(int index,CartList object) {
    items.add(index, object);
    notifyDataSetChanged();
  }

  public void addAll(Collection<? extends CartList> collection) {
    if (collection != null) {
      items.addAll(collection);
      notifyDataSetChanged();
    }
  }

  public void addAll(CartList... items) {
    addAll(Arrays.asList(items));
  }

  public void clear() {
    items.clear();
    notifyDataSetChanged();
  }

  public void remove(String object) {
    items.remove(object);
    notifyDataSetChanged();
  }

  public CartList getItem(int position) {
    return items.get(position);
  }

  @Override
  public long getItemId(int position) {
    return getItem(position).hashCode();
  }

  @Override
  public int getItemCount() {
    return items.size();
  }
}
