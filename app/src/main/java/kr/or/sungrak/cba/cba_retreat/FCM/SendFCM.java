package kr.or.sungrak.cba.cba_retreat.FCM;

import android.os.AsyncTask;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SendFCM {
    private static final String BASE_URL = "https://fcm.googleapis.com/fcm/send";
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    private static final String TITLE = "교회수호 진리수호";
    public static final String TOPIC = "2019winter";
    private static final String SERVER_KEY = "AAAA-PRsYvs:APA91bFiNlDHb8bBp5N4CJJuhtNSiV4Ej1KIh3tkIRsUbfrmHcCPvJvphxAWwg2oLohhgll1Ui0owWyRSP3nrkSDSrnr6M3ktjo75p2YFeqSl24naWo5ILf0yXVbWu08EvbqX0w8SoGSFFml6SmwIOh12ZmAgP1bMg";
    private static String TAG = "CBA/SendFCM";

    public static void sendOKhttp(final String message) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    OkHttpClient client = new OkHttpClient();
                    JSONObject json = new JSONObject();
                    JSONObject dataJson = new JSONObject();
                    dataJson.put("body", message);
                    dataJson.put("title", TITLE);
                    json.put("notification", dataJson);
                    json.put("to", "/topics/" + TOPIC);
                    RequestBody body = RequestBody.create(JSON, json.toString());
                    Request request = new Request.Builder()
                            .header("Authorization", "key=" + SERVER_KEY)
                            .url(BASE_URL)
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    String finalResponse = response.body().string();
                } catch (Exception e) {
                    //Log.d(TAG,e+"");
                }
                return null;
            }
        }.execute();
    }

}
