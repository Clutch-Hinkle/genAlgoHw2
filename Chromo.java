/******************************************************************************
*  A Teaching GA					  Developed by Hal Stringer & Annie Wu, UCF
*  Version 2, January 18, 2004
*******************************************************************************/

import java.io.*;
import java.util.*;
import java.text.*;


public class Chromo
{
/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/

	public int[] chromo;
	public double rawFitness;
	public double sclFitness;
	public double proFitness;

/*******************************************************************************
*                            INSTANCE VARIABLES                                *
*******************************************************************************/
private static double randnum;

/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public Chromo(){

		int[] tmp = new int[48];
		for (int i = 0; i < 48; i++)
		{
			tmp[i] = i;
		}
		
		List<Integer> intList = new ArrayList<Integer>(tmp.length);

    	for (int i : tmp)
    	{
      		intList.add(i);
    	}

		Collections.shuffle(intList);

		int[] x = intList.stream().mapToInt(i->i).toArray();

		this.chromo = x;

		this.rawFitness = -1;   //  Fitness not yet evaluated
		this.sclFitness = -1;   //  Fitness not yet scaled
		this.proFitness = -1;   //  Fitness not yet proportionalized
	}


/*******************************************************************************
*                                MEMBER METHODS                                *
*******************************************************************************/


	//  Mutate a Chromosome Based on Mutation Type *****************************

	public void doMutation(){

		String mutChromo = "";
		char x;

		switch (Parameters.mutationType){

		case 1: // Swap two indices

			// get two random indices that are not matching
			int i = Search.r.nextInt(48);
			int j = i;
			while (i == j)
			{
				j = Search.r.nextInt(48);
			}

			// Swaps them
			int tmp = this.chromo[i];
			this.chromo[i] = this.chromo[j];
			this.chromo[j] = tmp;

			break;

		default:
			System.out.println("ERROR - No mutation method selected");
		}
	}

/*******************************************************************************
*                             STATIC METHODS                                   *
*******************************************************************************/

	//  Select a parent for crossover ******************************************

	public static int selectParent(){

		double rWheel = 0;
		int j = 0;
		int k = 0;

		switch (Parameters.selectType){

		case 1:     // Proportional Selection
			randnum = Search.r.nextDouble();
			for (j=0; j<Parameters.popSize; j++){
				rWheel = rWheel + Search.member[j].proFitness;
				if (randnum < rWheel) return(j);
			}
			break;

		case 3:     // Random Selection
			randnum = Search.r.nextDouble();
			j = (int) (randnum * Parameters.popSize);
			return(j);

		case 2:     //  Tournament Selection

		default:
			System.out.println("ERROR - No selection method selected");
		}
	return(-1);
	}

	//  Produce a new child from two parents  **********************************

	public static void mateParents(int pnum1, int pnum2, Chromo parent1, Chromo parent2, Chromo child1, Chromo child2){

		int xoverPoint1;
		int xoverPoint2;

		switch (Parameters.xoverType){

		case 1:     //  Single Point Crossover


		case 2:     //  Two Point Crossover
			partiallyMappedCrossOver(parent1, parent2, child1, child2);
		case 3:     //  Uniform Crossover

		default:
			System.out.println("ERROR - Bad crossover method selected. Defaulting to PMX");
			partiallyMappedCrossOver(parent1, parent2, child1, child2);
		}

		//  Set fitness values back to zero
		child1.rawFitness = -1;   //  Fitness not yet evaluated
		child1.sclFitness = -1;   //  Fitness not yet scaled
		child1.proFitness = -1;   //  Fitness not yet proportionalized
		child2.rawFitness = -1;   //  Fitness not yet evaluated
		child2.sclFitness = -1;   //  Fitness not yet scaled
		child2.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Produce a new child from a single parent  ******************************

	public static void mateParents(int pnum, Chromo parent, Chromo child){

		//  Create child chromosome from parental material
		child.chromo = parent.chromo;

		//  Set fitness values back to zero
		child.rawFitness = -1;   //  Fitness not yet evaluated
		child.sclFitness = -1;   //  Fitness not yet scaled
		child.proFitness = -1;   //  Fitness not yet proportionalized
	}

	//  Copy one chromosome to another  ***************************************

	public static void copyB2A (Chromo targetA, Chromo sourceB){

		targetA.chromo = sourceB.chromo;

		targetA.rawFitness = sourceB.rawFitness;
		targetA.sclFitness = sourceB.sclFitness;
		targetA.proFitness = sourceB.proFitness;
		return;
	}

	public static void partiallyMappedCrossOver(Chromo parent1, Chromo parent2, Chromo child1, Chromo child2)
	{
		int minDist = 2;
		//Choose a random chunk size not equal to the chromo length
		int chunkSize = Search.r.nextInt((parent1.chromo.length - 2) - minDist) + minDist;


		//get the left index Random(0, chromoLength - chunk)
		int leftIndex = Search.r.nextInt(0, (parent1.chromo.length - 1) - chunkSize);
		//Get the right index left + chunkSize - 1
		int rightIndex = leftIndex + chunkSize - 1;

		//System.out.println("Chunksize = " + chunkSize + " left index: " + leftIndex + " right index: " + rightIndex);

		Hashtable<Integer, Boolean> child1Lookup = new Hashtable<Integer, Boolean>();


		for (int i = 0; i < parent1.chromo.length; i++)
		{
			//Lets add all the values to a lookup
			child1.chromo[i] = parent1.chromo[i];
			child1Lookup.put(parent1.chromo[i], true);
			child2.chromo[i] = parent2.chromo[i];

		}


		//Maintain duplicates
		ArrayList<Integer> child1DuplicateIndex = new ArrayList<Integer>();



		//Exchange our chunk of data
		for (int i = leftIndex; i <= rightIndex; i++)
		{
			child1.chromo[i] = parent2.chromo[i];
			child2.chromo[i] = parent1.chromo[i];

			child1Lookup.remove(parent1.chromo[i]);

			if (child1Lookup.containsKey(parent2.chromo[i]))
			{
				//Duplicate key
				//System.out.println("We think " + parent2[i] + " is a duplicate value");
				child1DuplicateIndex.add(i);
			}

			child1Lookup.put(parent2.chromo[i], true);

		}


		//Child 1 value to child 2 value
		Hashtable<Integer, Integer> relationMap = new Hashtable<Integer, Integer>();

		//Go through our duplicate indices to determine mapping relationship
		for (int i = 0; i < child1DuplicateIndex.size(); i++)
		{
			int index = child1DuplicateIndex.get(i);


			//If it doesn't have this value, nice. Just note the relation
			if (!child1Lookup.containsKey(child2.chromo[index]))
			{
				relationMap.put(child1.chromo[index], child2.chromo[index]);
			}
			//Otherwise it already has this value to, so things are a little more tricky.
			else
			{
				int child1StartingValue = child1.chromo[index];
				while (child1Lookup.containsKey(child2.chromo[index]))
				{
					//Find the index of this value in child 1
					for (int j = 0; j < child1.chromo.length; j++)
					{
						if (child1.chromo[j] == child2.chromo[index])
						{
							index = j;
							break;
						}
					}
				}

				relationMap.put(child1StartingValue, child2.chromo[index]);
			}
		}


		for (Map.Entry<Integer, Integer> set : relationMap.entrySet())
		{
			int child1Index = -1;
			int child2Index = -1;

			//System.out.println("\n\nLooking for value " + set.getKey() + " in child 1 and looking for " + set.getValue() + " in child 2");

			for (int i = 0; i < child1.chromo.length; i++)
			{
				//We need to skip everything in the swath
				if (i >= leftIndex && i<=rightIndex)
					continue;

				//We found the location of our child 1
				if (child1.chromo[i] == set.getKey())
				{
					child1Index = i;
					if (child2Index != -1)
						break;
				}

				if (child2.chromo[i] == set.getValue())
				{
					child2Index = i;
					if (child1Index != -1)
						break;
				}
			}
			//we now have our index so lets swap
			//System.out.println("Child1 Index = " + child1Index + " child2 index = " + child2Index);
			//If its -1, they might not have been actual duplicates.
			if (child1Index == -1 || child2Index == -1)
				continue;

			int temp = child1.chromo[child1Index];
			child1.chromo[child1Index] = child2.chromo[child2Index];
			child2.chromo[child2Index] = temp;
		}

	}

}   // End of Chromo.java ******************************************************
