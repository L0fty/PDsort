import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * 
 */

/**
 * @author Shawn H. Loftin
 *
 * Sorts an integer collection with an evenly distributed bucket sort.
 *
 */
public class PDSort {
	private final int numSample;
    private final int numCells;
    private final int numBuckets;
    private int numItems;
	private int minRange;
    private int maxRange;
    
    /*
     * Constructor for PDSort object the inputs are for the sample size and the amount of cells the range will be broken up into
     * 
     * @param k the number of sample elements to be randomly taken from the data
     * @param c the number of cells the samples are put in
     * @param b the number of buckets the data is sorted in
     * 
     * 
     */
    
    public PDSort(int k, int c,int b){
    	if (k<=0){
    		throw new IllegalArgumentException("The sample should be greater than zero");
    	}
    	if (c<=0){
    		throw new IllegalArgumentException("The number of cells should be greater than zero");
    	}
    	if (b<=0){
    		throw new IllegalArgumentException("The number of cells should be greater than zero");
    	}
    	this.numSample = k;
    	this.numCells = c;
    	this.numBuckets = b;
    	this.numItems = 0;

    	
    }
    
    
    /*
     * Constructor for PDSort object the inputs are for the sample size and the amount of cells the range will be broken up into
     * 
     * @param k the number of sample elements to be randomly taken from the data
     * @param c the number of cells the samples are put in
     * @param b the number of buckets the data is sorted in
     * 
     * 
     */  
    
  public void sort(List<Integer> list){
    this.numItems=list.size();
	//Traverses the list to find the range of the data  
    this.minRange=list.get(0);  
    this.maxRange=list.get(0);
	for(int i: list){
		if(this.minRange>i){
			this.minRange=i;
		}
		if(this.maxRange<i){
			this.maxRange=i;
		}
	}

	//Randomly samples the list of data
	final ArrayList<Integer> sample = new ArrayList<Integer>();
	Random rnd = new Random();
	while(sample.size()<this.numSample){
		int rand = rnd.nextInt(this.numItems);
		sample.add(list.get(rand));
	}

	//evenly divides the range and puts the sample into cells
	double delta = 1/((((double)this.numItems))/((double)this.numCells));
	ArrayList<ArrayList<Integer>> cells = new ArrayList<ArrayList<Integer>>(this.numCells);
	for(int i=0;i<this.numCells;i++){
		cells.add(new ArrayList<Integer>());
	}
	for(int i : sample){
		cells.get((int) Math.floor((i-this.minRange)*delta)).add(i);
	}

	//builds a distribution function based on the frequency of the sample within a cell
	ArrayList<Double> cdf = new ArrayList<Double>();
	cdf.add(0.0);
	ArrayList<Double> slope = new ArrayList<Double>();
	for(int i=0, j=1; i<cells.size();i++,j++){
		cdf.add(cdf.get(i)+(((double)(cells.get(i).size()+1)/(double)(this.numCells+this.numSample))));
		slope.add((cdf.get(j)-cdf.get(i))*delta);
	}

	//Uses the distribution function to partition the range of the list evenly into separate buckets
	ArrayList<ArrayList<Integer>> buckets = new ArrayList<ArrayList<Integer>>(this.numBuckets);
	for(int i=0;i<this.numBuckets;i++){
		buckets.add(new ArrayList<Integer>());
	}

	for(int i : list){
		int j = (int)((double)(i-this.minRange)*delta);
		double l = (double)i-(double)minRange-(double)(j)/delta;
		double icdf = cdf.get(j)+slope.get(j)*l;
		int bucketNumber = (int)(this.numBuckets*icdf);
		buckets.get(bucketNumber).add(i);
	}

	//sorts the buckets and fills the list with the sorted collection
	list.clear();
	for(int i=0; i<buckets.size();i++){
		Collections.sort(buckets.get(i));
		for(int j=0; j<buckets.get(i).size(); j++){
			list.add(buckets.get(i).get(j));	
		}
	}
  }
    
        
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ArrayList<Integer> list = new ArrayList<Integer>();
		for (int i=500; i>0 ; i--){
			list.add(i);	
		}
		PDSort test = new PDSort(40,8,20);
		test.sort(list);
		for(int i : list){
			System.out.println(i);
		}
	}


    }
