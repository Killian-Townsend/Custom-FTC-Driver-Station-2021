package com.qualcomm.robotcore.util;

public class NextLock {
  protected long count = 0L;
  
  protected final Object lock = this;
  
  public void advanceNext() {
    synchronized (this.lock) {
      this.count++;
      this.lock.notifyAll();
      return;
    } 
  }
  
  public Waiter getNextWaiter() {
    synchronized (this.lock) {
      return new Waiter(this.count + 1L);
    } 
  }
  
  public class Waiter {
    long nextCount;
    
    Waiter(long param1Long) {
      this.nextCount = param1Long;
    }
    
    public void awaitNext() throws InterruptedException {
      synchronized (NextLock.this.lock) {
        while (true) {
          NextLock.this.lock.wait();
          if (NextLock.this.count >= this.nextCount)
            return; 
        } 
      } 
    }
  }
}


/* Location:              C:\Users\Student\Desktop\APK Decompiling\com.qualcomm.ftcdriverstation_38_apps.evozi.com\classes-dex2jar.jar!\com\qualcomm\robotcor\\util\NextLock.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */