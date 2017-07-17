package com.example.user.newcoffeepuzzle.ming_spndcoffelist;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.ming_main.Common_ming;
import com.example.user.newcoffeepuzzle.ming_main.Profile_ming;

import org.json.JSONObject;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class Spndcoffeelist_Inser extends Fragment {
    private static final String PACKAGE = "com.google.zxing.client.android";
    private static final int REQUEST_BARCODE_SCAN = 0;
    private TextView Message;
    String store_id;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.ming_spndcoffeelist_inser, container, false);
        Profile_ming profile_ming = new Profile_ming(getContext());
        store_id = profile_ming.getStoreId();
        findView_QR(view);
        return view;
    }

    private void findView_QR(View view) {

        Intent intent = new Intent(
                "com.google.zxing.client.android.SCAN");
        try {
            startActivityForResult(intent, REQUEST_BARCODE_SCAN);
        }
        // 如果沒有安裝Barcode Scanner，就跳出對話視窗請user安裝
        catch (ActivityNotFoundException e) {
            showDownloadDialog();
        }
    }

    private void showDownloadDialog() {
        AlertDialog.Builder downloadDialog = new AlertDialog.Builder(getContext());
        downloadDialog.setTitle("No Barcode Scanner Found");
        downloadDialog
                .setMessage("Please download and install Barcode Scanner!");
        downloadDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Uri uri = Uri.parse("market://search?q=pname:"
                                + PACKAGE);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException ex) {
                            Log.e(ex.toString(),
                                    "Play Store is not installed; cannot install Barcode Scanner");
                        }
                    }
                });
        downloadDialog.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        downloadDialog.show();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_BARCODE_SCAN) {
            String message = "";
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String resultFormat = intent.getStringExtra("SCAN_RESULT_FORMAT");
                message = (  contents + "\nResult format: " + resultFormat);
            } else if (resultCode == RESULT_CANCELED) {
                message = "Scan was Cancelled!";
            }

        }
        if(Common_ming.networkConnected(getActivity())){
            String url = Common_ming.URL + "ming_Spndcoffelist_Servlet";
            try {
                String contents = intent.getStringExtra("SCAN_RESULT");
                JSONObject.quote(contents);
                JSONObject json = new JSONObject(contents);
                String mem_id = json.getString("mem_id");
                String spnd_id = json.getString("spnd_id");
                String spnd_prod = json.getString("spnd_prod");

                intent = new SpndcoffeelistGetInsert().execute(url,store_id,mem_id,spnd_id,spnd_prod).get();
            } catch (Exception e) {
                e.printStackTrace();
                Log.d(TAG, "intent: + intent" +intent);
            }

            Common_ming.showToast(getContext(),R.string.spndcoffeQR);
        }
    }
}