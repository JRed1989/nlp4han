package org.nlp4han.coref.hobbs;

import java.util.List;

import com.lc.nlp4han.constituent.TreeNode;

/**
 * 用于过滤中性词为代词的NP候选词
 * 
 * @author 杨智超
 *
 */
public class PNFilter extends Filtering
{

	public PNFilter(Filter filter)
	{
		this.filter = filter;
	}

	@Override
	public List<TreeNode> filtering()
	{
		List<TreeNode> treeNodes = filter.filtering();
		for (int i = 0; i < treeNodes.size(); i++)
		{
			TreeNode node = treeNodes.get(i);
			if (node.getNodeName().equals("NP"))
			{
				TreeNode head = TreeNodeUtil.getHead(node, NPHeadRuleSetPTB.getNPRuleSet());
				String strOfHead = TreeNodeUtil.getString(head);
				if (isPronoun(strOfHead))
				{
					treeNodes.remove(i);
					i--;
				}
			}
		}
		return treeNodes;
	}

	@Override
	public void setUp(List<TreeNode> treeNodes)
	{
		filter.setUp(treeNodes);
	}

	private boolean isPronoun(String str)
	{
		String[] pronouns = { "我", "我们", "你", "你们", "她", "她们", "他", "他们", "它", "它们" };
		for (String pro : pronouns)
		{
			if (str.equals(pro))
				return true;
		}
		return false;
	}

}
