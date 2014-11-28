package com.jalil.environ.server;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Bus {

	ExecutorService taskPool = Executors.newFixedThreadPool(1);
	
	private static Bus databaseBus = new Bus();
	private static Bus networkBus = new Bus();
	
	private Bus() {}
	
	public static Bus getDatabaseBus() {
		return databaseBus;
	}
	
	public static Bus getNetworkBus() {
		return networkBus;
	}
	
	public <T> Future<T> submit(Callable<T> task) {
		 return taskPool.submit(task);
	}
	
	public static void closeAllBuses() {
		databaseBus.close();
		networkBus.close();
	}
	
	private void close() {
		taskPool.shutdown();
		try {
        	System.out.println("Waiting for Bus to terminate ...");
	        while(!taskPool.awaitTermination(10, TimeUnit.SECONDS)) {
	        	System.out.println("Waiting for Bus to terminate ...");
	        }
        } catch (InterruptedException e) {
	        e.printStackTrace();
        };
	}
}
