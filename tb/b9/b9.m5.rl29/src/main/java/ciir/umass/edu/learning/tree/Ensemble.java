/*===============================================================================
 * Copyright (c) 2010-2012 University of Massachusetts.  All Rights Reserved.
 *
 * Use of the RankLib package is subject to the terms of the software license set 
 * forth in the LICENSE file included with this software, and also available at
 * http://people.cs.umass.edu/~vdang/ranklib_license.html
 *===============================================================================
 */

package ciir.umass.edu.learning.tree;

import ciir.umass.edu.learning.DataPoint;
import ciir.umass.edu.utilities.RankLibError;
import y.rl.YUtil;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 
 * - Added toJson() & getJson() to create top level ensemble structure
 * 		- set Json variable for ensemble structure & convert Json to string
 * 
 * @author vdang modified by yyl & dsm
 */

public class Ensemble {
	protected List<RegressionTree> trees = null;
	protected List<Float> weights = null;
	protected int[] features = null;
	
	
	public Ensemble()
	{
		trees = new ArrayList<RegressionTree>();
		weights = new ArrayList<Float>();
	}
	public Ensemble(Ensemble e)
	{
		trees = new ArrayList<RegressionTree>();
		weights = new ArrayList<Float>();
		trees.addAll(e.trees);
		weights.addAll(e.weights);
	}
	public Ensemble(String xmlRep)
	{
		try {
			trees = new ArrayList<RegressionTree>();
			weights = new ArrayList<Float>();
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			byte[] xmlDATA = xmlRep.getBytes();
			ByteArrayInputStream in = new ByteArrayInputStream(xmlDATA);
			Document doc = dBuilder.parse(in);
			NodeList nl = doc.getElementsByTagName("tree");
			HashMap<Integer, Integer> fids = new HashMap<Integer, Integer>();
			
			System.out.println("Ensemble(String xml): #trees="+nl.getLength()); //yyl
			
			for(int i=0;i<nl.getLength();i++)
			{
				Node n = nl.item(i);//each node corresponds to a "tree" (tag)
				System.out.println("Ensemble(String xml): NodeName=" + n.getNodeName() + " NodeValue=" + n.getNodeValue()); //yyl
				
				//create a regression tree from this node
				Split root = create(n.getFirstChild(), fids);
				//yyl: reset tree node ID back to 0 for each new tree
				SplitBean.nid=0;
				//System.out.println("Ensemble(String xml): toString =" + root.toString()); //yyl: print current tree
				System.out.println("Ensemble(String xml): toJson =" + root.JsonToString(root.toJson())); //yyl: print current tree

				//get the weight for this tree
				float weight = Float.parseFloat(n.getAttributes().getNamedItem("weight").getNodeValue());
				System.out.println("Ensemble(String xml): tree weight =" + weight); //yyl

				//add it to the ensemble
				trees.add(new RegressionTree(root));
				weights.add(weight);

			}//end-for
			//System.out.println("Ensemble(String xml) trees:" + trees.toString()); //yyl: print added tree
			System.out.println("Ensemble(String xml) trees size:" + trees.size()); //yyl: print added tree

			features = new int[fids.keySet().size()];
			int i = 0;
			for(Integer fid : fids.keySet())
				features[i++] = fid;
		}
		catch(Exception ex)
		{
			throw RankLibError.create("Error in Emsemble(xmlRepresentation): ", ex);
		}
	}
	
	public void add(RegressionTree tree, float weight)
	{
		trees.add(tree);
		weights.add(weight);
	}
	public RegressionTree getTree(int k)
	{
		return trees.get(k);
	}
	public float getWeight(int k)
	{
		return weights.get(k);
	}
	public double variance()
	{
		double var = 0;
		for (RegressionTree tree : trees) var += tree.variance();
		return var;
	}
	public void remove(int k)
	{
		trees.remove(k);
		weights.remove(k);
	}
	public int treeCount()
	{
		return trees.size();
	}
	public int leafCount()
	{
		int count = 0;
		for (RegressionTree tree : trees) count += tree.leaves().size();
		return count;
	}
	public float eval(DataPoint dp)
	{
		float s = 0;
		for(int i=0;i<trees.size();i++)
			s += trees.get(i).eval(dp) * weights.get(i);
		return s;
	}
	public String toString()
	{
		String strRep = "<ensemble>" + "\n";
		for(int i=0;i<trees.size();i++)
		{
			strRep += "\t<tree id=\"" + (i+1) + "\" weight=\"" + weights.get(i) + "\">" + "\n";
			strRep += trees.get(i).toString("\t\t");
			strRep += "\t</tree>" + "\n";
		}
		strRep += "</ensemble>" + "\n";
		return strRep;
	}
	
	//yyl: toJson() set Json variable convert Json to string
	public String toJson()
	{
		String strRep = "";
		EnsembleBean eb = new EnsembleBean();
			
		for(int i=0;i<trees.size();i++)
		{
			eb.setTreeID(i+1);
			eb.setWeight(weights.get(i));
			eb.setRegTree(trees.get(i));
			
		}//end-for
		
		try {
			strRep = YUtil.mapper.writerWithDefaultPrettyPrinter().writeValueAsString(eb);
		} catch (Exception e) {
			System.out.println("Exception in Ensemble-toJson() " +  e);
		}//end-try-catch
		
		return strRep;

	}//end-getJson method

	
	public int[] getFeatures()
	{
		return features;
	}
	
	/**
	 * Each input node @n corersponds to a <split> tag in the model file.
	 * @param n
	 * @return
	 */
	private Split create(Node n, HashMap<Integer, Integer> fids)
	{
		Split s = null;
		if(n.getFirstChild().getNodeName().compareToIgnoreCase("feature") == 0)//this is a split
		{
			NodeList nl = n.getChildNodes();
			int fid = Integer.parseInt(nl.item(0).getFirstChild().getNodeValue().trim());//<feature>
			fids.put(fid, 0);
			float threshold = Float.parseFloat(nl.item(1).getFirstChild().getNodeValue().trim());//<threshold>
			s = new Split(fid, threshold, 0);      //yyl:create a split with deviance=0
			s.setLeft(create(nl.item(2), fids));
			s.setRight(create(nl.item(3), fids));
			//System.out.println("Ensemble_create: feature fid=" + fid + " threshold=" + threshold); //yyl

		}
		else//this is a stump
		{
			float output = Float.parseFloat(n.getFirstChild().getFirstChild().getNodeValue().trim());
			s = new Split();
			s.setOutput(output);
			//System.out.println("Ensemble_create: output="+output); //yyl
		}
		return s;
	}
}
