import java.util.*;
import java.io.*;


import java.util.Random;
import java.util.UUID;
import java.util.ArrayList;


/**
 * test my filre
 *
 * @author (saar barak)
 * @version (14/01/2021)
 */
public class tester
{
    public static void main(String args[]) {
        

        Scanner in = new Scanner(System.in);  
        System.out.println("-------my bloom filter test-------- ");
        System.out.println("--------Enter m-------- ");
        int m = in.nextInt();
        System.out.println("--------Enter k-------- ");
        int k=in.nextInt();
        in.nextLine();
        System.out.println("--------Enter number of elemnt-------- ");
        int n= in.nextInt();

        in.close();

        
        double myp=0;
        double optimallp=0;
        int falsei=0;
        for(int test=0;test<1000;test++)
        {

            System.out.println("");
            ArrayList<String> v = new ArrayList<String>();
            ArrayList<String> v1 = new ArrayList<String>();

            BloomFilter instance = new BloomFilter(m,k);

            for (int i = 0; i < n; i++){
                v.add(UUID.randomUUID().toString());
                v1.add(UUID.randomUUID().toString());
            }
            instance.addAll(v);            
            instance.containsall(v1);
            int f=instance.elemntsFound();
            optimallp=instance.getFalsePositiveProbability(n,m,k);
            myp+=f;}
        double realp=(myp/n)/1000;
        System.out.println("-----"+falsei+"-------");
        System.out.println(" after 1000 test the optimal false posibilty is :"+optimallp);
        System.out.println(" in my filter the false posibilty is :" + realp);

    }
}