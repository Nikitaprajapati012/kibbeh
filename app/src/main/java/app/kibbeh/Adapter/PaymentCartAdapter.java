package app.kibbeh.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.kibbeh.Activity.OrderActivity;
import app.kibbeh.Activity.PaymentActivity;
import app.kibbeh.R;
import app.kibbeh.model.CreditCard;

/**
 * Created by archi on 11/15/2016.
 */

public class PaymentCartAdapter extends BaseAdapter {

    public Context context;
    public ArrayList<CreditCard> arrayList;
    public LayoutInflater inflater;

    public PaymentCartAdapter(Context context, ArrayList<CreditCard> arrayListCreditCard) {

        this.context = context;
        this.arrayList = arrayListCreditCard;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
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
        convertView =  inflater.inflate(R.layout.adapter_payment_cart,null);
        TextView tvPayWithThis = (TextView)convertView.findViewById(R.id.adp_payment_cart_pay_with_this);
        TextView tvCreditNumber = (TextView)convertView.findViewById(R.id.adp_payment_cart_last_four);
        tvCreditNumber.setText("****" +arrayList.get(position).getLastFour());

        tvPayWithThis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context,OrderActivity.class);
                context.startActivity(i);
            }
        });

        return convertView;
    }
}
