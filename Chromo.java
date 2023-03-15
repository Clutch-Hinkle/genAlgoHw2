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
		
		//Doesnt work :(
		Collections.shuffle(Arrays.asList(tmp));
		chromo = tmp;

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

		case 1:     //  Replace with new random number

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

		case 3:     //  Uniform Crossover

		default:
			System.out.println("ERROR - Bad crossover method selected");
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

		//Read all of the chromosome values to a values left table
		Hashtable<Integer, Boolean> valuesLeft = new Hashtable<Integer, Boolean>();
		for (int i = 0; i < parent1.chromo.length; i++)
		{
			valuesLeft.put(parent1.chromo[i], true);
		}

		//Copy the values within left and right of parent1 to child removing them from values left
		for (int i = leftIndex; i < rightIndex; i++)
		{
			child1.chromo[i] = parent1.chromo[i];
			valuesLeft.remove(parent1.chromo[i]);
		}

		//Mapping index to value
		Hashtable<Integer, Integer> parent2Remaining = new Hashtable<Integer, Integer>();
		//Figure out which values of parent 2 are still in values left
		for (int i = 0; i < parent2.chromo.length; i++)
		{
			if (valuesLeft.containsKey(parent2.chromo[i]))
			{
				//Store both the index and the value
				parent2Remaining.put(i, parent2.chromo[i]);
			}
		}

		//For each of the values
		for (Map.Entry<Integer, Integer> set : parent2Remaining.entrySet())
		{
			int index = set.getKey();
			int city = set.getValue();

			// 1. get the value (city) in parent 1 using parent 2 index
			int par1Val = parent1.chromo[index];

			//Locate this same value in parent 2
			for (int i = 0; i < parent2.chromo.length; i++)
			{
				System.out.println("Not implemented");
			}
		}


			//if the index of this value falls in our chunk.
				//Repeat from 1 using this value
			//else just add it to the child at this index.
		//Copy any remaining positions in parent two to the child.


	}

}   // End of Chromo.java ******************************************************
