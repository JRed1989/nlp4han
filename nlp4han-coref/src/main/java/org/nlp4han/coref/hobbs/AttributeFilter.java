package org.nlp4han.coref.hobbs;

import java.util.List;

import com.lc.nlp4han.constituent.TreeNode;

/**
 * 属性过滤，用于适配单复数、性别、动物性、人称等属性
 * 
 * @author 杨智超
 *
 */
public class AttributeFilter extends Filtering
{
	private TreeNode referenceNode;
	private MentionAttribute referenceNodeAttribute;
	private AttributeGenerator attributeGenerator;

	public AttributeFilter(Filter filter)
	{
		this.filter = filter;
	}

	public AttributeFilter(Filtering filter, AttributeGenerator attributeGenerator)
	{
		this(filter);
		this.attributeGenerator = attributeGenerator;
	}

	public void setUp(TreeNode referenceNode, AttributeGenerator attributeGenerator)
	{
		this.referenceNode = referenceNode;
		this.attributeGenerator = attributeGenerator;
		if (this.attributeGenerator != null)
			this.referenceNodeAttribute = this.attributeGenerator.extractAttributes(referenceNode);
	}

	public void setReferenceNode(TreeNode referenceNode)
	{
		this.referenceNode = referenceNode;
		if (this.attributeGenerator != null)
			this.referenceNodeAttribute = this.attributeGenerator.extractAttributes(referenceNode);
	}

	public void setAttributeGenerator(AttributeGenerator attributeGenerator)
	{
		this.attributeGenerator = attributeGenerator;
	}

	/**
	 * 比较两个mention的属性是否相匹配
	 * 
	 * @param attribute1
	 *            属性1
	 * @param attribute2
	 *            属性2
	 * @return 若两属性不排斥，则返回true；否则，返回false
	 */
	public boolean isMatched(MentionAttribute attribute1, MentionAttribute attribute2)
	{
		// TODO : 需完成属性的匹配规则，如性别、数量、动物性、人等
		return false;
	}

	@Override
	public List<TreeNode> filtering()
	{
		List<TreeNode> treeNodes = filter.filtering();
		if (this.referenceNode == null)
			throw new RuntimeException("未设置基准结点");
		if (this.attributeGenerator != null)
		{
			if (this.referenceNodeAttribute == null)
			{
				referenceNodeAttribute = this.attributeGenerator.extractAttributes(referenceNode);
			}
			for (int i = 0; i < treeNodes.size(); i++)
			{
				if (!isMatched(this.referenceNodeAttribute,
						this.attributeGenerator.extractAttributes(treeNodes.get(i))))
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

}
