package power_iterator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import static java.lang.Math.abs;
import java.util.ArrayList;
import static java.lang.Math.sqrt;

/**
 * // Power Iterator //
 * @author Savvas Theofilou
 */
public class Power_Iterator {
    /**
     * A method that prints the matrix
     * @param myMatrix the matrix
     * @param rows total rows of the matrix
     */
    public static void printMatrix(ArrayList<ArrayList<Integer>> myMatrix, int rows){
        for (int i=0;i<rows;i++){
            for (int j=0;j<rows;j++){
                System.out.print(myMatrix.get(i).get(j)+" ");
            }
            System.out.println();
        }
    }
    
    /**
     * A method that prints the matrix-multiplier
     * @param myMatrix the matrix
     * @param rows total rows of the matrix
     */
    public static void printMultiplyMatrix(ArrayList<Double> myMatrix, int rows){
        System.out.println("Vertex    EVC");
        for (int i=0;i<rows;i++){
            System.out.printf("%d         %.3f\n",(i+1),myMatrix.get(i));
        }
    }
    
    /**
     * A method that multiplies the file's matrix and the matrix-multiplier
     * @param myMatrix the file's matrix
     * @param myMultiplyMatrix the matrix-multiplier
     * @param rows total rows of the matrix
     * @return the result of the multiplication (the new matrix-multiplier)
     */
    public static ArrayList<Double> getNewMultiplyMatrix(ArrayList<ArrayList<Integer>> myMatrix, ArrayList<Double> myMultiplyMatrix, int rows){
        ArrayList<Double> tempMatrix=new ArrayList<>();
        double tempValue;
        
        for (int i=0;i<rows;i++){
            tempValue=0;
            for (int j=0;j<rows;j++){
                tempValue=tempValue+myMatrix.get(i).get(j)*myMultiplyMatrix.get(j);
            }
            tempMatrix.add(tempValue);
        }
        
        return tempMatrix;
    }
    
    /**
     * A method that finds the normalized value of the matrix-multiplier
     * @param myMatrix the matrix-multiplier
     * @param rows total rows of the matrix
     * @return the normalized value
     */
    public static double getNormalizedValue(ArrayList<Double> myMatrix, int rows){
        double temp=0;
        
        for (int i=0;i<rows;i++){
            temp=temp+myMatrix.get(i)*myMatrix.get(i);
        }
        
        return sqrt(temp);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        File inputFile=null;
        
        if (args.length==0){ //CHECKS IF USER PROVIDED AN ARGUMENT (filename.txt)
            System.err.println("Please provide an argument and run the program again. Exiting..");
            System.exit(-1);
        }
        
        inputFile=new File(args[0]);
        BufferedReader reader=null;
        ArrayList<ArrayList<Integer>> graphMatrix=null;
        boolean matrixIsOk=false;
        int rows=0;
        int totalValues=0;
        
        try{
            reader=new BufferedReader(new FileReader(inputFile));
            graphMatrix=new ArrayList<>();
            String text=null;
            
            
            while ((text=reader.readLine()) != null){ //READING OF THE FILE. LINE BY LINE.
                rows++;
                ArrayList<Integer> temp=new ArrayList<>();
                String[] tokens=text.split(" "); //SPLITS THE LINE THAT READ
                
                for (int i=0;i<tokens.length;i++){
                    try{
                        int nextBit=Integer.parseInt(tokens[i]);
                        if (nextBit!=0 && nextBit!=1){ //IF THE FILE CONTAINS A NUMBER NOT EQUAL TO 0 AND 1
                            System.err.println("There was an error while reading the file (Line:" + rows +"). Please check your file values. Exiting..");
                            System.exit(-1);
                        }
                        else{
                            if (!matrixIsOk){        //
                                if (nextBit==1){     //
                                    matrixIsOk=true; // CHECK IF MATRIX IS FULL OF ZEROS
                                }                    //
                            }                        //
                            temp.add(nextBit);
                            totalValues++;
                        }
                    } catch (NumberFormatException e){ //IF STRING IS NOT INTEGER
                        System.err.println("There was an error while reading the file (Line:" + rows +"). Please check your file values. Exiting..");
                        System.exit(-1);
                    }
                }
                
                graphMatrix.add(temp);
                
            }
            
        } catch (FileNotFoundException e){
            e.printStackTrace();
            System.exit(-1);
        } catch (IOException e){
            e.printStackTrace();
            System.exit(-1);
        } finally {
            try {
                if (reader!=null){
                    reader.close();
                }
            } catch (IOException e){
                
            }
        }
               
        if (!matrixIsOk){
            System.out.println("Matrix is full of zeros! Please check your file values. Exiting..");
            System.exit(-1);
        }
        else if (totalValues!=rows*rows){
            System.out.println("Matrix is not square! Please check your file values. Exiting..");
            System.exit(-1);
        }
        else{
            ArrayList<Double> multiplyMatrixOld=new ArrayList<>(); //
            for (int i=0;i<rows;i++){                              // CREATES A MATRIX OF ONES SIZE rows (FOR THE FIRST MULTIPLICATION)
                multiplyMatrixOld.add(1.0);                        //
            }                                                      //
            ArrayList<Double> multiplyMatrixNew=null;
            int iteration=0;
            double normalizedValueNew, normalizedValueOld=0, error=1;
            double temp;
            
            while (error>0.005){ //WHEN THE FIRST TWO DECIMALS OF normalizedValueNew AND normalizedValueOld ARE THE SAME THE ALGORITHM STOPS
                iteration++;
                
                System.out.println("Iteration "+iteration);
                
                multiplyMatrixNew=getNewMultiplyMatrix(graphMatrix, multiplyMatrixOld, rows);
                normalizedValueNew=getNormalizedValue(multiplyMatrixNew,rows);
                if (normalizedValueNew==0){
                    System.out.println("Normalized Value = 0! Can't perform more iterations. Exiting..");
                    System.exit(0);
                }
                for (int i=0;i<rows;i++){
                    multiplyMatrixNew.set(i, multiplyMatrixNew.get(i)/normalizedValueNew);
                }
                
                System.out.printf("Normalized Value = %.2f\n", normalizedValueNew);
                printMultiplyMatrix(multiplyMatrixNew,rows);
                
                error=abs(normalizedValueNew-normalizedValueOld);
                multiplyMatrixOld=(ArrayList<Double>) multiplyMatrixNew.clone();
                normalizedValueOld=normalizedValueNew;
                
                System.out.println();
            }
            
        }
        
    }
    
}
