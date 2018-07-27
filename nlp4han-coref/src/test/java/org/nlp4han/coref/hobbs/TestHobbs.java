package org.nlp4han.coref.hobbs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

import org.nlp4han.coref.hobbs.Hobbs;

import com.lc.nlp4han.constituent.BracketExpUtil;
import com.lc.nlp4han.constituent.TreeNode;

public class TestHobbs
{

	@Test
	public void testHobbs_1()
	{
		String str;
		// 句一：小明的妈妈是一名教师。
		str = "((IP(NP(DNP(NP(NN 小明))(DEG 的))(NP(NN 妈妈)))(VP(VC 是)(NP(QP(CD 一)(CLP(M 名)))(NP(NN 教师))))(PU 。)))";
		TreeNode s1 = BracketExpUtil.generateTree(str);
		// 句二：家长们都很喜欢她。
		str = "((IP(IP(NP(NN 家长们))(VP(ADVP(AD 都))(ADVP(AD 很))(VP(VV 喜欢)(NP(PN 她)))))(PU 。)))";
		TreeNode s2 = BracketExpUtil.generateTree(str);

		List<TreeNode> constituentTrees = new ArrayList<TreeNode>();
		constituentTrees.add(s1);
		constituentTrees.add(s2);

		TreeNode pronoun = s2.getChild(0).getChild(1).getChild(2).getChild(1).getChild(0);
		TreeNode goal = BracketExpUtil.generateTree("((NP(DNP(NP(NN 小明))(DEG 的))(NP(NN 妈妈))))");

		AttributeFilter attributeFilter = new AttributeFilter(new PNFilter(new NodeNameFilter())); // 组合过滤器
		attributeFilter.setAttributeGenerator(new AttributeGeneratorByDic()); // 装入属性生成器
		attributeFilter.setReferenceNode(pronoun);
		Hobbs hobbs = new Hobbs(attributeFilter);
		TreeNode result = hobbs.hobbs(constituentTrees, pronoun);
		assertEquals(goal, result);
	}

	@Test
	public void testHobbs_2()
	{
		String str;
		// 句一：小明的妈妈是一名教师。
		str = "((IP(NP(DNP(NP(NN 小明))(DEG 的))(NP(NN 妈妈)))(VP(VC 是)(NP(QP(CD 一)(CLP(M 名)))(NP(NN 教师))))(PU 。)))";
		TreeNode s1 = BracketExpUtil.generateTree(str);
		// 句二：张三的妈妈都很喜欢她。
		str = "((IP(IP(NP(DNP(NP(NR 张三))(DEG 的))(NP(NN 妈妈)))(VP(ADVP(AD 很))(VP(VV 喜欢)(NP(PN 她)))))(PU 。)))";
		TreeNode s2 = BracketExpUtil.generateTree(str);

		List<TreeNode> constituentTrees = new ArrayList<TreeNode>();
		constituentTrees.add(s1);
		constituentTrees.add(s2);

		TreeNode pronoun = s2.getChild(0).getChild(1).getChild(1).getChild(1).getChild(0);
		TreeNode goal = BracketExpUtil.generateTree("((NP(DNP(NP(NN 小明))(DEG 的))(NP(NN 妈妈))))");

		AttributeFilter attributeFilter = new AttributeFilter(new PNFilter(new NodeNameFilter())); // 组合过滤器
		attributeFilter.setAttributeGenerator(new AttributeGeneratorByDic()); // 装入属性生成器
		attributeFilter.setReferenceNode(pronoun);
		Hobbs hobbs = new Hobbs(attributeFilter);
		TreeNode result = hobbs.hobbs(constituentTrees, pronoun);
		System.out.println(result);
		assertNotEquals(goal, result);
	}

	@Test
	public void testHobbs_3()
	{	//注释：此句“咖啡”、“耸肩膀”需通过查字典赋Animacy属性排除。
		String str;
		// 句一：我在太平洋酒店的咖啡厅里看见了庞德。
		str = "[(IP(NP(PN 我))(VP(PP(P在)(LCP(NP(DNP(NP(NP(NR 太平洋))(NP(NN 酒店)))(DEG 的))(NP(NN 咖啡厅)))(LC 里)))(VP(VV 看见)(AS 了)(NP(NR 庞德))))(PU 。))]";
		TreeNode s1 = BracketExpUtil.generateTree(str);
		// 句二：他和一个陌生姑娘面对面坐着，喝咖啡，说话，耸肩膀。
		str = "[(IP(IP(NP(NP(PN 他))(CC 和)(NP(NP(QP(CD 一)(CLP(M 个)))(ADJP(JJ 陌生))(NP(NN 姑娘)))(ADJP(JJ 面对面))(NP(NN 坐着))))(PU ，)(VP(VP(VV 喝)(NP(NN 咖啡)))(PU ，)(VP(VV 说话))))(PU ，)(NP(NN 耸肩膀))(PU 。))]";
		TreeNode s2 = BracketExpUtil.generateTree(str);

		List<TreeNode> constituentTrees = new ArrayList<TreeNode>();
		constituentTrees.add(s1);
		constituentTrees.add(s2);

		TreeNode pronoun = s2.getChild(0).getChild(0).getChild(0).getChild(0);
		TreeNode goal = BracketExpUtil.generateTree("( (NP (NR 庞德)))");

		AttributeFilter attributeFilter = new AttributeFilter(new PNFilter(new NodeNameFilter())); // 组合过滤器
		attributeFilter.setAttributeGenerator(new AttributeGeneratorByDic()); // 装入属性生成器
		attributeFilter.setReferenceNode(pronoun);
		Hobbs hobbs = new Hobbs(attributeFilter);
		TreeNode result = hobbs.hobbs(constituentTrees, pronoun);
		assertEquals(goal, result);
	}

	//
	@Test
	public void testHobbs_4()
	{	//注释：此句“这一辈子”需通过查字典赋Animacy属性排除。

		String str;
		// 句一： 黄秋雅是个老姑娘，她这一辈子，大概连恋爱都没谈过。
		str = "((IP(IP(NP(NR 黄秋雅))(VP(VC 是)(NP(QP(CLP(M 个)))(ADJP(JJ 老))(NP(NN 姑娘)))))(PU ，)(IP(NP(PN 她))(NP(DP(DT 这)(QP(CD 一)))(NP(NN 辈子)))(PU ，)(VP(VP(ADVP(AD 大概))(ADVP(AD 连))(VP(VV 恋爱)))(VP(ADVP(AD 都))(ADVP(AD 没))(VP(VV 谈过)))))(PU 。)))";
		TreeNode s1 = BracketExpUtil.generateTree(str);

		List<TreeNode> constituentTrees = new ArrayList<TreeNode>();
		constituentTrees.add(s1);

		TreeNode pronoun = s1.getChild(2).getChild(0).getChild(0);
		TreeNode goal = BracketExpUtil.generateTree("((NP(NR 黄秋雅)))");

		AttributeFilter attributeFilter = new AttributeFilter(new PNFilter(new NodeNameFilter())); // 组合过滤器
		attributeFilter.setAttributeGenerator(new AttributeGeneratorByDic()); // 装入属性生成器
		attributeFilter.setReferenceNode(pronoun);
		Hobbs hobbs = new Hobbs(attributeFilter);
		TreeNode result = hobbs.hobbs(constituentTrees, pronoun);
		System.out.println(result);
		assertEquals(goal, result);
	}
}
