package com.podar.yesmaam;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends Activity {
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            Intent intent = new Intent(ACTION_SCAN);
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
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                Log.d("<<content", contents);
              /*
                String []data=contents.split("#");
                Log.d("Data", data.toString());
                Log.d("Data length", String.valueOf(data.length));
                b_title=data[0];
                Log.d("content", b_title);
                b_services=data[1];
                Log.d("content", b_services);
                post =data[2];
                Log.d("content", post);
                _id=data[3];
                Log.d("content", _id);
                mobile_no=data[4];
                Log.d("content", mobile_no);
                email =data[5];
                Log.d("content", email);
                website=data[6];
                Log.d("content", website);
                address=data[7];
                Log.d("content", address);*/

               /* if(b_title.isEmpty()==true || b_services.isEmpty()==true || post.isEmpty()==true || _id.isEmpty()==true ||
                        mobile_no.isEmpty()==true || email.isEmpty()==true || website.isEmpty()==true || address.isEmpty()==true){
                    Toast toast=Toast.makeText(getActivity().getBaseContext(), "Invalid QR code!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER,0, 0);
                    toast.show();
                }
                else{
                    db=getActivity().openOrCreateDatabase("CARD_DB", Context.MODE_PRIVATE, null);
                    db.execSQL("CREATE TABLE IF NOT EXISTS cards(b_title VARCHAR,b_services VARCHAR, post VARCHAR, _id VARCHAR, mobile_no VARCHAR, email VARCHAR, website VARCHAR, address VARCHAR);");
                    db.execSQL("INSERT INTO cards VALUES('"+b_title+"','"+b_services+
                            "','"+post+"','"+_id+
                            "','"+mobile_no+"','"+email+
                            "','"+website+"','"+address+"');");
                    Log.d("card receive","card store in database");

                    Toast toast = Toast.makeText(getActivity().getBaseContext(),"Profile card received successfully!", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();

                }*/


            }

        }
    }
}
