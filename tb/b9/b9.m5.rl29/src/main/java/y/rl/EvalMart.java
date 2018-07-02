package y.rl;

import java.util.ArrayList;
import java.util.List;

import ciir.umass.edu.eval.Evaluator;
import ciir.umass.edu.features.Normalizer;
import ciir.umass.edu.features.SumNormalizor;
import ciir.umass.edu.learning.RANKER_TYPE;
import ciir.umass.edu.learning.RankerFactory;
import ciir.umass.edu.learning.tree.LambdaMART;
import ciir.umass.edu.metric.METRIC;
import ciir.umass.edu.metric.MetricScorer;
import ciir.umass.edu.metric.MetricScorerFactory;
import ciir.umass.edu.utilities.*;



public class EvalMart {
	public static void main( String[] args )  {
		
		String[] rType = new String[] { "MART"};
		RANKER_TYPE[] rType2 = new RANKER_TYPE[] { RANKER_TYPE.MART};

		
		String trainFile = "";
		String featureDescriptionFile = "";
		float ttSplit = 0;//train-test split
		float tvSplit = 0;//train-validation split
		int foldCV = -1;
		String validationFile = "";
		String testFile = "";
		List<String> testFiles = new ArrayList<>();
		int rankerType = 4;
		String trainMetric = "ERR@1";
		String testMetric = trainMetric;
		Evaluator.normalize = false;
		String savedModelFile = "";
		List<String> savedModelFiles = new ArrayList<>();
		String kcvModelDir = "";
		String kcvModelFile = "";
		String rankFile = "";
		String prpFile = "";

		int nThread = 1; // nThread = #cpu-cores   (set to -1 if want to grab available threads)
		//for my personal use
		String indriRankingFile = "";
		String scoreFile = "";
		
		System.out.println("====== Begin ranker: " + rType[0] + " ========");
		//MART / LambdaMART / Random forest
		LambdaMART.nTrees = 10;
		LambdaMART.nTreeLeaves = 10;
		LambdaMART.learningRate = (float) 0.1;
		LambdaMART.minLeafSupport = 1;
		LambdaMART.nRoundToStopEarly = 2;
		//for debugging
		//LambdaMART.gcCycle = Integer.parseInt(args[++i]);
		
		if(nThread == -1)
			nThread = Runtime.getRuntime().availableProcessors();
		MyThreadPool.init(nThread);

		Evaluator e = new Evaluator(rType2[0], trainMetric, testMetric);

		
		MyThreadPool.getInstance().shutdown();

				

		System.out.println("====== End ranker: " + rType[0] + " ========");
		
	}//end-main
	
	
	//main settings
	public static boolean mustHaveRelDoc = false;
	public static boolean useSparseRepresentation = false;
	public static boolean normalize = false;
	public static Normalizer nml = new SumNormalizor();
	public static String modelFile = "";
 	
 	public static String qrelFile = "";//measure such as NDCG and MAP requires "complete" judgment.
 	//The relevance labels attached to our samples might be only a subset of the entire relevance judgment set.
 	//If we're working on datasets like Letor/Web10K or Yahoo! LTR, we can totally ignore this parameter.
 	//However, if we sample top-K documents from baseline run (e.g. query-likelihood) to create training data for TREC collections,
 	//there's a high chance some relevant document (the in qrel file TREC provides) does not appear in our top-K list -- thus the calculation of
 	//MAP and NDCG is no longer precise. If so, specify that "external" relevance judgment here (via the -qrel cmd parameter)
 	
 	//tmp settings, for personal use
 	public static String newFeatureFile = "";
 	public static boolean keepOrigFeatures = false;
 	public static int topNew = 2000;

 	protected RankerFactory rFact = new RankerFactory();
	protected MetricScorerFactory mFact = new MetricScorerFactory();
	
	protected MetricScorer trainScorer = null;
	protected MetricScorer testScorer = null;
	protected RANKER_TYPE type = RANKER_TYPE.MART;
	

	/*public Evaluator(RANKER_TYPE rType, METRIC trainMetric, METRIC testMetric)
	{
		this.type = rType;
		trainScorer = mFact.createScorer(trainMetric);
		testScorer = mFact.createScorer(testMetric);
		if(qrelFile.compareTo("") != 0)
		{
			trainScorer.loadExternalRelevanceJudgment(qrelFile);
			testScorer.loadExternalRelevanceJudgment(qrelFile);
		}
	}//end-function:Evaluator
    */
}//end-class
