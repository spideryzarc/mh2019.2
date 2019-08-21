package com.company;

public class Main {

    public static void main(String[] args) {
	    Queens q = new Queens(700);

        long t = System.currentTimeMillis();


	    //q.greedy();
        System.out.println(q);
        System.out.println(q.fo());

        Metodos.RMS(q);

        t = System.currentTimeMillis()-t;
        System.out.println("time: " + t);

        System.out.println(q);
        System.out.println(q.fo());
//	    int min = 10000;
//        for (int i = 0; i < 10000000; i++) {
//            q.greedy();
//   //         System.out.println(q);
//            //q.printTab();
//            int x = q.fo();
//            if(x < min){
//                min = x;
//                System.out.println(min);
//                if(x == 0)
//                    break;
//            }
//
//        }
    }
}
