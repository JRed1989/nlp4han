package org.nlp4han.coref.hobbs;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.nlp4han.coref.hobbs.MentionAttribute.Animacy;
import org.nlp4han.coref.hobbs.MentionAttribute.Gender;
import org.nlp4han.coref.hobbs.MentionAttribute.Number;

import com.lc.nlp4han.constituent.TreeNode;
import com.lc.nlp4han.util.DictionaryLoader;

public class AttributeGeneratorByDic implements AttributeGenerator
{

	@Override
	public MentionAttribute extractAttributes(TreeNode treeNode)
	{
		MentionAttribute result = new MentionAttribute();
		result.setAni(getAnimacy(treeNode));
		result.setGen(getGender(treeNode));
		result.setNum(getNumber(treeNode));
		return result;
	}

	public Properties loadProperties(String fileName, String encoding) throws IOException
	{
		Properties result = new Properties();
		InputStream stream = AttributeGeneratorByDic.class.getClassLoader()
				.getResourceAsStream(fileName);
		result.load(new InputStreamReader(stream, encoding));

		return result;
	}

	/**
	 * 获取性别属性
	 * 
	 * @param treeNode
	 * @return
	 */
	public Gender getGender(TreeNode treeNode)
	{
		
		try
		{
			String value;
			if (treeNode.getNodeName().equals("PN"))
			{
				Properties genderDic = loadProperties("gender_PN.properties", "utf-8");
				value = genderDic.getProperty(TreeNodeUtil.getString(treeNode));
			}
			else
			{
				TreeNode head = TreeNodeUtil.getHead(treeNode);
				Properties genderDic = loadProperties("gender.properties", "utf-8");
				value = genderDic.getProperty(TreeNodeUtil.getString(head));
			}
			if (value != null && value.equalsIgnoreCase("female"))
				return Gender.FEMALE;
			else if (value != null && value.equalsIgnoreCase("male"))
				return Gender.MALE;
			else
				return Gender.UNKNOWN;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return Gender.UNKNOWN;
	}

	/**
	 * 获取单复数属性
	 * 
	 * @param treeNode
	 * @return
	 */
	public Number getNumber(TreeNode treeNode)
	{

		if (treeNode.getNodeName().equals("PN"))
		{
			
			
			String value;
			Properties numberDic;
			try
			{
				numberDic = loadProperties("number_PN.properties", "utf-8");
				value = numberDic.getProperty(treeNode.getChildName(0));
				if (value != null && value.equalsIgnoreCase("singular"))
					return Number.SINGULAR;
				else if (value != null && value.equalsIgnoreCase("plural"))
					return Number.PLURAL;
				else
					return Number.UNKNOWN;
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}
		else
		{
			if (treeNode.getNodeName().equals("NP"))
			{
				TreeNode head = TreeNodeUtil.getHead(treeNode);
				String stringOfHead = TreeNodeUtil.getString(head);
				if (stringOfHead.contains("们"))
					return Number.PLURAL;
				if (TreeNodeUtil.isParataxisNP(head))
				{
					return Number.PLURAL;
				}
			}
			String stringOfLeafNodes;
			if (TreeNodeUtil.hasNodeName((List<TreeNode>) treeNode.getChildren(), "DP"))
			{// 含有DP结点，则查表number_DP.properties
				List<TreeNode> nodes = TreeNodeUtil.getChildNodeWithSpecifiedName(treeNode, new String[] { "DP" });
				TreeNode dpNode = nodes.get(0);
				stringOfLeafNodes = TreeNodeUtil.getString(treeNode);
				Properties numberDicDP;
				try
				{
					numberDicDP = loadProperties("number_DP.properties", "utf-8");
					Set keys = numberDicDP.keySet();
					Iterator it = keys.iterator();
					while (it.hasNext())
					{
						String key = (String) it.next();
						if (stringOfLeafNodes.contains(key))
						{
							String value = numberDicDP.getProperty(key);
							if (value != null && value.equalsIgnoreCase("singular"))
								return Number.SINGULAR;
							else if (value != null && value.equalsIgnoreCase("plural"))
								return Number.PLURAL;
							else
								return Number.UNKNOWN;
						}
					}
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}

			if (TreeNodeUtil.hasNodeName((List<TreeNode>) treeNode.getChildren(), "QP"))
			{
				List<TreeNode> qpNodes = TreeNodeUtil.getChildNodeWithSpecifiedName(treeNode, new String[] { "QP" });
				TreeNode qpNode = qpNodes.get(0);
				if (TreeNodeUtil.hasNodeName((List<TreeNode>) qpNode.getChildren(), "CD"))
				{
					List<TreeNode> cdNodes = TreeNodeUtil.getNodesWithSpecified(treeNode,
							new String[] { "CD" });
					TreeNode cdNode = cdNodes.get(0);
					if (cdNode.getChild(0).getNodeName().equals("一") || cdNode.getChild(0).getNodeName().equals("1"))
					{
						return Number.SINGULAR;
					}
					return Number.PLURAL;
				}
			}
		}

		return Number.UNKNOWN;
	}

	/**
	 * 获取动物性属性
	 * 
	 * @param treeNode
	 * @return
	 */
	public Animacy getAnimacy(TreeNode treeNode)
	{
		
		try
		{
			String value = null;
			if (treeNode.getNodeName().equals("PN"))
			{
				Properties animacyDic = loadProperties("animacy_PN.properties", "utf-8");
				value = animacyDic.getProperty(TreeNodeUtil.getString(treeNode));
			}
			else
			{
				TreeNode head = TreeNodeUtil.getHead(treeNode);
				Properties animacyDic = loadProperties("animacy.properties", "utf-8");
				Set keys = animacyDic.keySet();
				Iterator it = keys.iterator();
				String strOfHead = TreeNodeUtil.getString(head);
				while (it.hasNext())
				{
					String key = (String)it.next();
					if (strOfHead.contains(key))
					{
						value = animacyDic.getProperty(key);
						break;
					}
				}
			}
			if (value != null && value.equalsIgnoreCase("true"))
				return Animacy.TRUE;
			else if (value != null && value.equalsIgnoreCase("false"))
				return Animacy.FALSE;
			else
				return Animacy.UNKNOWN;
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return Animacy.UNKNOWN;
	}
	
}
