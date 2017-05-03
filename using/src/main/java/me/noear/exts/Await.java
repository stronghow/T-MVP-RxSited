package me.noear.exts;

import java.util.concurrent.CountDownLatch;

public class Await {
	public Integer state1;
	public Integer state2;
	
	public Boolean isOk;
	
	public Object obj1;
	public Object obj2;
	
	public CountDownLatch latch;
	
	public Await()
	{
		latch = new CountDownLatch(1);
	}
	
	public void reinit()
	{
		latch = null;
		latch = new CountDownLatch(1);
		
		this.isOk=false;
		
		this.state1=0;
		this.state2=0;
		
		this.obj1=null;
		this.obj2=null;
	}
	
	public void waiting()
	{
		try
		{
			latch.await();
		}
		catch(Exception ex){}
	}
	
	public void complete()
	{
		latch.countDown();
	}
}
