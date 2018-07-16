package org.nlp4han.coref.hobbs;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.Test;

import org.nlp4han.coref.hobbs.Hobbs;
import org.nlp4han.coref.hobbs.Hobbs;

import com.lc.nlp4han.constituent.BracketExpUtil;
import com.lc.nlp4han.constituent.TreeNode;

public class TestHobbs {

    @Test
    public void testHobbs_1() {
	
	String str;
	//句一：小明的妈妈是一名教师。
	str = "(IP(NP(DNP(NP(NN 小明))(DEG 的))(NP(NN 妈妈)))(VP(VC 是)(NP(QP(CD 一)(CLP(M 名)))(NP(NN 教师))))(PU 。))";
	TreeNode s1 = BracketExpUtil.generateTree(str);
	//句二：家长们都很喜欢她。
	str = "(IP(IP(NP(NN 家长们))(VP(ADVP(AD 都))(ADVP(AD 很))(VP(VV 喜欢)(NP(PN 她)))))(PU 。))";
	TreeNode s2 = BracketExpUtil.generateTree(str);
	
	List<TreeNode> constituentTrees = new ArrayList<TreeNode>();
	constituentTrees.add(s1);
	constituentTrees.add(s2);
	
	TreeNode pronoun =  s2.getChild(0).getChild(1).getChild(2).getChild(1).getChild(0);
	TreeNode goal = BracketExpUtil.generateTree("(NP(DNP(NP(NN 小明))(DEG 的))(NP(NN 妈妈)))");
	
	AttributeFilter attributeFilter = null;
	Hobbs hobbs = new Hobbs(attributeFilter);
	TreeNode result = hobbs.hobbs(constituentTrees, pronoun);
	assertEquals(goal, result);
    }

    @Test
    public void testHobbs_2() {
	String str;
	//句一：小明的妈妈是一名教师。
	str = "(IP(NP(DNP(NP(NN 小明))(DEG 的))(NP(NN 妈妈)))(VP(VC 是)(NP(QP(CD 一)(CLP(M 名)))(NP(NN 教师))))(PU 。))";
	TreeNode s1 = BracketExpUtil.generateTree(str);
	//句二：张三的妈妈都很喜欢她。
	str = "(IP(IP(NP(DNP(NP(NR 张三))(DEG 的))(NP(NN 妈妈)))(VP(ADVP(AD 很))(VP(VV 喜欢)(NP(PN 她)))))(PU 。))";
	TreeNode s2 = BracketExpUtil.generateTree(str);
	
	List<TreeNode> constituentTrees = new ArrayList<TreeNode>();
	constituentTrees.add(s1);
	constituentTrees.add(s2);
	
	TreeNode pronoun =  s2.getChild(0).getChild(1).getChild(1).getChild(1).getChild(0);
	TreeNode goal = BracketExpUtil.generateTree("(NP(DNP(NP(NN 小明))(DEG 的))(NP(NN 妈妈)))");
	
	AttributeFilter attributeFilter = null;
	Hobbs hobbs = new Hobbs(attributeFilter);
	TreeNode result = hobbs.hobbs(constituentTrees, pronoun);
	assertNotEquals(goal, result);
    }
}
