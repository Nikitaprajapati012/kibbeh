package app.kibbeh.Adapter;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.kibbeh.Activity.PaymentActivity;
import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.Util;
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;
import app.kibbeh.model.AddressList;

/**
 * Created by archi on 11/14/2016.
 */

public class DeliveryAddressAdapter extends BaseAdapter {
    public Context context;
    public ArrayList<AddressList> arrayList;
    public LayoutInflater inflater;

    public DeliveryAddressAdapter(Context context, ArrayList<AddressList> arrayAddress) {
        this.context = context;
        this.arrayList = arrayAddress;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount()
    {
        return arrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.adapter_delivery_address,null);
        TextView tvLable = (TextView)convertView.findViewById(R.id.adp_delivery_add_label);
        TextView tvType = (TextView)convertView.findViewById(R.id.adb_delivery_add_resident);
        TextView tvAddress = (TextView)convertView.findViewById(R.id.adp_delivery_add_address);
        TextView tvZipCode = (TextView)convertView.findViewById(R.id.adp_delivery_add_zipcode);
        TextView tvButtonAdd = (TextView)convertView.findViewById(R.id.adp_delivery_tv_delevery_here);

        tvLable.setText(arrayList.get(position).getLabel());
        tvType.setText(arrayList.get(position).getType());
        tvAddress.setText(arrayList.get(position).getAddress());
        tvZipCode.setText(arrayList.get(position).getZipcode());
        final View finalConvertView = convertView;
        tvButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strSelectedTime = Utils.ReadSharePrefrence(context, Constant.ValuofRadioGroup);
                String selectedDate = Utils.ReadSharePrefrence(context,Constant.DeliveryDate);

                if (strSelectedTime.equals(""))
                {
                 Toast.makeText(context,"Please select the time",Toast.LENGTH_SHORT).show();
                }
                else if (selectedDate.equals(""))
                {
                    Toast.makeText(context,"please select the date ",Toast.LENGTH_SHORT).show();
                }
                else
                {

                  Intent i = new Intent(context,PaymentActivity.class);
                  context.startActivity(i);
                }

            }
        });

        return convertView;
    }
}
