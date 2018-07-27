package org.nlp4han.coref.hobbs;

import java.util.List;

import org.nlp4han.coref.hobbs.MentionAttribute.Animacy;
import org.nlp4han.coref.hobbs.MentionAttribute.Gender;
import org.nlp4han.coref.hobbs.MentionAttribute.Number;
import org.nlp4han.coref.hobbs.MentionAttribute.Person;

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
		if ((attribute1.getGen().equals(Gender.FEMALE) && attribute2.getGen().equals(Gender.MALE))
				|| (attribute1.getGen().equals(Gender.MALE) && attribute2.getGen().equals(Gender.FEMALE)))
			return false;
		if ((attribute1.getNum().equals(Number.PLURAL) && attribute2.getNum().equals(Number.SINGULAR))
				|| (attribute2.getNum().equals(Number.PLURAL) && attribute1.getNum().equals(Number.SINGULAR)))
			return false;
		if ((attribute1.getAni().equals(Animacy.TRUE) && attribute2.getAni().equals(Animacy.FALSE))
				|| (attribute2.getAni().equals(Animacy.TRUE) && attribute1.getAni().equals(Animacy.FALSE)))
			return false;
		if ((attribute1.getPer().equals(Person.TRUE) && attribute2.getPer().equals(Person.FALSE))
				|| (attribute2.getPer().equals(Person.TRUE) && attribute1.getPer().equals(Person.FALSE)))
			return false;
		return true;
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
