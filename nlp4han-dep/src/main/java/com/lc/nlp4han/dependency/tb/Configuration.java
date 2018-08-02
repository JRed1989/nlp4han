package com.lc.nlp4han.dependency.tb;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;



public class Configuration
{
	private ArrayDeque<Vertice> stack = new ArrayDeque<Vertice>();
	private LinkedList<Vertice> wordsBuffer = new LinkedList<Vertice>();
	private ArrayList<Arc> arcs = new ArrayList<Arc>();

	public Configuration(ArrayDeque<Vertice> stack, LinkedList<Vertice> wordsBuffer, ArrayList<Arc> arcs)
	{

		stack.push(wordsBuffer.get(0));
		wordsBuffer.remove(0);
		this.stack = stack;
		this.wordsBuffer = wordsBuffer;
		this.arcs = arcs;
	}

	public Configuration(String[] words, String[] pos)
	{
		if (words.length != 0)
		{
			wordsBuffer = Vertice.getWordsBuffer(words, pos);
			stack.push(wordsBuffer.get(0));
			wordsBuffer.remove(0);
		}
	}

	
	public Configuration()
	{
		
	}

	// public static Configuration initialConf(DependencySample sample)
	// {//通过sample得到初始的一个Configuration
	// //······
	// return new Configuration();
	// }
	
	
	//包括“核心”
	public static Configuration generateConfByActions(String[] wordpos, String[] priorActions)
	{
		String[] words = new String[(wordpos.length + 1) / 2];
		String[] poses = new String[(wordpos.length + 1) / 2];
		for (int i = 0; i < words.length; i++)
		{
			String[] word_pos = wordpos[i].split("_");
			words[i] = word_pos[0];
			poses[i] = word_pos[1];
		}
		Configuration conf = new Configuration(words,poses);
		for (String preAction : priorActions)
		{
			ActionType at = ActionType.toType(preAction);
			conf.transition(at);
		}
		return conf;
	}
	
	
	
	
	
	
	/**
	 * 产生一个sample初始的Configuration
	 * 
	 * @param words
	 *            词语
	 * @param pos
	 *            词性
	 * @param dependency
	 *            依存关系
	 * @param dependencyWords
	 *            依存词
	 * @param dependencyIndices
	 *            依存词的下标
	 * @param ac
	 *            额外的信息
	 * @return 事件列表
	 */
	public static Configuration initialConf(String[] words, String[] pos)
	{
		return new Configuration(words, pos);
	}

	public boolean isFinalConf()
	{
		if (wordsBuffer.isEmpty() && stack.size() == 1)
			return true;
		else
			return false;
	}

	/**
	 * 当栈顶元素和buffer第一个单词没有关系时，判断是否reduce
	 * 
	 * @return 有关系返回true
	 */
	public boolean canReduce(String[] dependencyIndices)
	{// words包括人工添加的“核心”
//		if (wordsBuffer.isEmpty())
//			return false;
		Vertice[] wordsInStack = stack.toArray(new Vertice[stack.size()]);
		int indexOfWord_Si;// 该单词在words中索引
		int indexOfWord_B1 = wordsBuffer.get(0).getIndexOfWord();
		int headIndexOfWord_Si;// 栈顶单词中心词在words中的索引
		int headIndexOfWord_B1 = Integer.parseInt(dependencyIndices[indexOfWord_B1 - 1]);
		for (int i = 1; i < stack.size(); i++)
		{
			indexOfWord_Si = wordsInStack[i].getIndexOfWord();// 该单词在words中索引
			if (indexOfWord_Si == 0)
				headIndexOfWord_Si = -1;
			else
				headIndexOfWord_Si = Integer.parseInt(dependencyIndices[indexOfWord_Si - 1]);// 栈顶第i个单词中心词在words中的索引
			if (indexOfWord_Si == headIndexOfWord_B1 || indexOfWord_B1 == headIndexOfWord_Si)
				return true;
		}
		return false;
	}

	// 共四类基本操作RIGHTARC_SHIFT、LEFTARC_REDUCE、SHIFT、REDUCE
	public Configuration transition(ActionType actType)
	{
		switch (actType.getBaseAction())
		{
		case "RIGHTARC_SHIFT":
			return addArc(new Arc(actType.getRelation(), stack.peek(), wordsBuffer.get(0))).shift();
		case "LEFTARC_REDUCE":
			return addArc(new Arc(actType.getRelation(), wordsBuffer.get(0), stack.peek())).reduce();
		case "SHIFT":
			return shift();
		case "REDUCE":
			return reduce();
		default:
			throw new IllegalArgumentException("参数不合法!");
		}
	}



	public Configuration addArc(Arc arc)
	{
		arcs.add(arc);
		return this;
	}

	public Configuration shift()
	{
		if (wordsBuffer.size() != 0)
		{
			stack.push(wordsBuffer.remove(0));
			return this;
		}
		else
		{
			return null;// ?
		}

	}

	public Configuration reduce()
	{
		if (!stack.isEmpty())
		{
			stack.pop();
			return this;
		}
		else
		{
			return null;
		}

	}

	public String toString()
	{
		Vertice[] vS = stack.toArray(new Vertice[stack.size()]);
		Vertice[] vB = wordsBuffer.toArray(new Vertice[wordsBuffer.size()]);
		StringBuilder stackStr = new StringBuilder();
		StringBuilder bufferStr = new StringBuilder();
		for (int i = 0; i < stack.size(); i++)
		{
			stackStr.append(vS[stack.size() - i - 1].toString() + " ") ;
		}
		for (int i = 0; i < wordsBuffer.size(); i++)
		{
			bufferStr.append(vB[i].toString() + " ");
		}

		
		return "栈底至栈顶元素：" + stackStr.toString() + " ___" + "buffer:"+bufferStr.toString();
	}

	public String arcsToString()
	{
		StringBuilder allArc = new StringBuilder();
		allArc.append("arcs:"+"\r\n");
		for (int i = 0; i < arcs.size(); i++)
		{
			allArc.append(arcs.get(arcs.size() - i - 1).toString()+"\r\n");
		}
		return allArc.toString();
	}

	public ArrayDeque<Vertice> getStack()
	{
		return stack;
	}

	public void setStack(ArrayDeque<Vertice> stack)
	{
		this.stack = stack;
	}

	public LinkedList<Vertice> getWordsBuffer()
	{
		return wordsBuffer;
	}

	public void setWordsBuffer(LinkedList<Vertice> wordsBuffer)
	{
		this.wordsBuffer = wordsBuffer;
	}

	public ArrayList<Arc> getArcs()
	{
		return arcs;
	}

	public void setArcs(ArrayList<Arc> arcs)
	{
		this.arcs = arcs;
	}
	public static void main(String[] args)
	{
		String[] words = { "根", "我", "爱", "自然", "语言", "处理" };
		String[] pos = { "0", "1", "2", "3", "4", "5" };
		LinkedList<Vertice> buffer = Vertice.getWordsBuffer(words, pos);
		ArrayDeque<Vertice> stack = new ArrayDeque<Vertice>();
		Configuration conf = new Configuration(stack, buffer, new ArrayList<Arc>());
		System.out.println(conf.toString());
		conf.shift();
		System.out.println(conf.toString());
		conf.reduce();
		System.out.println(conf.toString());
	}
}