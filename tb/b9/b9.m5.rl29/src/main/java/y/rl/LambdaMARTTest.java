package y.rl;

import ciir.umass.edu.learning.tree.*;
import ciir.umass.edu.metric.MetricScorer;
import ciir.umass.edu.metric.MetricScorerFactory;
import ciir.umass.edu.features.FeatureManager;
import ciir.umass.edu.learning.DataPoint;
import ciir.umass.edu.learning.RANKER_TYPE;
import ciir.umass.edu.learning.RankList;
import ciir.umass.edu.learning.Ranker;
import ciir.umass.edu.learning.RankerFactory;
import ciir.umass.edu.learning.RankerTrainer;
import ciir.umass.edu.utilities.FileUtils;
import ciir.umass.edu.utilities.MyThreadPool;
import ciir.umass.edu.utilities.SimpleMath;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

public class LambdaMARTTest {

	public static int[] TEST_FEATURES = new int[]{2, 5, 6, 7, 8, 9};
	public static int[] features;
	public static int num_fea;
	public static List<RankList> trn_dat;
	public static MetricScorer trainScorer = null;
	public static RANKER_TYPE rtype = RANKER_TYPE.LAMBDAMART;

	//main settings
	public boolean mustHaveRelDoc = false;
	public boolean useSparseRep = false;

	public String filename="/Users/dwyk/y6/tb/po/gbm_po/treeDisp/input.txt";

	@Test
	public void testLoadFromFactory() {

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
		//Ranker ranker = factoryRanker.loadRankerFromFile("/Users/dwyk/y6/tb/po/gbm_po/treeDisp/lambdamart1.xml");

		//assertEquals(ranker.name(), "LambdaMART");
		//assertArrayEquals(ranker.getFeatures(), FEATURES);

		//@test
		//LambdaMART mart = new LambdaMART();
		//mart.loadFromString(FileUtils.read("/Users/dwyk/y6/tb/po/gbm_po/treeDisp/lambdamart1.xml", "UTF-8"));
		//assertEquals(mart.name(), "LambdaMART");
		//assertArrayEquals(mart.ensemble.getFeatures(), FEATURES);
	}//end-test-func: testLoadFromFactory()


	@Test
	public void testReadInputFromFile() {
		/* Read input file FeatureManager has main() & served as API for read files
		 *  - FeatureManager.readInput() calls either SparseDataPoint() or DenseDataPoint()
		 *  - Both Sparse/DenseDataPoint implements DataPoint abstract class
		 *     - In Dense/SparseDataPoint() transform unknown value (Float.NaN) to 0.0
		 */
		trn_dat = FeatureManager.readInput(filename, mustHaveRelDoc, useSparseRep);

		num_fea = DataPoint.getFeatureCount();		
		System.out.println("TestReadInputFromFile: no. of features=" + num_fea);

		features = FeatureManager.getFeatureFromSampleVector(trn_dat);
		for (RankList itm : trn_dat) {
			for (int i=0; i<itm.size(); i++) {
				DataPoint dp = itm.get(i);
				//System.out.println("TestReadInputFromFile: ----- input "+i
				//		+" dpID=" + dp.getID() +" dpLbl="+ dp.getLabel()+" -------");

				for (int ii=0; ii<num_fea; ii++) {
					int fid = features[ii];
				//	System.out.println("TestReadInputFromFile: fid="+fid+" val="+dp.getFeatureValue(fid));
				}//end-for each feature in a datapoint
			}//end-for each data point in a rank list
		}//end-for each rank list	

	}//end-test-function:testReadInputFromFile() 

	@Test
	public void testReadFeatures() {
	}//end-test: testReadFeatures()


	@Test
	public void testRankTrain() {
		//e.g.: metric = "NDCG@5"
		MyThreadPool.init(1); //yyl added: force for single thread exe

		MetricScorerFactory mFact = new MetricScorerFactory();
		//trainScorer = mFact.createScorer("P@3"); //Precision at 3
		trainScorer = mFact.createScorer("NDCG@3"); //NDCG
		
		/*
		 * Ranker is an abstract class, and RankerTrainer implements the class
		 * RankerTrainer calls createRanker() from RankerFactory class
		 *   - createRanker() creates the Ranker object with another createRanker(type) in RankerFactory
		 *   - setTrainingSet(samples); setFeatures(features); & setMetricScorer(scorer);
		 * RankerTrainer calls ranker.init() from Ranker class (abstract class)
		 *   - init() is being implemented in LambdaMART
		 *   	- for each datapoint, get martSample, init modelSocres, pseduoResp & weight to 0
		 *   	- sort (MART) samples by each feature so that we can quickly retrieve later used threadPool (increasing order)
		 *      - get uniq values for each fea. if >256 uniq values, use compression (max-min)/256
		 *      - build feature histograms for y, default pusedo resp & response squared are all 0s
		 *   - learn() implemented in LambdaMART
		 *      - start GBM process, init nTrees=1000 by default
		 *      - computePseduoRep for each sample
		 *         - sort model scores under current qid (all 0 initially)
		 *         - for each qid, call scorer.swapChange() from MetricScorer abs class calls NDCGScorer (see below)
		 *            - DCGScorer extends MetricScorer & NDCGScorer extends DCGScorer
		 *               - DCGScorer caches discount (5000 initially, expand 1K each time) & gain (6 initially, expand 10 each time)
		 *                  - discount[i] = 1.0/SimpleMath.logBase2(i+2);
		 *                  - gain[i] = (1<<i) - 1;//2^i-1  (6 values 0-5=0,1,3,7,15&31)
		 *                  - swapChange for DCG is: changes[j][i]=changes[i][j]=(discount(i)-discount(j)) * (gain(rel[i])-gain(rel[j]));
		 *               - idealScore for topK rel is: dcg += gain(rel[idx[k]]) * discount(k);
		 *            - swapChange is: changes[j][i]=changes[i][j]=(discount(i)-discount(j)) * (gain(rel[i])-gain(rel[j]))/ideal;
		 *         - for each pair of sample in a qid
		 *            - if p1_lbl > p2_lbl
		 *               - deltaNDCG = abs(changes[j][k])
		 *               - rho = 1.0 / (1 + exp(modelScores_mj - modelScores_mk))
		 *               - double lambda = rho * deltaNDCG
		 *               - pseudoResponses[mj] += lambda
		 *               - pseudoResponses[mk] -= lambda
		 *               - delta = rho * (1.0 - rho) * deltaNDCG
		 *               - weights[mj] += delta
		 *               - weights[mk] += delta
		 *      - update feature histogram
		 *      - create regressionTree(ntree, samples, lables, hist, minLeaf)
		 *      - fit regressionTree
		 *         - set root(idx, hist, MAX, 0)
		 *         - split() calls hist.findBestSplit from featureHistogram class to ensure split is possible
		 *            - samplingRate in FeatureHistogram controls sub feature sampling rate
		 *            - best = SumLeft^2/CntLeft + SumRight^2/CntRight        (biggest score)
		 *
		 *
		 */
		//Way1: use RanderTrainer
		//RankerTrainer trainer = new RankerTrainer();
		//Ranker ranker = trainer.train(rtype, trn_dat, features, trainScorer); //rtype can be string
		
		//Way2: call RankerFactory directly
		double trainingTime = 0;
		RankerFactory rf = new RankerFactory();
		System.out.println("RankerType="+rtype);
		Ranker ranker = rf.createRanker(rtype, trn_dat, features, trainScorer);
		long start = System.nanoTime();
		ranker.init();
		ranker.learn();
		ranker.printParameters();
		trainingTime = System.nanoTime() - start;
		System.out.println("Training time: " + SimpleMath.round((trainingTime)/1e9, 2) + " seconds");	
		
		//Ranker ranker = trainer.train(rtype, train, validation, features, trainScorer);

	}//end-test-function: testRankTrain()





}//end all unit tests