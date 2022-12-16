package org.example;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class App 
{
//    更改为本地路径
    private String readFileName = "E:/Pro/MultiThreadRW/src/file/test.xlsx";
    private String  writeFileName = "E:/Pro/MultiThreadRW/src/file/test2.xlsx";

    private boolean end = false;

    private final BlockingQueue<List<String>> queue = new LinkedBlockingQueue<>();

    // 生产者
    class Producer extends Thread{

        @Override
        public void run(){
            // 读取excel文件
            ExcelReader reader = ExcelUtil.getReader(readFileName);
            List<List<Object>> lists = reader.read();

            // 将对象按行写入队列
            for (List<Object> list:lists ) {
                try{
                    queue.put(cast(list));
                    System.out.println("[" + Thread.currentThread().getName() + "]生产者生产一行数据...");

                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            end = true;
            reader.close();
        }

        // object 转 String
        public List<String> cast(List<Object> list){
            List<String> tmp = new ArrayList<>();
            for (Object li :list ) {
                tmp.add((String) li);
            }
            return tmp;
        }
    }

    // 消费者
    class Consumer extends Thread{
        @Override
        public void run(){
            ExcelWriter writer = ExcelUtil.getWriter(writeFileName);
            // 当输入流完成或者队列为空
            while (!end || !queue.isEmpty()){
                try {
                    // 拿出一条数据
                    List<String> list = queue.take();
                    List<List<String>> row = new ArrayList<>();
                    row.add(list);

                    // 写入到输出流中
                    writer.write(row,true);
                    System.out.println("["+Thread.currentThread().getName()+"]消费者消费一行数据...");
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
            }
            writer.close();
        }
    }

    // 主函数
    public static void main( String[] args ) {
        App app = new App();
        Producer producer = app.new Producer();
        Consumer consumer = app.new Consumer();
        Consumer consumer2 = app.new Consumer();

        producer.start();
        consumer.start();
        consumer2.start();

        try {
            producer.join();
            consumer.join();
            consumer2.join();
            System.out.println("["+Thread.currentThread().getName()+"]完成...");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}

