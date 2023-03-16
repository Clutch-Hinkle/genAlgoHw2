import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

public class Test {

    public static void main(String[] args)
    {
        int[] parent1 = new int[] { 8,5,7,3,6,2,5,1,9,0};
        int[] parent2 = new int[] { 0,1,2,3,4,5,6,7,8,9};
        int[] child1 = new int[10];
        int[] child2 = new int[10];
        System.out.print("Running test with par1: ");
        for (int i = 0; i < parent1.length; i++)
        {
            System.out.print(parent1[i]);
        }
        System.out.print("\nRunning test with par2: ");
        for (int i = 0; i < parent2.length; i++)
        {
            System.out.print(parent2[i]);
        }

        System.out.println();
        partiallyMappedCrossOver(parent1, parent2, child1, child2);


    }

    public static void partiallyMappedCrossOver(int[] parent1, int[] parent2, int[] child1, int[] child2)
    {
        int minDist = 2;
        //Choose a random chunk size not equal to the chromo length
        int chunkSize = Search.r.nextInt((parent1.length - 2) - minDist) + minDist;


        //get the left index Random(0, chromoLength - chunk)
        int leftIndex = Search.r.nextInt(0, (parent1.length - 1) - chunkSize);
        //Get the right index left + chunkSize - 1
        int rightIndex = leftIndex + chunkSize - 1;

        System.out.println("Chunksize = " + chunkSize + " left index: " + leftIndex + " right index: " + rightIndex);

        Hashtable<Integer, Boolean> child1Lookup = new Hashtable<Integer, Boolean>();


        for (int i = 0; i < parent1.length; i++)
        {
            //Lets add all the values to a lookup
            child1[i] = parent1[i];
            child1Lookup.put(parent1[i], true);
            child2[i] = parent2[i];

        }


        //Maintain duplicates
        ArrayList<Integer> child1DuplicateIndex = new ArrayList<Integer>();



        //Exchange our chunk of data
        for (int i = leftIndex; i <= rightIndex; i++)
        {
            child1[i] = parent2[i];
            child2[i] = parent1[i];

            child1Lookup.remove(parent1[i]);

            if (child1Lookup.containsKey(parent2[i]))
            {
                //Duplicate key
                System.out.println("We think " + parent2[i] + " is a duplicate value");
                child1DuplicateIndex.add(i);
            }

            child1Lookup.put(parent2[i], true);

        }

        //We now need to clean things that weren't actually duplicates


        System.out.println("Our duplicates are ");
        for (int i = 0; i < child1DuplicateIndex.size(); i++)
        {
            System.out.print(child1DuplicateIndex.get(i));
        }


        //Child 1 value to child 2 value
        Hashtable<Integer, Integer> relationMap = new Hashtable<Integer, Integer>();

        //Go through our duplicate indices to determine mapping relationship
        for (int i = 0; i < child1DuplicateIndex.size(); i++)
        {
            int index = child1DuplicateIndex.get(i);


            //If it doesn't have this value, nice. Just note the relation
            if (!child1Lookup.containsKey(child2[index]))
            {
                relationMap.put(child1[index], child2[index]);
            }
            //Otherwise it already has this value to, so things are a little more tricky.
            else
            {
                int child1StartingValue = child1[index];
                while (child1Lookup.containsKey(child2[index]))
                {
                    //Find the index of this value in child 1
                    for (int j = 0; j < child1.length; j++)
                    {
                        if (child1[j] == child2[index])
                        {
                            index = j;
                            break;
                        }
                    }
                }

                relationMap.put(child1StartingValue, child2[index]);
            }
        }

        System.out.println("\nbefore repairs our children are ");
        for (int i = 0; i < child1.length; i++)
        {
            System.out.print(child1[i]);
        }
        System.out.print("\n");
        for (int i = 0; i < child2.length; i++)
        {
            System.out.print(child2[i]);
        }

        for (Map.Entry<Integer, Integer> set : relationMap.entrySet())
        {
            int child1Index = -1;
            int child2Index = -1;

            System.out.println("\n\nLooking for value " + set.getKey() + " in child 1 and looking for " + set.getValue() + " in child 2");

            for (int i = 0; i < child1.length; i++)
            {
                //We need to skip everything in the swath
                if (i >= leftIndex && i<=rightIndex)
                    continue;

                //We found the location of our child 1
                if (child1[i] == set.getKey())
                {
                    child1Index = i;
                    if (child2Index != -1)
                        break;
                }

                if (child2[i] == set.getValue())
                {
                    child2Index = i;
                    if (child1Index != -1)
                        break;
                }
            }
            //we now have our index so lets swap
            System.out.println("Child1 Index = " + child1Index + " child2 index = " + child2Index);
            if (child1Index == -1 || child2Index == -1)
                continue;

            int temp = child1[child1Index];
            child1[child1Index] = child2[child2Index];
            child2[child2Index] = temp;
        }

        System.out.print("\nThe new child is ");
        for (int i = 0; i < child1.length; i++)
        {
            System.out.print(child1[i]);
        }
    }

    private static int getIndex(int par1Val, int[] parent2)
    {

        int newIndex = -9;

        for (int i = 0; i < parent2.length; i++)
        {
            if (par1Val == parent2[i])
            {
                newIndex = i;
                break;
            }
        }
        return newIndex;
    }
}
