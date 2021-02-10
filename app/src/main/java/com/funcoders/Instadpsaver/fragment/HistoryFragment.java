package com.funcoders.Instadpsaver.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.funcoders.Instadpsaver.HistoryAdapter;
import com.funcoders.Instadpsaver.R;
import com.funcoders.Instadpsaver.RoomDb.TaskAppDatabase;
import com.funcoders.Instadpsaver.bean.InstaSearchHistorybean;
import com.funcoders.Instadpsaver.common.AddUtils;
import com.funcoders.Instadpsaver.common.Constants;
import com.funcoders.Instadpsaver.common.DialogCallBack;
import com.google.android.gms.ads.AdView;

import java.util.List;

public class HistoryFragment extends Fragment {
    RecyclerView history_rv;
    ImageView search_clear;
    HistoryAdapter adapter;
    private TaskAppDatabase appDatabase;
    List<InstaSearchHistorybean> idlist;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        intiUI(view);
        AdView mAdView = (AdView) getView().findViewById(R.id.adView);
        AddUtils.showGoogleBannerAd(getActivity(),mAdView);
        initRecyclerview();

    }

    private void initRecyclerview() {
        history_rv.setLayoutManager(new GridLayoutManager(getActivity(),2));

        if(idlist.size()!=0)
        {
         adapter = new HistoryAdapter(getActivity(), idlist);
            history_rv.setAdapter(adapter);


        }

    }

    private void intiUI(View view) {
        history_rv=view.findViewById(R.id.history_rv);
        search_clear=view.findViewById(R.id.search_clear);
        appDatabase = TaskAppDatabase.getInstance(getActivity());

        idlist = appDatabase.taskDao().getAllsearchids();
        if(idlist.size()!=0)
        {
            search_clear.setVisibility(View.VISIBLE);
        }else {
            search_clear.setVisibility(View.GONE);
        }
        search_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constants.dialogBoxWithButton(getActivity(), "Are you sure?","This will clear the previously entered usernames.",
                        getActivity().getString(R.string.yes), getActivity().getString(R.string.No), new DialogCallBack() {
                            @Override
                            public void clickedStatus(boolean clickedStatus) {
                                if (clickedStatus) {
                                    appDatabase.taskDao().deleteHistoryTable();
                                    idlist.clear();
                                    adapter.notifyDataSetChanged();
search_clear.setVisibility(View.GONE);

                                }
                            }
                        });


            }
        });



    }

}
