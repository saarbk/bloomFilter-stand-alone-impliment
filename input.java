import java.util.*;



/**
 * input class for txt file
 *
 * @author (saar barak)
 * @version (14/01/2021)
 */
public class input
{
    public static void main(String args[]) {
         
        Scanner in = new Scanner(System.in);  
        System.out.println("--------Enter Your Details-------- ");
        System.out.println("--------Enter m-------- ");
        int m = in.nextInt();
        System.out.println("--------Enter k-------- ");
        int k=in.nextInt();
        in.nextLine();
        System.out.println("--------Enter path with files to add-------- ");
        String s2= in.nextLine();
        BloomFilter testFilter = new BloomFilter(m, k);
        testFilter.addPath(s2);
        //in.nextLine();
        System.out.println("--------Enter path to test-------- ");
        s2= in.nextLine();
        testFilter.addPathToTest(s2);
        in.close();

        

        
    }
}
