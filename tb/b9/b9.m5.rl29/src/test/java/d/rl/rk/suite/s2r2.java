package d.rl.rk.suite;

import org.junit.experimental.categories.Categories;
import org.junit.experimental.categories.Categories.ExcludeCategory;
import org.junit.experimental.categories.Categories.IncludeCategory;
import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import d.rl.rk.r1;
import d.rl.rk.suite.cat.c0;
import d.rl.rk.suite.cat.c2;


@RunWith(Categories.class)
@IncludeCategory(c2.class)
@ExcludeCategory(c0.class)
@SuiteClasses({ r1.class })
public class s2r2 {}	

