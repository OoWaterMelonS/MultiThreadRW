package org.example;

import org.junit.Test;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author ShaoYJ
 * @date 2022/12/12 周一
 * @desc
 */
public class LockDemo{
   private static int sum = 0;

   @Test
   public void test(){
      ExecutorService executorService = Executors.newFixedThreadPool(10);

      CountDownLatch countDownLatch = new CountDownLatch(10);

      new ThreadPoolExecutor(10, 12, 6000, TimeUnit.MINUTES, new BlockingQueue<Runnable>() {
         @Override
         public boolean add(Runnable runnable) {
            return false;
         }

         @Override
         public boolean offer(Runnable runnable) {
            return false;
         }

         @Override
         public Runnable remove() {
            return null;
         }

         @Override
         public Runnable poll() {
            return null;
         }

         @Override
         public Runnable element() {
            return null;
         }

         @Override
         public Runnable peek() {
            return null;
         }

         @Override
         public void put(Runnable runnable) throws InterruptedException {

         }

         @Override
         public boolean offer(Runnable runnable, long timeout, TimeUnit unit) throws InterruptedException {
            return false;
         }

         @Override
         public Runnable take() throws InterruptedException {
            return null;
         }

         @Override
         public Runnable poll(long timeout, TimeUnit unit) throws InterruptedException {
            return null;
         }

         @Override
         public int remainingCapacity() {
            return 0;
         }

         @Override
         public boolean remove(Object o) {
            return false;
         }

         @Override
         public boolean containsAll(Collection<?> c) {
            return false;
         }

         @Override
         public boolean addAll(Collection<? extends Runnable> c) {
            return false;
         }

         @Override
         public boolean removeAll(Collection<?> c) {
            return false;
         }

         @Override
         public boolean retainAll(Collection<?> c) {
            return false;
         }

         @Override
         public void clear() {

         }

         @Override
         public int size() {
            return 0;
         }

         @Override
         public boolean isEmpty() {
            return false;
         }

         @Override
         public boolean contains(Object o) {
            return false;
         }

         @Override
         public Iterator<Runnable> iterator() {
            return null;
         }

         @Override
         public Object[] toArray() {
            return new Object[0];
         }

         @Override
         public <T> T[] toArray(T[] a) {
            return null;
         }

         @Override
         public int drainTo(Collection<? super Runnable> c) {
            return 0;
         }

         @Override
         public int drainTo(Collection<? super Runnable> c, int maxElements) {
            return 0;
         }
      });

      ReentrantLock lock = new ReentrantLock();
      for (int i = 0; i < 10; i++) {
         executorService.submit(new Runnable() {
            @Override
            public void run() {
               for (int j = 0; j < 1000; j++) {
                  lock.lock();
                  try {
                     sum++;
                  }finally {
                     lock.unlock();
                  }
               }
               System.out.println(Thread.currentThread().getName()+"本线累加完成");
               countDownLatch.countDown();
            }
         });
      }
      try {
         countDownLatch.await();
      }catch (InterruptedException e){
         e.printStackTrace();
      }
      System.out.println("累加结果："+sum);
   }
}
