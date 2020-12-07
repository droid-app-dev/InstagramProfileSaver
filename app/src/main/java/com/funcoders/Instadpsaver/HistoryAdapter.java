package com.funcoders.Instadpsaver;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.funcoders.Instadpsaver.bean.InstaSearchHistorybean;
import com.funcoders.Instadpsaver.common.Constants;
import com.funcoders.Instadpsaver.fragment.SearchFragment;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.MyViewHolder> {

    Context mcontext;
    List<InstaSearchHistorybean> idlist;


    public HistoryAdapter(Context mcontext, List<InstaSearchHistorybean> idlist) {

        this.mcontext = mcontext;
        this.idlist = idlist;

    }


    @NonNull
    @Override
    public HistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_item_history, parent, false);

        HistoryAdapter.MyViewHolder myViewHolder = new HistoryAdapter.MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryAdapter.MyViewHolder holder, int position) {

        final InstaSearchHistorybean bean = idlist.get(position);
        holder.history_tv.setText(bean.getInsta_id());
        holder.history_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //setClipboard(mcontext,bean.getInsta_id());
                System.out.println("Insta ID"+bean.getInsta_id());

              //  openHistoryfragment(bean.getInsta_id());

                copyToClipboard(bean.getInsta_id());
            }
        });

    }

    public void copyToClipboard(String copyText) {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager)
                    mcontext.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(copyText);
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                    mcontext.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData
                    .newPlainText("Your OTP", copyText);
            clipboard.setPrimaryClip(clip);
        }
        Toast toast = Toast.makeText(mcontext,
                "Your UserName copied", Toast.LENGTH_SHORT);
        toast.show();
        //displayAlert("Your OTP is copied");
    }

    @Override
    public int getItemCount() {
        return idlist.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView history_tv;
        LinearLayout history_ll;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            history_tv=itemView.findViewById(R.id.hystory_tv);
            history_ll=itemView.findViewById(R.id.history_ll);

        }
    }

    public void openHistoryfragment(String InstaID)
    {
        Fragment historyFragment = new SearchFragment();
        Bundle bundle = new Bundle();
       // bundle.putInt(Constants.EXTRA_COME_FROM, 1);
        bundle.putString(Constants.EXTRA_COMEFROM,"HistoryFragment");
        bundle.putString(Constants.EXTRA_HISTORY_InstaID,""+InstaID);
     //   bundle.putBoolean(Constants.EXTRA_READ_FROM_TECH_CACHE, readFromTechCache);
      //  bundle.putBoolean(Constants.LoginFlag, true);
//        if (fromLogin != null) {
//            bundle.putString("coimgFromLogin",fromLogin);
//        }
        historyFragment.setArguments(bundle);
        openFragment(historyFragment);

    }

    public void openFragment(Fragment mainMenuFragment) {
        try {
            try {
                FragmentManager fragmentManager =((FragmentActivity)mcontext).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.flContainer, mainMenuFragment, mainMenuFragment.getClass().getName());
                //        fragmentTransaction.commit();
                fragmentTransaction.commitAllowingStateLoss();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

}