package com.example.toddo;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.example.toddo.Utils.DBHandler;
import com.example.toddo.dummy.DummyContent;
import com.example.toddo.tasks.TaskContent;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A fragment representing a list of Items.
 */
public class InboxFragment extends Fragment  implements DialogCloseListener {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;

    public static final String TAG = "INBOX";

    private List<TaskContent> taskList;//
    private DBHandler db;
    private FloatingActionButton fab;
    ItemTouchHelper itemTouchHelper;

    MyTaskRecyclerViewAdapter adapter;
    MyTaskRecyclerViewAdapter.ClickListener listener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public InboxFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static InboxFragment newInstance(int columnCount) {
        InboxFragment fragment = new InboxFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        db = new DBHandler(getActivity());
        db.openDatabase();
        taskList = new ArrayList<>();
        taskList = db.getAllTasks();
        Collections.reverse(taskList);
        //moveToEnd();
        adapter = new MyTaskRecyclerViewAdapter(taskList, listener, getActivity());
        //Log.d("task", "task1: " + taskList.get(0).getTask_name());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inbox, container, false);

        //taskList = ((MainActivity)getActivity()).taskList;//
        //db = ((MainActivity)getActivity()).db;
//        db = new DBHandler(getActivity());
//        db.openDatabase();
//        taskList = new ArrayList<>();
//        taskList = db.getAllTasks();

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNewTask.newInstance(getThis()).show(getChildFragmentManager(), AddNewTask.TAG);
            }
        });

        // Set the adapter
//        adapter = new MyTaskRecyclerViewAdapter(taskList, listener);
        View rView = view.findViewById(R.id.list);
        if (rView instanceof RecyclerView) {
            Context context = rView.getContext();
            RecyclerView recyclerView = (RecyclerView) rView;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(adapter);
        itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        }
        return view;
    }

    @Override
    public void onAttach (Context context) {
        super.onAttach(context);
        listener = new MyTaskRecyclerViewAdapter.ClickListener() {
            @Override
            public void onDeleteDialogClick(int position) {
                deleteItem(position);
            }

            @Override
            public void onItemClick(int position, View v) {
                Log.d("clicked", "onItemClick position: " + position);
            }

            @Override
            public void onItemLongClick(int position, View v) {
                Log.d("long clicked", "onItemLongClick pos = " + position);
            }

            @Override
            public void onCheckboxCheckedChange(int position, CompoundButton c, boolean isChecked) {
                Log.d("checked", "onCheck pos = " + position);
                int id = taskList.get(position).getId();
                if(isChecked) {
                    db.updateStatus(id, 1);
                    taskList = db.getAllTasks();
                    Collections.reverse(taskList);
                    //moveToEnd();
                    adapter.setTasks(taskList);
                } else {
                    db.updateStatus(id, 0);
                    taskList = db.getAllTasks();
                    Collections.reverse(taskList);
                    //moveToEnd();
                    adapter.setTasks(taskList);
                }
            }

            @Override
            public void onEditSwipe(int position) {
                editItem(position);
            }
        };
    }

    public void deleteItem(int position) {
        TaskContent item = taskList.get(position);
        db.deleteTask(item.getId());
        taskList.remove(position);
        adapter.notifyItemRemoved(position);
    }

    public void editItem(int position) {
        TaskContent item = taskList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task_name", item.getTask_name());
        bundle.putString("date", item.getDate());
        bundle.putString("time", item.getTime());
        bundle.putString("priority", item.getPriority());
        AddNewTask fragment = new AddNewTask(this);
        fragment.setArguments(bundle);
        fragment.show(getChildFragmentManager(), AddNewTask.TAG);
    }

    @Override
    public void handleDialogClose(DialogInterface dialog) {
        taskList = db.getAllTasks();
        //Log.d("task", "task1: " + taskList.get(1).getTask_name());
        Collections.reverse(taskList);
        adapter.setTasks(taskList);
//        adapter.notifyDataSetChanged();
        //Log.d("closed", "closing");
    }

    private Fragment getThis(){
        return this;
    }

    private void moveToEnd() {
        for(int i=0; i<taskList.size(); i++) {
            TaskContent task = taskList.get(i);
            if(task.isCompleted()) {
                taskList.remove(i);
                taskList.add(task);
            }
        }
    }
}