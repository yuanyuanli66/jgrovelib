package y.rl.rk;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import ciir.umass.edu.eval.Evaluator;
import ciir.umass.edu.learning.CoorAscent;
import ciir.umass.edu.learning.DataPoint;
import ciir.umass.edu.learning.Ranker;
import ciir.umass.edu.learning.RankerFactory;
import ciir.umass.edu.utilities.TmpFile;

public class r2 extends r1 {


	// This is a pretty crazy test;
	//  1. First it cooks up a data file in which feature 1 is good, and feature 2 is random crap.
	//  2. Then it runs the Evaluator to tune for MAP.
	//  3. Then it manually loads the Ranker, checks its the kind we asked for, and then makes sure that it learned something valuable.
	@Test
	public void testCoorAscent() throws IOException {
		
		System.out.println(FDatTmp.getAbsolutePath() );
		System.out.println(FDatTrn.getAbsolutePath() );
		System.out.println(FMdl.getAbsolutePath() );
		System.out.println(FMdl.getPath() );
		
		
		Evaluator.main(new String[] {
				"-train", FDatTrn.getPath(),
				"-metric2t", "map",
				"-ranker", "4",
				"-save", FMdl.getPath()});
				

		RankerFactory rf = new RankerFactory();
		Ranker model = rf.loadRankerFromFile( FMdl.getPath() );
		CoorAscent cmodel = (CoorAscent) model;
		System.out.println(Arrays.toString(cmodel.weight));
		
	}

}