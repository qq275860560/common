package com.github.qq275860560.common.util;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 */
@Slf4j
public class ThreadPoolExecutorUtil {
	 
	public static ThreadFactory threadFactory = new ThreadFactory() {
		@Override
		public Thread newThread(Runnable runnable) {
			return new Thread(runnable);
		}
	};
	public static ThreadPoolExecutor threadPoolExecutor = null;
	static {
		int poolSize = Runtime.getRuntime().availableProcessors() * 2 + 1;
		LinkedBlockingQueue<Runnable> requestQueue = new LinkedBlockingQueue<Runnable>(1024);
		//
		threadPoolExecutor = new ThreadPoolExecutor(poolSize, poolSize, 60L, TimeUnit.SECONDS, requestQueue,
				threadFactory);
		threadPoolExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy());
	}

}
