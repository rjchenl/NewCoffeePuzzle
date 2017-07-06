package com.example.user.newcoffeepuzzle.ming_spndcoffelist;

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
import com.example.user.newcoffeepuzzle.ming_main.Common_ming;
import com.example.user.newcoffeepuzzle.ming_main.Profile_ming;

import java.util.List;

/**
 * Created by Java on 2017/6/26.
 */

public class Spndcoffelist_Fragment extends Fragment{
    private final static String TAG = "ming_spndcoffelist_fragment";
    private RecyclerView ry_spndcoffelist;
    private String store_id;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.ming_spndcoffelist_fragment,container,false);
        ry_spndcoffelist =(RecyclerView) view.findViewById(R.id.ry_spndcoffelist);
        ry_spndcoffelist.setLayoutManager(new LinearLayoutManager(getActivity()));
        Profile_ming profile_ming = new Profile_ming(getContext());
        store_id = profile_ming.getStoreId();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: (step1)");
        if (Common_ming.networkConnected(getActivity())) {
            Log.d(TAG, "onStart: (step2)");
            String url = Common_ming.URL + "ming_Spndcoffelist_Servlet";
            List<SpndcoffelistVO> spndcoffelistVOList = null;

            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            try {
                spndcoffelistVOList = new Spndcoffelist_GetAllTask().execute(url,store_id).get();
                Log.d(TAG, "onStart: (step3)");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.toString());
            }
            if(spndcoffelistVOList == null || spndcoffelistVOList.isEmpty()){
                Common_ming.showToast(getActivity(), "no activity found");
                Log.d(TAG, "onStart: (step4)");
            }else{
                ry_spndcoffelist.setAdapter(new Spndcoffelist_Fragment.Spndcoffelist_RecyclerViewAdapter(getActivity(), spndcoffelistVOList));
                Log.d(TAG, "onStart: (step5)");
            }
            progressDialog.cancel();

        }else{
            Common_ming.showToast(getActivity(), "no network connection available");
        }
    }

    private class Spndcoffelist_RecyclerViewAdapter extends RecyclerView.Adapter<Spndcoffelist_RecyclerViewAdapter.ViewHolder> {
        private LayoutInflater layoutInflater;
        private List<SpndcoffelistVO> spndcoffelistVOList;
        private boolean[] actExpanded;

        public Spndcoffelist_RecyclerViewAdapter(Context context, List<SpndcoffelistVO> spndcoffelistVOList) {
            layoutInflater = LayoutInflater.from(context);
            this.spndcoffelistVOList = spndcoffelistVOList;
            actExpanded = new boolean[spndcoffelistVOList.size()];
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView list_id,spn_memId,spn_number,spn_mem_name,spnd_prod;

            public ViewHolder(View itemView) {
                super(itemView);
                list_id = (TextView) itemView.findViewById(R.id.list_id);
                spn_memId = (TextView) itemView.findViewById(R.id.spn_memId);
                spn_number = (TextView) itemView.findViewById(R.id.spn_number);
                spn_mem_name = (TextView) itemView.findViewById(R.id.spn_mem_name);
                spnd_prod = (TextView) itemView.findViewById(R.id.spnd_prod);

            }
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.ming_spndcoffelist_item,parent,false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            SpndcoffelistVO spndcoffelistVO = spndcoffelistVOList.get(position);
            String list_id = spndcoffelistVO.getList_id();
            holder.list_id.setText(list_id);
            String memId = spndcoffelistVO.getMem_id();
            holder.spn_memId.setText(memId);
            String spn_mem_name = spndcoffelistVO.getMem_name();
            holder.spn_mem_name.setText(spn_mem_name);
            String spnd_prod = spndcoffelistVO.getSpnd_prod();
            holder.spnd_prod.setText(spnd_prod);
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
