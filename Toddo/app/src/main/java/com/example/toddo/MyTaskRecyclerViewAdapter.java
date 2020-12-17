package com.example.toddo;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.toddo.dummy.DummyContent.DummyItem;
import com.example.toddo.tasks.TaskContent;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyTaskRecyclerViewAdapter extends RecyclerView.Adapter<MyTaskRecyclerViewAdapter.ViewHolder>{

    //private final List<DummyItem> mValues;
    private List<TaskContent> mValues;
    private ClickListener mClickListener;
    private FragmentActivity activity;
    //SparseBooleanArray states = new SparseBooleanArray();

    public MyTaskRecyclerViewAdapter(List<TaskContent> items, ClickListener clickListener, FragmentActivity activity) {
        mValues = items;
        this.mClickListener = clickListener;
        this.activity = activity;
//        for (int i=0; i<mValues.size(); i++){
//            states.append(i, mValues.get(i).isCompleted());
//        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_task, parent, false);
        return new ViewHolder(view, mClickListener);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        //holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).getTask_name());
        holder.mTimeView.setText(mValues.get(position).getTime());
        holder.mDateView.setText(mValues.get(position).getDate());
        holder.mPriorityView.setText(mValues.get(position).getPriority());
        holder.cbSelect.setOnCheckedChangeListener(null);
        holder.cbSelect.setChecked(mValues.get(position).isCompleted());
        if(mValues.get(position).isCompleted()){
            holder.mContentView.setTextColor(Color.GRAY);
            holder.mDateView.setTextColor(Color.GRAY);
            holder.mTimeView.setTextColor(Color.GRAY);
            holder.mPriorityView.setTextColor(Color.GRAY);
            holder.mContentView.setPaintFlags(holder.mContentView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }  else {
            holder.mContentView.setTextColor(Color.BLACK);
            holder.mDateView.setTextColor(Color.BLACK);
            holder.mTimeView.setTextColor(Color.BLACK);
            holder.mPriorityView.setTextColor(Color.BLACK);
            holder.mContentView.setPaintFlags(holder.mContentView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        holder.cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                //states.put(position, isChecked);
                holder.clickListener.onCheckboxCheckedChange(position, compoundButton, isChecked);
//                if(isChecked){
//                    holder.mContentView.setTextColor(Color.GRAY);
//                    holder.mDateView.setTextColor(Color.GRAY);
//                    holder.mTimeView.setTextColor(Color.GRAY);
//                    holder.mPriorityView.setTextColor(Color.GRAY);
//                    holder.mContentView.setPaintFlags(holder.mContentView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                } else {
//                    holder.mDateView.setTextColor(Color.BLACK);
//                    holder.mTimeView.setTextColor(Color.BLACK);
//                    holder.mPriorityView.setTextColor(Color.BLACK);
//                    holder.mContentView.setTextColor(Color.BLACK);
//                    holder.mContentView.setPaintFlags(holder.mContentView.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
//                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public void setTasks(List<TaskContent> taskList) {
        this.mValues = taskList;
        notifyDataSetChanged();
    }

    public Context getContext() {
        return activity;
    }

    public void deleteItem(int position) {
        mClickListener.onDeleteDialogClick(position);
    }

    public void editItem(int position) {
        mClickListener.onEditSwipe(position);
    }

//    public void editItem(int position) {
//        TaskContent item = mValues.get(position);
//        Bundle bundle = new Bundle();
//        bundle.putInt("id", item.getId());
//        bundle.putString("task_name", item.getTask_name());
//        bundle.putString("date", item.getDate());
//        bundle.putString("time", item.getTime());
//        bundle.putString("priority", item.getPriority());
//        AddNewTask fragment = new AddNewTask()
//    }

    public class ViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener, View.OnLongClickListener {
        public final View mView;
        //public final TextView mIdView;
        public final TextView mContentView;
        public final TextView mTimeView;
        public final TextView mDateView;
        public final TextView mPriorityView;
        public TaskContent mItem;
        public CheckBox cbSelect;
        ClickListener clickListener;

        public ViewHolder(View view, ClickListener clickListener) {
            super(view);
            mView = view;
            //mIdView = (TextView) view.findViewById(R.id.item_number);
            mContentView = (TextView) view.findViewById(R.id.content);
            mTimeView = (TextView) view.findViewById(R.id.time);
            mDateView = (TextView) view.findViewById(R.id.date);
            mPriorityView = (TextView) view.findViewById(R.id.priority);
            cbSelect = (CheckBox) view.findViewById(R.id.checkBox);
            this.clickListener = clickListener;
            view.setOnClickListener(this);
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }

        @Override
        public boolean onLongClick(View v) {
            clickListener.onItemLongClick(getAdapterPosition(), v);
            return false;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }

    public interface ClickListener {
        void onDeleteDialogClick(int position);
        void onItemClick(int position, View v);
        void onItemLongClick(int position, View v);
        void onCheckboxCheckedChange(int position, CompoundButton c, boolean isChecked);

        void onEditSwipe(int position);
    }

    public void updateAppearance(){
        //ViewHolder holder =
        //holder.mItem = mValues.get(position);
    }
}