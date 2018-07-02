package y.rl.rk;

import java.io.File;
import java.io.PrintWriter;
import java.util.Random;

import org.junit.Test;

public class r1 {
	
	/**
	 * 
	 */
	public static String sFileDatTmp="rl/dat.tmp.txt";
	public static String sFileDatTrn="rl/dat.trn.txt";
	public static String sFileMdl="rl/mdl.lmart.txt";
	public static File FMdl = new File(gettingFile(sFileMdl));
	public static File FDatTrn = new File(gettingFile(sFileDatTrn));
	public static File FDatTmp = new File(gettingFile(sFileDatTmp));
	
	protected static String gettingFile(String sFile) {

		String trainingDataPath = ClassLoader.getSystemClassLoader().getResource(sFile).getFile();
		return trainingDataPath;

	}

	@Test
	public void r0c_data_gen() throws Exception {
		gen_data_file(FDatTmp);
		gen_data_file(FDatTrn);
	}	
	
	protected void gen_data_file(File fFile ) throws Exception {
		
		PrintWriter pwDat = new PrintWriter(fFile);
		Random rand = new Random();
		for (int i = 0; i < 100; i++) {
			String w1 = (rand.nextBoolean() ? "-1.0" : "1.0");
			String w2 = (rand.nextBoolean() ? "-1.0" : "1.0");
			pwDat.println("1 qid:x 1:1.0 2:"+w1+" # P"+Integer.toString(i));
			pwDat.println("0 qid:x 1:0.9 2:"+w2+" # N"+Integer.toString(i));
		}

		pwDat.close();		
		
	}

}
