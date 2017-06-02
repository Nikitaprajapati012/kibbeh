package app.kibbeh.Fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.MakeServiceCall;
import app.kibbeh.Constant.Util;
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;

/**
 * Created by archi on 10/29/2016.
 */

public class HelpFragment extends Fragment {
    public WebView webView;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_help, container, false);
        new pdfRead().execute();
        webView = (WebView) rootView.findViewById(R.id.frg_help_wembview);
        return rootView;

    }

    private class pdfRead extends AsyncTask<String, String, String>{
        public ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(getActivity());
            pd.setMessage("Loading....");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String Url = Constant.BASE_URL + "faq_and_help_content";
            Utils utils = new Utils(getActivity());
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {

            super.onPostExecute(s);
            try {
                    JSONObject jobj = new JSONObject(s);
                    String status =  jobj.getString("status");


                    if (status.equalsIgnoreCase("true")){
                    String pdf =  jobj.getString("data");
                    String url1 = "http://docs.google.com/gview?embedded=true&url=" + pdf;

                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.getSettings().setPluginState(WebSettings.PluginState.ON);
                    webView.loadUrl(url1);
                        pd.dismiss();
                }
                else
                    {
                        Toast.makeText(getActivity(),getString(R.string.somethig_went_wrong),Toast.LENGTH_SHORT).show();
                        pd.dismiss();
                    }


            } catch (JSONException e) {
                e.printStackTrace();
                pd.dismiss();
            }

        }
    }


}
