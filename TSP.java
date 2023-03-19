import java.io.*;
import java.sql.SQLOutput;
import java.util.*;
import java.text.*;

public class TSP extends FitnessFunction{

    double[][] cityArray; 

/*******************************************************************************
*                              HELPER CLASSES                                  *
*******************************************************************************/

    private class Point {
        double Lat, Lon;

        public Point(double x, double y) {
            this.Lat = x * (Math.PI / 180);
            this.Lon = y * (Math.PI / 180);
        }

        public double dist(Point p) {
            // Distance formula with Lat and Long
            // 3963.0 * arccos[(sin(lat1) * sin(lat2)) + cos(lat1) * cos(lat2) * cos(long2 – long1)]
            return  3963.0 * Math.acos((Math.sin(Lat)* Math.sin(p.Lat)) + (Math.cos(Lat) * Math.cos(p.Lat) * Math.cos(p.Lon - Lon)));
        }
    }

/*******************************************************************************
*                            STATIC VARIABLES                                  *
*******************************************************************************/

/*******************************************************************************
*                              CONSTRUCTORS                                    *
*******************************************************************************/

	public TSP(double[][] city) {
		name = "TSP problem";
        this.cityArray = city;
	}

//  COMPUTE A CHROMOSOME'S RAW FITNESS *************************************

	public void doRawFitness(Chromo X){
        // Assumes no more than 100 points
        int fitness = 0;

        //For loop adds the distance between each city in the chromosome
        for (int i=0; i<47; i++)
        {
            int city1 = X.chromo[i];
            int city2 = X.chromo[i+1];
            Point p1 = new Point(cityArray[city1][0],cityArray[city1][1]);
            Point p2 = new Point(cityArray[city2][0],cityArray[city2][1]);

            fitness += p1.dist(p2);
        }

        // Add the distance from first city to last city since its a cycle
        int city1 = X.chromo[47];
        int city2 = X.chromo[0];
        Point p1 = new Point(cityArray[city1][0],cityArray[city1][1]);
        Point p2 = new Point(cityArray[city2][0],cityArray[city2][1]);

        fitness += p1.dist(p2);

        X.rawFitness = fitness;

	}

//  PRINT OUT AN INDIVIDUAL GENE TO THE SUMMARY FILE *********************************

	public void doPrintGenes(Chromo X, FileWriter output) throws java.io.IOException{
        Hwrite.right("[", 3, output);
        for (int i=0; i<X.chromo.length; i++){
            String temp = X.chromo[i] + ",";
            Hwrite.right(temp,3,output);
        }
        Hwrite.right("]", 3, output);
        output.write("\nRawFitness");
        Hwrite.right((int) X.rawFitness,13,output);
        output.write("\n\n");
        return;

	}
}