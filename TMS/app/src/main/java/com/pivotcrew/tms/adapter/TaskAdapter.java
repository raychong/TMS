package com.pivotcrew.tms.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pivotcrew.tms.R;
import com.pivotcrew.tms.model.Task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
/**
 * Created by RayChongJH on 5/6/17.
 */
public class TaskAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Task> list;
    private Context mContext;
    private AdapterCallback mAdapterCallback;
    private Fragment selectedFragment;
    private SimpleDateFormat sdf;
    private String timeFormat;

    public TaskAdapter(Context mContext, Fragment selectedFragment,ArrayList<Task> list) {
        this.mContext = mContext;
        this.selectedFragment = selectedFragment;
        this.list = list;
        timeFormat = "%1$02d";
        sdf = new SimpleDateFormat("dd-M-yyyy HH:mm:ss", Locale.ENGLISH);
        try{
            mAdapterCallback = (AdapterCallback)selectedFragment;
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        RecyclerView.ViewHolder vh = null;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_task, parent, false);
        vh = new VhTask(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
        Task task = list.get(position);
        VhTask taskHolder = (VhTask)holder;
        try{
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(task.getDate()));
            taskHolder.tvTaskName.setText(task.getTaskName());
            taskHolder.tvDate.setText(calendar.get(Calendar.DAY_OF_MONTH)+"-"+
                    (calendar.get(Calendar.MONTH)+1)+"-"+
                    calendar.get(Calendar.YEAR)+" "+
                    calendar.get(Calendar.HOUR)+":"+
                    String.format(timeFormat, calendar.get(Calendar.MINUTE))+":"+
                    String.format(timeFormat, calendar.get(Calendar.SECOND))+" "+((calendar.get(Calendar.AM_PM))==0?"AM":"PM"));
            taskHolder.tvCreated.setText(task.getIsModified()==1?mContext.getString(R.string.hint_updated):mContext.getString(R.string.hint_created));
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public interface AdapterCallback {
        void onCardSelected(int pos);
    }
    @Override
    public int getItemCount() {
        if(list == null)
            return 0;
        return list.size();
    }

    class VhTask extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvTaskName,tvCreated,tvDate;
        CardView cvTask;
        public VhTask(View itemView) {
            super(itemView);
            tvTaskName = (TextView) itemView.findViewById(R.id.tvTaskName);
            tvCreated = (TextView) itemView.findViewById(R.id.tvCreated);
            tvDate = (TextView) itemView.findViewById(R.id.tvDate);

            cvTask = (CardView) itemView.findViewById(R.id.cvTask);
            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            try{
                if(mAdapterCallback != null)
                    mAdapterCallback.onCardSelected(getAdapterPosition());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        try{
            if(holder instanceof VhTask){
                VhTask itemHolder = (VhTask) holder;
                itemHolder.tvTaskName.setText("");
                itemHolder.tvCreated.setText("");
                itemHolder.tvDate.setText("");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
