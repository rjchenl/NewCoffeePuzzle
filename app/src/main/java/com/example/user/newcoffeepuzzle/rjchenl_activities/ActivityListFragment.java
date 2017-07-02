package com.example.user.newcoffeepuzzle.rjchenl_activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.rjchenl_main.Common_RJ;

import java.util.List;

/**
 * Created by user on 2017/6/23.
 */

public class ActivityListFragment extends Fragment {
    private static final String TAG = "ActivityListFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvactivities;
    private boolean[] actExpanded;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rj_fragment_activity_list, container, false);
        //以下滑下自動更新可以省略
//        swipeRefreshLayout =  (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                swipeRefreshLayout.setRefreshing(true);
//                showAllActs();
//                swipeRefreshLayout.setRefreshing(false);
//            }
//        });
        //recyclerview 才需要
        rvactivities = (RecyclerView) view.findViewById(R.id.rvactivities);
        rvactivities.setLayoutManager(new LinearLayoutManager(getActivity()));
//        rvactivities.setLayoutManager(new RecyclerView.LayoutManager(getActivity()));為什麼不行


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        showAllActs();
    }

    private void showAllActs() {
        if (Common_RJ.networkConnected(getActivity())) {
            String url = Common_RJ.URL + "ActivityServlet";
            List<ActivityVO> acts = null;
            try {
                acts = new ActivityGetAllTask().execute(url).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (acts == null || acts.isEmpty()) {
                Common_RJ.showToast(getActivity(), "No acts found");
            } else {
                rvactivities.setAdapter(new ActivitiesRecyclerViewAdapter(getActivity(),acts));
            }
        }
    }

    private class ActivitiesRecyclerViewAdapter extends RecyclerView.Adapter<ActivitiesRecyclerViewAdapter.MyViewHolder> {
        private LayoutInflater layoutInflater;
        private List<ActivityVO> acts;

        public ActivitiesRecyclerViewAdapter(Context context, List<ActivityVO> acts) {
            layoutInflater = LayoutInflater.from(context);
            actExpanded = new boolean[acts.size()];
            this.acts = acts;
        }

        @Override
        public int getItemCount() {
            return acts.size();
        }//

        //取得viewhold佈局檔
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.rj_activities_recycle_item, parent, false);
            return new MyViewHolder(itemView);
        }
        //與資料做連結
        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final ActivityVO act = acts.get(position);
            String url = Common_RJ.URL + "ActivityServlet";
            //取得table pk
            String activid = act.getActiv_id();
            int imageSize = 250;
            new ActivityGetImageTask(holder.itemImage).execute(url, activid, imageSize);
            holder.activity_name.setText(act.getActiv_name());
            holder.activity_intro.setText(act.getActiv_intro());

            //按下彈出其它隱藏資訊
            holder.activity_intro.setVisibility(actExpanded[position]? View.VISIBLE : View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    expand(holder.getAdapterPosition());
                }
            });

        }

        //創立MyViewHolder類別 夾帶要帶的view
        class MyViewHolder extends RecyclerView.ViewHolder {
            ImageView itemImage;
            TextView activity_name, activity_intro;

            public MyViewHolder(View itemView) {
                super(itemView);
                itemImage = (ImageView) itemView.findViewById(R.id.itemImage);
                activity_name = (TextView) itemView.findViewById(R.id.activity_name);
                activity_intro = (TextView) itemView.findViewById(R.id.activity_intro);
            }
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