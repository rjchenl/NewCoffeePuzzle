package com.example.user.newcoffeepuzzle.rjchenl_member;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.rjchenl_main.Profile;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.EnumMap;
import java.util.Map;


public class MyInfoFragent extends Fragment {

    private String mem_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rj_myinfo_fragment,container,false);
        Profile profile = new Profile(getActivity());
        mem_id = profile.getMemId();


        TextView tvmember_name = (TextView) view.findViewById(R.id.tvmember_name);
        tvmember_name.setText(profile.getMem_name());
        TextView tvmem_add = (TextView) view.findViewById(R.id.tvmem_add);
        tvmem_add.setText(profile.getMem_add());
        TextView tvmem_tel = (TextView) view.findViewById(R.id.tvmem_tel);
        tvmem_tel.setText(profile.getMem_tel());
        TextView tvmem_email = (TextView) view.findViewById(R.id.tvmem_email);
        tvmem_email.setText(profile.getMem_email());
        TextView tvmem_points = (TextView) view.findViewById(R.id.tvmem_points);
        tvmem_points.setText(profile.getMem_points().toString());








        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //夾帶資訊-出去
        final Bundle bundle = new Bundle();
        bundle.putString("mem_id",mem_id);

        //點擊顯示qr code
        Button bt_memberinfo_show_qrcode = (Button) view.findViewById(R.id.bt_memberinfo_show_qrcode);
        bt_memberinfo_show_qrcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MemberDialogeFragment membberdialogefragment = new MemberDialogeFragment();

                membberdialogefragment.setArguments(bundle);
                FragmentManager fragmentManger = getFragmentManager();
                membberdialogefragment.show(fragmentManger,"");
            }
        });



    }

    public static class MemberDialogeFragment extends DialogFragment{


        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = super.onCreateDialog(savedInstanceState);
            //不顯示標題
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);


            return dialog;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.rj_member_qrcode_dialoge,null);

            return view;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            Button bt_member_leave = (Button) view.findViewById(R.id.bt_member_leave);
            bt_member_leave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            Bundle bundle = getArguments();
            String mem_id = bundle.getString("mem_id");
            //startpaste
            // QR code 的內容
            String QRCodeContent = mem_id;
            // QR code 寬度
            int QRCodeWidth = 800;
            // QR code 高度
            int QRCodeHeight = 800;
            // QR code 內容編碼
            Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

            MultiFormatWriter writer = new MultiFormatWriter();
            try
            {

                String url = "https://www.google.com.tw/";
                // 容錯率姑且可以將它想像成解析度，分為 4 級：L(7%)，M(15%)，Q(25%)，H(30%)
                // 設定 QR code 容錯率為 H
                hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

                // 建立 QR code 的資料矩陣
                BitMatrix result = writer.encode(QRCodeContent, BarcodeFormat.QR_CODE, QRCodeWidth, QRCodeHeight, hints);
                // ZXing 還可以生成其他形式條碼，如：BarcodeFormat.CODE_39、BarcodeFormat.CODE_93、BarcodeFormat.CODE_128、BarcodeFormat.EAN_8、BarcodeFormat.EAN_13...

                //建立點陣圖
                Bitmap bitmap = Bitmap.createBitmap(QRCodeWidth, QRCodeHeight, Bitmap.Config.ARGB_8888);
                // 將 QR code 資料矩陣繪製到點陣圖上
                for (int y = 0; y<QRCodeHeight; y++)
                {
                    for (int x = 0;x<QRCodeWidth; x++)
                    {
                        bitmap.setPixel(x, y, result.get(x, y) ? Color.BLACK : Color.WHITE);
                    }
                }

                ImageView imgView = (ImageView) view.findViewById(R.id.member_id_qrcode);
                // 設定為 QR code 影像
                imgView.setImageBitmap(bitmap);
            }
            catch (WriterException e)
            {

                e.printStackTrace();
            }
        }











    }


}

