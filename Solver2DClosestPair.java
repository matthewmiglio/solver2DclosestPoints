import java.awt.Point;
import java.util.Random;



public class Solver2DClosestPair {
    //ints to store the operation counts as we go through the program
    public static int operationCountForSorting = 0;
    public static int operationCountForSearching = 0;


    public static void main(String[] args) {
        introductionPrintout();
        
        
        
        int pointListSize =14;
        
        //make a max value for the coords
        int maxCoordinateValue = pointListSize * 10;
        
        //make random array of the given size using a coord maximum according to the function above
        Point[] pointList = generateRandomPointList(pointListSize, maxCoordinateValue);
       
        //print this unsorted list
        String outString = "\n-------------\nUnsorted list of random coordinates on a board of size " + String.valueOf(pointListSize) + " x " + String.valueOf(pointListSize);
        System.out.println(outString);
        System.out.println(pointArrayToString(pointList));
        
        //sort this list
        pointList = getSortedPointList(pointList);
        
        //get closest points with exhaustive search
        Point[] closestPointsExhaustive = getClosestPointsExhaustiveSearch(pointList);
        
        //print closest points
        System.out.println("-------------\nExhaustive Search method:\n-Closest points are:");
        System.out.println(pointArrayToString(closestPointsExhaustive));
        
        //print the distance between these points
        System.out.println("   Distance: "+String.valueOf(getDistanceCalculation(closestPointsExhaustive[0], closestPointsExhaustive[1])));
        
        //print operation counts for exhaustive method
        String operationCountOutStringExhaustive = "-Operation counts: "+"\n   Searching count: "+String.valueOf(operationCountForSearching);
        System.out.println(operationCountOutStringExhaustive);
        
        //reset search operation count for the DAQ method
        operationCountForSearching=0;
        
        //get closest points with DAQ method
        Point[] closestPointsDAQ = getClosestPointsDivideAndConquer(pointList);
        
        //print closest points
        System.out.println("-------------\nDivide-and-conquer Search method:\n-Closest points are:");
        System.out.println(pointArrayToString(closestPointsDAQ));
        
        //print the distance between these points
        System.out.println("   Distance: "+String.valueOf(getDistanceCalculation(closestPointsDAQ[0], closestPointsDAQ[1])));
        
        //print out operation counts for DAQ method
        int operationCountTotal = operationCountForSorting+operationCountForSearching;
        String operationCountOutStringDAQ = "-Operation counts: "+String.valueOf(operationCountTotal)+ "\n   Sorting count: "+String.valueOf(operationCountForSorting) + "\n   Searching count: "+String.valueOf(operationCountForSearching);
        System.out.println(operationCountOutStringDAQ);
    }


    //method for introductory printout in terminal
    public static void introductionPrintout(){
        System.out.println("Matthew Miglio, Kettering University");
        System.out.println("2D Closest Points Solver");
        System.out.println("June 2022");
    }

    //method to get the two closest points from a list of points with the divide and conquer method
    public static Point[] getClosestPointsDivideAndConquer(Point[] pointList){
        int pointCount = pointList.length;


        if (pointCount > 2){

            //recursively get to and return the points with the smallest distance
            Point[][] leftAndRightPointLists = getHalfedPointArray(pointList);
            Point[] leftPointList = leftAndRightPointLists[0];
            Point[] rightPointList = leftAndRightPointLists[1];

            //recall this method with either half until down to two points
            Point[] closestPointsInLeftPointList = getClosestPointsDivideAndConquer(leftPointList);
            Point[] closestPointsInRightPointList = getClosestPointsDivideAndConquer(rightPointList);
            return getDistanceCalculation(closestPointsInLeftPointList[0], closestPointsInLeftPointList[1]) > getDistanceCalculation(closestPointsInRightPointList[0], closestPointsInRightPointList[1]) ? closestPointsInRightPointList : closestPointsInLeftPointList;
        }
        else if (pointCount ==2){
            return pointList;
        }
        else {
            return new Point[] {new Point(0,0), new Point(99999,99999)};
        }
    }

    //method to get the two closest points from a list of points with an exhaustive search method
    public static Point[] getClosestPointsExhaustiveSearch(Point[] pointList){
        //get the total number of points in pointList
        int pointCount = pointList.length;
        
        //if pointCount is 2 just return the given list because its the only option.
        operationCountForSearching++;
        if (pointCount ==2)return pointList;

        double smallestDistance=Double.valueOf(pointCount) * Double.valueOf(100);
        Point[] smallestDistancePoints= new Point[] {new Point(9999,9999),new Point(9999,9999)};
        for (int indexA = 0; indexA < pointCount; indexA++){
            for (int indexB = 0; indexB < pointCount; indexB++){
                operationCountForSearching++;
                if (indexA != indexB){
                    Point pointA = pointList[indexA];
                    Point pointB = pointList[indexB];

                    double currentDistance = getDistanceCalculation(pointA, pointB);

                    if (currentDistance<smallestDistance){
                        smallestDistance=currentDistance;
                        smallestDistancePoints=new Point[] {pointA,pointB};
                    }
                }
                
                
            }
        }



        return smallestDistancePoints;
    }

    //getting the distance between two points
    public static double getDistanceCalculation(Point pointA, Point pointB){
        return pointA.distance(pointB);
    }

    //method to split a given point list into a left and right list (by half)
    public static Point[][] getHalfedPointArray(Point[] pointList) {
        //increment splitting operation count
        operationCountForSearching++;
        //this method begins with a sorted array
        //declare left and right side lists
        Point[] leftPointList;
        Point[] rightPointList;
        
        //get lenth of given point list
        int length = pointList.length;
        int halfLength = length / 2;



        //if length is even make the two lists half the length each
        if (length % 2 == 0) {
            //declare left and right lists with length
            leftPointList = new Point[halfLength];
            rightPointList = new Point[halfLength];
            //fill leftPointList with first half of points
            int indexOfPointList = 0;
            while (indexOfPointList < halfLength){
                leftPointList[indexOfPointList]=pointList[indexOfPointList];
                indexOfPointList++;
            }
            //fill rightPointList with last half of points
            int indexOfRightPointList = 0;
            while (indexOfPointList < length){
                rightPointList[indexOfRightPointList]=pointList[indexOfPointList];
                indexOfPointList++;
                indexOfRightPointList++;
            }

        } 
        //if length is odd make the lists half each with left getting the extra
        else {
            //declare left and right lists with length
            leftPointList = new Point[(length / 2) + 1];
            rightPointList = new Point[length / 2];
            //fill out these lists depending on which side the point appears on
            int indexOfPointList = 0;
            //first half of indices +1 go to the left side array
            while (indexOfPointList < halfLength +1){
                leftPointList[indexOfPointList]=pointList[indexOfPointList];
                indexOfPointList++;
            }
            //second half of indicies go to the right side array
            int indexOfRightPointList=0;
            while (indexOfPointList < length){
                rightPointList[indexOfRightPointList]=pointList[indexOfPointList];
                indexOfPointList++;
                indexOfRightPointList++;
            }
        }
        //return both the left and right point list together as an array of arrays
        return new Point[][] { leftPointList, rightPointList };
    }

    //method to make a list of random unique points of a given size
    public static Point[] generateRandomPointList(int pointListSize,int maxCoordinateValue){
        //initialize empty point list
        Point[] randomPointList = new Point[pointListSize];
        //for each slot in the empty point list: fill with a unique point
        for (int pointListIndex = 0; pointListIndex < pointListSize; pointListIndex++){
            randomPointList[pointListIndex]=makeRandomUniquePoint(maxCoordinateValue, randomPointList);
        }
        return randomPointList;
    }

    //method to make a random point with a given maximum
    public static Point makeRandomUniquePoint(int maxCoordinateValue, Point[] pointList){
        //declare random object to use for random coordinate making
        Random random = new Random();

        //make random x and y coord
        int randomXCoord=random.nextInt(maxCoordinateValue);
        int randomYCoord=random.nextInt(maxCoordinateValue);
        
        //make a new point
        Point newPoint = new Point(randomXCoord, randomYCoord);

        //check if this point is unique
        boolean pointIsUnique = checkIfPointIsUnique(newPoint, pointList);
        
        //if this point is not unique, recall this method
        if (!(pointIsUnique)){
            makeRandomUniquePoint(maxCoordinateValue,pointList);
        }
        return newPoint;
    }

    //method to check if a point appears in a point list or not
    public static boolean checkIfPointIsUnique(Point checkPoint, Point[] pointList){
        //get length for the for loop
        int pointListSize = pointList.length;
        //loop once for each point in pointlist
        for (int pointListIndex = 0; pointListIndex < pointListSize; pointListIndex++){
            //null point check
            if (pointList[pointListIndex] != null){
                //run this if the point is NOT null
                //get current point
                Point currentPoint = pointList[pointListIndex];
                //if this point in the pointlist is equal to the checkpoint then return false
                if (checkIfPointsAreEqual(currentPoint,checkPoint)){

                    return false;
                }
            }
        }
        return true;
    }

    //method to see if two points are equal
    public static boolean checkIfPointsAreEqual(Point pointA, Point pointB){
        //null check
        if ((pointA==null)||(pointB==null))return false;
        //run the comparisons
        //if either of the Xs or Ys aren't equal return false
        if ((pointA.getX() != pointB.getX())||(pointA.getY() !=  pointB.getY()))return false; else return true;
    }

    //method to convert a point list into a printable String
    public static String pointArrayToString(Point[] pointArray){
        //make return string
        String returnString = "";
        //get length for the for loop
        int pointArrayLength = pointArray.length;
        //loop once for each point in the given point array
        for (int pointArrayIndex = 0; pointArrayIndex < pointArrayLength; pointArrayIndex++){
            //null point check
            if (pointArray[pointArrayIndex] != null){
                //run this if the point is NOT null
                //get current point
                Point currentPoint = pointArray[pointArrayIndex];
                //turn current point to a string
                String currentPointAsString = "   C"+pointArrayIndex+": "+pointToString(currentPoint);
                //add that string to the returnString String
                returnString=returnString+currentPointAsString+"\n";
            }
            else{
                //run this if the point IS null
                returnString=returnString+"null"+"\n";
            }
            
        }
        return returnString;
    }

    //method to convert a point into a printable String
    public static String pointToString(Point point){
        //get the values of each coordinate of the current point
        //current point (which is denoted by pointArraayIndex)
        double xCoord=point.getX();
        double yCoord=point.getY();
        //make printout and print this point as 1 line
        return "("+(xCoord)+","+(yCoord)+")";
    }

    //insertion sort method for sorting a list of points
    public static Point[] getSortedPointList(Point[] pointList){
        for (int indexA=1; indexA < pointList.length; indexA++){
            Point currentPoint = pointList[indexA];
            int indexB = indexA-1;
            while ((indexB >= 0)&&(pointList[indexB].getX()> currentPoint.getX())){
                pointList[indexB + 1] = pointList[indexB];
                indexB=indexB-1;
                operationCountForSorting++;
            }
            pointList[indexB + 1] = currentPoint;
        }
        return pointList;
    }

}

