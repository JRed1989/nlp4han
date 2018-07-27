package org.nlp4han.coref.hobbs;

import java.util.List;

import com.lc.nlp4han.constituent.TreeNode;

public interface Filter
{
	public List<TreeNode> filtering();

	public void setUp(List<TreeNode> treeNodes);
}
