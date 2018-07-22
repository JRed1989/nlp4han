package org.nlp4han.coref.hobbs;

import java.util.List;

import com.lc.nlp4han.constituent.TreeNode;

public class NodeNameFilter implements Filter
{
	private List<TreeNode> treeNodes;
	private String[] treeNodeName;
	
	public NodeNameFilter()
	{
		
	}

	public NodeNameFilter(List<TreeNode> treeNodes)
	{
		this.treeNodes = treeNodes;
	}

	public NodeNameFilter(List<TreeNode> treeNodes, String[] treeNodeName)
	{
		super();
		this.treeNodes = treeNodes;
		this.treeNodeName = treeNodeName;
	}

	public void setTreeNodes(List<TreeNode> treeNodes)
	{
		this.treeNodes = treeNodes;
	}

	public void setTreeNodeName(String[] treeNodeName)
	{
		this.treeNodeName = treeNodeName;
	}

	@Override
	public List<TreeNode> filtering()
	{
		if (treeNodeName != null)
		{
			for (int i = 0; i < treeNodes.size(); i++)
			{
				boolean flag = false;
				for (int j = 0; j < treeNodeName.length; j++)
				{
					if (treeNodeName[j].equals(treeNodes.get(i).getNodeName()))
					{
						flag = true;
						break;
					}
				}
				if (flag)
					break;
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
