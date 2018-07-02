/*===============================================================================
 * Copyright (c) 2010-2012 University of Massachusetts.  All Rights Reserved.
 *
 * Use of the RankLib package is subject to the terms of the software license set 
 * forth in the LICENSE file included with this software, and also available at
 * http://people.cs.umass.edu/~vdang/ranklib_license.html
 *===============================================================================
 */

package ciir.umass.edu.learning.tree;

import java.util.ArrayList;
import java.util.List;

import ciir.umass.edu.learning.DataPoint;
import y.rl.YUtil;
import y.rl.hw3;

/**
 * 
 * - Added toJson() & getJson() methods
 * 		- set Json variable for tree structure & convert Json to string
 * 
 * @author vdang modified by yyl & dsm
 *
 */
public class Split {
	//Key attributes of a split (tree node)
	protected int featureID = -1;     //yyl: changed from private
	protected float threshold = 0F;   //yyl: changed from private
	protected double avgLabel = 0.0F; //yyl: changed from private
	
	//Intermediate variables (ONLY used during learning)
	//*DO NOT* attempt to access them once the training is done
	protected boolean isRoot = false;  //yyl: changed from private
	protected double sumLabel = 0.0;   //yyl: changed from private
	protected double sqSumLabel = 0.0; //yyl: changed from private
	protected Split left = null;       //yyl: changed from private
	protected Split right = null;      //yyl: changed from private
	protected double deviance = 0F;    //mean squared error "S"        yyl: changed from private
	protected int[][] sortedSampleIDs = null;	//yyl: changed from private
	
	public int[] samples = null;
	public FeatureHistogram hist = null;
	
	public Split()
	{
		
	}
	public Split(int featureID, float threshold, double deviance)
	{
		this.featureID = featureID;
		this.threshold = threshold;
		this.deviance = deviance;
	}
	public Split(int[][] sortedSampleIDs, double deviance, double sumLabel, double sqSumLabel)
	{
		this.sortedSampleIDs = sortedSampleIDs;
		this.deviance = deviance;
		this.sumLabel = sumLabel;
		this.sqSumLabel = sqSumLabel;
		avgLabel = sumLabel/sortedSampleIDs[0].length;
	}
	public Split(int[] samples, FeatureHistogram hist, double deviance, double sumLabel)
	{
		this.samples = samples;
		this.hist = hist;
		this.deviance = deviance;
		this.sumLabel = sumLabel;
		avgLabel = sumLabel/samples.length;
	}
	
	public void set(int featureID, float threshold, double deviance)
	{
		this.featureID = featureID;
		this.threshold = threshold;
		this.deviance = deviance;
	}
	public void setLeft(Split s)
	{
		left = s;
	}
	public void setRight(Split s)
	{
		right = s;
	}
	public void setOutput(float output)
	{
		avgLabel = output;
	}
	
	public Split getLeft()
	{
		return left;
	}
	public Split getRight()
	{
		return right;
	}
	public double getDeviance()
	{
		return deviance;
	}
	public double getOutput()
	{
		return avgLabel;
	}
	
	public List<Split> leaves()
	{
		List<Split> list = new ArrayList<Split>();
		leaves(list);
		return list;		
	}
	private void leaves(List<Split> leaves)
	{
		if(featureID == -1)
			leaves.add(this);
		else
		{
			left.leaves(leaves);
			right.leaves(leaves);
		}
	}
	
	public double eval(DataPoint dp)
	{
		Split n = this;
		while(n.featureID != -1)
		{
			if(dp.getFeatureValue(n.featureID) <= n.threshold)
				n = n.left;
			else
				n = n.right;
		}
		return n.avgLabel;
	}
	
	public String toString()
	{
		return toString("");
	}
	
	public String toString(String indent)
	{
		String strOutput = indent + "<split>" + "\n";
		strOutput += getString(indent + "\t");
		strOutput += indent + "</split>" + "\n";
		return strOutput;
	}
	

	public String getString(String indent)
	{
		String strOutput = "";
		if(featureID == -1)
		{
			strOutput += indent + "<output> " + avgLabel + " </output>" + "\n";
		}
		else
		{
			strOutput += indent + "<feature> " + featureID + " </feature>" + "\n";
			strOutput += indent + "<threshold> " + threshold + " </threshold>" + "\n";
			strOutput += indent + "<split pos=\"left\">" + "\n";
			strOutput += left.getString(indent + "\t");
			strOutput += indent + "</split>" + "\n";
			strOutput += indent + "<split pos=\"right\">" + "\n";
			strOutput += right.getString(indent + "\t");
			strOutput += indent + "</split>" + "\n";
		}
		return strOutput;
	}

	// yyl: toJson() calls toJson(String) & getJson()
	public SplitBean toJson()
	{
		return toJson("");
	}//end-toJson method
	
	// yyl: toJson(String) calls getJson()
	public SplitBean toJson(String indent)
	{	
		return getJson(indent);
	}//end-toJson method
	
	//yyl: getJson() set Json variable convert Json to string
	public SplitBean getJson(String indent)
	{
		SplitBean sb_top = new SplitBean();
		List<SplitBean> stumps = new ArrayList<SplitBean>();
		String feid = String.valueOf(featureID);
		String thre = String.valueOf(threshold);
		String outp = String.valueOf(avgLabel);
		
		// create JSON variable
		if(featureID == -1) //if leave, set output 
		{
			sb_top.setId(String.valueOf(SplitBean.nid++));
			sb_top.setName(outp);
		}
		else //if node, set featureID, split value, leftChild & rightChild
		{
			//create family tree struct
			sb_top.setId(String.valueOf(SplitBean.nid++));
			sb_top.setName(feid + "<" + thre);			
			stumps.add(left.getJson(indent));
			stumps.add(right.getJson(indent));
			sb_top.setChildren(stumps);			
		}//end-if-else
		return sb_top;
	}//end-getJson method

	//yyl: covert JSON to String
	public String JsonToString(SplitBean sb) {
		String strOutput = "";
		
		try {//convert JSON to string
			strOutput = YUtil.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(sb);
		} catch (Exception e) {
			System.out.println("Exception in Split-getJson() " +  e);
		}//end-try-catch

		return strOutput;
	}//end-JsonToString()
	
	//Internal functions(ONLY used during learning)
	//*DO NOT* attempt to call them once the training is done
	public boolean split(double[] trainingLabels, int minLeafSupport)
	{
		return hist.findBestSplit(this, trainingLabels, minLeafSupport);
	}
	public int[] getSamples()
	{
		if(sortedSampleIDs != null)
			return sortedSampleIDs[0];
		return samples;
	}
	public int[][] getSampleSortedIndex()
	{
		return sortedSampleIDs;
	}
	public double getSumLabel()
	{
		return sumLabel;
	}
	public double getSqSumLabel()
	{
		return sqSumLabel;
	}
	public void clearSamples()
	{
		sortedSampleIDs = null;
		samples = null;
		hist = null;
	}
	public void setRoot(boolean isRoot)
	{
		this.isRoot = isRoot;
	}
	public boolean isRoot()
	{
		return isRoot;
	}
}