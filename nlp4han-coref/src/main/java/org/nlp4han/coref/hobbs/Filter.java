package org.nlp4han.coref.hobbs;

import java.util.List;

import com.lc.nlp4han.constituent.TreeNode;

/**
 * 过滤器接口
 * 
 * @author 杨智超
 *
 */
public interface Filter
{
	/**
	 * 进行过滤
	 * 
	 * @return
	 */
	public List<TreeNode> filtering();

	/**
	 * 设置参照结点
	 * 
	 * @param treeNodes
	 */
	public void setUp(List<TreeNode> treeNodes);
}
