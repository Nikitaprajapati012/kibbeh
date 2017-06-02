package app.kibbeh.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import app.kibbeh.R;

public class TermsAndConditions extends AppCompatActivity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);
        webView = (WebView)findViewById(R.id.act_term_condition_webview);
        webView.loadUrl("file:///android_asset/terms.html");

        /*webview.loadUrl("file:///android_asset/index.html");*/
        TextView txtHeader = (TextView)findViewById(R.id.actionBarTitle);
        txtHeader.setText(R.string.terms_condition);
        ImageView imgBack = (ImageView)findViewById(R.id.actionBarBackImage);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
