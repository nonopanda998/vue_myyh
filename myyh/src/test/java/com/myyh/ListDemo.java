package com.myyh;

import java.util.ArrayList;
import java.util.List;

public class ListDemo {
    public static void main(String[] args) {
        List list = new ArrayList<String>();

        for (int a = 0; a < 10000000; a++) {
            list.add("99999999");
        }

        for (Object o : list) {

        }

        for (Object o : list) {

        }
    }
}
