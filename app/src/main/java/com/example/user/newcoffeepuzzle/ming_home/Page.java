package com.example.user.newcoffeepuzzle.ming_home;

import android.support.v4.app.Fragment;

/**
 * Created by Java on 2017/6/21.
 */

public class Page {
    private Fragment fragment;
    private String title;

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Page(Fragment fragment, String title) {

        this.fragment = fragment;
        this.title = title;
    }
}
