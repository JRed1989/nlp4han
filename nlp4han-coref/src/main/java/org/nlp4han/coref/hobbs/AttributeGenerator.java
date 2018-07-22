package org.nlp4han.coref.hobbs;

import com.lc.nlp4han.constituent.TreeNode;

/**
 * @author 杨智超
 *
 */
public interface AttributeGenerator
{
	/**
	 * 从treeNode中提取属性
	 * 
	 * @param treeNode
	 *            被提取属性的结构树根节点
	 * @return 返回提取出的属性
	 */
	public MentionAttribute extractAttributes(TreeNode treeNode);
}
