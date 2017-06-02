package app.kibbeh.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.kibbeh.Adapter.FavoriteListAdapter;
import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;
import app.kibbeh.model.Favorite;

/**
 * Created by archirayan on 20-Oct-16.
 * FAVORITE PRODUCT LIST
 * http://web-medico.com/web2/kibbeh/api/v1/product/list_add_fav/87/1
 */

public class FavoriteFragment extends Fragment {
    public ArrayList<Favorite> favorateList;
    public FavoriteListAdapter favoriteAdapter;
    public RecyclerView rvFavoriteList;
    public Utils utils;

//    public FavoriteFragment() {
//        // Required empty public constructor
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        rvFavoriteList = (RecyclerView) rootView.findViewById(R.id.rvFavoriteList);
        utils = new Utils(getActivity());

//        if (utils.isConnectingToInternet()) {
        new getFavoriteList().execute();
//        } else {
//            Toast.makeText(getActivity(), "No internet connection found please check your internet conectivity", Toast.LENGTH_SHORT).show();
//        }


        return rootView;
    }


    private class getFavoriteList extends AsyncTask<String, String, String> {

        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            favorateList = new ArrayList<>();
            pd.show();

        }

        @Override
        protected String doInBackground(String... params) {
            //http://web-medico.com/web2/kibbeh/api/v1/product/list_add_fav/87/1
            String Url = Constant.BASE_URL + "product/list_add_fav/" + utils.ReadSharePrefrence(getActivity(), Constant.UserId) + "/" + utils.ReadSharePrefrence(getActivity(), Constant.UserStoreId);
            Log.e("URL FAV", "FAV  >> " + Url);
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
                    JSONArray favoriteArray = mainObject.getJSONArray("products");
                    Log.e("SIZE","11>> "+favoriteArray.length());
                    for (int i = 0; i < favoriteArray.length(); i++) {
                        JSONObject productObject = favoriteArray.getJSONObject(i);
                        Favorite favorite = new Favorite();
                        favorite.setId(productObject.getString("id"));
                        favorite.setFav_id(productObject.getString("fav_id"));
                        favorite.setName(productObject.getString("name"));
                        favorite.setPrice(productObject.getString("price"));
                        favorite.setImage_url(productObject.getString("image_url"));
                        favorateList.add(favorite);
                    }
                }else{
                        Toast.makeText(getActivity(), ""+mainObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.e("SIZE",">> "+favorateList.size());
            favoriteAdapter = new FavoriteListAdapter(getActivity(), favorateList);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            llm.setOrientation(LinearLayoutManager.VERTICAL);
//            recyclerView.setItemAnimator(new DefaultItemAnimator());
            rvFavoriteList.setLayoutManager(llm);
            rvFavoriteList.setAdapter(favoriteAdapter);
        }
    }

}
