package com.example.user.newcoffeepuzzle.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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


public class Activities_fragment extends Fragment {

    private final static String TAG = "Activities_fragment";
    private RecyclerView rvactivities;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activities_fragment,container,false);
        rvactivities =(RecyclerView) view.findViewById(R.id.ryActivieiess);
        rvactivities.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "NewsServlet2";
            List<ActivityVO> actList = null;

            ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Loading...");
            progressDialog.show();
            try {
                actList = new ActivityGetAllTask().execute(url).get();
                Log.d(TAG, "onStart: here");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.toString());
            }
            if(actList == null || actList.isEmpty()){
                Common.showToast(getActivity(), "no activity found");
            }else{
                rvactivities.setAdapter(new ActivityRecyclerViewAdapter(getActivity(), actList));
            }
            progressDialog.cancel();

        }else{
            Common.showToast(getActivity(), "no network connection available");
        }
    }

    private class ActivityRecyclerViewAdapter extends RecyclerView.Adapter<ActivityRecyclerViewAdapter.ViewHolder>{
        private LayoutInflater layoutInflater;
        private List<ActivityVO> actList;
        private boolean[] actExpanded;

        public ActivityRecyclerViewAdapter(Context context,List<ActivityVO> actList){
            layoutInflater = LayoutInflater.from(context);
            this.actList=actList;
            actExpanded = new boolean[actList.size()];
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            TextView activity_name,activity_intro;

            public ViewHolder(View itemView) {
                super(itemView);
                activity_name = (TextView) itemView.findViewById(R.id.activity_name);
                activity_intro = (TextView) itemView.findViewById(R.id.activity_intro);

            }
        }



        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.activities_recycle_item,parent,false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            ActivityVO actVO = actList.get(position);
            String activ_name = actVO.getActiv_name();
            holder.activity_name.setText(activ_name);
            holder.activity_intro.setText(actVO.getActiv_intro());
            holder.activity_intro.setVisibility(
                    actExpanded[position] ? View.VISIBLE : View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expand(holder.getAdapterPosition());
                }
            });

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

        @Override
        public int getItemCount() {
            return actList.size();
        }






    }




}
