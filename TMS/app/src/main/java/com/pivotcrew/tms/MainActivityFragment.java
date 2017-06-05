package com.pivotcrew.tms;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.pivotcrew.tms.adapter.TaskAdapter;
import com.pivotcrew.tms.database.DBOperation;
import com.pivotcrew.tms.dialog.AddTaskDialogFragment;
import com.pivotcrew.tms.dialog.EditTaskDialogFragment;
import com.pivotcrew.tms.model.Task;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.pivotcrew.tms.Global.TAG_ADD_TASK;
import static com.pivotcrew.tms.Global.TAG_EDIT_TASK;
/**
 * Created by RayChongJH on 5/6/17.
 */
public class MainActivityFragment extends Fragment implements AddTaskDialogFragment.onAddTaskListener,
        TaskAdapter.AdapterCallback, EditTaskDialogFragment.onUpdateTaskListener {

    Context mContext;
    DBOperation dbOperation;

    @BindView(R.id.layoutEmptyState)
    RelativeLayout emptyStateLayout;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.fabAdd)
    FloatingActionButton fabAdd;

    ArrayList<Task> tasks;
    TaskAdapter adapter;
    Calendar calendar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        dbOperation = new DBOperation(mContext);
        dbOperation.openDB();
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        tasks = new ArrayList<>();
        if (dbOperation.getEntryCount() > 0)
            tasks = dbOperation.getTaskList();

        adapter = new TaskAdapter(mContext, this, tasks);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (dbOperation != null) {
                dbOperation.openDB();
                recyclerView.setVisibility(dbOperation.getEntryCount() > 0 ? View.VISIBLE : View.GONE);
                emptyStateLayout.setVisibility(dbOperation.getEntryCount() > 0 ? View.GONE : View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            if (dbOperation != null)
                dbOperation.closeDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.fabAdd)
    public void onAddNewEntry() {
        AddTaskDialogFragment addEntryDialogFragment = new AddTaskDialogFragment();
        addEntryDialogFragment.setCallbackFragment(this);
        addEntryDialogFragment.show(getFragmentManager(), TAG_ADD_TASK);
    }

    @Override
    public void onAddTask(Task task) {
        try {
            if (task != null && task.getTaskName().length() > 0) {
                calendar = Calendar.getInstance();
                String dateTimeStr = calendar.get(Calendar.DAY_OF_MONTH) + "-" +
                        (calendar.get(Calendar.MONTH) + 1) + "-" +
                        calendar.get(Calendar.YEAR) + " " +
                        calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                        calendar.get(Calendar.MINUTE) + ":" +
                        calendar.get(Calendar.SECOND);
                task.setDate(dateTimeStr);
                task.setIsModified(0);
                Task task_ = dbOperation.addTask(task);

                tasks.add(task_);
                adapter.notifyDataSetChanged();
                recyclerView.setVisibility(dbOperation.getEntryCount() > 0 ? View.VISIBLE : View.GONE);
                emptyStateLayout.setVisibility(dbOperation.getEntryCount() > 0 ? View.GONE : View.VISIBLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCardSelected(int pos) {
        try {
            EditTaskDialogFragment editTaskDialogFragment = new EditTaskDialogFragment();
            editTaskDialogFragment.setCallbackFragment(this);
            editTaskDialogFragment.setAdapterPos(pos);
            editTaskDialogFragment.setSelectedTask(tasks.get(pos));
            editTaskDialogFragment.show(getFragmentManager(), TAG_EDIT_TASK);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateTask(Task task, int pos) {
        try {
            if (dbOperation != null) {
                calendar = Calendar.getInstance();
                String dateTimeStr = calendar.get(Calendar.DAY_OF_MONTH) + "-" +
                        (calendar.get(Calendar.MONTH) + 1) + "-" +
                        calendar.get(Calendar.YEAR) + " " +
                        calendar.get(Calendar.HOUR_OF_DAY) + ":" +
                        calendar.get(Calendar.MINUTE) + ":" +
                        calendar.get(Calendar.SECOND);
                task.setDate(dateTimeStr);
                task.setIsModified(1);
                dbOperation.updateTask(task);
                tasks.set(pos, task);
                adapter.notifyItemChanged(pos);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDeleteTask(final Task task, final int pos) {
        try {
            if (dbOperation != null) {
                dbOperation.deleteTaskEntry(task);
                tasks.remove(pos);
                adapter.notifyItemRemoved(pos);
            }
            recyclerView.setVisibility(dbOperation.getEntryCount() > 0 ? View.VISIBLE : View.GONE);
            emptyStateLayout.setVisibility(dbOperation.getEntryCount() > 0 ? View.GONE : View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
