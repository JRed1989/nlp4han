package org.nlp4han.coref.hobbs;

import java.util.List;

import com.lc.nlp4han.constituent.TreeNode;

public class NodeNameFilter implements Filter
{
	private List<TreeNode> treeNodes;
	private String[] treeNodeNames;

	public NodeNameFilter()
	{

	}

	public NodeNameFilter(List<TreeNode> treeNodes)
	{
		this.treeNodes = treeNodes;
	}

	public NodeNameFilter(List<TreeNode> treeNodes, String[] treeNodeNames)
	{
		super();
		this.treeNodes = treeNodes;
		this.treeNodeNames = treeNodeNames;
	}

	public void setTreeNodes(List<TreeNode> treeNodes)
	{
		this.treeNodes = treeNodes;
	}

	public void setTreeNodeName(String[] treeNodeName)
	{
		this.treeNodeNames = treeNodeName;
	}

	@Override
	public List<TreeNode> filtering()
	{
		if (treeNodeNames != null)
		{
			for (int i = 0; i < treeNodes.size(); i++)
			{
				boolean flag = false;
				for (int j = 0; j < treeNodeNames.length; j++)
				{
					if (treeNodeNames[j].equals(treeNodes.get(i).getNodeName()))
					{
						flag = true;
						break;
					}
				}
				if (flag)
					continue;
				treeNodes.remove(i);
				i--;
			}
		}
		return treeNodes;
	}

	@Override
	public void setUp(List<TreeNode> treeNodes)
	{
		this.treeNodes = treeNodes;

	}

}
