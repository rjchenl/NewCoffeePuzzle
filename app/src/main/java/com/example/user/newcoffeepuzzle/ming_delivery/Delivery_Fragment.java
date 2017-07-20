package com.example.user.newcoffeepuzzle.ming_delivery;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.ming_QRin.SpndcoffeelistGetUpdate_text;
import com.example.user.newcoffeepuzzle.ming_main.Common_ming;
import com.example.user.newcoffeepuzzle.ming_main.Profile_ming;

import org.json.JSONObject;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;


public class Delivery_Fragment extends Fragment{
    private static final String PACKAGE = "com.google.zxing.client.android";
    private static final int REQUEST_BARCODE_SCAN = 0;
    private TextView tvMessage;
    private String store_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.ming_delivery_fragment,container,false);
        findViews(view);
        Profile_ming profile_ming = new Profile_ming(getContext());
        store_id = profile_ming.getStoreId();
        return view;
    }

    private void findViews(View view) {
        tvMessage = (TextView) view.findViewById(R.id.tvMessage);
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

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        String mem_name = null,ord_id = null,ord_shipping,ord_add = null,ord_time = null,store_id;

        if (requestCode == REQUEST_BARCODE_SCAN) {
            String message = "";
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String resultFormat = intent.getStringExtra("SCAN_RESULT_FORMAT");
                message = (  contents + "\nResult format: " + resultFormat);
            } else if (resultCode == RESULT_CANCELED) {
                message = "Scan was Cancelled!";
            }
            tvMessage.setText(message);
        }
        if(Common_ming.networkConnected(getActivity())){
            String url = Common_ming.URL + "ming_Orderlist_Servlet";
            try {
                String contents = intent.getStringExtra("SCAN_RESULT");
                JSONObject.quote(contents);
                JSONObject json = new JSONObject(contents);
                mem_name = json.getString("mem_name");
                ord_id = json.getString("ord_id");
                ord_shipping = json.getString("ord_shipping");
                ord_add = json.getString("ord_add");
                ord_time = json.getString("ord_time");
                store_id = json.getString("store_id");


                if (Delivery_Fragment.this.store_id.equals(store_id)){
                    new DeliveryGetUpdate().execute(url, ord_id, ord_shipping, store_id).get();
                    Common_ming.showToast(getContext(),R.string.delivery_OK);
                }else {
                    Toast toast = Toast.makeText(getContext(), "錯誤訂單!!", Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            final Bundle bundle = new Bundle();
            bundle.putString("mem_name",mem_name);
            bundle.putString("ord_id",ord_id);
            bundle.putString("ord_add",ord_add);
            bundle.putString("ord_time",ord_time);
            Intent in = new Intent(getContext(),Delivery_Fragment_text.class);
            in.putExtras(bundle);
            startActivity(in);
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
}
