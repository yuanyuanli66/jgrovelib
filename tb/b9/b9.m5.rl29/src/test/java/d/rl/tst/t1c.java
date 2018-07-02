package d.rl.tst;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runners.MethodSorters;

import d.rl.tst.sui.c1;
import d.rl.tst.sui.c2;


@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class t1c {
	
	@Test
	@Category(c1.class)
	public void t1c1() {
		System.out.println("t1c1");
	}	

	@Test
	@Category(c2.class)
	public void t1c2() {
		System.out.println("t1c2");
	}

	@Test
	@Category(c1.class)
	public void t1c3() {
		System.out.println("t1c3");
	}	

	@Test
	@Category(c2.class)
	public void t1c4() {
		System.out.println("t1c4");
	}	


}
