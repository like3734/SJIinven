package baekjoon;

import java.util.*;
import java.io.*;
import java.math.*;
import java.lang.Math;


public class Main{
    static int n;
    static int [][] list;
    
// 
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        
        list = new int[n][2];
        
        for(int i=0; i<n; i++) {
        	list[i][0] = sc.nextInt(); list[i][1] = sc.nextInt();
        }
        
        Arrays.sort(list, new Comparator<int []>() {
        	public int compare(int a[], int b[]) {
        		if(a[0] == b[0]) return Integer.compare(a[1], b[1]);
        		else return Integer.compare(a[0], b[0]);
        	}
        });
        
        for(int i=0; i<n; i++) {
        	System.out.println(list[i][0] + " " + list[i][1]);
        }
    }
}
