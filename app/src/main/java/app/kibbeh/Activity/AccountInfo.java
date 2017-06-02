package app.kibbeh.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import app.kibbeh.R;

/**
 * Created by archirayan on 14-Oct-16.
 */

public class AccountInfo extends Activity implements View.OnClickListener {
    public LinearLayout llAcountInfo, llAccountInfoAddress, llpayment, llCoupen, llHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_info);
        (findViewById(R.id.actionBarBackImage)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ((TextView) findViewById(R.id.actionBarTitle)).setText("Account Info");
        llAcountInfo = (LinearLayout) findViewById(R.id.activity_acount_info_accont);
        llAccountInfoAddress = (LinearLayout) findViewById(R.id.frag_acount_info_address);
        llpayment = (LinearLayout) findViewById(R.id.frag_acount_info_payment_method);
        llCoupen = (LinearLayout) findViewById(R.id.frag_acount_info_coupnes);
        llHistory = (LinearLayout) findViewById(R.id.frag_acount_info_order_history);
        init();

    }

    private void init() {
        llAcountInfo.setOnClickListener(this);
        llAccountInfoAddress.setOnClickListener(this);
        llpayment.setOnClickListener(this);
        llCoupen.setOnClickListener(this);
        llHistory.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.activity_acount_info_accont:
                Intent Accont = new Intent(AccountInfo.this, AccountMain.class);
                startActivity(Accont);

                break;
            case R.id.frag_acount_info_address:
                Intent addressIntent = new Intent(AccountInfo.this, AddressDetails.class);
                startActivity(addressIntent);

                break;

            case R.id.frag_acount_info_payment_method:
                Intent paymentIntent = new Intent(AccountInfo.this, PaymentAccountActivity.class);
                startActivity(paymentIntent);

                break;
            case R.id.frag_acount_info_coupnes:
                Intent coupnesIntent = new Intent(AccountInfo.this, CoupnesActivity.class);
                startActivity(coupnesIntent);

                break;
            case R.id.frag_acount_info_order_history:
                Intent historyIntent = new Intent(AccountInfo.this, OrderHistoryActivity.class);
                startActivity(historyIntent);

                break;
        }
    }
}
