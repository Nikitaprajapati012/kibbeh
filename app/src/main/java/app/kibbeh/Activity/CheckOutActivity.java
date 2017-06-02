package app.kibbeh.Activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.LruCache;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import app.kibbeh.Adapter.DeliveryAddressAdapter;
import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.Util;
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;
import app.kibbeh.model.AddressList;

/**
 * Created by archi on 11/12/2016.
 */

public class CheckOutActivity extends Activity implements View.OnClickListener {
    public TextView tvActionHeader,tvSelectedDate;
    public Utils utils;
    public ImageView ivBack;
    public ListView lvDelivery;
    public ArrayList<AddressList> arrayAddress;
    public DeliveryAddressAdapter deliveryAddressAdapter;
    public RelativeLayout rvAddress;
    public EditText etAddressLable,etAddress,etAddressPostal;
    public RadioButton rbResident,rbBussiness;
    public Button btnSaveAddress;
    public RadioGroup rbGroupAdrress,rbGroupDeliveryTime;
    public String deliveryTime;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_out);
        tvActionHeader = (TextView)findViewById(R.id.actionBarCheckOutDelivery);
        tvActionHeader.setAlpha(1);
        ivBack = (ImageView)findViewById(R.id.actionBarBackImage);
        tvSelectedDate = (TextView)findViewById(R.id.act_check_out_date_picker);
        utils = new Utils(CheckOutActivity.this);
        arrayAddress = new ArrayList<>();
        lvDelivery = (ListView)findViewById(R.id.act_chek_out_list_address);
        rvAddress = (RelativeLayout)findViewById(R.id.act_ckeck_rv_add_adress);
        etAddressLable = (EditText)findViewById(R.id.act_check_et_lable);
        etAddress = (EditText)findViewById(R.id.act_check_et_address);
        etAddressPostal = (EditText)findViewById(R.id.act_check_et_postal_code);
        rbResident = (RadioButton)findViewById(R.id.act_check_rb_Residential);
        rbBussiness = (RadioButton)findViewById(R.id.act_check_rb_Business);
        btnSaveAddress = (Button)findViewById(R.id.act_check_addred_btn_submit);
        rbGroupAdrress = (RadioGroup)findViewById(R.id.rg_check_out_address);
        rbGroupDeliveryTime = (RadioGroup)findViewById(R.id.radioButton);
        init();
    }
    private void init() {
     ivBack.setOnClickListener(this);
         tvSelectedDate.setOnClickListener(this);
         btnSaveAddress.setOnClickListener(this);
         getCurrentDate();
         new getAddress().execute();
        utils.WriteSharePrefrence(CheckOutActivity.this,Constant.ValuofRadioGroup,"0");


        rbGroupDeliveryTime.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton)findViewById(checkedId);

                int idx = rbGroupDeliveryTime.indexOfChild(rb);
                String vlaueOfDeliveryRadio = "0";

                if (idx == 0)
                {
                    vlaueOfDeliveryRadio ="1";
                }
                else if(idx == 1)
                {
                    vlaueOfDeliveryRadio = "2";
                }
                else if(idx == 2)
                {
                    vlaueOfDeliveryRadio = "3";
                }
                else if (idx == 3)
                {
                    vlaueOfDeliveryRadio = "4";
                }
                else if (idx == 4)
                {
                    vlaueOfDeliveryRadio = "5";
                }

                utils.WriteSharePrefrence(CheckOutActivity.this,Constant.ValuofRadioGroup,vlaueOfDeliveryRadio);

            }
        });

     }
    private void getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        tvSelectedDate.setText(formattedDate);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.actionBarBackImage:
                onBackPressed();
                finish();
                break;
            case R.id.act_check_out_date_picker:
                selectDate();
                break;
            case R.id.act_check_addred_btn_submit:
                saveAddress();
                break;
        }
        
    }

    private void saveAddress() {
    String strLable,strAddress,strPostalCode ,strResidant;
        strLable = etAddressLable.getText().toString();
        strAddress = etAddress.getText().toString();
        strPostalCode = etAddressPostal.getText().toString();
        int strResidantId = rbGroupAdrress.getCheckedRadioButtonId();
        RadioButton rbRes = (RadioButton)findViewById(strResidantId);
        strResidant = rbRes.getText().toString();
        if (strLable.length() > 0 && strAddress.length() > 0 && strPostalCode.length() >=5)
        {
            new AddAdreess(strLable,strAddress,strPostalCode,strResidant).execute();
            Toast.makeText(getApplicationContext(),strResidant,Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(CheckOutActivity.this,"please enter valied address",Toast.LENGTH_SHORT).show();
        }
    }

    private void selectDate()
    {
        Calendar mcurrentDate=Calendar.getInstance();
        int mYear = mcurrentDate.get(Calendar.YEAR);
        int mMonth=mcurrentDate.get(Calendar.MONTH);
        int mDay=mcurrentDate.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog mDatePicker=new DatePickerDialog(CheckOutActivity.this, new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
            // TODO Auto-generated method stub
                    /*      Your code   to get date and time    */
            String date = selectedyear+"/"+selectedmonth+"/"+selectedday;
            Util.WriteSharePrefrence(CheckOutActivity.this,Constant.DeliveryDate,date);
            tvSelectedDate.setText(date);
        }
    },mYear, mMonth, mDay);
        mDatePicker.show();
    }

    private class getAddress extends AsyncTask<String,String,String> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(CheckOutActivity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //http://web-medico.com/web2/kibbeh/api/v1/user/my_account/address/99

          String UserId = Utils.ReadSharePrefrence(CheckOutActivity.this, Constant.UserId);
            String Url = Constant.BASE_URL + "user/my_account/address/" + UserId;
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("address_list");
                for (int i=0;i<jsonArray.length();i++)
                {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    AddressList addressList = new AddressList();
                    addressList.setId(String.valueOf(jsonObject1.getInt("id")));
                    addressList.setLabel(jsonObject1.getString("label"));
                    addressList.setType(jsonObject1.getString("type"));
                    addressList.setAddress(jsonObject1.getString("address"));
                    addressList.setZipcode(jsonObject1.getString("zipcode"));
                    arrayAddress.add(addressList);
                }

                if (arrayAddress.size() <= 0)
                {
                lvDelivery.setVisibility(View.GONE);
                rvAddress.setVisibility(View.VISIBLE);
                }
                else
                {
                    lvDelivery.setVisibility(View.VISIBLE);
                    rvAddress.setVisibility(View.INVISIBLE);
                    deliveryAddressAdapter = new DeliveryAddressAdapter(CheckOutActivity.this, arrayAddress);
                    lvDelivery.setAdapter(deliveryAddressAdapter);
                }
                }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            pd.dismiss();
        }
    }

    private class AddAdreess extends AsyncTask<String,String,String> {
        ProgressDialog pd;
        String strLable,strAddress,strPostalCode,strResidant;
        public AddAdreess(String strLable, String strAddress, String strPostalCode, String strResidant) {
            this.strLable = strLable;
            this.strAddress = strAddress;
            this.strPostalCode = strPostalCode;
            this.strResidant = strResidant;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(CheckOutActivity.this);
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();

        }

        @Override
        protected String doInBackground(String... params) {

            //http://web-medico.com/web2/kibbeh/api/v1/user/my_account/address/add_address/54?zipcode=22201&label=newlabel&type=Residential%20&address=thisisaddress
            String userId = Utils.ReadSharePrefrence(CheckOutActivity.this,Constant.UserId);
            String ZipCode = Utils.ReadSharePrefrence(CheckOutActivity.this,Constant.ZipCode);
            String Url1 = Constant.BASE_URL + "user/my_account/address/add_address/" +userId + "?zipcode=" + ZipCode + "&label=" + strLable + "&type=" + strResidant + "&address=" + strAddress ;
            Url1 = Url1.replaceAll(" ","%");
            return utils.MakeServiceCall(Url1);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true"))
                {
                    new getAddress().execute();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
             pd.dismiss();
        }
    }
}
