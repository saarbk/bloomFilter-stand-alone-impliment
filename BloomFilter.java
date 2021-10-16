/**
 * mmn14 bloom filter impliment
 * Version 0.1,  2021
 *
 * @Saar Barak 
 */

import java.io.*;
import java.io.BufferedReader;
import java.util.*;
import java.nio.charset.Charset;
import java.util.BitSet;

public class BloomFilter {
    private int  k; 
    static final Charset charset = Charset.forName("UTF-8"); //give good result with less memory    
    private int numElementsInTheFilter;
    private int m;
    private BitSet bitSet;
    private int seed= 89478583;//using murmur3
    private int elemntsFound;

    /**
     * Makes a new bloom filter with m slots and k hash functions .
     * @param m number of slots in bitset
     * @param k number of hash functions to use
     * 
     */
    public BloomFilter(int m, int k) {
        this.k = k;      
        this.m=m;
        this.bitSet= new BitSet(m);
        this.numElementsInTheFilter=0;
        this.elemntsFound=0;
    }

    /**
     * adds valus (seperated with comma) from txt file path 
     * such as c:\\...\\Desktop\\tester.txt (no " " needed)
     * @param path the path to the file
     */
    public void addPath(String path){
        ArrayList<String> temp= new ArrayList<String>();
        
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));
            String str;
            while ((str = in.readLine())!= null) {
                
                //after split the line we set value in temp arrayList
                temp.addAll(Arrays.asList(str.split(",")));
            }
            addAll(temp);
            System.out.println("file add! total : "+numElementsInTheFilter+" elemnts added");
            System.out.println("False Positive Probability "+getFalsePositiveProbability(numElementsInTheFilter,m,k));

        } catch (IOException e) {
            System.out.println("File Read Error");
        }
    }

    /**
     * ceck if valus (seperated with comma) from txt file is find in the filter
     * such as c:\\...\\Desktop\\tester.txt (no " " needed)
     * @param path the path to the file 
     */

    public void addPathToTest(String path){
        ArrayList<String> temp= new ArrayList<String>();
        
        try {
            BufferedReader in = new BufferedReader(new FileReader(path));
            String str;
            while ((str = in.readLine())!= null) {
                
                //after split the line we set value in temp arrayList
                temp.addAll(Arrays.asList(str.split(",")));
            }
            System.out.println("file add!");
            containsall(temp);
            
            
        } catch (IOException e) {
            System.out.println("File Read Error");
        }
    }

    /**
     * Makes a sinlge hash function  from integer
     * @param val value to insert the hash 
     * 
     */    static int hash0(int val) {
        //val=0x12b9b0a1;

        val = (val + 0x7ed55d16) + (val << 12);
        val = (val ^ 0xc761c23c) ^ (val >> 19);
        val = (val + 0x165667b1) + (val << 5);
        val = (val + 0xd3a2646c) ^ (val << 9);
        val = (val + 0xfd7046c5) + (val << 3);
        val = (val ^ 0xb55a4f09) ^ (val >> 16);
        return val;
    }

    /**
     * murmuer3 method from github.
     * @param bytes of the value to set
     * @param seed for murmur function
     * 
     */
    public static int murmur3_signed( byte[] bytes,int seed) {
        int h1 = seed;
        //Standard in Guava
        int c1 = 0xcc9e2d51;
        int c2 = 0x1b873593;
        int len = bytes.length;
        int i = 0;

        while (len >= 4) {
            //process()
            int k1  = (bytes[i++] & 0xFF);
            k1 |= (bytes[i++] & 0xFF) << 8;
            k1 |= (bytes[i++] & 0xFF) << 16;
            k1 |= (bytes[i++] & 0xFF) << 24;

            k1 *= c1;
            k1 = Integer.rotateLeft(k1, 15);
            k1 *= c2;

            h1 ^= k1;
            h1 = Integer.rotateLeft(h1, 13);
            h1 = h1 * 5 + 0xe6546b64;

            len -= 4;
        }

        //processingRemaining()
        int k1 = 0;
        switch (len) {
            case 3:
            k1 ^= (bytes[i + 2] & 0xFF) << 16;
            // fall through
            case 2:
            k1 ^= (bytes[i + 1] & 0xFF) << 8;
            // fall through
            case 1:
            k1 ^= (bytes[i] & 0xFF);

            k1 *= c1;
            k1 = Integer.rotateLeft(k1, 15);
            k1 *= c2;
            h1 ^= k1;
        }
        i += len;

        //makeHash()
        h1 ^= i;

        h1 ^= h1 >>> 16;
        h1 *= 0x85ebca6b;
        h1 ^= h1 >>> 13;
        h1 *= 0xc2b2ae35;
        h1 ^= h1 >>> 16;

        return h1;
    }

    /**
     * hash create array with hash valuse to set for 1 elemnt
     * param bytes of the value of the elemnt.
     * @param bytes of the value to set
     */

    int[] hash(byte[] bytes) {
        int[] h = new int[k];//set an array with the value to add the filter
        int h0=murmur3_signed(bytes,seed);
        int h3=hash0(hashBytes(bytes));
        for (int i=0; i<k; i++)//use only 2 function to create k function
        {
            int x=((h0+i*(h3)));
            h[i] = Math.abs(x)%m;
        }
        return h;

    }

    /**
     * add 1 elemnt to the filter using k hash function
     * @param bytes of the value to set
     */
    public boolean add(byte[] bytes) {
        boolean added = false;
        for (int h:hash(bytes)) {
            if (!bitSet.get(h))
                added = true;
            bitSet.set(h, true);
        }
        numElementsInTheFilter++;
        return added;		
    }

    /**
     * add all elments to the filter from arraylist .
     * @param st list with valus to add
     *
     * 
     */
    public void addAll(ArrayList<String> st){
        for ( int i=0;i<st.size();i++)
        {add(st.get(i).getBytes(charset));}
    }

    /**
     * check if givem string is on the filter
     * @param string of the value check
     */
    public boolean contains(String s) {
        byte[]  v=s.getBytes(charset);
        for (int h:hash(v)) {
            if (!bitSet.get(h))
                return false;
        }
        return true;		
    }

    /**
     * check if givem string list is on the filter
     * @param string of the value check
     */
    public void containsall(ArrayList<String> st)
    {
        int found=0;
        for ( int i=0;i<st.size();i++)
        {
            if(contains(st.get(i))){
                System.out.println("filter countain : " +st.get(i));
                found++;
            }
        }
        if(found==0){
            System.out.println("no elemnts found");
        }else System.out.println("total elmnnts found : " +found);
        this.elemntsFound+=found;
    } 

    /**
     * calculate the probability of a false positive .
     *
     * @param numberOfElements number of inserted elements.
     * @return probability of a false positive.
     */
    public double getFalsePositiveProbability(double numberOfElements,int m,int k) {
        // (1 - e^(-k * n / m)) ^ k
        return Math.pow((1 - Math.exp(-k * (double) numberOfElements
                    / (double) m)), k);

    }
    /** hash bytes f0r my filter get using for hash0
     * @param a[] given value as byte
     * @return int for given byts
     */
    static int hashBytes(byte a[]) {

        long FNV_PRIME = 16777619;
        long FNV_OFFSET_BASIS = 2166136261l;

        if (a == null)
            return 0;

        long result = FNV_OFFSET_BASIS;
        for (byte element : a) {
            result = (result * FNV_PRIME) & 0xFFFFFFFF;
            result ^= element;
        }

        return (int) result;}

    /**
     * @return  number elemnts that found in the filter
     * 
     * 
     */    public int elemntsFound(){return elemntsFound;}
} 
