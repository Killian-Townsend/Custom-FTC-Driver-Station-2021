package org.firstinspires.ftc.robotcore.internal.collections;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import org.firstinspires.ftc.robotcore.external.function.Consumer;
import org.firstinspires.ftc.robotcore.internal.system.Assert;

public class EvictingBlockingQueue<E> extends AbstractQueue<E> implements BlockingQueue<E> {
  protected Consumer<E> evictAction = null;
  
  protected BlockingQueue<E> targetQueue;
  
  protected final Object theLock = new Object();
  
  public EvictingBlockingQueue(BlockingQueue<E> paramBlockingQueue) {
    this.targetQueue = paramBlockingQueue;
  }
  
  public int drainTo(Collection<? super E> paramCollection) {
    synchronized (this.theLock) {
      return this.targetQueue.drainTo(paramCollection);
    } 
  }
  
  public int drainTo(Collection<? super E> paramCollection, int paramInt) {
    synchronized (this.theLock) {
      paramInt = this.targetQueue.drainTo(paramCollection, paramInt);
      return paramInt;
    } 
  }
  
  public Iterator<E> iterator() {
    return this.targetQueue.iterator();
  }
  
  public boolean offer(E paramE) {
    synchronized (this.theLock) {
      if (this.targetQueue.remainingCapacity() == 0) {
        E e = this.targetQueue.poll();
        Assert.assertNotNull(e);
        if (this.evictAction != null)
          this.evictAction.accept(e); 
      } 
      boolean bool = this.targetQueue.offer(paramE);
      Assert.assertTrue(bool);
      this.theLock.notifyAll();
      return bool;
    } 
  }
  
  public boolean offer(E paramE, long paramLong, TimeUnit paramTimeUnit) throws InterruptedException {
    return offer(paramE);
  }
  
  public E peek() {
    return this.targetQueue.peek();
  }
  
  public E poll() {
    synchronized (this.theLock) {
      return this.targetQueue.poll();
    } 
  }
  
  public E poll(long paramLong, TimeUnit paramTimeUnit) throws InterruptedException {
    synchronized (this.theLock) {
      long l = System.nanoTime();
      paramLong = paramTimeUnit.toNanos(paramLong);
      while (true) {
        paramTimeUnit = (TimeUnit)poll();
        if (paramTimeUnit != null)
          return (E)paramTimeUnit; 
        if (!Thread.currentThread().isInterrupted()) {
          long l1 = l + paramLong - System.nanoTime();
          if (l1 > 0L) {
            long l2 = l1 / 1000000L;
            Long.signum(l2);
            this.theLock.wait(l2, (int)(l1 - 1000000L * l2));
            continue;
          } 
          return null;
        } 
        throw new InterruptedException();
      } 
    } 
  }
  
  public void put(E paramE) throws InterruptedException {
    offer(paramE);
  }
  
  public int remainingCapacity() {
    return Math.max(this.targetQueue.remainingCapacity(), 1);
  }
  
  public void setEvictAction(Consumer<E> paramConsumer) {
    synchronized (this.theLock) {
      this.evictAction = paramConsumer;
      return;
    } 
  }
  
  public int size() {
    return this.targetQueue.size();
  }
  
  public E take() throws InterruptedException {
    synchronized (this.theLock) {
      while (true) {
        E e = poll();
        if (e != null)
          return e; 
        if (!Thread.currentThread().isInterrupted()) {
          this.theLock.wait();
          continue;
        } 
        throw new InterruptedException();
      } 
    } 
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\org\firstinspires\ftc\robotcore\internal\collections\EvictingBlockingQueue.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */