package app.kibbeh.Activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import app.kibbeh.Adapter.AddressDetailsAdapter;
import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;

/**
 * Created by archirayan on 18-Oct-16.
 */

public class AddressDetails extends Activity implements View.OnClickListener {
    ImageView ivAddAddress;
    ArrayList<HashMap<String,String>> addresslist;
    AddressDetailsAdapter adapter;
    private RadioButton radioButton;
    public Utils utils;
    RecyclerView rvAddressList;
    private String labelStr, addressStr, postalcodeStr,typeStr;
    private PopupWindow mPopupWindow;
    private RelativeLayout rlAddressDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        utils = new Utils(AddressDetails.this);
        addresslist = new ArrayList<>();
        init();
        (findViewById(R.id.actionBarBackImage)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                ivAddAddress.setVisibility(View.GONE);
                finish();

            }
        });
        ((TextView) findViewById(R.id.actionBarTitle)).setText("Address List");
        if(utils.isConnectingToInternet()) {
            new getAddressList().execute();
        }else{
            Toast.makeText(AddressDetails.this, "No internet connection found please check your internet conectivity", Toast.LENGTH_SHORT).show();
        }

    }

    private void init() {
        rvAddressList = (RecyclerView) findViewById(R.id.rvAddressList_AccountDetails);
        ivAddAddress = (ImageView) findViewById(R.id.ivAddButton);
        ivAddAddress.setVisibility(View.VISIBLE);
        rlAddressDetails = (RelativeLayout) findViewById(R.id.rlAddressDetails);
        ivAddAddress.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ivAddButton:
                showAddressPopupWindow();
//                showPopup();
                break;
        }
    }

    private void showAddressPopupWindow() {
        final Dialog dialog = new Dialog(AddressDetails.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_add_address_details);
        dialog.setCancelable(false);
        final RadioGroup radioGroup = (RadioGroup)dialog.findViewById(R.id.radioAddressDetails);
        final EditText etLabel = (EditText) dialog.findViewById(R.id.etLabel);
        final EditText etAddress = (EditText) dialog.findViewById(R.id.etAddress);
        final EditText etPostalCode = (EditText) dialog.findViewById(R.id.etPostalCode);
        final RadioButton btResidential = (RadioButton) dialog.findViewById(R.id.radioResidential);
        final RadioButton btBusiness = (RadioButton) dialog.findViewById(R.id.radioBusiness);
        Button btnSave  = (Button) dialog.findViewById(R.id.btSaveAddressDetails);
        Button btnCancel = (Button) dialog.findViewById(R.id.btCancelAddressDetails);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!etLabel.getText().toString().equalsIgnoreCase("")){
                    if(!etAddress.getText().toString().equalsIgnoreCase("")){
                        if(!etPostalCode.getText().toString().equalsIgnoreCase("")){
                            labelStr = etLabel.getText().toString();
                            addressStr = etAddress.getText().toString();
                            postalcodeStr = etPostalCode.getText().toString();

                            // get selected radio button from radioGroup
                            int selectedId = radioGroup.getCheckedRadioButtonId();

                            // find the radiobutton by returned id
                            radioButton = (RadioButton) dialog.findViewById(selectedId);
                            Log.e(">>>>>",">>>"+radioButton.getText());
                            typeStr = radioButton.getText().toString();
                            new saveAddressData().execute();
                            dialog.dismiss();
                        }else{
                            Toast.makeText(AddressDetails.this, "Please Enter Postal Code", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(AddressDetails.this, "Please Enter Address", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(AddressDetails.this, "Please Enter Label", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();


    }


    private class saveAddressData extends AsyncTask<String, String, String> {
        public ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(AddressDetails.this);
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // add_address/54?zipcode=22201&label=newlabel&type=Residential%20&address=thisisaddress
            String userId = utils.ReadSharePrefrence(getApplicationContext(), Constant.UserId);
//            String Url = Constant.BASE_URL + "user/my_account/address/add_address/"+userId+"?zipcode="+postalcodeStr+"&label="+labelStr+"&type="+typeStr+"&address="+addressStr;
            String Url = Constant.BASE_URL + "user/my_account/address/add_address/"+userId+"?zipcode=22201&label="+labelStr+"&type="+typeStr+"&address="+addressStr;
            Log.e("URL",">>"+Url);
            Url = Url.replace(" ","%20");
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("RESPOENSE",">> "+s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if(jsonObject.get("status").toString().equalsIgnoreCase("true")){
                    Toast.makeText(AddressDetails.this, ""+jsonObject.get("msg"), Toast.LENGTH_SHORT).show();
                    adapter.notifyDataSetChanged();
                    JSONObject jsonObject1 = jsonObject.getJSONObject("address");
                    HashMap<String, String> hashmap = new HashMap<>();
                    hashmap.put("id", String.valueOf(jsonObject1.getInt("id")));
                    hashmap.put("user_id", jsonObject1.getString("user_id"));
                    hashmap.put("label", jsonObject1.getString("label"));
                    hashmap.put("type", jsonObject1.getString("type"));
                    hashmap.put("created_at", jsonObject1.getString("created_at"));
                    hashmap.put("updated_at", jsonObject1.getString("updated_at"));
                    hashmap.put("address", jsonObject1.getString("address"));
                    hashmap.put("zipcode", jsonObject1.getString("zipcode"));
                    addresslist.add(hashmap);
                    adapter = new AddressDetailsAdapter(AddressDetails.this,addresslist);

                }else{
                    Toast.makeText(AddressDetails.this, ""+jsonObject.get("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class getAddressList extends AsyncTask<String, String, String> {
        public ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(AddressDetails.this);
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            addresslist.clear();
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //http://web-medico.com/web2/kibbeh/api/v1/user/my_account/address/54
            String userId = utils.ReadSharePrefrence(getApplicationContext(), Constant.UserId);
            String Url = Constant.BASE_URL + "user/my_account/address/"+userId ;
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("RESPOENSE",">ADDRS> "+s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray arry = jsonObject.getJSONArray("address_list");
                Log.e("SIZE",">>"+arry.length());
                if(arry.length() > 0) {
                    for (int i = 0; i < arry.length(); i++) {
                        JSONObject obj = arry.getJSONObject(i);
                        Log.e(">>>OBJ",">>"+obj);
                        HashMap<String, String> hashmap = new HashMap<>();
                        hashmap.put("id", obj.getString("id"));
                        hashmap.put("user_id", obj.getString("user_id"));
                        hashmap.put("label", obj.getString("label"));
                        hashmap.put("type", obj.getString("type"));
                        hashmap.put("created_at", obj.getString("created_at"));
                        hashmap.put("updated_at", obj.getString("updated_at"));
                        hashmap.put("address", obj.getString("address"));
                        hashmap.put("zipcode", obj.getString("zipcode"));
                        addresslist.add(hashmap);
                    }
                }else{
                    Toast.makeText(AddressDetails.this, "No Address in List", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Log.e("AddressList",">>>"+addresslist);
            adapter = new AddressDetailsAdapter(AddressDetails.this,addresslist);
            LinearLayoutManager llm = new LinearLayoutManager(AddressDetails.this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            rvAddressList.setLayoutManager(llm);
            rvAddressList.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }
    }
}
