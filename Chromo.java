public static void alternatingPositionCrossover(Chromo parent1, Chromo parent2, Chromo child1, Chromo child2)
	{
		//Acquire two random indexes, use these two indexes to alternate between their positions in filling of the child gene
		int l = Search.r.nextInt(48);
		int r = l;
		while(l == r)
		{
			r = Search.r.nextInt(48);
		}
		
		//Create a hashmap to keep track of the child index values and positions.
		Hashtable<Integer, Boolean> child1aLookup = new Hashtable<Integer, Boolean>(); 
		Hashtable<Integer, Boolean> child2bLookup = new Hashtable<Integer, Boolean>(); 

		for(int i = 0; i <= parent1.chromo.length; i++)
		/*implement the check condition to check for duplicate values between the child chromo index 
		and the parent chromo index it should check the individual index value 
		of the child array against all values of both parent arrays and if duplicate do not insert that index value into
		the child chromo. 
		*/
			//while(i <= child1.chromo.length)
			//loop through all parent index values each time to check for duplicate values in the child
			for(int k = 0; k < parent1.chromo.length; k++)
		{
			if(child1.chromo[i] == parent1.chromo[k] || child1.chromo[i] == parent2.chromo[k])
			break;
			//do not continue with the insertion
			else
			{
				if(i % 2 == 0)
			{
				//want to assign parent 1 to the even index positions
				//push parent index values into the child stack
				child1aLookup.put(parent1.chromo[i], true);
				child2.chromo[i] = parent1.chromo[i];
			}
			//check for if position index is odd by !even condition
			else
			//else condition assign parent 2 to the odd index postions 
			child1aLookup.put(parent2.chromo[i], true);
			child2.chromo[i] = parent2.chromo[i];
			}
		}
			

		//perform the alternate position XO using the inverse relation of the parent chromos.
 		for(int i = 0; i <= parent1.chromo.length; i++)
		{
			//while(i <= child1.chromo.length)
			//loop through all parent index values each time to check for duplicate values in the child
			for(int k = 0; k < parent1.chromo.length; k++)
		{
			if(child1.chromo[i] == parent1.chromo[k] || child1.chromo[i] == parent2.chromo[k])
			break;
			//do not continue with the insertion
			else
			{
				if(i % 2 == 0)
			{
				//want to assign parent 1 to the even index positions
				//push parent index values into the child stack
				child2bLookup.put(parent2.chromo[i], true);
				child2.chromo[i] = parent2.chromo[i];
			}
			//check for if position index is odd by !even condition
			else
			//else condition assign parent 2 to the odd index postions 
			child2bLookup.put(parent1.chromo[i], true);
			child2.chromo[i] = parent1.chromo[i];
			}
		}
			
		}
	}
	}
	/*Pseudo Code Logic
	 * public class APXCrossover {

    public static List<Integer> apx(List<Integer> parent1, List<Integer> parent2) {
        List<Integer> child1 = new ArrayList<>(Collections.nCopies(parent1.size(), -1));
        List<Integer> child2 = new ArrayList<>(Collections.nCopies(parent2.size(), -1));
        boolean useParent1 = true;

        for (int i = 0; i < parent1.size(); i++) {
            int gene = useParent1 ? parent1.get(i) : parent2.get(i);

            if (!child1.contains(gene)) {
                child1.set(i, gene);
            } else {
                child2.set(i, gene);
            }

            useParent1 = !useParent1;
        }

        for (int i = 0; i < parent2.size(); i++) {
            int gene = useParent1 ? parent1.get(i) : parent2.get(i);

            if (!child1.contains(gene)) {
                child1.set(i, gene);
            } else if (!child2.contains(gene)) {
                child2.set(i, gene);
            }
            
            useParent1 = !useParent1;
        }

        return Arrays.asList(child1, child2);
    }

	*/
