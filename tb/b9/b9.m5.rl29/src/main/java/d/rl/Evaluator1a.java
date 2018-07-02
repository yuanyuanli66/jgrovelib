package d.rl;

import java.util.ArrayList;
import java.util.List;

import ciir.umass.edu.eval.Evaluator;
import ciir.umass.edu.features.FeatureManager;
import ciir.umass.edu.features.Normalizer;
import ciir.umass.edu.features.SumNormalizor;
import ciir.umass.edu.learning.RANKER_TYPE;
import ciir.umass.edu.learning.RankList;
import ciir.umass.edu.learning.Ranker;
import ciir.umass.edu.learning.RankerFactory;
import ciir.umass.edu.learning.RankerTrainer;
import ciir.umass.edu.metric.ERRScorer;
import ciir.umass.edu.metric.METRIC;
import ciir.umass.edu.metric.MetricScorer;
import ciir.umass.edu.metric.MetricScorerFactory;
import ciir.umass.edu.utilities.MyThreadPool;
import ciir.umass.edu.utilities.SimpleMath;

/**
 * Hello world!
 *
 */
public class Evaluator1a {  

	public static void main( String[] args )  {
		
		RANKER_TYPE[] rType2 = new RANKER_TYPE[] { RANKER_TYPE.MART, RANKER_TYPE.RANKNET, 
                RANKER_TYPE.RANKBOOST, RANKER_TYPE.ADARANK, 
                RANKER_TYPE.COOR_ASCENT, RANKER_TYPE.LAMBDARANK, 
                RANKER_TYPE.LAMBDAMART, RANKER_TYPE.LISTNET, 
                RANKER_TYPE.RANDOM_FOREST, RANKER_TYPE.LINEAR_REGRESSION };
		

		String trainFile = "";
		String featureDescriptionFile = "";
		float ttSplit = 0;//train-test split
		float tvSplit = 0.8f;//train-validation split
		int foldCV = -1;
		String validationFile = "";
		String testFile = "";
		List<String> testFiles = new ArrayList<>();
		int rankerType = 4;
		String trainMetric = "DCG@3";
		String testMetric = "DCG@3";
		Evaluator.normalize = false;
		String savedModelFile = "";
		List<String> savedModelFiles = new ArrayList<>();
		String kcvModelDir = "";
		String kcvModelFile = "";
		String rankFile = "";
		String prpFile = "";


		for(int i=0;i<args.length;i++) { 
			if (args[i].equalsIgnoreCase ("-train"))
				trainFile = args[++i];
			else if (args[i].equalsIgnoreCase ("-ranker"))
				rankerType = Integer.parseInt(args[++i]);
			else if (args[i].equalsIgnoreCase ("-feature"))
				featureDescriptionFile = args[++i];
			else if (args[i].equalsIgnoreCase ("-metric2t"))
				trainMetric = args[++i];
			else if (args[i].equalsIgnoreCase ("-metric2T"))
				testMetric = args[++i];
			else if (args[i].equalsIgnoreCase ("-gmax"))
				ERRScorer.MAX = Math.pow(2, Double.parseDouble(args[++i]));			
			else if (args[i].equalsIgnoreCase ("-tts"))
				ttSplit = Float.parseFloat(args[++i]);
			else if (args[i].equalsIgnoreCase ("-tvs"))
				tvSplit = Float.parseFloat(args[++i]);
			else if (args[i].equalsIgnoreCase ("-kcv"))
				foldCV = Integer.parseInt(args[++i]);
			else if (args[i].equalsIgnoreCase ("-validate"))
				validationFile = args[++i];
			else if (args[i].equalsIgnoreCase ("-test")) { 
				testFile = args[++i];
				testFiles.add(testFile);
			} else if (args[i].equalsIgnoreCase ("-save")) {
				Evaluator1a.modelFile = args[++i];
			}
			
		}
		
		//int nThread = Runtime.getRuntime().availableProcessors();
		int nThread=1;
		MyThreadPool.init(nThread);
				
		
		System.out.println( " trainMetric " + trainMetric );
		System.out.println( " testMetric  " + testMetric );
		
		Evaluator1a e = new Evaluator1a(rType2[rankerType], trainMetric, testMetric);		
		
		RankerFactory rf = new RankerFactory();		
		rf.createRanker(rType2[rankerType]).printParameters();
		System.out.println(" trainFile " +  trainFile);
		System.out.println(" Model file: " + modelFile);

		if( trainFile.length() > 0  && modelFile.length() > 0 ) { 
			e.evaluate(trainFile, tvSplit, testFile, featureDescriptionFile);
		} 

		
		MyThreadPool.getInstance().shutdown();
	} 
	
	
	protected MetricScorerFactory mFact = new MetricScorerFactory();	
	protected MetricScorer trainScorer = null;
	protected MetricScorer testScorer = null;
	protected RANKER_TYPE type = RANKER_TYPE.MART;
	
	public static boolean normalize = false;
	public static Normalizer nml = new SumNormalizor();
	public static String modelFile = "";
	
	public static boolean mustHaveRelDoc = false;
	public static boolean useSparseRepresentation = false;
	
	
	public Evaluator1a(RANKER_TYPE rType, String trainMetric, String testMetric) { 
		this.type = rType;
		trainScorer = mFact.createScorer(trainMetric);
		testScorer = mFact.createScorer(testMetric);
	}	
	
	
	public void evaluate(String trainFile, double percentTrain, String testFile, String featureDefFile)
	{
		List<RankList> train = new ArrayList<>();
		List<RankList> validation = new ArrayList<>();
		int[] features = prepareSplit(trainFile, featureDefFile, percentTrain, normalize, train, validation);
		List<RankList> test = null;

		//if(testFile.compareTo("") != 0)
		if (!testFile.isEmpty())
		{
			test = readInput(testFile);
			if(normalize)
				normalize(test, features);
		}
		
		RankerTrainer trainer = new RankerTrainer();
		Ranker ranker = trainer.train(type, train, validation, features, trainScorer);
		
		
		if(test != null)
		{
			double rankScore = evaluate(ranker, test);		
			System.out.println(testScorer.name() + " on test data: " + SimpleMath.round(rankScore, 4));
		}
		if(modelFile.compareTo("")!=0)
		{
			System.out.println("");
			ranker.save(modelFile);
			System.out.println("Model saved to: " + modelFile);
		}
	}
	
	public double evaluate(Ranker ranker, List<RankList> rl)
	{
		List<RankList> l = rl;
		if(ranker != null)
			l = ranker.rank(rl);
		return testScorer.score(l);
	}

	
	/**
	 * Split the input file into two with respect to a specified split size.
	 * @param sampleFile Input data file
	 * @param featureDefFile Feature definition file (if it's an empty string, all features in the input file will be used)
	 * @param percentTrain How much of the input data will be used for training? (the remaining will be reserved for test/validation)
	 * @param normalize Whether to do normalization.
	 * @param trainingData [Output] Training data (after splitting) 
	 * @param testData [Output] Test (or validation) data (after splitting)
	 * @return A list of ids of the features to be used for learning.
	 */
	private int[] prepareSplit(String sampleFile, String featureDefFile, double percentTrain, boolean normalize, List<RankList> trainingData, List<RankList> testData)
	{
                //read input
		List<RankList> data = readInput(sampleFile);

                //read features
		int[] features = readFeature(featureDefFile);

                // no features specified ==> use all features in the training file
		if(features == null)
			features = FeatureManager.getFeatureFromSampleVector(data);
		
		if(normalize)
			normalize(data, features);
		
		FeatureManager.prepareSplit(data, percentTrain, trainingData, testData);
		return features;
	}
	
	public List<RankList> readInput(String inputFile)	
	{
		return FeatureManager.readInput(inputFile, mustHaveRelDoc, useSparseRepresentation);		
	}

	
	public int[] readFeature(String featureDefFile)
	{
                //if(featureDefFile.compareTo("") == 0)
                if (featureDefFile.isEmpty())
			return null;
		return FeatureManager.readFeature(featureDefFile);
	}
	
	public void normalize(List<RankList> samples, int[] fids)
	{
		for (RankList sample : samples) nml.normalize(sample, fids);
	}
	
	
	
	
	
}
