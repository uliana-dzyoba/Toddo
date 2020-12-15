package com.example.toddo.tasks;

public class TaskContent {
    private int id, is_completed;
    private String task_name;
    private String time;
    private String date;
    private String list;
    private String priority;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIs_completed() {
        return is_completed;
    }

    public boolean isCompleted() {
        if(is_completed==0)
        return false;
        else return true;
    }

    public void setIs_completed(int is_completed) {
        this.is_completed = is_completed;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getList() {
        return list;
    }

    public void setList(String list) {
        this.list = list;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
