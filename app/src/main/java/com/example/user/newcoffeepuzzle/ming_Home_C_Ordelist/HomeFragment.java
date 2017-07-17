package com.example.user.newcoffeepuzzle.ming_Home_C_Ordelist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.user.newcoffeepuzzle.R;


public class HomeFragment extends Fragment{
    ImageView imagecoco,cart,delivery_truck,checked;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        final View view = inflater.inflate(R.layout.ming_home_fragment,container,false);
        findView_Home(view);
        imagecoco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Ordelist_1_Activtiy.class);
                startActivity(intent);
            }
        });
        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Ordelist_2_Activtiy.class);
                startActivity(intent);
            }
        });
        delivery_truck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Ordelist_4_Activtiy.class);
                startActivity(intent);
            }
        });
        checked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Ordelist_3_Activtiy.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void findView_Home(View view) {
        imagecoco = (ImageView) view.findViewById(R.id.imagecoco);
        cart = (ImageView) view.findViewById(R.id.cart);
        delivery_truck = (ImageView) view.findViewById(R.id.delivery_truck);
        checked = (ImageView) view.findViewById(R.id.checked);
    }

}
