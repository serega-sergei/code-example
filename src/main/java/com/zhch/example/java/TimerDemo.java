package com.zhch.example.java;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TimerDemo {
	Timer mTimer;
	public void test() {

		mTimer = new Timer();
        TimerTask task = new TimerTask(){
            public void run(){
                System.out.println(new Date().toString());
            }
        };
        mTimer.schedule(task, 5000);//开启定时器，delay 5s后执行task
        System.out.println(new Date().toString());
	}
	

	public static void main(String[] args) {
		TimerDemo t = new TimerDemo();
		t.test();
	}

}
