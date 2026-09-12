package suanfati;

import java.util.Scanner;

public class zifuchuxiancishu {
    public  static void main (String[] args){
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine().trim().toLowerCase();
        String c = sc.nextLine().trim().toLowerCase();
        char target = c.charAt(0);
        int cnt=0;
       for(char ch : s.toCharArray()){
           if(ch == target){
               cnt++;
           }
       }
        System.out.println(cnt);
    }
}
