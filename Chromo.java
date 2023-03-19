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

		System.out.println("\n\n\n");

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

		case 2:
			int minDist = 2;
			//Choose a random chunk size not equal to the chromo length
			int chunkSize = Search.r.nextInt((this.chromo.length - 2) - minDist) + minDist;

			//get the left index Random(0, chromoLength - chunk)
			int leftIndex = Search.r.nextInt(0, (this.chromo.length - 1) - chunkSize);
			//Get the right index left + chunkSize - 1
			int rightIndex = leftIndex + chunkSize - 1;

			int[] subString = new int[chunkSize];

			for (int index = leftIndex; index <= rightIndex; index++)
			{
				subString[index - leftIndex] = this.chromo[index];
			}

			for (int idx = 0; idx < subString.length / 2; idx++)
			{
				int temp = subString[idx];
				subString[idx] = subString[subString.length - 1 - idx];
				subString[subString.length - 1 - idx] = temp;
			}

			for (int index = leftIndex; index <= rightIndex; index++)
			{
				this.chromo[index] = subString[index - leftIndex];
			}
			break;

			case 3:

				ArrayList<Integer> selectedIndices = new ArrayList<Integer>();

				//Scramble mutation
				for (int index = 0; index < this.chromo.length; index++)
				{
					//50 50 chance for selection
					if (Search.r.nextDouble() <= .5)
					{
						//select a random subset of the string (Doesn't need to be contigious)
						selectedIndices.add(index);
					}

				}


				Collections.shuffle(selectedIndices);

				int lastMoveValue = -1;
				int[] chromoCopy = this.chromo.clone();

				for (int index = 1; index <= selectedIndices.size(); index++)
				{
					//0 moves to 1, 1 moves to 0
					int firstIndex = index - 1;
					int secondIndex = index % selectedIndices.size();

					//First index moves to second index
					//This is our first move
					if (lastMoveValue == -1)
					{
						lastMoveValue = chromoCopy[selectedIndices.get(secondIndex)];
						chromoCopy[selectedIndices.get(secondIndex)] = chromoCopy[selectedIndices.get(firstIndex)];
						continue;
					}

					int temp = chromoCopy[selectedIndices.get(secondIndex)];
					chromoCopy[selectedIndices.get(secondIndex)] = lastMoveValue;
					lastMoveValue = temp;
				}

				this.chromo = chromoCopy;

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

		case 1:     //  OX Crossover
			OXCrossover( parent1, parent2, child1, child2);
			break;
		case 2:     //  PMX Crossover
			partiallyMappedCrossOver(parent1, parent2, child1, child2);
			break;
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

	public static void partiallyMappedCrossOver(Chromo parent1Act, Chromo parent2Act, Chromo child1Act, Chromo child2Act)
	{
		int [] parent1 = parent1Act.chromo.clone();
		int [] parent2 = parent2Act.chromo.clone();
		int [] child1 = parent1.clone();
		int [] child2 = parent2.clone();


		int minDist = 2;
		//Choose a random chunk size not equal to the chromo length
		int chunkSize = Search.r.nextInt((parent1.length - 2) - minDist) + minDist;


		//get the left index Random(0, chromoLength - chunk)
		int leftIndex = Search.r.nextInt(0, (parent1.length - 1) - chunkSize);
		//Get the right index left + chunkSize - 1
		int rightIndex = leftIndex + chunkSize - 1;

		//System.out.println("Chunksize = " + chunkSize + " left index: " + leftIndex + " right index: " + rightIndex);

		Hashtable<Integer, Boolean> child1Lookup = new Hashtable<Integer, Boolean>();


		for (int i = 0; i < parent1.length; i++)
		{
			//Lets add all the values to a lookup
			child1[i] = parent1[i];
			if (i < leftIndex || i > rightIndex)
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


			if (child1Lookup.containsKey(parent2[i]))
			{
				//Duplicate key
				//System.out.println("We think " + parent2[i] + " is a duplicate value");
				child1DuplicateIndex.add(i);
			}

			child1Lookup.put(parent2[i], true);

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
					//System.out.println("While looping " + child2.chromo[index]);
					//Find the index of this value in child 1
					for (int j = 0; j < child1.length; j++)
					{

						if (child1[j] == child2[index])
						{
							//System.out.println("Found " + child1.chromo[j]);
							//child1Lookup.remove(child2.chromo[index]);
							index = j;
							break;
						}
					}
				}

				relationMap.put(child1StartingValue, child2[index]);
			}
		}


		for (Map.Entry<Integer, Integer> set : relationMap.entrySet())
		{
			int child1Index = -1;
			int child2Index = -1;

			//System.out.println("\n\nLooking for value " + set.getKey() + " in child 1 and looking for " + set.getValue() + " in child 2");

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
			//System.out.println("Child1 Index = " + child1Index + " child2 index = " + child2Index);
			//If its -1, they might not have been actual duplicates.
			if (child1Index == -1 || child2Index == -1)
				continue;

			int temp = child1[child1Index];
			child1[child1Index] = child2[child2Index];
			child2[child2Index] = temp;
		}

		child1Act.chromo = child1;
		child2Act.chromo = child2;

	}

	public static void OXCrossover(Chromo parent1, Chromo parent2, Chromo child1, Chromo child2)
	{
		// Grab two random indexes
		int l = Search.r.nextInt(48);
		int r = l;
		while (l == r)
		{
			r = Search.r.nextInt(48);
		}

		// make sure l is the lower number
		if (l > r)
		{
			int tmp = l;
			l = r;
			r = tmp;
		}

		// Create hashmaps to keep track of what values have been used
		Hashtable<Integer, Boolean> child1Lookup = new Hashtable<Integer, Boolean>();
		Hashtable<Integer, Boolean> child2Lookup = new Hashtable<Integer, Boolean>();

		int[] par1 = parent1.chromo.clone();
		int[] par2 = parent2.chromo.clone();
		int[] ch1 = new int[48];
		int[] ch2 = new int[48];

		for (int i = l; i<=r; i++)
		{
			ch1[i] = par1[i];
			child1Lookup.put(par1[i], true);

			ch2[i] = par2[i];
			child2Lookup.put(par2[i], true);
		}

		// Starting from the right of the substring fill in from the other parent
		int p1 = 0;
		int p2 = 0;
		for (int i = r + 1; i != l; i++)
		{
			// if were at the end of the array go back to the beginning
			if (i == 48){
				i = 0;
				if (i == l)
          			break;
			}

			while(child2Lookup.containsKey(par1[p1]))
			{
				p1 += 1;
			}

			ch2[i] = par1[p1];
			child2Lookup.put(ch2[i], true);


		}
					
		//System.out.println("child2 " +Arrays.toString(child2.chromo));

		for (int i = r + 1; i != l; i++)
		{
			// if were at the end of the array go back to the beginning
			if (i == 48){
				i = 0;
			}
			if (i == l)
          		break;

			while(child1Lookup.containsKey(parent2.chromo[p2]))
			{
				p2 += 1;
				
			}

			ch1[i] = par2[p2];
			child1Lookup.put(ch1[i], true);

		}

		child1.chromo = ch1;
		child2.chromo = ch2;



	}

}   // End of Chromo.java ******************************************************
