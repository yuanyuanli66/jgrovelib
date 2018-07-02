package d.rl.rk;

import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.MethodSorters;

import ciir.umass.edu.eval.Evaluator;
import d.rl.Evaluator1a;
import d.rl.rk.suite.cat.c1;

@Category(c1.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class r1 extends r0 {

	
	@Test
	public void r1b_train() throws Exception {
		
		System.out.println("r1b_train : " + FDatTrn.getAbsolutePath());
		System.out.println("r1b_model : " + FMdl.getAbsolutePath());
		assertEquals(4, 4);		
		
		Evaluator1a.main(new String[] {
				"-train", FDatTrn.getAbsolutePath(),
				"-metric2t", "DCG@3",
				"-metric2T", "DCG@3",
				"-ranker", "6",
				"-save", FMdl.getAbsolutePath(),
		});

	}	

	@Ignore
	@Test
	public void r1c_model() {
		
		System.out.println("r1c_model");

		Evaluator.main(new String[]{
				"-load", FMdl.getAbsolutePath(),
				"-test", FDatTrn.getAbsolutePath()				
		});

	}
	
	


}	

