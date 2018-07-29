package org.nlp4han.coref.hobbs;

import static org.junit.Assert.assertEquals;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import com.lc.nlp4han.constituent.BracketExpUtil;
import com.lc.nlp4han.constituent.TreeNode;

public class TestTreeNodeUtil
{
	@Test
	public void testGetHead()
	{
		TreeNode npNode = BracketExpUtil.generateTree("[(NP(IP(NP(NN 外商))(VP(VV 投资)))(NP(NN 企业)))]");
		TreeNode goal = BracketExpUtil.generateTree("[(NP(NN 企业))]");
		TreeNode result = TreeNodeUtil.getHead(npNode);
		assertEquals(goal, result);
		
		
		TreeNode npNode2 = BracketExpUtil.generateTree("[(NP (NN 改革) (CC 和) (NN 开放))]");
		TreeNode goal2 = BracketExpUtil.generateTree("[(NP(NN 改革)(CC 和)(NN 开放))]");
		TreeNode result2 = TreeNodeUtil.getHead(npNode2);
		assertEquals(goal2, result2);
		
		
		TreeNode npNode3 = BracketExpUtil.generateTree("[(NP (NN 产值)(QP (CD 5亿)(CLP (M 元))))]");
		TreeNode goal3 = BracketExpUtil.generateTree("[(NN 产值)]");
		TreeNode result3 = TreeNodeUtil.getHead(npNode3);
		assertEquals(goal3, result3);
		
		
		TreeNode npNode4 = BracketExpUtil.generateTree("[(NP(NP (NR 中国))(NP (NN 国民) (NN 经济) (NN 总产值)))]");
		TreeNode goal4 = BracketExpUtil.generateTree("[(NN 总产值)]");
		TreeNode result4 = TreeNodeUtil.getHead(npNode4);
		assertEquals(goal4, result4);
	}
	
	@Test
	public void testGetNodesWithSpecifiedNameOnLeftOrRightOfPath()
	{
		String str;
		str = "((IP(IP(NP(NN 家长们))(VP(ADVP(AD 都))(ADVP(AD 很))(VP(VV 喜欢)(NP(PN 她)))))(PU 。)))";
		TreeNode s = BracketExpUtil.generateTree(str);
		TreeNode t1 = s.getChild(0).getChild(1).getChild(2).getChild(1).getChild(0);
		TreeNode t2 = s.getChild(0).getChild(1);
		
		Path path1 = new Path();
		path1.getPath(t1, t2);
		List<TreeNode> result = TreeNodeUtil.getNodesWithSpecifiedNameOnLeftOrRightOfPath(t2, path1, "LEfT", new String[]{"AD", "ADVP"});
		
		List<TreeNode> goal = new LinkedList<TreeNode>();
		goal.add(BracketExpUtil.generateTree("[(ADVP(AD 都))]"));
		goal.add(BracketExpUtil.generateTree("[(ADVP(AD 很))]"));
		goal.add(BracketExpUtil.generateTree("[(AD 都)]"));
		goal.add(BracketExpUtil.generateTree("[(AD 很)]"));
		
		assertEquals(goal, result);
	}
	
}
