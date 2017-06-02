package app.kibbeh.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import app.kibbeh.Adapter.RecipesAdapter;
import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;
import app.kibbeh.model.RecipesDetails;

/**
 * Created by archirayan on 13-Oct-16.
 */

public class Recipes extends android.support.v4.app.Fragment {

    public RecyclerView recyclerView;
    public Utils utils;
    public ArrayList<RecipesDetails> recipesArray;
    private RecipesAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragmemt_recipes, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.fragment_recipes_recycler);
        utils = new Utils(getActivity());

        if (utils.isConnectingToInternet()) {

            new getRecipes().execute();

        } else
        {
            Toast.makeText(getActivity(),"NO INTERNET CONNECTION",Toast.LENGTH_SHORT).show();
        }

        return view;
    }

    private class getRecipes extends AsyncTask<String, String, String> {
        public ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            recipesArray = new ArrayList<>();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {

            String Url = Constant.BASE_URL + "recipes?store_id=" + Utils.ReadSharePrefrence(getActivity(), Constant.UserStoreId);
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject mainObject = new JSONObject(s);
                if (mainObject.getString("status").equalsIgnoreCase("true")) {

                    JSONArray array = mainObject.getJSONObject("data").getJSONArray("lists");
                    for (int i = 0; i < array.length(); i++) {

                        JSONObject object = array.getJSONObject(i);
                        RecipesDetails details = new RecipesDetails();
                        details.setName(object.getString("name"));
                        details.setId(object.getString("id"));
                        details.setBackground_image(object.getString("background_image_url"));
                        details.setDescription(object.getString("description"));
                        details.setDirection(object.getString("direction"));
                        details.setCategory_id(object.getString("category_id"));
                        details.setOwner_id(object.getString("owner_id"));
                        details.setCreated_at(object.getString("created_at"));
                        details.setUpdated_at(object.getString("updated_at"));
                        details.setFav_count(object.getString("fav_count"));
                        recipesArray.add(details);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            mAdapter = new RecipesAdapter(getActivity(), recipesArray);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            pd.dismiss();
        }
    }
}
