package edu.rowan.algorithms;

import java.util.ArrayList;

/**
 * This class takes a Tour object and tries, by using a branch-and-bound
 * approach, to compute the shortest tour distance.
 * @author Nacer Abreu and Emmanuel Bonilla
 */
public class BranchAndBoundSolver {

	//final Tour tour;
    //final double[][] adjacencyMatrix;
	Tour tour;
    double[][] adjacencyMatrix;
    double[][] lowerboundMatrix;
    double[][] currentTourDistMatrix = new double[6][3];
    
    ArrayList<Integer> visitedCities;
    ArrayList<Integer> bestTourSoFar;
    ArrayList<Integer> cities;

    double tmpTourCost;
    double costOfBestTourSoFar;
    double edge;
    
    int NodeA = 1;
    int NodeB = 2;
    int	closestPointA = 0;
    int closestDistA =1;
    int closestPointB = 2;
    int closestDistB = 3;
    
	public BranchAndBoundSolver(Tour tour) {
        this.tour = tour;
        this.adjacencyMatrix = tour.getAdjacencyMatrix();
        this.lowerboundMatrix = tour.getLowerBoundMatrix();
        this.cities = tour.getCities();
        

        tmpTourCost = 0.0;
        visitedCities = new ArrayList<Integer>();
        bestTourSoFar = new ArrayList<Integer>();
        
        //TODO: find a better way to initialize this variable.
        costOfBestTourSoFar = 1000000000.00; 

        generatePermutation();
        
		//ComputeLowerBound(1);
		for (int i = 0; i < tour.getDimension(); i++){
			bestTourSoFar.add(i);
			if (i != (tour.getDimension()-1)){
			edge = adjacencyMatrix[i][i+1];
			} else
				edge = adjacencyMatrix[i][0];
			tmpTourCost += edge;
		}
/*
for (int i = 0; i< tour.getDimension();i++){
		edge = adjacencyMatrix[i][i+1];
		tmpTourCost += edge;
} */
		//System.out.println("\n" + bestTourSoFar);
		//System.out.println(tmpTourCost);
		//System.out.println(adjacencyMatrix[0][5]);
		
		test();
	}

    /**
     * This function returns an array representing a solution for the TSP
     * problem using a nearest neighbor algorithm.
     * @return  An array representing the shortest tour found. 
     */
    public ArrayList<Integer> getShortestTour() {
         
        /**
         * Test every city as the starting location/node. 
         * The shortest tour will be save off in the variable 'bestTourSoFar' 
         * and its cost in the variable 'costOfBestTourSoFar'
         */
    	for(int city = 0; city < tour.getDimension(); city++){
            
            //Repetitive Nearest-Neighbor Algorithm (RNNA)

            //determineShortestTour(city);
            
        }
        
        printSolution(bestTourSoFar);
        System.out.println("\nTour cost = " + costOfBestTourSoFar);

        return bestTourSoFar;
    }//end of getShortestTour()  
   
    private void generatePermutation() {
        int lastItemIndex = tour.getDimension() - 1;
        int count = 1;
        //System.out.println("lastItemIndex = "+lastItemIndex);
        for (int trail = lastItemIndex; trail >= 0; trail--) {
        	int head = (trail - 1);
            if (head > 0) {
            	if (cities.get(trail) > cities.get(head)) {
            		int lastSlot = lastItemIndex;
                    while (cities.get(head) > cities.get(lastSlot)) {
                    	lastSlot--;
                    }
                    //System.out.println(count+" "+cities.toString());
                    //System.out.println(cities);
                    count++;
                    swap(lastSlot, head);
                    sort(head);
                    trail = lastItemIndex + 1;
            	}
            }
        }
        //System.out.println(count+" "+cities.toString());
        //System.out.println(cities.get(0));
    } //end generatePermutation

    private void test (){
    	double compLowerBound = 0;
    	System.out.println(cities);
    	for (int i = 0; i < 6; i++){
    		compLowerBound = ComputeLowerBound(i);
    		//System.out.println("\nStep "+(i+1));
    		//System.out.println("   City: "+(cities.get(i))+" Lower Bound: "+compLowerBound);
    		System.out.println("Current Tour Dist Matrix: ["+i+"] "+(int)currentTourDistMatrix[i][0]
    				+ " "+currentTourDistMatrix[i][1]+" "+currentTourDistMatrix[i][2]);
    	}
    	System.out.println("\n");
    	for (int i = 0; i < 6; i++){
    		System.out.println("Current Tour Dist Matrix: ["+i+"] "+(int)currentTourDistMatrix[i][0]
    				+ " "+currentTourDistMatrix[i][NodeA]+" "+currentTourDistMatrix[i][NodeB]);
    	}
    	double costOfCurrentTour=0;
    	for (int i = 0; i < (tour.getDimension()-1); i++){
    		costOfCurrentTour += adjacencyMatrix[(cities.get(i)-1)][(cities.get(i+1)-1)];
    	}
    	// Add last node to close loop
    	costOfCurrentTour += adjacencyMatrix[1][cities.get(tour.getDimension()-1)];
    	System.out.println("costOfCurrentTour: "+costOfCurrentTour);
    }
    private double ComputeLowerBound (int node) {
    	
    	double lowerBound = 0;
    	double finalLowerBound = 0;
    	if (node == 0){
    		for (int i = 0; i < tour.getDimension(); i++){
    			lowerBound += lowerboundMatrix[i][closestDistA] + lowerboundMatrix[i][closestDistB];
    			currentTourDistMatrix[i][0] = cities.get(i);
    			currentTourDistMatrix[i][NodeA] = lowerboundMatrix[cities.get(i)-1][closestDistA];
    			currentTourDistMatrix[i][NodeB] = lowerboundMatrix[cities.get(i)-1][closestDistB];
    		}  // end for loop
    		finalLowerBound = lowerBound/2;
    	} 
    	else {
    		if (cities.get(node)==lowerboundMatrix[(cities.get(node)-1)][closestPointA] ||
    			cities.get(node)==lowerboundMatrix[(cities.get(node)-1)][closestPointB]){
    			System.out.println("Don't change Matrix");
    		} 
    		else {
    			if (node == 1){
    				currentTourDistMatrix[node-1][NodeB] = adjacencyMatrix[(cities.get(node)-1)][(cities.get(node-1)-1)];
    				currentTourDistMatrix[node][NodeB] = adjacencyMatrix[(cities.get(node)-1)][(cities.get(node-1)-1)];
    			}
    			else {
    				currentTourDistMatrix[node-1][NodeA] = adjacencyMatrix[(cities.get(node)-1)][(cities.get(node-1)-1)];
    				currentTourDistMatrix[node][NodeB] = adjacencyMatrix[(cities.get(node)-1)][(cities.get(node-1)-1)];
    			}
    		}
    		System.out.println("cities.get(node): "+cities.get(node));
    		System.out.println("lbMatrixA: "+(lowerboundMatrix[(cities.get(node)-1)][closestPointA]+1));
    		System.out.println("lbMatrixB: "+(lowerboundMatrix[(cities.get(node)-1)][closestPointB]+1));

    		//Complete circuit
			currentTourDistMatrix[0][NodeA] = adjacencyMatrix[(cities.get(node)-1)][0];
			currentTourDistMatrix[node][NodeA] = adjacencyMatrix[(cities.get(node)-1)][0];

    		for (int i = node; i >= 0; i--){
    			//System.out.println(node+"   * "+cities.get(i)+" & "+cities.get(i)+" *");
    			 
    			//lowerBound += adjacencyMatrix[i][i+1];
    		}
    		finalLowerBound = lowerBound;
    	}// end else of node=0
    	//System.out.println("Lower Bound = " + finalLowerBound);
      // Prints out the lower bound Matrix  	
/*    	for (int i =0; i < 6; i++){
            	System.out.println("City " + (i+1) + ": " + 
            			((int)lowerboundMatrix[i][0]+1) + "-" + lowerboundMatrix[i][1] + " " +
            			((int)lowerboundMatrix[i][2]+1) + "-" + lowerboundMatrix[i][3]);
    	}    	
*/
    	return finalLowerBound;
    } //end ComputeLowerBound
   
    private void swap(int j, int k) {
        Integer tmp = cities.get(j);
        cities.set(j, cities.get(k));
        cities.set(k, tmp);
    }//end of swap()
    
    private void sort(int m) {
        //last item index
        int index = (cities.size() - 1);
        int n = m;
        //System.out.println("head = "+ m + "; index = " + index);
        for (int i = m + 1; i < index; i++) {
            for (int j = n + 1; j < index; j++) {
                if (cities.get(j) > cities.get(j + 1)) {
                    swap(j, (j + 1));
                }
            }
        }
    }//end of sort()

    /**
     * FOR DEBUGGING PURPOSES. It prints out all the cost for each leg of a 
     * trip (edges).
     * @param tmpSolution The array tmpSolution
     */
    private void printSolution(ArrayList<Integer> solution) {
        double cost = 0.0;
        double edge = 0.0;
        int currentNode = -1;
        int nextNode = -1;
            
        for (int i = 1; i < solution.size(); i++) {
            currentNode = solution.get(i - 1);
            nextNode = solution.get(i);           
            
            edge = adjacencyMatrix[currentNode -1][nextNode - 1];
            cost += edge;
            
            System.out.println("Node: " + currentNode);            
            System.out.println("Nearest Node: " + nextNode  + "; Edge: " + edge);
            System.out.println();
        }//end of for loop.
        
        // The following ties the end node back to the initial
        currentNode = nextNode;
        nextNode = solution.get(0);
        edge = adjacencyMatrix[currentNode - 1][nextNode - 1];
        cost += edge;      
        
        System.out.println("Node: " + currentNode);
        System.out.println("Nearest Node: " + nextNode + "; Edge: " + edge);
        
    }//end of printSolution()
}//end of class
