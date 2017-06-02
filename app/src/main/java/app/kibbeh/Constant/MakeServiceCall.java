package app.kibbeh.Constant;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by archirayan on 2/17/2016.
 */
public class MakeServiceCall {
    String URL;
    HashMap<String, String> hashMap;
    public static final int GET = 1;
    public static final int POST = 2;
    StringBuilder response;

    public MakeServiceCall() {

    }

    public String MakeServiceCall(String URLSTR, int type) {

        if (type == GET) {

            try {
                response = new StringBuilder();
                URL url = new URL(URLSTR);
                HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();
                if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()), 8192);
                    String strLine = null;
                    while ((strLine = input.readLine()) != null) {
                        response.append(strLine);
                    }
                    input.close();
                }
            } catch (Exception e) {

            }
            return response.toString();

        } else {

            return "";
        }


    }

}
