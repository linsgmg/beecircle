package com.lin.baselib.bean;


public class ManageRewardBean {
    public ManageRewardBean(boolean isShow, boolean isStartTask, boolean isStartRest) {
        this.isShow = isShow;
        this.isStartTask = isStartTask;
        this.isStartRest = isStartRest;
    }

    private boolean isShow = false;
    private boolean isStartTask = false;
    private boolean isStartRest = false;
    private String taskId;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public boolean isStartRest() {
        return isStartRest;
    }

    public void setStartRest(boolean startRest) {
        isStartRest = startRest;
    }

    public boolean isStartTask() {
        return isStartTask;
    }

    public void setStartTask(boolean startTask) {
        isStartTask = startTask;
    }
}
