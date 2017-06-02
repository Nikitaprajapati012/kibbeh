package app.kibbeh.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import app.kibbeh.Activity.AccountInfo;
import app.kibbeh.Activity.PinCodeActivity;
import app.kibbeh.Activity.TermsAndConditions;
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccount extends Fragment implements View.OnClickListener {

   public LinearLayout llMyAcoount,llTermsCondition,llLogOut;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_my_account, container, false);
        llMyAcoount = (LinearLayout)rootView.findViewById(R.id.frag_my_acount_lv_my_accont);
        llTermsCondition=(LinearLayout)rootView.findViewById(R.id.frag_my_acount_lv_terms_coondition);
        llLogOut=(LinearLayout)rootView.findViewById(R.id.act_my_acount_lv_logout);
        init();

        return rootView;
    }

    private void init()
    {
        llMyAcoount.setOnClickListener(this);
        llTermsCondition.setOnClickListener(this);
        llLogOut.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.frag_my_acount_lv_my_accont:
                Intent accountIinfo = new Intent(getActivity(),AccountInfo.class);
                startActivity(accountIinfo);
                break;

            case R.id.frag_my_acount_lv_terms_coondition:
                Intent termsCondition = new Intent(getActivity(), TermsAndConditions.class);
                startActivity(termsCondition);
                break;

            case R.id.act_my_acount_lv_logout:
                Utils.clearSharedPreferenceData(getActivity());
                Intent lgout =new Intent(getActivity(), PinCodeActivity.class);
                lgout.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(lgout);
                break;

            default:
                break;

        }

    }
}
