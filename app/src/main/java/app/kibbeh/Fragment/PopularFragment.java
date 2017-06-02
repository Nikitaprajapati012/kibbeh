package app.kibbeh.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import app.kibbeh.Adapter.PopularListAdaper;
import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.Util;
import app.kibbeh.R;
import app.kibbeh.model.Popular;

/**
 * Created by archi on 10/28/2016.
 */

public class PopularFragment extends Fragment
{

    public Util utils;
    public ArrayList<Popular> arrayPopular;
    public PopularListAdaper adapterPopular;
    public RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View RooView = inflater.inflate(R.layout.fragment_popular,container,false);
        utils = new Util();
        recyclerView = (RecyclerView) RooView.findViewById(R.id.recyclerview_popular);

        if (utils.isConnectingToInternet(getActivity()) == true)
        {
            init();
        }
        else
        {
            Toast.makeText(getActivity(),"NO INTERNET CONNECTION",Toast.LENGTH_LONG).show();

        }
        return RooView;
    }

    private void init() {

        new getPopularList().execute();

    }

    private class getPopularList extends AsyncTask<String,String,String>{
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Please Wait...");
            pd.setCancelable(false);
            pd.show();
        }
        @Override
        protected String doInBackground(String... params) {

           // http://web-medico.com/web2/kibbeh/api/v1/product/popular/4/87 {store id}{user id}

            String UserId =  utils.ReadSharePrefrence(getActivity(), Constant.UserId);
            String StoreId = utils.ReadSharePrefrence(getActivity(), Constant.UserStoreId);
            String Url = Constant.BASE_URL + "product/popular/"+StoreId+"/"+UserId;

            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            arrayPopular = new ArrayList<>();

            try {
                JSONObject jsonObject = new JSONObject(s);
                 if (jsonObject.getBoolean("status")==true)
                 {
                     JSONArray jsonArray =jsonObject.getJSONArray("data");

                     for (int i=0;i<jsonArray.length();i++)
                     {
                         JSONObject jsonObjectBeta = jsonArray.getJSONObject(i);
                         Popular popular = new Popular();
                         String id = String.valueOf(jsonObjectBeta.getInt("product_id"));
                         String name = jsonObjectBeta.getString("name");
                         String image =jsonObjectBeta.getString("image_url");
                         String price = String.valueOf(jsonObjectBeta.getInt("price"));
                         String disc = jsonObjectBeta.getString("description");
                         String cart = String.valueOf(jsonObjectBeta.getInt("added_cart"));

                         popular.setId(id);
                         popular.setName(name);
                         popular.setPrice(price);
                         popular.setImage(image);
                         popular.setCarstStatus(cart);
                         popular.setDiscription(disc);
                         arrayPopular.add(popular);
                     }

                     adapterPopular = new PopularListAdaper(getActivity(),arrayPopular);
                     LinearLayoutManager llm = new LinearLayoutManager(getActivity());
                     llm.setOrientation(LinearLayoutManager.VERTICAL);
                     recyclerView.setLayoutManager(llm);
                     recyclerView.setAdapter(adapterPopular);
                 }
                else
                 {
                     Toast.makeText(getActivity(),jsonObject.getString("msg"),Toast.LENGTH_SHORT).show();
                 }


            } catch (JSONException e) {

                Toast.makeText(getActivity(),"something is wrong",Toast.LENGTH_SHORT).show();
            }
            pd.dismiss();
        }
    }
}
