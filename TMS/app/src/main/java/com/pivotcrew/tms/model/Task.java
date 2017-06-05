package com.pivotcrew.tms.model;

import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by RayChongJH on 5/6/17.
 */
public class Task implements Parcelable {
    long id;
    String taskName;
    String date;
    int isModified;

    public Task() {
    }
    public Task(long id) {
        this.id = id;
    }

    public Task(String taskName) {
        this.taskName = taskName;
    }

    public Task(long id, String taskName) {
        this.id = id;
        this.taskName = taskName;
    }

    public Task(long id, String taskName, String date, int isModified) {
        this.id = id;
        this.taskName = taskName;
        this.date = date;
        this.isModified = isModified;
    }

    protected Task(Parcel in) {
        id = in.readLong();
        taskName = in.readString();
        date = in.readString();
        isModified = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(taskName);
        dest.writeString(date);
        dest.writeInt(isModified);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getIsModified() {
        return isModified;
    }

    public void setIsModified(int isModified) {
        this.isModified = isModified;
    }
}
