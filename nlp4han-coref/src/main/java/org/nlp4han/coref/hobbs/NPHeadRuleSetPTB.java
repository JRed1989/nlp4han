package org.nlp4han.coref.hobbs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.lc.nlp4han.constituent.HeadRule;

public class NPHeadRuleSetPTB
{
	private static List<String> RIGHT2LEFT1 = new ArrayList<String>();
	private static List<String> RIGHT2LEFT2 = new ArrayList<String>();
	private static List<String> RIGHT2LEFT3 = new ArrayList<String>();
	
	private static HashMap<String, List<HeadRule>> NPRules = new HashMap<>();
	
	static 
	{
		String[] NPStr1 = { "NN", "NR" };
		for (int i = 0; i < NPStr1.length; i++)
		{
			RIGHT2LEFT1.add(NPStr1[i]);
		}
		String[] NPStr2 = { "NP" };
		for (int i = 0; i < NPStr2.length; i++)
		{
			RIGHT2LEFT2.add(NPStr2[i]);
		}
		String[] NPStr3 = { "PN" };
		for (int i = 0; i < NPStr3.length; i++)
		{
			RIGHT2LEFT3.add(NPStr3[i]);
		}
		List<HeadRule> NPRule = new ArrayList<>();
		NPRule.add(new HeadRule(RIGHT2LEFT1, "left"));
		NPRule.add(new HeadRule(RIGHT2LEFT2, "left"));
		NPRule.add(new HeadRule(RIGHT2LEFT3, "left"));
		NPRules.put("NP", NPRule);
	}
	

	/**
	 * 获取NP规则
	 * 
	 * @return
	 */
	public static HashMap<String, List<HeadRule>> getNPRuleSet()
	{
		return NPRules;
	}
}
