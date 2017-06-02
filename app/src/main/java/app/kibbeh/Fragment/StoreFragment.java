package app.kibbeh.Fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
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

import app.kibbeh.Adapter.StoresAdapter;
import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;
import app.kibbeh.model.StoreDetails;

/**
 * Created by archi1 on 11/12/2016.
 */

public class StoreFragment extends android.support.v4.app.Fragment
{

    public RecyclerView recyclerView;
    public Utils utils;
    public ArrayList<StoreDetails> storeArray;
    private StoresAdapter mAdapter;
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragmemt_store,container,false);
        recyclerView=(RecyclerView)view.findViewById(R.id.fragment_store_recycler);
        utils = new Utils(getActivity());

        if (utils.isConnectingToInternet() == true)
        {

            new getStore().execute();
        }else {

            Toast.makeText(getActivity(), "No INTERNET CONNECTION", Toast.LENGTH_SHORT).show();
        }

        return view;
    }
    /*public StoreFragment() {
        // Required empty public constructor
    }*/

    private class getStore extends AsyncTask<String, String, String>{

        public ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String Url = Constant.BASE_URL +"user/stores/"+ Utils.ReadSharePrefrence(getActivity(),Constant.ZipCode);
            /*http://web-medico.com/web2/kibbeh/api/v1/user/stores/22201*/
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                storeArray = new ArrayList<>();
                JSONObject mainobject = new JSONObject(s);



                if (mainobject.getString("status").equalsIgnoreCase("true")){

                    JSONArray jsonArray = mainobject.getJSONArray("stores");


                    for (int i=0;i<jsonArray.length();i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        StoreDetails details = new StoreDetails();
                        Utils.WriteSharePrefrence(getActivity(),Constant.StoreName,jsonObject.getString("name"));
                        details.setStoreId(jsonObject.getString("id"));
                        details.setStoreName(jsonObject.getString("name"));
                        details.setStoreLogo(jsonObject.getString("logo_url"));
                        details.setStoreWhiteLogo(jsonObject.getString("logo_white_url"));
                        details.setZipCode(jsonObject.getString("zipcode"));
                        storeArray.add(details);



//                        String strId = String.valueOf(jsonObject.getInt("id"));
//                        String strName = jsonObject.getString("name");
//                        String strLogo = jsonObject.getString("logo_url");
//                        String strWhiteLogo = jsonObject.getString("logo_white_url");
//                        String strZipCode = jsonObject.getString("zipcode");

                    }



                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            mAdapter = new StoresAdapter(getActivity(),storeArray);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(mAdapter);
            pd.dismiss();
            //mAdapter = new StoresAdapter(getActivity(),storeArray);


        }
    }
}
