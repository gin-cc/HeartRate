package com.android.tweedle.heartrate;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewFragment extends Fragment {
    private int id_;
    private View view_;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        id_ = getArguments().getInt("id");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view_ = inflater.inflate(id_, container,false);
        return view_;
    }


    public static final ViewFragment new_instance(int id){
        // 构造页面
        ViewFragment vf_ = new ViewFragment();
        Bundle bundle_ = new Bundle();
        bundle_.putInt("id", id);
        // 设置参数
        vf_.setArguments(bundle_);

        return vf_;
    }

}
