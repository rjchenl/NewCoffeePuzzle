package com.example.user.newcoffeepuzzle.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.main.Common;

import java.util.List;

/**
 * Created by user on 2017/6/23.
 */

public class ActivityListFragment extends Fragment {
    private static final String TAG = "ActivityListFragment";
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView rvactivities;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: yesyes");
        View view = inflater.inflate(R.layout.activity_list_fragment, container, false);

        rvactivities = (RecyclerView) view.findViewById(R.id.rvactivities);
        rvactivities.setLayoutManager(new LinearLayoutManager(getActivity()));





        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        showAllActs();
    }

    private void showAllActs() {
        if (Common.networkConnected(getActivity())) {
            String url = Common.URL + "NewsServlet2";
            List<ActivityVO> acts = null;
            try {
                acts = new ActivityGetAllTask().execute(url).get();
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            if (acts == null || acts.isEmpty()) {
                Common.showToast(getActivity(), "No acts found");
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
            this.acts = acts;
        }

        @Override
        public int getItemCount() {
            return acts.size();
        }//


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = layoutInflater.inflate(R.layout.activities_recycle_item, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            final ActivityVO act = acts.get(position);
            String url = Common.URL + "NewsServlet2";
            String activid = act.getActiv_id();
            int imageSize = 250;
            new ActivityGetImageTask(holder.itemImage).execute(url, activid, imageSize);
            holder.activity_name.setText(act.getActiv_name());
            holder.activity_intro.setText(act.getActiv_intro());

//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Fragment fragment = new
//                }
//            });


        }


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

        private void switchFragment(Fragment fragment) {
            FragmentTransaction fragmentTransaction =
                    getFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
}