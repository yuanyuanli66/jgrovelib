package d.rl.tst;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.MethodSorters;

import d.rl.tst.sui.c1;
import d.rl.tst.sui.c2;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class t1d {
	
	@Test
	@Category(c1.class)
	public void t1d1() {
		System.out.println("t1d1");
	}	

	@Test
	@Category(c2.class)
	public void t1d2() {
		System.out.println("t1d2");
	}

	@Test
	@Category(c1.class)
	public void t1d3() {
		System.out.println("t1d3");
	}	

	@Test
	@Category(c2.class)
	public void t1d4() {
		System.out.println("t1d4");
	}	


}
