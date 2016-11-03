package com.tik.floatviewdemo.manager;

import android.app.Activity;
import android.content.Context;
import android.os.Process;


import java.lang.Class;
import java.lang.Exception;
import java.lang.System;
import java.util.Stack;


/**
 * 应用程序Activity管理类：用于Activity管理和应用程序退出
 */
public class ActivityManager {

    private volatile static ActivityManager instance;

    private ActivityManager() {
    }

    ;

    public static ActivityManager getInstance() {
        if (instance == null) {
            synchronized (ActivityManager.class) {
                if (instance == null) {
                    instance = new ActivityManager();
                }
            }
        }
        return instance;
    }

    private Stack<Activity> activityStack;

    public int getSize() {
        return activityStack.size();
    }

    public Stack getActivityStack() {
        return activityStack;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        if (!activityStack.contains(activity)) {
            activityStack.add(activity);
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity getLastActivity() {
        return activityStack.lastElement();
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishLastActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if(activityStack != null && activityStack.size() > 0)
        if (activity != null) {
            activity.finish();
            activityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        if (activityStack != null) {
            Activity act = null;
            for (Activity activity : activityStack) {
                if (activity.getClass().equals(cls)) {
                    act = activity;
                }
            }
            finishActivity(act);
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityStack != null) {
            for (int i = 0, size = activityStack.size(); i < size; i++) {
                if (null != activityStack.get(i)) {
                    activityStack.get(i).finish();
                }
            }
            activityStack.clear();
            System.exit(0);
        }
    }

    public void removeActivity(Class<?> clz) {
        try {
            if (activityStack != null) {
                for (int i = 0, size = activityStack.size(); i < size; i++) {
                    if (activityStack.get(i).getClass().equals(clz)) {
                        activityStack.remove(i);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void finishAllExceptThis(Class<?> clz) {
        if (activityStack != null) {
            for (int i = 0, size = activityStack.size(); i < size; i++) {
                Activity activity = activityStack.get(i);
                if (null != activity && !activity.getClass().equals(clz)) {
                    activityStack.get(i).finish();
                }
            }
        }
    }


    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            int pid = Process.myPid();
            Process.killProcess(pid);
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}