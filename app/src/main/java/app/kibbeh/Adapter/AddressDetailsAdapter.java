package app.kibbeh.Adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;

import static app.kibbeh.R.id.tvPostcode_Adapter;
import static app.kibbeh.R.id.tvType_Adapter;

/**
 * Created by archirayan on 18-Oct-16.
 */

public class AddressDetailsAdapter extends RecyclerView.Adapter<AddressDetailsAdapter.MyViewHolder> {

    public Utils utils;
    public Context mContext;
    public ArrayList<HashMap<String, String>> addressList;
    String userId,addressidStr,labelStr,typeStr,addressStr,zipcodeStr;
    HashMap<String,String> hashmap;
    String delete_id,address_idStr;
    RadioButton radioButton;


    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView label, type, address, zipcode, tvEdit, tvDelete;

        public MyViewHolder(View view) {
            super(view);
            label = (TextView) view.findViewById(R.id.tvLabel_Adapter);
            type = (TextView) view.findViewById(tvType_Adapter);
            address = (TextView) view.findViewById(R.id.tvAddress_Adapter);
            zipcode = (TextView) view.findViewById(tvPostcode_Adapter);
            tvEdit = (TextView) view.findViewById(R.id.tvEdit_Adapter);
            tvDelete = (TextView) view.findViewById(R.id.tvDelete_Adapter);
        }
    }
    public AddressDetailsAdapter(Context mContext, ArrayList<HashMap<String, String>> addresslist) {
        this.mContext = mContext;
        this.addressList = addresslist;
        utils = new Utils(mContext);
    }

    @Override
    public AddressDetailsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_address_list, parent, false);

        return new AddressDetailsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        hashmap = addressList.get(position);
        Log.e("LABEL",""+hashmap.get("label"));
        Log.e("address",""+hashmap.get("address"));
        holder.label.setText(hashmap.get("label"));
        holder.type.setText(hashmap.get("type"));
        holder.address.setText(hashmap.get("address"));
        holder.zipcode.setText(hashmap.get("zipcode"));
        //holder.count.setText(album.getNumOfSongs() + " songs");
        holder.tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                labelStr = addressList.get(position).get("label");
                addressStr = addressList.get(position).get("address");

                zipcodeStr = addressList.get(position).get("zipcode");
                typeStr =  addressList.get(position).get("type");
                address_idStr = addressList.get(position).get("id");
                Log.e("ON CLICK",">>>>>>>>>>>>>>>");
                Log.e("Label",""+labelStr);
                Log.e("ADDRESS",""+addressStr);
                Log.e("ZIP",""+zipcodeStr);
                Log.e("typeStr",""+typeStr);
                Log.e("ID",""+address_idStr);

                showAddressPopupWindow(position,labelStr,addressStr,zipcodeStr,typeStr);
//                new editAddressFromList().execute();
            }
        });


        holder.tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //                    http://web-medico.com/web2/kibbeh/api/v1/user/my_account/address/delete_address/54?delete_id=31
                delete_id = hashmap.get("id");
                Log.e("delete_id",""+delete_id);
                new DeleteAddressFromList(position).execute();
            }
        });
    }


    @Override
    public int getItemCount() {
        return addressList.size();
    }


    private class editAddressFromList extends AsyncTask<String, String, String> {
        public ProgressDialog pd;
        String addressId, label, type, address, zipcode;
        int position;
        public editAddressFromList(int position, String address_idStr, String labelStr, String typeStr, String addressStr, String zipcodeStr) {
            this.position = position;
            this.addressId = address_idStr;
            this.label = labelStr;
            this.type = typeStr;
            this.address = addressStr;
            this.zipcode = zipcodeStr;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
//            http://web-medico.com/web2/kibbeh/api/v1/user/my_account/address/
// edit_address/541?address_id=31&label=this%20is%20edited%20label&type=Business&address=this%20is%20edit%20adress&zipcode=22201
            userId = utils.ReadSharePrefrence(mContext, Constant.UserId);
            Log.e("UID",""+userId);
            String Url = Constant.BASE_URL + "user/my_account/address/edit_address/"+userId+"?address_id="+addressId+"&label="+label+"&type="+type+"&address="+address+"&zipcode="+zipcode ;
            Url = Url.replace(" ","%20");
            Log.e("URL","EDIT "+Url);
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("RESPOENSE",">ADDRS> "+s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if(jsonObject.getBoolean("status") == true){
                    Toast.makeText(mContext, ""+jsonObject.get("msg"), Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                    HashMap<String, String> hashmap = new HashMap<>();
                    hashmap.put("id",addressId);
                    hashmap.put("user_id", userId);
                    hashmap.put("label", labelStr);
                    hashmap.put("type", typeStr);
                  //  hashmap.put("created_at", obj.getString("created_at"));
                  //  hashmap.put("updated_at", obj.getString("updated_at"));
                    hashmap.put("address", address_idStr);
                    hashmap.put("zipcode", zipcode);
                    addressList.set(position,hashmap);

                }else{
                    Toast.makeText(mContext, ""+jsonObject.get("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }



    private class DeleteAddressFromList extends AsyncTask<String, String, String> {
        public ProgressDialog pd;
        private int pos;

        public DeleteAddressFromList(int position) {
            this.pos = position;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(mContext);
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //http://web-medico.com/web2/kibbeh/api/v1/user/my_account/address/54
            userId = utils.ReadSharePrefrence(mContext, Constant.UserId);
            String Url = Constant.BASE_URL + "user/my_account/address/delete_address/"+userId+"?delete_id="+delete_id ;
            Log.e("URL DELETE",">> "+Url);
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.e("RESPOENSE",">ADDRS> "+s);
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if(jsonObject.get("status").toString().equalsIgnoreCase("true")){
                    Toast.makeText(mContext, ""+jsonObject.get("error"), Toast.LENGTH_SHORT).show();
                     notifyDataSetChanged();
                    addressList.remove(pos);

                }else{
                    Toast.makeText(mContext, ""+jsonObject.get("error"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    private void showAddressPopupWindow(final int position, String labelStr1, String addressStr1, String zipcodeStr1, String type1) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_add_address_details);
        dialog.setCancelable(false);
        final RadioGroup radioGroup = (RadioGroup)dialog.findViewById(R.id.radioAddressDetails);
        final TextView tvHeader = (TextView)dialog.findViewById(R.id.pupup_header_name);
        tvHeader.setText("Update An Address");
        final EditText etLabel = (EditText) dialog.findViewById(R.id.etLabel);
        final EditText etAddress = (EditText) dialog.findViewById(R.id.etAddress);
        final EditText etPostalCode = (EditText) dialog.findViewById(R.id.etPostalCode);
        final RadioButton btResidential = (RadioButton) dialog.findViewById(R.id.radioResidential);
        final RadioButton btBusiness = (RadioButton) dialog.findViewById(R.id.radioBusiness);
        if(type1.toLowerCase().equalsIgnoreCase("business")){
            btBusiness.setActivated(true);
        }else{
            btResidential.setActivated(true);
        }
        etLabel.setText(labelStr1);
        etAddress.setText(addressStr1);
        etPostalCode.setText(zipcodeStr1);


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
                            zipcodeStr = etPostalCode.getText().toString();

                            // get selected radio button from radioGroup
                            int selectedId = radioGroup.getCheckedRadioButtonId();

                            // find the radiobutton by returned id
                            radioButton = (RadioButton) dialog.findViewById(selectedId);
                            Log.e(">>>>>",">>>"+radioButton.getText());
                            Toast.makeText(mContext,
                                    radioButton.getText(), Toast.LENGTH_SHORT).show();
                            typeStr = radioButton.getText().toString();

                            Log.e("IN EDIT",">>>>>>>>>>>>>>>");
                            Log.e("Label",""+labelStr);
                            Log.e("ADDRESS",""+addressStr);
                            Log.e("ZIP",""+zipcodeStr);
                            Log.e("typeStr",""+typeStr);
                            Log.e("ID",""+address_idStr);

                            http://web-medico.com/web2/kibbeh/api/v1/user/my_account/address/edit_address/541?address_id=31&label=this%20is%20
                            // edited%20label&type=Business&address=this%20is%20edit%20adress&zipcode=22201

                            new editAddressFromList(position,address_idStr,labelStr,typeStr,addressStr,zipcodeStr).execute();
                            dialog.dismiss();
                        }else{
                            Toast.makeText(mContext, "Please Enter Postal Code", Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(mContext, "Please Enter Address", Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(mContext, "Please Enter Label", Toast.LENGTH_SHORT).show();
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

}
