package com.example.user.newcoffeepuzzle.take_orders;

import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.main.Common;

import java.util.List;

/**
 * Created by Java on 2017/6/26.
 */

public class Spndcoffelist_Fragment extends Fragment{
    private final static String TAG = "spndcoffelist_fragment";
    private RecyclerView ry_spndcoffelist;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.spndcoffelist_fragment,container,false);
        ry_spndcoffelist =(RecyclerView) view.findViewById(R.id.ry_spndcoffelist);
        ry_spndcoffelist.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: (step1)");
        if (Common.networkConnected(getActivity())) {
            Log.d(TAG, "onStart: (step2)");
            String url = Common.URL + "Spndcoffelist_Servlet";
            List<SpndcoffelistVO> spndcoffelistVOList = null;

            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            try {
                spndcoffelistVOList = new Spndcoffelist_GetAllTask().execute(url).get();
                Log.d(TAG, "onStart: (step3)");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.toString());
            }
            if(spndcoffelistVOList == null || spndcoffelistVOList.isEmpty()){
                Common.showToast(getActivity(), "no activity found");
                Log.d(TAG, "onStart: (step4)");
            }else{
                ry_spndcoffelist.setAdapter(new Spndcoffelist_Fragment.Take_Orders_RecyclerViewAdapter(getActivity(), spndcoffelistVOList));
                Log.d(TAG, "onStart: (step5)");
            }
            progressDialog.cancel();

        }else{
            Common.showToast(getActivity(), "no network connection available");
        }
    }

    private class Take_Orders_RecyclerViewAdapter extends RecyclerView.Adapter<Take_Orders_RecyclerViewAdapter.ViewHolder> {
        private LayoutInflater layoutInflater;
        private List<SpndcoffelistVO> spndcoffelistVOList;
        private boolean[] actExpanded;

        public Take_Orders_RecyclerViewAdapter(Context context, List<SpndcoffelistVO> spndcoffelistVOList) {
            layoutInflater = LayoutInflater.from(context);
            this.spndcoffelistVOList = spndcoffelistVOList;
            actExpanded = new boolean[spndcoffelistVOList.size()];
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView list_id,spn_memId,spn_number;

            public ViewHolder(View itemView) {
                super(itemView);
                list_id = (TextView) itemView.findViewById(R.id.list_id);
                spn_memId = (TextView) itemView.findViewById(R.id.spn_memId);
                spn_number = (TextView) itemView.findViewById(R.id.spn_number);

            }
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.spndcoffelist_item,parent,false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            SpndcoffelistVO spndcoffelistVO = spndcoffelistVOList.get(position);
            String list_id = spndcoffelistVO.getList_id();
            holder.list_id.setText(list_id);
            String memId = spndcoffelistVO.getMem_id();
            holder.spn_memId.setText(memId);
            Integer spn_number = spndcoffelistVO.getList_left();
            holder.spn_number.setText(spn_number.toString());

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expand(holder.getAdapterPosition());
                }
            });

        }

        @Override
        public int getItemCount() {
            return spndcoffelistVOList.size();
        }

        private void expand(int position) {
            // 被點擊的資料列才會彈出內容，其他資料列的內容會自動縮起來
            // for (int i=0; i<newsExpanded.length; i++) {
            // newsExpanded[i] = false;
            // }
            // newsExpanded[position] = true;

            actExpanded[position] = !actExpanded[position];
            notifyDataSetChanged();
        }
    }
}
