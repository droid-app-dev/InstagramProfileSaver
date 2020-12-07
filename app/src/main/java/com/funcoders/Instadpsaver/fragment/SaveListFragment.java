package com.funcoders.Instadpsaver.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.funcoders.Instadpsaver.InstaListAdapter;
import com.funcoders.Instadpsaver.R;
import com.funcoders.Instadpsaver.RoomDb.TaskAppDatabase;
import com.funcoders.Instadpsaver.SwipeToDeleteCallback;
import com.funcoders.Instadpsaver.bean.ProfileBean;

import java.util.ArrayList;
import java.util.List;

public class SaveListFragment extends Fragment {

    RecyclerView instalist_rv;
    TextView norecorsfound_tv;
    List<ProfileBean> profileList = new ArrayList<>();
    InstaListAdapter adapter;
    RelativeLayout relativelayout_rl;
    TaskAppDatabase appDatabase;
    ProgressDialog progressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_save, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        initUI(view);
        initRecyclerView(view);
        getAllDataFromdb();

    }

    private void getAllDataFromdb() {
        appDatabase = TaskAppDatabase.getInstance(getActivity());
        GetTasks gt = new GetTasks();
        gt.execute();

    }


    private void initRecyclerView(View view) {
        instalist_rv.setLayoutManager(new LinearLayoutManager(getContext()));
        instalist_rv.setItemAnimator(new DefaultItemAnimator());
    }

    private void initUI(View view) {
        instalist_rv = view.findViewById(R.id.instalist_rv);
        norecorsfound_tv = view.findViewById(R.id.norecorsfound_tv);
        relativelayout_rl = view.findViewById(R.id.relativelayout_rl);

    }


    class GetTasks extends AsyncTask<Void, Void, List<ProfileBean>> {
        @Override
        protected void onPreExecute() {

           // progressDialog = Constants.showProgressDialog(getActivity(), "");

            super.onPreExecute();
        }

        @Override
        protected List<ProfileBean> doInBackground(Void... voids) {

            List<ProfileBean> profileList = appDatabase.taskDao().getAll();

            return profileList;
        }

        @Override
        protected void onPostExecute(List<ProfileBean> profileList) {
            super.onPostExecute(profileList);

            if (profileList.size() != 0) {

                instalist_rv.setVisibility(View.VISIBLE);
                norecorsfound_tv.setVisibility(View.GONE);

                adapter = new InstaListAdapter(getActivity(), profileList);
                instalist_rv.setAdapter(adapter);

                enableSwipeToDeleteAndUndo();


            }
            else {
                instalist_rv.setVisibility(View.GONE);

                norecorsfound_tv.setVisibility(View.VISIBLE);
            }

        //    progressDialog.dismiss();
        }
    }



    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getActivity()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final ProfileBean item = adapter.getData().get(position);

                adapter.removeItem(position);


                /*Snackbar snackbar = Snackbar
                        .make(relativelayout_rl, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        adapter.restoreItem(item, position);
                        instalist_rv.scrollToPosition(position);
                    }
                });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();*/

                appDatabase.taskDao().delete(item);



            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(instalist_rv);
    }

}



