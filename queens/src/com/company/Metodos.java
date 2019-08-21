package com.company;

import java.util.Random;

public class Metodos {

    private static boolean localSearchFirstImp(Queens q){
        int fo = q.fo();
        for (int i = 0; i < q.N; i++) {
            for (int j = i+1; j < q.N; j++) {
                q.swap(i,j);
                if( q.fo() < fo){
                    return true;
                }
                q.swap(i,j);
            }
        }
        return false;
    }

    private static boolean localSearchBestImp(Queens q){
        int fo = q.fo();
        int min = fo;
        int argi=-1, argj=-1;
        for (int i = 0; i < q.N; i++) {
            for (int j = i+1; j < q.N; j++) {
                q.swap(i,j);
                int d = q.fo();
                if( d < min){
                    min = d;
                    argi = i;
                    argj = j;
                }
                q.swap(i,j);
            }
        }
        if(argi!=-1){
            q.swap(argi,argj);
            return true;
        }
        return false;
    }

    public static int hillclimbing(Queens q, boolean firstImp){
        boolean imp;
        do{
            if(firstImp)
                imp = localSearchFirstImp(q);
            else
                imp = localSearchBestImp(q);
            //System.out.println(q.fo());
        }while (imp);

        return q.fo();
    }

    public static Random rd = new Random(7);
    private static void shuffler(int v[]){
        for (int i = v.length-1; i > 0 ; i--) {
            int x = rd.nextInt(i);
            int aux = v[i];
            v[i] = v[x];
            v[x] = aux;
        }
    }

    public static int RMS(Queens q){
        Queens current = new Queens(q.N);
        current.copy(q);
        int bestfo = q.fo();
        System.out.println("ini "+ q.fo());
        for (int i = 0; i < 100; i++) {
            shuffler(current.vet);
            int x = hillclimbing(current,true);
            if(bestfo > x){
                bestfo = x;
                q.copy(current);
                System.out.println(" RMS: ite "+i+" -- "+bestfo);
            }
            if(x == 0)
                break;
        }

        return q.fo();
    }
}
