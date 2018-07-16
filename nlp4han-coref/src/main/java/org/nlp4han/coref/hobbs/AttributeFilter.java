package org.nlp4han.coref.hobbs;



import com.lc.nlp4han.constituent.TreeNode;

/**
 * 属性过滤，用于适配单复数、性别、动物性、人称等属性
 * @author 杨智超
 *
 */
public abstract class AttributeFilter {
    private TreeNode sample;
    private MentionAttribute sampleAttribute;
    private AttributeGenerator attributeGenerator;
    
    public AttributeFilter(TreeNode sample, AttributeGenerator attributeGenerator)
    {
	this.sample = sample;
	this.attributeGenerator = attributeGenerator;
	sampleAttribute = attributeGenerator.extractAttributes(sample);
    }
    
    /**
     * 比较样本结点sample与被测结点comparedNode是否相匹配
     * @param comparedNode 被测结点
     * @return 若两者属性不排斥，则返回true；否则，返回false
     */
    public boolean doesMatch(TreeNode comparedNode) {
	MentionAttribute attribute = attributeGenerator.extractAttributes(comparedNode);
	return match(sampleAttribute, attribute);
    }
    
    /**
     * 比较两个mention的属性是否相匹配
     * @param attribute1 属性1
     * @param attribute2 属性2
     * @return 若两属性不排斥，则返回true；否则，返回false
     */
    public abstract boolean match(MentionAttribute attribute1, MentionAttribute attribute2);
}
