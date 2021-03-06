package com.zhch.example.java.concurrent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolExample {
	long start = System.currentTimeMillis();

	// 默认 cachedThreadPool, 有多少任务就起多少个线程(最多Integer.MAX_VALUE个)
	public void cachedPoll() {
		ExecutorService pool = Executors.newCachedThreadPool();

		// Executors.newCachedThreadPool() 的实现如下
		new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS,
			new SynchronousQueue<Runnable>());

		// 将线程放入池中进行执行
		for (int i = 0; i < 30; i++) {
			pool.execute(new MyThread());
		}
		pool.shutdown();
	}

	// 这个是单线程的线程池, 任务一个一个来, 排队
	public void singlePoll() {
		ExecutorService pool = Executors.newSingleThreadExecutor();
		// 将线程放入池中进行执行
		for (int i = 0; i < 3; i++) {
			pool.execute(new MyThread());
		}

		// shutdown 后面的语句会立即执行，线程会执行完所有任务然后结束
		pool.shutdown();

		// 如果是 shutdownNow(), 已启动的任务会执行结束， 没启动的任务不会执行
		//		pool.shutdownNow();

		System.out.println("single poll shutdown.");
	}

	// 设置最大线程数，请求数超出会报错
	public void maxPoll() {
		// 最多有3个线程在跑
		ExecutorService pool = new ThreadPoolExecutor(0, 3, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
		// 将线程放入池中进行执行， 数量超过最大数量会报错，SynchronousQueue 不允许插入数据
		for (int i = 0; i < 8; i++) {
			pool.execute(new MyThread());
		}
		pool.shutdown();
	}

	// 设置最大线程数， 有队列可以存储多的请求
	public void maxQueuePoll() {
		BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>();
		// 最多有6个线程在跑
		// 第一个参数 coolPoolSize 是一直放在池中的线程数量, 设为0 会一直只有一个线程在工作
		// PoolExecute 的逻辑是超过 coolPoolSize的请求先放进队列， 队列满了才增加线程数量！！一旦线程数量超出  maximumPoolSize就会报错
		ExecutorService pool = new ThreadPoolExecutor(3, 6, 60L, TimeUnit.SECONDS, taskQueue);
		// 将线程放入池中进行执行， 数量超过最大数量会报错，SynchronousQueue 不允许插入数据
		for (int i = 0; i < 20; i++) {
			pool.submit(new MyThread());
		}
		pool.shutdown();
		while (taskQueue.size() > 0) {
			System.out.println("queue size:" + taskQueue.size());
			sleep(200);
		}
		System.out.println("all exit.");
	}



	class MyThread implements Runnable {

		@Override
        public void run() {
			for (int i = 0; i < 2; i++) {
				sleep(200);
				System.out.println(
						"thread:" + Thread.currentThread().getName() + " " + (System.currentTimeMillis() - start));
			}
		}

	}

	public void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
			System.out.println("===========error thread:" + Thread.currentThread().getName());
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ThreadPoolExample t = new ThreadPoolExample();
//				t.cachedPoll();
//				t.singlePoll();
//				t.maxPoll();
		t.maxQueuePoll();
	}
}
