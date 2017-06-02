package app.kibbeh.Fragment;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import app.kibbeh.Activity.ProductDetails;
import app.kibbeh.Activity.ViewMoreActivity;
import app.kibbeh.Adapter.CartListAdapter;
import app.kibbeh.Adapter.ImageAdapter;
import app.kibbeh.Adapter.ProductHeadersAdapter;
import app.kibbeh.Adapter.ViewPagerAdapter;
import app.kibbeh.Constant.BagdeDrawable;
import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.Util;
import app.kibbeh.Constant.Utils;
import app.kibbeh.R;
import app.kibbeh.model.CartList;
import app.kibbeh.model.DepartmentDetails;
import app.kibbeh.model.HomeModel;
import app.kibbeh.model.Product;

import static android.R.attr.fragment;
import static android.R.attr.layout;


public class HomeFragment extends Fragment implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private ImageAdapter adapter;
    private List<Product> albumList;
    protected View view;
    private ImageButton ivStorIcon;
    private ViewPager intro_images;
    private LinearLayout pager_indicator;
    private int dotsCount = 5;
    private ImageView[] dots;
    private ViewPagerAdapter mAdapter;
    public Utils utils;
    public ArrayList<Product> arrayProduct;
    public ArrayList<HomeModel> arrayHome;
    private SearchView searchView;
    public LayoutInflater layoutInflater;
    public LinearLayout linearLayout;
    public String DataString, storeItem;
    public ImageView ivStorDetail,ivAddFav;
    public TextView tvEmptyView,tvRemove;
    public ArrayList<DepartmentDetails> arrayDepartment = new ArrayList<>();
    public String DepartmentItem, Department;
    View subCat;
    public int arraysize;
    public SearchView.OnQueryTextListener queryTextListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // new getCartItems().execute();
    }

    private String[] mImageResources = {Constant.SITE_BASE_URL + "assets//img/banner1.jpg", Constant.SITE_BASE_URL + "assets//img/banner2.png", Constant.SITE_BASE_URL + "assets//img/banner3.png"};
    //public String[] SPINNERLIST = {"Beans & Peas", "Hummus"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        albumList = new ArrayList<>();
        adapter = new ImageAdapter(getActivity(), albumList);
        utils = new Utils(getActivity());
        intro_images = (ViewPager) rootView.findViewById(R.id.pager_introduction);
        pager_indicator = (LinearLayout) rootView.findViewById(R.id.viewPagerCountDots);
        linearLayout = (LinearLayout) rootView.findViewById(R.id.frag_home_linear);
        layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ivStorDetail = (ImageView) rootView.findViewById(R.id.iv_frg_home_stor);
        tvEmptyView = (TextView) rootView.findViewById(R.id.tv_empty_view);
        MaterialBetterSpinner materialDesignSpinner = (MaterialBetterSpinner) rootView.
                findViewById(R.id.spinner_department);
        setRefernce();
        setStorLogo();
        ArrayAdapter<DepartmentDetails> arrayAdapter = new ArrayAdapter<DepartmentDetails>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, arrayDepartment);
        materialDesignSpinner.setAdapter(arrayAdapter);
        materialDesignSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DepartmentItem = parent.getItemAtPosition(position).toString();
                Log.v("DepartmentItem", DepartmentItem);
                new getData(DepartmentItem).execute();
                Toast.makeText(getActivity(), DepartmentItem, Toast.LENGTH_SHORT).show();
            }
        });
        // setDepartment(rootView);
        fragmentBack(rootView);
        return rootView;
    }

    private void setStorLogo() {
        if (Utils.ReadSharePrefrence(getActivity(), Constant.StorLogo).equals("")) {
            Glide.with(getActivity()).load(R.drawable.ic_k_logo).placeholder(R.drawable.ic_placeholder).into(ivStorDetail);
        } else {
            Glide.with(getActivity()).load(Util.ReadSharePrefrence(getActivity(), Constant.StorLogo)).placeholder(R.drawable.ic_placeholder).into(ivStorDetail);
        }
    }

    private void fragmentBack(View rootView) {

        rootView.setFocusableInTouchMode(true);
        rootView.requestFocus();
        rootView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    new AlertDialog.Builder(getActivity())
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("Close app")
                            .setMessage("Are you sure you want to close this app?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getActivity().finish();

                                }

                            })

                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            })
                            .show();
                    return true;
                }
                return false;

            }


        });
    }

    private void setRefernce() {

        mAdapter = new ViewPagerAdapter(getActivity(), mImageResources);
        intro_images.setAdapter(mAdapter);
        intro_images.setCurrentItem(0);
        intro_images.setOnPageChangeListener(this);
        setUiPageViewController();

        new getData(DepartmentItem).execute();

        /*try {
            if (DepartmentItem != null) {
                new getData(DepartmentItem).execute();
            } else {
                Toast.makeText(getActivity(), "Select Department", Toast.LENGTH_SHORT).show();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }*/


    }

    private void setUiPageViewController() {
        dotsCount = mAdapter.getCount();
        dots = new ImageView[dotsCount];
        for (int i = 0; i < dotsCount; i++) {
            dots[i] = new ImageView(getActivity());
            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nonselecteditem_dot));

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(4, 0, 4, 0);

            pager_indicator.addView(dots[i], params);
        }

        dots[0].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.selecteditem_dot));


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }


    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//          //  case R.id.btn_next:
//                intro_images.setCurrentItem((intro_images.getCurrentItem() < dotsCount)
//                        ? intro_images.getCurrentItem() + 1 : 0);
//                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        for (int i = 0; i < dotsCount; i++) {
            dots[i].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.nonselecteditem_dot));
        }
        dots[position].setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.selecteditem_dot));

//        if (position + 1 == dotsCount) {
//            // btnNext.setVisibility(View.GONE);
//            btnFinish.setVisibility(View.GONE);
//        } else {
////            btnNext.setVisibility(View.VISIBLE);
//            btnFinish.setVisibility(View.GONE);
//        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }



    private class getCartItems extends AsyncTask<String, String, String> {
        /*ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd= new ProgressDialog(getActivity());
            pd.setMessage("Please wait...");
            pd.setCancelable(false);
            pd.show();
        }*/

        Menu menu;
        MenuInflater inflater;
        public getCartItems(Menu menu , MenuInflater inflater)
        {
            this.menu = menu;
            this.inflater = inflater;

        }
        @Override
        protected String doInBackground(String... params) {

            //http://web-medico.com/web2/kibbeh/api/v1/user/product/cartlist/87
            String userId = utils.ReadSharePrefrence(getActivity(), Constant.UserId);
            String url = Constant.BASE_URL + "user/product/cartlist/" + userId;
            return utils.MakeServiceCall(url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            try {

                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("status") == true) {
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    String pId, pName, pImage, pPrice, pCartTotal, pCartId;
                    arraysize = jsonArray.length();
                    storeItem = String.valueOf(arraysize);
                    // inflater.inflate(R.menu.menu_main, menu);
                   /* MenuItem NumberofItem = menu.findItem(R.id.action_Item);
                    NumberofItem.setTitle(storeItem + " " + "Items");*/

                }
            } catch (JSONException e) {
                Toast.makeText(getActivity(), R.string.somethig_went_wrong, Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void setUpSerch(String dataString, String query) {
        linearLayout.removeAllViews();
        try {
            JSONObject mainObject = new JSONObject(dataString);
            if (mainObject.getString("status").equalsIgnoreCase("true")) {

                JSONArray array = mainObject.getJSONArray("data");
                Log.d("kibbeh","@@"+array);
                for (int i = 0; i < array.length(); i++) {
                    HomeModel homeModel = new HomeModel();
                    JSONObject object = array.getJSONObject(i);
                    Log.d("kibbeh","@@"+object);
                    View view = layoutInflater.inflate(R.layout.main_cat_item, null);
                    TextView textcatName = (TextView) view.findViewById(R.id.main_cat_item_name);
                    textcatName.setText(object.getString("name"));
                    String DeparmentID = String.valueOf(object.getInt("department_id"));
                    String StorID = String.valueOf(object.getInt("store_id"));

                    linearLayout.addView(view);
                    JSONArray productArray = object.getJSONArray("products");
                    Log.d("kibbeh","@@"+productArray);
                    if (productArray.length() == 0) {
                        tvEmptyView.setVisibility(View.VISIBLE);
                    } else {

                        tvEmptyView.setVisibility(View.GONE);
                        for (int k = 0; k < productArray.length(); k++) {
                            JSONObject productObject = productArray.getJSONObject(k);
                            Log.d("kibbeh","@@"+productObject);
                            final Product product = new Product();
                            product.setId(String.valueOf(productObject.getInt("id")));
                            product.setName(productObject.getString("name"));
                            product.setPrice(productObject.getString("price"));
                            product.setImage(productObject.getString("image_url"));
                            product.setIs_Fav(productObject.getString("is_favourite"));
                            product.setAdded_Cart(productObject.getString("added_cart"));
                            product.setFav_Id(productObject.getString("favourite_id"));
                            product.setQty(productObject.getString("quantity"));
                            arrayProduct.add(product);
                            homeModel.setProductArray(arrayProduct);

                            subCat = layoutInflater.inflate(R.layout.adapter_home_product, null);
                            ivAddFav = (ImageView) subCat.findViewById(R.id.ivAddFave_HomeAdapter);
                            TextView tvAdd = (TextView) subCat.findViewById(R.id.tvAdd_HomeAdapter);
                            tvRemove = (TextView) subCat.findViewById(R.id.tvRemove_HomeAdapter);
                            TextView tvName = (TextView) subCat.findViewById(R.id.tvTitle_adapter_home_product);
                            tvName.setText(product.getName());
                            TextView tvPrice = (TextView) subCat.findViewById(R.id.tvPrice_adapter_home_product);
                            tvPrice.setText("$" + product.getPrice());
                            ImageView thumbnail = (ImageView) subCat.findViewById(R.id.adapter_home_image);
                            Glide.with(getActivity()).load(product.getImage()).into(thumbnail);
                            query.toLowerCase();
                            String lowerString = productObject.getString("name").toLowerCase();
                            if (lowerString.contains(query)) {
                                linearLayout.addView(subCat);
                            }


                            if (product.getIs_Fav().equalsIgnoreCase("1")) {
                                ivAddFav.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_red_heart));
                            } else {
                                ivAddFav.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_blue_heart));
                            }

                            if (Integer.parseInt(product.getAdded_Cart()) >= 1) {
                                tvRemove.setVisibility(View.VISIBLE);
                            } else {
                                tvRemove.setVisibility(View.INVISIBLE);
                            }

                            ivAddFav.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (product.getIs_Fav().equalsIgnoreCase("1")) {
                                        new RemoveFavourite(product.getFav_Id(), ivAddFav).execute();
                                        product.setFav_Id("0");
                                    } else {
                                        new AddFavourite(product.getId(), ivAddFav).execute();
                                        product.setFav_Id("1");
                                    }
                                }
                            });


                            subCat.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent productDetailsIntent = new Intent(getActivity(), ProductDetails.class);
                                    productDetailsIntent.putExtra("id", product.getId());
                                    getActivity().startActivity(productDetailsIntent);
                                }
                            });

                            //  Add to cart
                            tvAdd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new ProductAddToCart(product.getId(), tvRemove).execute();
                                }
                            });
                            //       Remove from cart
                            tvRemove.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    new ProductRemoveToCart(product.getId(), tvRemove).execute();
                                }
                            });
                        }

                    }
                    arrayHome.add(homeModel);

                }
            } else {
                Toast.makeText(getActivity(), "" + mainObject.getString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private class getData extends AsyncTask<String, String, String> {

        ProgressDialog pd;
        String departmentitem;

        private getData(String _departmentitem) {
            this.departmentitem = _departmentitem;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayProduct = new ArrayList<>();
            arrayHome = new ArrayList<>();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            String url;
            //http://web-medico.com/web2/kibbeh/api/v1/login?email=archiryan12@gmail.com&password=archiryan12
            String userid = Utils.ReadSharePrefrence(getActivity(), Constant.UserId);
            String zipcode = Utils.ReadSharePrefrence(getActivity(), Constant.ZipCode);
            String departmentCOde = Utils.ReadSharePrefrence(getActivity(), Constant.StorId);
            //http://web-medico.com/web2/kibbeh/api/v1/user/store_departments/159/22201/3
            if (departmentCOde.equalsIgnoreCase("")) {
                url = Constant.BASE_URL + "user/store_departments/" + userid + "/" + zipcode;
            } else {
                url = Constant.BASE_URL + "user/store_departments/" + userid + "/" + zipcode + "/" + departmentCOde;
            }
            Log.d("jai", "url @@:" + url);
            return utils.MakeServiceCall(url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Log.d("REPONSE","@@"+s);
            try {
                DataString = s;
                JSONObject mainObject = new JSONObject(s);
                if (mainObject.getString("status").equalsIgnoreCase("true")) {
                    JSONArray array = mainObject.getJSONArray("data");
                    for (int i = 0; i < array.length(); i++) {
                        HomeModel homeModel = new HomeModel();
                        JSONObject object = array.getJSONObject(i);
                        Department = object.getString("name");
                        final String DeparmentID = String.valueOf(object.getInt("department_id"));
                        final String StorID = String.valueOf(object.getInt("store_id"));
                        arrayDepartment.add(new DepartmentDetails(Department, DeparmentID, StorID));
                        View view = layoutInflater.inflate(R.layout.main_cat_item, null);
                        TextView textcatName = (TextView) view.findViewById(R.id.main_cat_item_name);
                        textcatName.setText(DepartmentItem);
                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent i = new Intent(getActivity(), ViewMoreActivity.class);
                                i.putExtra("department_ID", DeparmentID);
                                i.putExtra("store_ID", StorID);
                                startActivity(i);
                            }
                        });

                        final int Length = array.length();
                        if (DepartmentItem != null) {
                            if (DepartmentItem.equalsIgnoreCase(Department)) {
                                arrayHome.clear();
                                arrayProduct.clear();
                                JSONArray productArray = object.getJSONArray("products");
                                for (int k = 0; k < productArray.length(); k++) {
                                    JSONObject productObject = productArray.getJSONObject(k);
                                    final Product product = new Product();
                                    product.setId(String.valueOf(productObject.getInt("id")));
                                    product.setName(productObject.getString("name"));
                                    product.setPrice(productObject.getString("price"));
                                    product.setImage(productObject.getString("image_url"));
                                    product.setIs_Fav(productObject.getString("is_favourite"));
                                    product.setAdded_Cart(productObject.getString("added_cart"));
                                    product.setFav_Id(productObject.getString("favourite_id"));
                                    product.setQty(productObject.getString("quantity"));
                                    arrayProduct.add(product);
                                    homeModel.setProductArray(arrayProduct);
                                    View subCat = layoutInflater.inflate(R.layout.adapter_home_product, null);
                                    ivAddFav = (ImageView) subCat.findViewById(R.id.ivAddFave_HomeAdapter);
                                    TextView tvAdd = (TextView) subCat.findViewById(R.id.tvAdd_HomeAdapter);
                                    tvRemove = (TextView) subCat.findViewById(R.id.tvRemove_HomeAdapter);
                                    TextView tvName = (TextView) subCat.findViewById(R.id.tvTitle_adapter_home_product);
                                    TextView tvPrice = (TextView) subCat.findViewById(R.id.tvPrice_adapter_home_product);
                                    ImageView thumbnail = (ImageView) subCat.findViewById(R.id.adapter_home_image);
                                    tvName.setText(product.getName());
                                    tvPrice.setText("$" + product.getPrice());
                                    Glide.with(getActivity()).load(product.getImage()).into(thumbnail);
                                    linearLayout.addView(subCat);
                                    subCat.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent productDetailsIntent = new Intent(getActivity(), ProductDetails.class);
                                            productDetailsIntent.putExtra("id", product.getId());
                                            getActivity().startActivity(productDetailsIntent);

                                        }
                                    });
                                    ivAddFav.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (product.getIs_Fav().equalsIgnoreCase("1")) {
                                                new RemoveFavourite(product.getFav_Id(), ivAddFav).execute();
                                            } else {
                                                new AddFavourite(product.getId(), ivAddFav).execute();
                                            }
                                        }
                                    });

                                    if (product.getIs_Fav().equalsIgnoreCase("1")) {
                                        ivAddFav.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_red_heart));
                                    } else {
                                        ivAddFav.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_blue_heart));
                                    }
                                    //  Add to cart
                                    tvAdd.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                           // Log.d("text","@@"+tvRemove.getText().toString().length());
                                            new ProductAddToCart(product.getId(), tvRemove).execute();
                                        }
                                    });
                                    //       Remove from cart
                                /*    tvRemove.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            new ProductRemoveToCart(product.getId(), tvRemove).execute();
                                        }
                                    });
*/

                                }
                                if (productArray.length() != 0) {
                                    arrayHome.add(homeModel);
                                } else {
                                    arrayHome.clear();
                                    arrayProduct.clear();
                                    linearLayout.removeAllViews();
                                }
                            }
                        } else {
                            // Toast.makeText(getActivity(), "Select Department", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(getActivity(), "" + mainObject.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


    }


    private class ProductAddToCart extends AsyncTask<String, String, String> {
        String productId;
        ProgressDialog pd;
        TextView txtRemove;

        private ProductAddToCart(String id,TextView tvRemove) {
            this.productId = id;
            this.txtRemove = tvRemove;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Loading ...");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            //http://web-medico.com/web2/kibbeh/api/v1/user/add_product_cart/87/4/56?quantity=1
            String userid = Utils.ReadSharePrefrence(getActivity(), Constant.UserId);
            String storeid = Utils.ReadSharePrefrence(getActivity(), Constant.UserStoreId);
            String Url = Constant.BASE_URL + "user/add_product_cart/" + userid + "/" + storeid + "/" + productId + "?quantity=1";
            Log.d("URL_CART",""+Url);
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            pd.dismiss();
            Log.d("RESPONSE","@@"+s);
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("status").equalsIgnoreCase("true")) {
                    Toast.makeText(getActivity(), object.getString("msg"), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), object.getString("msg"), Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), getString(R.string.somethig_went_wrong), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class ProductRemoveToCart extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String productId;
        TextView tvRemove;

        public ProductRemoveToCart(String id, TextView tvRemove) {
            this.productId = id;
            this.tvRemove = tvRemove;
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
            //http://web-medico.com/web2/kibbeh/api/v1/product/add_fav/84/25
            String userid = Utils.ReadSharePrefrence(getActivity(), Constant.UserId);
            String storeid = Utils.ReadSharePrefrence(getActivity(), Constant.UserStoreId);
            String Url = Constant.BASE_URL + "user/remove_from_cart/" + userid + "/" + storeid + "/" + productId;
            return utils.MakeServiceCall(Url);
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    tvRemove.setVisibility(View.INVISIBLE);
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

    private class RemoveFavourite extends AsyncTask<String, String, String> {
        String productId;
        ProgressDialog pd;
        ImageView ivFav;

        public RemoveFavourite(String fav_id, ImageView ivAddFav) {
            this.productId = fav_id;
            this.ivFav = ivAddFav;
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
            String userid = Utils.ReadSharePrefrence(getActivity(), Constant.UserId);
            String Url = Constant.BASE_URL + "product/remove_fav/" + userid + "/" + productId;
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    ivFav.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_blue_heart));
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

    private class AddFavourite extends AsyncTask<String, String, String> {
        ProgressDialog pd;
        String productId;
        ImageView ivFav;

        public AddFavourite(String id, ImageView ivAddFav) {
            this.productId = id;
            this.ivFav = ivAddFav;
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
            String userid = Utils.ReadSharePrefrence(getActivity(), Constant.UserId);
            String Url = Constant.BASE_URL + "product/add_fav/" + userid + "/" + productId;
            return utils.MakeServiceCall(Url);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                    Toast.makeText(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();
                    ivFav.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.ic_red_heart));
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
    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {
        BagdeDrawable badge;
        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BagdeDrawable) {
            badge = (BagdeDrawable) reuse;
        } else {
            badge = new BagdeDrawable(context);
        }
        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_main, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);

        // TODO: 5/15/2017 set cart and number of item on cart.
        MenuItem cart = menu.findItem(R.id.action_cart);
        BitmapDrawable iconBitmap = (BitmapDrawable) cart.getIcon();
        LayerDrawable iconLayer = new LayerDrawable(new Drawable [] { iconBitmap });
        setBadgeCount(getActivity(), iconLayer, "9");

        new getCartItems(menu,inflater).execute();
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

       if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();

        }
        if (searchView != null) {


            // searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);
                    return true;
                }

                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.d("jai", "searche text :" + query);
                    setUpSerch(DataString, query);
                    return false;
                }
            };

            ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
            closeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    new getData(DepartmentItem).execute();
                  /* try {
                        if (DepartmentItem != null) {
                            new getData(DepartmentItem).execute();
                        } else {
                            Toast.makeText(getActivity(), "Select Department", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }*/
                    linearLayout.removeAllViews();
                    //Clear query
                    searchView.setQuery("", false);
                    //Collapse the action view
                    searchView.onActionViewCollapsed();

                }
            });

        }
        cart.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Fragment mFragment = new CartFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container_body, mFragment).commit();
                return true;
            }
        });

        super.onCreateOptionsMenu(menu, inflater);
    }
}
