package com.example.user.newcoffeepuzzle.rjchenl_favoriatestore;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.user.newcoffeepuzzle.R;
import com.example.user.newcoffeepuzzle.rjchenl_main.Common_RJ;

import java.util.List;

public class BrowseMyFavoriateStoreListFragment extends Fragment {
    private static final String TAG = "BrowseMyFavoriateStoreListFragment";
    private RecyclerView rc_myfavoriatestore;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rj_browsemyfavoriatestorelist_fragment,container,false);
        rc_myfavoriatestore = (RecyclerView) view.findViewById(R.id.rc_myfavoriatestore);
        //使用LinearLayoutManager 採垂直顯示
        rc_myfavoriatestore.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getMyfavoriateSotre_fromDB();

    }

    private void getMyfavoriateSotre_fromDB() {
//        if (Common_RJ.networkConnected(getActivity())) {
//            String url = Common_RJ.URL + "ActivityServlet";
//            List<ActivityVO> acts = null;
//            try {
//                acts = new ActivityGetAllTask().execute(url).get();
//            } catch (Exception e) {
//                Log.e(TAG, e.toString());
//            }
//            if (acts == null || acts.isEmpty()) {
//                Common_RJ.showToast(getActivity(), "No acts found");
//            } else {
//                rvactivities.setAdapter(new ActivitiesRecyclerViewAdapter(getActivity(),acts));
//            }
        }




}
