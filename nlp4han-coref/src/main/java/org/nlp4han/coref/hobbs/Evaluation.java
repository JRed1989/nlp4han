package org.nlp4han.coref.hobbs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.lc.nlp4han.constituent.BracketExpUtil;
import com.lc.nlp4han.constituent.TreeNode;

/**
 * 评价类
 * 
 * @author 杨智超
 *
 */
public class Evaluation
{
	private List<String> oneInput = new ArrayList<String>();
	private List<TreeNode> constituentTrees = new ArrayList<TreeNode>();
	private int total = 0;
	private int count = 0;
	private Hobbs hobbs = new Hobbs();

	public static void main(String[] args)
	{
		Evaluation eva = new Evaluation();
		eva.readFileByLines("语料.txt", "utf-8");
	}

	private void readFileByLines(String fileName, String encoding)
	{

		InputStream is = null;

		BufferedReader reader = null;

		try
		{
			is = Evaluation.class.getClassLoader().getResourceAsStream(fileName);

			reader = new BufferedReader(new InputStreamReader(is, encoding));

			String tempString = null;

			// 一次读入一行，直到读入null为文件结束
			while ((tempString = reader.readLine()) != null)
			{
				processingData(tempString);

			}

			System.out.println("成功：" + count + "/" + total);
			reader.close();

		}
		catch (IOException e)
		{

			e.printStackTrace();

		}
		finally
		{

			if (reader != null)
			{

				try
				{

					reader.close();
				}
				catch (IOException e1)
				{

				}

			}

		}
	}

	private void processingData(String oneLine)
	{

		if (oneLine.startsWith("#"))
		{
			int num = oneInput.size();
			for (int i = 0; i < num - 2; i++)
			{
				String str = oneInput.get(i);
				TreeNode si = BracketExpUtil.generateTree("(" + str + ")");
				constituentTrees.add(si);
			}
			TreeNode pronoun = getPronoun(constituentTrees, oneInput.get(num - 2));
			TreeNode goal = BracketExpUtil.generateTree("(" + oneInput.get(num - 1) + ")");

			AttributeFilter attributeFilter = new AttributeFilter(new PNFilter(new NodeNameFilter())); // 组合过滤器
			attributeFilter.setAttributeGenerator(new AttributeGeneratorByDic()); // 装入属性生成器
			attributeFilter.setReferenceNode(pronoun);
			hobbs.setFilter(attributeFilter);
			TreeNode result = hobbs.hobbs(constituentTrees, pronoun);
			total++;
			String strOfGoal = TreeNodeUtil.getString(goal);
			String strOfResult = TreeNodeUtil.getString(result);
			String headStrOfGoal = TreeNodeUtil.getString(TreeNodeUtil.getHead(goal));
			String headStrOfResult = TreeNodeUtil.getString(TreeNodeUtil.getHead(result));
			if (strOfResult.contains(strOfGoal) && headStrOfGoal.equals(headStrOfResult))
				count++;
			else
			{
				System.out.println("第" + total + "个未通过：");
				System.out.println("\t\tHobbs结果：" + strOfResult);
				System.out.println("\t\t预期结果：" + strOfGoal);
			}

			constituentTrees.clear();
			oneInput.clear();
		}
		else
			oneInput.add(oneLine);
	}

	private TreeNode getPronoun(List<TreeNode> trees, String siteInfor)
	{
		String[] str1 = siteInfor.split("-");
		int site1 = Integer.valueOf(str1[0]);
		int site2 = Integer.valueOf(str1[1]);
		String word = str1[2].trim();
		List<TreeNode> candidates = TreeNodeUtil.getNodesWithSpecified(trees.get(site1), new String[] { "PN" });
		TreeNode result = null;

		for (TreeNode node : candidates)
		{
			if (node.getChildName(0).equals(word))
			{
				site2--;
				if (site2 < 0)
				{
					result = node;
					break;
				}
			}
		}
		return result;
	}
}
