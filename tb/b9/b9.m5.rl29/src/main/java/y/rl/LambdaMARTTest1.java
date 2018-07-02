package y.rl;

import java.util.List;

import org.junit.Test;

import ciir.umass.edu.features.FeatureManager;
import ciir.umass.edu.learning.DataPoint;
import ciir.umass.edu.learning.RankList;
//import ciir.umass.edu.learning.tree.*;
import ciir.umass.edu.learning.Ranker;
import ciir.umass.edu.learning.RankerFactory;
//import ciir.umass.edu.utilities.FileUtils;

public class LambdaMARTTest1 {

	public static void main(String [] args) {
		System.out.println("=== Hello! Main() ===");

		//testLoadFromFactory();
		testReadInputFromFile();
		System.out.println("=== Bye! Main() ===");

	}//end-main

	//Test load from existing LambdaMART model
	public static void testLoadFromFactory() {
		System.out.println("-- start testLoadFromFactory() --");

		/* Create RankerFactory object & load from existing file
		 *  - loadRankerFromFile() calls loadRankerFromString() in RankerFactory class
		 *  - loadRankerFromString() calls loadFromString() in Ranker ABSTRACT class
		 *  - loadFromString() is implemented in LambdaMART class
		 *  		- 1. calls Ensemble class & create an Ensemble(String xmlRep) variable
		 *  			- For each tree tag, call Split class to create a tree
		 *  				- Note: all deviance=0 as default, when load a file (avgLabel, sumLabel, sqSumLabel are all 0.0 as default)
		 *  				- toJson(), toJson(String) & getJson worked in Split class
		 *  			- Call RegressionTree to add each tree to trees 
		 *  	    - 2. set features by calling getFeatures() from Ensemble class
		 */
		RankerFactory factoryRanker = new RankerFactory();
		Ranker ranker = factoryRanker.loadRankerFromFile("/Users/dwyk/y6/tb/po/gbm_po/treeDisp/lambdamart1.xml");

		//System.out.println(ranker.name());
		//System.out.println("Score on trn data: " + ranker.getScoreOnTrainingData());
		//System.out.println("Score on val data: " + ranker.getScoreOnValidationData());
		for (int i=0; i<ranker.getFeatures().length; i++) {
			//	System.out.println("Feature idx: " + ranker.getFeatures()[i]);
		}
		System.out.println("-- end testLoadFromFactory() --");
		//assertArrayEquals(ranker.getFeatures(), FEATURES);
	}//end-loadModelFrom file

	public static void testReadInputFromFile() {
		//main settings
		boolean mustHaveRelDoc = false;
		boolean useSparseRep = true;
		String filename="/Users/dwyk/y6/tb/po/gbm_po/treeDisp/input1.txt";


		/* Read input file FeatureManager has main() & served as API for read files
		 *  - FeatureManager.readInput() calls either SparseDataPoint() or DenseDataPoint()
		 *  - Both Sparse/DenseDataPoint implements DataPoint abstract class
		 *     - In Dense/SparseDataPoint() transform unknown value (Float.NaN) to 0.0
		 */
		List<RankList> trn_dat = FeatureManager.readInput(filename, mustHaveRelDoc, useSparseRep);

		int fc = DataPoint.getFeatureCount();		
		System.out.println("TestReadInputFromFile: feature count=" + fc);

		int [] features = FeatureManager.getFeatureFromSampleVector(trn_dat);

		for (RankList itm : trn_dat) {
			for (int i=0; i<fc; i++) {
				DataPoint dp = itm.get(i);
				System.out.println("TestReadInputFromFile: ----- input "+i
						+" dpID=" + dp.getID() +" dpLbl="+ dp.getLabel()+" -------");

				for (int ii=0; ii<features.length; ii++) {
					int fid = features[ii];
					System.out.println("TestReadInputFromFile: fid="+fid+" val="+dp.getFeatureValue(fid)  );
				}//end-for each feature in a data point
			}//end-for each data point in a rank list
		}//end-for each rank list
	}//end-testReadInput
	
	
}//end-main