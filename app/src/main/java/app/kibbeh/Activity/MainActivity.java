package app.kibbeh.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import app.kibbeh.Constant.Constant;
import app.kibbeh.Constant.Utils;
import app.kibbeh.Fragment.CartFragment;
import app.kibbeh.Fragment.FavoriteFragment;
import app.kibbeh.Fragment.HelpFragment;
import app.kibbeh.Fragment.HomeFragment;
import app.kibbeh.Fragment.MyAccount;
import app.kibbeh.Fragment.PopularFragment;
import app.kibbeh.Fragment.Recipes;
import app.kibbeh.Fragment.StoreFragment;
import app.kibbeh.R;


public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();
    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);

        TextView textView = (TextView) findViewById(R.id.nav_header_username);
        textView.setText(Utils.ReadSharePrefrence(MainActivity.this, Constant.FirstName) + " " + Utils.ReadSharePrefrence(MainActivity.this, Constant.LastName));

        ImageView imageView = (ImageView) findViewById(R.id.nav_header_image);
        Glide.with(MainActivity.this).load(Utils.ReadSharePrefrence(MainActivity.this, Constant.UserProPic)).placeholder(R.drawable.ic_profile).into(imageView);

        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch

          if (Utils.ReadSharePrefrence(MainActivity.this,Constant.RegId).equals("1"))
          {
              displayView(0);
          }
        else
          {
              displayView(1);
          }
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        if(id == R.id.action_search){
//            Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }
    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                title = getString(R.string.app_name);
                break;
            case 1:
                fragment = new StoreFragment();
                title = getString(R.string.store);
                break;
            case 2:
                fragment = new Recipes();
                title = getString(R.string.Recipes);
                break;
            case 3:
                fragment = new FavoriteFragment();
                title = getString(R.string.Fevorite);
                break;
            case 4:
                fragment = new PopularFragment();
                title = getString(R.string.popular);
                break;
            case 5:
                fragment = new CartFragment();
                title = getString(R.string.cart);
                break;
            case 6:
                fragment = new HelpFragment();
                title = getString(R.string.help);
                break;
            case 7:
                fragment = new MyAccount();
                title = getString(R.string.my_account);
                break;

            case 8:
                Utils.clearSharedPreferenceData(MainActivity.this);
                Intent in = new Intent(MainActivity.this, PinCodeActivity.class);
                in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(in);
                finish();
                break;
            default:
                break;
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment,"home");
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }

        
    }
}