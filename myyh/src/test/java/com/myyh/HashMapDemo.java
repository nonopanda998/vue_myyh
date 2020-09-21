package com.myyh;

import java.awt.image.VolatileImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HashMapDemo {

    public synchronized static void main(String[] args) throws IOException {
        System.in.read();

        List list = new ArrayList<String>();

        for (int a = 0; a < 10000000; a++) {
            list.add("  System.in.read();  System.in.read();  System.in.read();  System.in.read();  System.in.read();  System.in.read();  System.in.read();  System.in.read();  System.in.read();  System.in.read();  System.in.read();  System.in.read();");
        }
        System.in.read();
         long  start = 0L;
        long end = 0L;
        start = System.currentTimeMillis();
        System.out.println("第1次遍历开始时间：" + start);

        for (Object o : list) {
            o.equals(1);
        }
        end = System.currentTimeMillis();
        System.out.println("第1次遍历结束时间：" + end);
        System.out.println("第1次总共耗时时间：" + (end - start));

        System.in.read();
        start = System.currentTimeMillis();
        System.out.println("第2次遍历开始时间：" + start);

        for (Object o : list) {
            o.equals(1);
        }
        end = System.currentTimeMillis();
        System.out.println("第2次遍历结束时间：" + end);
        System.out.println("第2次总共耗时时间：" + (end - start));


        System.in.read();
        start = System.currentTimeMillis();
        System.out.println("第3次遍历开始时间：" + start);

        for (Object o : list) {
            o.equals(1);
        }
        end = System.currentTimeMillis();
        System.out.println("第3次遍历结束时间：" + end);
        System.out.println("第3次总共耗时时间：" + (end - start));


        System.in.read();

        start = System.currentTimeMillis();
        System.out.println("第4次遍历开始时间：" + start);

        for (Object o : list) {
            o.equals(1);
        }
        end = System.currentTimeMillis();
        System.out.println("第4次遍历结束时间：" + end);
        System.out.println("第4次总共耗时时间：" + (end - start));


        System.in.read();
        start = System.currentTimeMillis();
        System.out.println("第4次遍历开始时间：" + start);

        for (Object o : list) {
            o.equals(1);
        }
        end = System.currentTimeMillis();
        System.out.println("第4次遍历结束时间：" + end);
        System.out.println("第4次总共耗时时间：" + (end - start));


        System.in.read();
        start = System.currentTimeMillis();
        System.out.println("第4次遍历开始时间：" + start);

        for (Object o : list) {
            o.equals(1);
        }
        end = System.currentTimeMillis();
        System.out.println("第4次遍历结束时间：" + end);
        System.out.println("第4次总共耗时时间：" + (end - start));


        System.in.read();
        start = System.currentTimeMillis();
        System.out.println("第4次遍历开始时间：" + start);

        for (Object o : list) {
            o.equals(1);
        }
        end = System.currentTimeMillis();
        System.out.println("第4次遍历结束时间：" + end);
        System.out.println("第4次总共耗时时间：" + (end - start));


        System.in.read();
        start = System.currentTimeMillis();
        System.out.println("第4次遍历开始时间：" + start);

        for (Object o : list) {
            o.equals(1);
        }
        end = System.currentTimeMillis();
        System.out.println("第4次遍历结束时间：" + end);
        System.out.println("第4次总共耗时时间：" + (end - start));


        System.in.read();
        start = System.currentTimeMillis();
        System.out.println("第4次遍历开始时间：" + start);

        for (Object o : list) {
            o.equals(1);
        }
        end = System.currentTimeMillis();
        System.out.println("第4次遍历结束时间：" + end);
        System.out.println("第4次总共耗时时间：" + (end - start));


        System.in.read();
        start = System.currentTimeMillis();
        System.out.println("第4次遍历开始时间：" + start);

        for (Object o : list) {
            o.equals(1);
        }
        end = System.currentTimeMillis();
        System.out.println("第4次遍历结束时间：" + end);
        System.out.println("第4次总共耗时时间：" + (end - start));


        System.in.read(); start = System.currentTimeMillis();
        System.out.println("第4次遍历开始时间：" + start);

        for (Object o : list) {
            o.equals(1);
        }
        end = System.currentTimeMillis();
        System.out.println("第4次遍历结束时间：" + end);
        System.out.println("第4次总共耗时时间：" + (end - start));


        System.in.read();
        start = System.currentTimeMillis();
        System.out.println("第4次遍历开始时间：" + start);

        for (Object o : list) {
            o.equals(1);
        }
        end = System.currentTimeMillis();
        System.out.println("第4次遍历结束时间：" + end);
        System.out.println("第4次总共耗时时间：" + (end - start));


        System.in.read();
        start = System.currentTimeMillis();
        System.out.println("第4次遍历开始时间：" + start);

        for (Object o : list) {
            o.equals(1);
        }
        end = System.currentTimeMillis();
        System.out.println("第4次遍历结束时间：" + end);
        System.out.println("第4次总共耗时时间：" + (end - start));


        System.in.read();
        start = System.currentTimeMillis();
        System.out.println("第4次遍历开始时间：" + start);

        for (Object o : list) {
            o.equals(1);
        }
        end = System.currentTimeMillis();
        System.out.println("第4次遍历结束时间：" + end);
        System.out.println("第4次总共耗时时间：" + (end - start));


        System.in.read();
        start = System.currentTimeMillis();
        System.out.println("第4次遍历开始时间：" + start);

        for (Object o : list) {
            o.equals(1);
        }
        end = System.currentTimeMillis();
        System.out.println("第4次遍历结束时间：" + end);
        System.out.println("第4次总共耗时时间：" + (end - start));


        System.in.read();
        start = System.currentTimeMillis();
        System.out.println("第4次遍历开始时间：" + start);

        for (Object o : list) {
            o.equals(1);
        }
        end = System.currentTimeMillis();
        System.out.println("第4次遍历结束时间：" + end);
        System.out.println("第4次总共耗时时间：" + (end - start));


        System.in.read();
        start = System.currentTimeMillis();
        System.out.println("第4次遍历开始时间：" + start);

        for (Object o : list) {
            o.equals(1);
        }
        end = System.currentTimeMillis();
        System.out.println("第4次遍历结束时间：" + end);
        System.out.println("第4次总共耗时时间：" + (end - start));


        System.in.read();
        start = System.currentTimeMillis();
        System.out.println("第4次遍历开始时间：" + start);

        for (Object o : list) {
            o.equals(1);
        }
        end = System.currentTimeMillis();
        System.out.println("第4次遍历结束时间：" + end);
        System.out.println("第4次总共耗时时间：" + (end - start));


        System.in.read();


    }
}
