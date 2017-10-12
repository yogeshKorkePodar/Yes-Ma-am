package com.podar.yesmaam;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.ToneGenerator;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends Activity {
    public static Intent intent;
    private String url = "http://www.betweenus.in/PODARAPP/PodarApp.svc/QrCodeVerificationDetails";
    public static String client_ID, student_ID, student_name;
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    public static ToneGenerator toneG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toneG = new ToneGenerator(AudioManager.STREAM_ALARM, 100);


        try {
            intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            showDialog(this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    public void scanQR(View v) {
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (ActivityNotFoundException anfe) {
            showDialog(this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
        }
    }

    private  AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
        downloadDialog.setTitle(title);
        downloadDialog.setMessage(message);
        downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                final String appPackageName = "com.google.zxing.client.android"; // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });
        downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        return downloadDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == this.RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                Log.d("<<QR content", contents);

                String [] data=contents.split("\\|");

                client_ID = data[0];
                Log.d("<< client_ID", client_ID);
                student_ID = data[1];
                Log.d("student_ID", student_ID);
                student_name =data[2];
                Log.d("student_name", student_name);

                try{
                    JSONObject parameters = new JSONObject();
                    parameters.put("sch_Id",client_ID);
                    parameters.put("Stu_id",student_ID);

                    makeJsonObjectRequest(parameters);

                }catch (JSONException e){
                    Log.d("<< JSONException", e.toString());
                }


            }

        }
    }

    private void makeJsonObjectRequest(JSONObject parameters) {


        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, parameters, new Response.Listener<JSONObject>() {



            @Override
            public void onResponse(JSONObject response) {
                Log.d("<< onResponse", response.toString());

                try {
                    // Parsing json object response
                    // response will be a json object

                    String status_code = response.getString("Status");
                    Log.d("status_code", status_code);

                    String status_message = response.getString("StatusMsg");
                    Log.d("status_message", status_message);

                    if(status_code.equalsIgnoreCase("1")){
                        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.thankyou);
                        mp.start();

                        startActivityForResult(intent, 0);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Error: " + e.getMessage(),
                            Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("<< onErrorResponse", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hide the progress dialog

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjReq);
    }
}
