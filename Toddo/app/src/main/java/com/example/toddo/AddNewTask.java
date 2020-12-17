package com.example.toddo;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.toddo.Utils.DBHandler;
import com.example.toddo.tasks.TaskContent;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.List;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";
    private EditText newTaskText;
    private EditText newTaskDate;
    private EditText newTaskTime;
    private Spinner newTaskPriority;
    private ImageButton newTaskSaveButton;
    private ImageButton newTaskCalendarButton;
    private DBHandler db;
    private Fragment fragment;

    public AddNewTask(Fragment fragment) {
        this.fragment = fragment;
    }

    public static AddNewTask newInstance(Fragment fragment) {
        return new AddNewTask(fragment);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_task, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newTaskText = getView().findViewById(R.id.newTaskText);
        newTaskDate = getView().findViewById(R.id.newTaskDate);
        newTaskTime = getView().findViewById(R.id.newTaskTime);
        newTaskPriority = getView().findViewById(R.id.newTaskPriority);
        newTaskCalendarButton = getView().findViewById(R.id.newTaskCalendarButton);
        newTaskSaveButton = getView().findViewById(R.id.newTaskButton);


        db = new DBHandler(getActivity());
        db.openDatabase();

        boolean isUpdate = false;
        final Bundle bundle = getArguments();
        if(bundle != null) {
            isUpdate = true;
            String task = bundle.getString("task_name");
            String date = bundle.getString("date");
            String time = bundle.getString("time");
            String priority = bundle.getString("priority");
            int pindex = 0;
            switch(priority) {
                case "!":
                    pindex = 0;
                    break;
                case "!!":
                    pindex = 1;
                    break;
                case "!!!":
                    pindex = 2;
                    break;
            }
            newTaskText.setText(task);
            newTaskDate.setText(date);
            newTaskTime.setText(time);
            newTaskPriority.setSelection(pindex);
            if(task.length()>0)
                newTaskSaveButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange_900));
        }
        newTaskText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().equals("")) {
                    newTaskSaveButton.setEnabled(false);
                    newTaskSaveButton.setBackgroundColor(Color.TRANSPARENT);
                }
                else {
                    newTaskSaveButton.setEnabled(true);
                    newTaskSaveButton.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.orange_900));
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        boolean finalIsUpdate = isUpdate;
        newTaskSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = newTaskText.getText().toString();
                String date = newTaskDate.getText().toString();
                String time = newTaskTime.getText().toString();
                int pindex = newTaskPriority.getSelectedItemPosition();
                String priority="";
                switch(pindex) {
                    case 1:
                        priority = "!";
                        break;
                    case 2:
                        priority = "!!";
                        break;
                    case 3:
                        priority = "!!!";
                        break;
                }
                if(finalIsUpdate) {
                    db.updateTask(bundle.getInt("id"), text, date, time, priority);
                }
                else {
                    TaskContent task = new TaskContent();
                    task.setTask_name(text);
                    task.setDate(date);
                    task.setTime(time);
                    task.setList("");//
                    task.setPriority(priority);
                    task.setIs_completed(0);
                    db.insertTask(task);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if(fragment instanceof DialogCloseListener) {
            ((DialogCloseListener)fragment).handleDialogClose(dialog);
            //Log.d("closed", "closing: ");
        }
//        Fragment fragment = getParentFragment();
//        Log.d("fragment", String.valueOf(fragment.getTag()));
//        List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();
//        for (Fragment fragment : fragments) {
//            if(fragment instanceof DialogCloseListener) {
//                ((DialogCloseListener)fragment).handleDialogClose(dialog);
//                Log.d("closed", "closing: ");
//            }
//
//        }
        //Log.d("closed", "closing: ");
//        Activity activity = getActivity();
//        if(activity instanceof DialogCloseListener) {
//            ((DialogCloseListener)activity).handleDialogClose(dialog);
//        }
//        int currentFragment = findNavController()
    }
}
