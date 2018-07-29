package org.nlp4han.coref.hobbs;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.nlp4han.coref.hobbs.MentionAttribute.Animacy;
import org.nlp4han.coref.hobbs.MentionAttribute.Gender;

import com.lc.nlp4han.constituent.BracketExpUtil;
import com.lc.nlp4han.constituent.TreeNode;
import org.nlp4han.coref.hobbs.MentionAttribute.Number;

public class TestAttributeGeneratorByDic
{
	AttributeGeneratorByDic agbd;
	
	@Before
	public void setUp()
	{
		agbd = new AttributeGeneratorByDic();
	}
	
	@Test
	public void testGetGender()
	{
		TreeNode treeNode = BracketExpUtil.generateTree("[(NP(DNP (NP (NN 小明))(DEG 的))(NP (NN 妈妈)))]");
		Gender result = agbd.getGender(treeNode);
		Gender goal = Gender.FEMALE;
		assertEquals(goal, result);
		
		TreeNode treeNode1 = BracketExpUtil.generateTree("[(NP (DNP (NP (NR 李明)) (DEG 的)) (NP (NN 弟弟)))]");
		Gender result1 = agbd.getGender(treeNode1);
		Gender goal1 = Gender.MALE;
		assertEquals(goal1, result1);
		
		TreeNode treeNode2 = BracketExpUtil.generateTree("[(NP(QP (CD 三)(CLP (M 个)))(NP (NN 文件)))]");
		Gender result2 = agbd.getGender(treeNode2);
		Gender goal2 = Gender.UNKNOWN;
		assertEquals(goal2, result2);
		
	}
	
	@Test
	public void testGetNumber()
	{
		TreeNode treeNode = BracketExpUtil.generateTree("[(NP(DNP (NP (NN 小明))(DEG 的))(NP (NN 妈妈)))]");
		Number result = agbd.getNumber(treeNode);
		Number goal = Number.UNKNOWN;
		assertEquals(goal, result);
		
		TreeNode treeNode2 = BracketExpUtil.generateTree("[(NP(QP (CD 三)(CLP (M 个)))(NP (NN 文件)))]");
		Number result2 = agbd.getNumber(treeNode2);
		Number goal2 = Number.PLURAL;
		assertEquals(goal2, result2);
		
		TreeNode treeNode3 = BracketExpUtil.generateTree("[(NP(NP(DNP(NP(NR 张三))(DEG 的))(NP(NN 妈妈)))(PU 、)(NP(DNP(NP(ADJP(JJ 小))(NP(NN 明)))(DEG 的))(NP(NN 哥哥)))(CC 和)(NP(PN 我)))]");
		Number result3 = agbd.getNumber(treeNode3);
		Number goal3 = Number.PLURAL;
		assertEquals(goal3, result3);
		
		TreeNode treeNode4 = BracketExpUtil.generateTree("[(NP(QP (CD 一) (CLP (M 条)))(NP (NN 狗)))]");
		Number result4 = agbd.getNumber(treeNode4);
		Number goal4 = Number.SINGULAR;
		assertEquals(goal4, result4);
		
		TreeNode treeNode5 = BracketExpUtil.generateTree("[(NP(DP (DT 这个)) (NP (NN 人)))]");
		Number result5 = agbd.getNumber(treeNode5);
		Number goal5 = Number.SINGULAR;
		assertEquals(goal5, result5);
		
		TreeNode treeNode6 = BracketExpUtil.generateTree("[(NP(DP (DT 那些)) (NP (NN 东西)))]");
		Number result6 = agbd.getNumber(treeNode6);
		Number goal6 = Number.PLURAL;
		assertEquals(goal6, result6);
	}
	
	@Test
	public void testGetAnimacy()
	{
		TreeNode treeNode = BracketExpUtil.generateTree("[(NP (QP (CD 一) (CLP (M 只))) (ADJP (JJ 大)) (NP (NN 狼狗)))]");
		Animacy result = agbd.getAnimacy(treeNode);
		Animacy goal = Animacy.TRUE;
		assertEquals(goal, result);
		
		TreeNode treeNode2 = BracketExpUtil.generateTree("[(NP (DNP (NP (NN 黑色))(DEG 的)) (NP (NN 椅子)))]");
		Animacy result2 = agbd.getAnimacy(treeNode2);
		Animacy goal2 = Animacy.FALSE;
		assertEquals(goal2, result2);
		
		TreeNode treeNode3 = BracketExpUtil.generateTree("[(NP(DNP (NP (NN 小明))(DEG 的))(NP (NN 妈妈)))]");
		Animacy result3 = agbd.getAnimacy(treeNode3);
		Animacy goal3 = Animacy.UNKNOWN;
		assertEquals(goal3, result3);
	}
}
