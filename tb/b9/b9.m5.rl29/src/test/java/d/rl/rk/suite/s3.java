package d.rl.rk.suite;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import d.rl.rk.suite.cat.c1;
import d.rl.rk.suite.cat.c2;
import d.rl.tst.t1c;
import d.rl.tst.t1d;

@RunWith(Categories.class)
@IncludeCategory(c1.class)
@ExcludeCategory(c2.class)
@SuiteClasses({ t1c.class, t1d.class })
public class s3 {}