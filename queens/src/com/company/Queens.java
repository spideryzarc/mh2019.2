package com.company;

import java.util.Arrays;
import java.util.Random;

public class Queens {
    int N;
    int vet[];
    Random rd = new Random(7);

    public Queens(int n){
        N = n;
        vet = new int[n];
        for (int i = 0; i < n; i++) {
            vet[i]=i;
        }
    }

    @Override
    public String toString() {
        return "Queens{" +
                "vet=" + Arrays.toString(vet) +
                '}';
    }

    public void printTab(){
        for (int i = 0; i < N; i++) {
            System.out.print("| ");
            for (int j = 0; j < vet[i]; j++) {
                System.out.print("\t| ");
            }
            System.out.print("x");
            for (int j = vet[i]; j < N; j++) {
                System.out.print("\t| ");
            }
            System.out.println();
        }
    }

    public int fo(){
        int c = 0;
        for (int i = 0; i < N; i++) {
            for (int j = i+1; j < N; j++) {
                if(vet[i]-i == vet[j]-j){
                    c++;
                }

                if(vet[i]+i == vet[j]+j){
                    c++;
                }
            }
        }

        return c;
    }

    private int fo(int n){
        int c = 0;
        for (int i = 0; i < n; i++) {
            int j = n;
             {
                if(vet[i]-i == vet[j]-j){
                    c++;
                }

                if(vet[i]+i == vet[j]+j){
                    c++;
                }
            }
        }

        return c;
    }

    public void enumerate(){
        used = new boolean[N];
        if(next(0))
                return;


    }
    private boolean used[];
    private boolean next(int i) {
        for (int j = 0; j < N; j++)
            if(!used[j]) {
                vet[i] = j;
                used[j] = true;
                if(i == N-1){
                  //  System.out.println(Arrays.toString(vet));
                    if(fo() == 0)
                        return true;
                }else{
                    if(fo(i) == 0)
                        if(next(i+1))
                            return true;
                }
                used[j] = false;
            }
        return false;
    }

    public void greedy(){

        used = new boolean[N];
        for (int i = 0; i < N; i++) {
            int min = Integer.MAX_VALUE;
            int argj  =-1;
            for (int j = 0; j < N; j++)
                if(!used[j]){
                    vet[i] = j;
                    int c = fo(i);
                    if(c < min || (c == min && rd.nextBoolean())){
                        min = c;
                        argj = j;
                    }
                }
            vet[i] = argj;
            used[argj] = true;
        }
    }


    public void swap(int i, int j) {
        int aux = vet[i];
        vet[i] = vet[j];
        vet[j] = aux;
    }

    public void copy(Queens q) {
        if(N != q.N) {
            vet = new int[q.N];
            N = q.N;
        }
        for (int i = 0; i < q.N; i++) {
            vet[i] = q.vet[i];
        }
    }
}
