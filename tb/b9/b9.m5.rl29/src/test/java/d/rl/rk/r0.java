package d.rl.rk;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.MethodSorters;

import ciir.umass.edu.eval.Evaluator;
import ciir.umass.edu.learning.DataPoint;
import ciir.umass.edu.utilities.TmpFile;
import d.rl.rk.suite.cat.c0;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@Category(c0.class)
public class r0 {
	

	
	/**
	 * 
	 */
	public static String sFileDatTmp="rl/dat.tmp.txt";
	public static String sFileDatTrn="rl/dat.trn.txt";
	public static String sFileMdl="rl/mdl.lmart.txt";
	public static File FMdl = new File(gettingFile(sFileMdl));
	public static File FDatTrn = new File(gettingFile(sFileDatTrn));
	public static File FDatTmp = new File(gettingFile(sFileDatTmp));

	
	/**
	 * 
	 * @throws Exception
	 */
	@Test
	@Category(c0.class)
	public void r0c_data_gen() throws Exception {

		PrintWriter pwDat = new PrintWriter(FDatTmp);
		Random rand = new Random();
		for (int i = 0; i < 100; i++) {
			String w1 = (rand.nextBoolean() ? "-1.0" : "1.0");
			String w2 = (rand.nextBoolean() ? "-1.0" : "1.0");
			pwDat.println("1 qid:x 1:1.0 2:"+w1+" # P"+Integer.toString(i));
			pwDat.println("0 qid:x 1:0.9 2:"+w2+" # N"+Integer.toString(i));
		}

		pwDat.close();
	}	
	
	
	
	
	/**
	 * Algorithms
	 */
	private static final Map<String,Integer> MDL_LST;  
	static {
		Map<String,Integer> mapTmp = new HashMap<String,Integer>();
		mapTmp.put("MART",0);
		mapTmp.put("RankNet",1);
		mapTmp.put("RankBoost",2);
		mapTmp.put("AdaRank",3);
		mapTmp.put("Coordinate Ascent",4);
		mapTmp.put("LambdaMART",6);
		mapTmp.put("ListNet",7);
		mapTmp.put("Random Forests",8);
		mapTmp.put("LinearRegL2",9);
		MDL_LST=Collections.unmodifiableMap(mapTmp);
		} 
	

	@Test
	@Category(c0.class)
	public void r0a() {
		System.out.println("r0a");
		assertEquals( new Integer(MDL_LST.get("LambdaMART")), new Integer(6));
	}	

	@Test
	@Category(c0.class)
	public void t1a2() {
		System.out.println("r0a2");
	}

	@Test
	public void t1a3() {
		System.out.println("r0a3");
	}	

	@Test
	public void t1a4() {
		System.out.println("r0a4");
	}	
	
	protected static String gettingFile(String sFile) {

		String trainingDataPath = ClassLoader.getSystemClassLoader().getResource(sFile).getFile();
		return trainingDataPath;

	}

	void writeRandomData(TmpFile dataFile) throws IOException {
		try (PrintWriter out = dataFile.getWriter()) {
			// feature 1 is the only good one:
			Random rand = new Random();
			for (int i = 0; i < 100; i++) {
				String w1 = (rand.nextBoolean() ? "-1.0" : "1.0");
				String w2 = (rand.nextBoolean() ? "-1.0" : "1.0");
				out.println("1 qid:x 1:1.0 2:"+w1+" # P"+Integer.toString(i));
				out.println("0 qid:x 1:0.9 2:"+w2+" # N"+Integer.toString(i));
			}
		}
	}
	
	
	@Test
	@Category(c0.class)
	public void r0f_eval_no_args() {
		// DataPoint has ugly globals, so for now, make this threadsafe
		synchronized (DataPoint.class) {
			Evaluator.main(new String[]{});
		}
	}	


}	

