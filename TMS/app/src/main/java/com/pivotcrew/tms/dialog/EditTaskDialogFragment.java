package com.pivotcrew.tms.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.pivotcrew.tms.R;
import com.pivotcrew.tms.model.Task;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by RayChongJH on 5/6/17.
 */
public class EditTaskDialogFragment extends DialogFragment {

    private Context mContext;
    private onUpdateTaskListener mCallback;
    private Fragment callbackFragment;
    private AlertDialog dialog;
    private Task selectedTask;
    private int adapterPos;

    @BindView(R.id.taskNameWrapper)
    TextInputLayout taskNameWrapper;

    @BindView(R.id.etTaskName)
    TextInputEditText etTaskName;

    @BindView(R.id.tvDialogTitle)
    TextView tvDialogTitle;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        try {
            mCallback = (onUpdateTaskListener) callbackFragment;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public interface onUpdateTaskListener {
        void onUpdateTask(Task task, int pos);

        void onDeleteTask(Task task, int pos);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.dialog_add, null, false);
        ButterKnife.bind(this, view);

        setCancelable(false);
        builder.setView(view)
                .setPositiveButton(R.string.action_update, null)
                .setNeutralButton(R.string.action_delete, null)
                .setNegativeButton(R.string.action_discard, null);
        tvDialogTitle.setText(getString(R.string.dialog_edit_title));
        etTaskName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0)
                    taskNameWrapper.setError(null);
            }
        });
        etTaskName.setText(selectedTask.getTaskName());
        etTaskName.setSelection(etTaskName.length());
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    public void hideKeyboard() {
        try {
            InputMethodManager inputManager = (InputMethodManager) mContext
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            View v = ((Activity) mContext).getCurrentFocus();
            if (v == null)
                return;
            inputManager.hideSoftInputFromWindow(v.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            dialog.setTitle(getString(R.string.dialog_edit_title));
            dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(mContext, R.color.colorDarkGrey));
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (etTaskName.length() > 0) {
                        selectedTask.setTaskName(etTaskName.getText().toString().trim());
                        if (mCallback != null)
                            mCallback.onUpdateTask(selectedTask, adapterPos);
                        dismiss();
                    } else {
                        taskNameWrapper.setError(getString(R.string.error_task_name));
                    }
                }
            });
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(mContext)
                            .setMessage(getString(R.string.dialog_delete_message))
                            .setPositiveButton(getString(android.R.string.yes), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (mCallback != null)
                                        mCallback.onDeleteTask(selectedTask, adapterPos);
                                    dialogInterface.dismiss();
                                    dismiss();
                                }
                            })
                            .setNegativeButton(getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                }
            });


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        try {
            hideKeyboard();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Fragment getCallbackFragment() {
        return callbackFragment;
    }

    public void setCallbackFragment(Fragment callbackFragment) {
        this.callbackFragment = callbackFragment;
    }

    public void setSelectedTask(Task selectedTask) {
        this.selectedTask = selectedTask;
    }

    public void setAdapterPos(int adapterPos) {
        this.adapterPos = adapterPos;
    }
}
