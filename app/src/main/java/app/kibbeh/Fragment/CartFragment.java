package app.kibbeh.Fragment;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import app.kibbeh.Adapter.CartListAdapter;
import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;
import app.kibbeh.model.CartList;

/**
 * Created by archi on 10/28/2016.
 */

public class CartFragment extends Fragment {
    public RecyclerView rvCartList;
    public CartListAdapter cartListAdapter;
    public Utils utils;
    public ArrayList<CartList> arrayCart;
    public CartList details;
    public Paint p = new Paint();
    public int arraysize;
    public ImageView imgPlus;
    public TextView tvTotalCount, emptyView, tvTaxCharge, tvDeliveryCharge, tvCartAmount,
            tvMinimumLabanese, tvMinimumBebyLon, tvTotalLabanese, tvTotalBebyLon;
    public String userId;
    public LinearLayout firstView, secondView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);
        tvTotalCount = (TextView) rootView.findViewById(R.id.frag_cart_total_price);
        tvTaxCharge = (TextView) rootView.findViewById(R.id.frag_cart_taxcharge);
        tvDeliveryCharge = (TextView) rootView.findViewById(R.id.frag_cart_delivery_charge);
        tvCartAmount = (TextView) rootView.findViewById(R.id.frag_cart_amount);
        imgPlus = (ImageView) rootView.findViewById(R.id.fragment_cart_plus);
        rvCartList = (RecyclerView) rootView.findViewById(R.id.rvCartList);
        emptyView = (TextView) rootView.findViewById(R.id.frg_cart_emty_view);
        firstView = (LinearLayout) rootView.findViewById(R.id.firstview);
        secondView = (LinearLayout) rootView.findViewById(R.id.secondview);
        firstView.setVisibility(View.VISIBLE);
        secondView.setVisibility(View.GONE);
        init();
        click();
        onBackpress(rootView);
        return rootView;
    }

    // TODO: 5/13/2017 to perfrom click
    private void click() {
        imgPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (secondView.getVisibility() == View.VISIBLE) {
                    firstView.setVisibility(View.VISIBLE);
                    secondView.setVisibility(View.GONE);
                } else {
                    firstView.setVisibility(View.VISIBLE);
                    secondView.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void onBackpress(View rootView) {
        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    Fragment mFragment = new HomeFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_body, mFragment).commit();
                    return true;
                }
                return false;
            }


        });
    }

    private void init() {
        utils = new Utils(getActivity());
        userId = Utils.ReadSharePrefrence(getActivity(), Constant.UserId);
        if (utils.isConnectingToInternet() == true) {
            new getCartItems().execute();
            new getChargeDetails(userId).execute();
            initSwip();
        } else {
            Toast.makeText(getActivity(), "NO INTERNET CONNECTION", Toast.LENGTH_LONG).show();
        }


    }

    private void initSwip() {

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (direction == ItemTouchHelper.LEFT) {
                    String userID = arrayCart.get(position).getpId();
                    Log.d("jai", "position get on touch :" + userID);
                    new deleteFromCart(userID).execute(userID);
                    arrayCart.remove(position);
                    cartListAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                Bitmap icon;
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;

                    p.setColor(Color.parseColor("#D32F2F"));
                    RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
                    c.drawRect(background, p);
                    icon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_cancel);
                    RectF icon_dest = new RectF((float) itemView.getRight() - 2 * width, (float) itemView.getTop() + width, (float) itemView.getRight() - width, (float) itemView.getBottom() - width);
                    c.drawBitmap(icon, null, icon_dest, p);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(rvCartList);

    }

    private class getCartItems extends AsyncTask<String, String, String> {
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }


        @Override
        protected String doInBackground(String... params) {
            //http://web-medico.com/web2/kibbeh/api/v1/user/product/cartlist/87
            String userId = Utils.ReadSharePrefrence(getActivity(), Constant.UserId);
            String url = Constant.BASE_URL + "user/product/cartlist/" + userId;
            Log.d("URL", "%CART%" + url);
            return utils.MakeServiceCall(url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            try {

                JSONObject jsonObject = new JSONObject(s);
                arrayCart = new ArrayList<>();
                if (jsonObject.getBoolean("status") == true) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    String pId, pName, pImage, pCartTotal, pCartId;
                    String pPrice;
                    arraysize = jsonArray.length();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        details = new CartList();
                        JSONObject ChildJsonObject = jsonArray.getJSONObject(i);
                        details.setpTotalProduct(jsonObject.getInt("items"));
                        Log.d("item", "&&" + jsonObject.getInt("items"));
                        details.setpStoreName(ChildJsonObject.getString("store_name"));
                        details.setpStoreMinimumAmount(ChildJsonObject.getString("minimum_order_amount"));
                        details.setpDeliveryAmmount(ChildJsonObject.getString("delivery_amount"));
                        pId = String.valueOf(ChildJsonObject.getInt("product_id"));
                        pName = ChildJsonObject.getString("name");
                        pImage = ChildJsonObject.getString("image_url");
                        pPrice = ChildJsonObject.getString("price");
                        pCartTotal = String.valueOf(ChildJsonObject.getInt("cart_quantity"));
                        pCartId = String.valueOf(ChildJsonObject.getInt("cart_id"));
                        details.setpId(pId);
                        details.setpName(pName);
                        details.setpImage(pImage);
                        details.setpPrice(pPrice);
                        details.setpCartTotal(pCartTotal);
                        details.setpCartId(pCartId);
                        arrayCart.add(details);
                    }
                }
            } catch (JSONException e) {
                emptyView.setVisibility(View.VISIBLE);
            }

        }


    }

    private class deleteFromCart extends AsyncTask<String, String, String> {

        ProgressDialog pd;
        String ProductId;

        public deleteFromCart(String userID) {
            this.ProductId = userID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();

        }


        @Override
        protected String doInBackground(String... params) {

            //http://web-medico.com/web2/kibbeh/api/v1/user/remove_from_cart/87/5/567 -USERiD/STORiD/PRODUCTiD
            String userid = Utils.ReadSharePrefrence(getActivity(), Constant.UserId);
            String storeid = Utils.ReadSharePrefrence(getActivity(), Constant.UserStoreId);
            String Url = Constant.BASE_URL + "user/remove_from_cart/" + userid + "/" + storeid + "/" + ProductId;
            return utils.MakeServiceCall(Url);

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {


                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    new getCartItems().execute();
                } else {
                    Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), getString(R.string.somethig_went_wrong), Toast.LENGTH_SHORT).show();
            }

            pd.dismiss();
        }
    }

    private class getChargeDetails extends AsyncTask<String, String, String> {

        ProgressDialog pd;
        String userId;

        public getChargeDetails(String userID) {
            this.userId = userID;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //http://web-medico.com/web2/kibbeh/api/v1/user/checkout/invoice_new/148
            String url = Constant.BASE_URL + "user/checkout/invoice_new/" + userId + "a";
            Log.d("URL", "%%" + url);
            return utils.MakeServiceCall(url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            arrayCart = new ArrayList<>();
            pd.dismiss();
            try {
                JSONObject jsonObject = new JSONObject(s);
                details = new CartList();
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    Iterator<String> iter = jsonObject.keys();
                    while (iter.hasNext()) {
                        String key = iter.next();
                        details = new CartList();
                        details.setpTotalProduct(jsonObject.getInt("items"));
                        JSONObject jsonObjectGrandTotal = jsonObject.getJSONObject("grand_total");
                        details.setpGrandTotal(jsonObjectGrandTotal.getString("grand_total"));
                        details.setpPriceTotal(jsonObjectGrandTotal.getString("total_price"));
                        details.setpTaxTotal(jsonObjectGrandTotal.getString("total_tax"));
                        details.setpDeliveryChargeTotal(jsonObjectGrandTotal.getString("total_delivery_charge"));
                        Utils.WriteSharePrefrence(getActivity(), Constant.GRAND_TOTAL, jsonObjectGrandTotal.getString("grand_total"));
                        Utils.WriteSharePrefrence(getActivity(), Constant.ALL_PRODUCT_TOTAL, jsonObjectGrandTotal.getString("total_price"));
                        Utils.WriteSharePrefrence(getActivity(), Constant.ALL_PRODUCT_TAX, jsonObjectGrandTotal.getString("total_tax"));
                        Utils.WriteSharePrefrence(getActivity(), Constant.ALL_PRODUCT_DELIVERY_CHARGE, jsonObjectGrandTotal.getString("total_delivery_charge"));
                        try {
                            if (jsonObject.optJSONArray(key) != null) {
                                JSONArray productArray = jsonObject.getJSONArray(key);
                                for (int j = 0; j < productArray.length(); j++) {
                                    JSONObject childJsonObject1 = productArray.getJSONObject(j);
                                    details.setpName(childJsonObject1.getString("name"));
                                    details.setpImage(childJsonObject1.getString("image_url"));
                                    details.setpPrice(childJsonObject1.getString("price"));
                                    details.setpProductTotal(childJsonObject1.getString("product_price"));
                                    details.setpCartTotal(childJsonObject1.getString("cart_quantity"));
                                    details.setpStoreName(childJsonObject1.getString("store_name"));
                                    details.setpDeliveryAmmount(childJsonObject1.getString("delivery_amount"));
                                    details.setpTaxcharge(childJsonObject1.getString("tax"));
                                    details.setpDeliveryCharge(childJsonObject1.getString("delivery_charge"));
                                    details.setpStoreMinimumAmount(childJsonObject1.getString("minimum_order_amount"));
                                    arrayCart.add(details);
                                }
                            }
                        } catch (JSONException e) {
                            Log.d("error", e.toString());
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), getString(R.string.somethig_went_wrong), Toast.LENGTH_SHORT).show();
            }
            if (arrayCart.size() > 0) {
                tvTaxCharge.setText("$" + details.getpTaxTotal());
                tvDeliveryCharge.setText("$" + details.getpDeliveryChargeTotal());
                tvTotalCount.setText("$" + details.getpGrandTotal());
                tvCartAmount.setText("$" + details.getpPriceTotal());
                cartListAdapter = new CartListAdapter(getActivity(), arrayCart, tvTotalCount, tvCartAmount);
                rvCartList.setAdapter(cartListAdapter);
                rvCartList.setLayoutManager(new LinearLayoutManager(getActivity()));

                final StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(cartListAdapter);
                rvCartList.addItemDecoration(headersDecor);
                cartListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onChanged() {
                        headersDecor.invalidateHeaders();
                    }
                });
            } else {
                emptyView.setVisibility(View.VISIBLE);
            }
        }
    }
}
