package com.lc.nlp4han.constituent;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import com.lc.nlp4han.ml.util.FileInputStreamFactory;

/**
 * 句法树预处理运行类
 * 
 * 去掉空节点和功能标记
 * 
 * @author 刘小峰
 * @author 王馨苇
 *
 */
public class TreePreprocessTool
{

	/**
	 * 预处理
	 * 
	 * @param frompath
	 *            要进行处理的文档路径
	 * @param topath
	 *            预处理之后的文档路径
	 * @param haveWordIndex
	 *            是否加入词的下标
	 * @throws IOException
	 */
	public static void preProcess(String frompath, String topath, boolean haveWordIndex) throws IOException
	{
		// 读取一颗树
		PlainTextByTreeStream lineStream = new PlainTextByTreeStream(new FileInputStreamFactory(new File(frompath)),
				"utf8");
		// 创建输出流
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(topath)));
		String tree = "";
		while ((tree = lineStream.read()) != "")
		{
			TreeNode node = BracketExpUtil.generateTree(tree);
			
			// 对树进行遍历
			deleteNone(node);

			String newTreeStr;
			if (haveWordIndex == true)
			{
				newTreeStr = node.toStringWordIndexNoNone();
			}
			else
			{
				newTreeStr = node.toStringNoNone();
			}
			
			TreeNode newTree = BracketExpUtil.generateTree("(" + newTreeStr + ")");
			bw.write("(" + TreeNode.printTree(newTree, 1) + ")");
			bw.newLine();
		}
		
		bw.close();
		lineStream.close();
	}

	/**
	 * 对树进行遍历删除NONE【这里的删除是将属性flag设置为false】
	 * 
	 * @param node
	 *            一棵树
	 */
	public static void deleteNone(TreeNode node)
	{
		if (node.getChildrenNum() != 0)
		{
			for (TreeNode treenode : node.getChildren())
			{
				deleteNone(treenode);
			}
		}
		if (!node.isLeaf())
		{
			if (node.getNodeName().contains("NONE"))
			{
				// 该节点的父节点只有空节点一个孩子
				if (node.getParent().getChildrenNum() > 1)
				{
					// 将NONE和NONE的子节点标记位false
					node.setFlag(false);
					node.getFirstChild().setFlag(false);
					// (SBAR(-NONE- 0)(S(-NONE- *T*-1)))
					if (node.getParent().getChildrenNum() == 2)
					{
						node.getParent().setFlag(false);
						if (node.getParent().getChild(1).getChildrenNum() == 1)
						{
							if (node.getParent().getChild(1).getFirstChildName().contains("NONE"))
							{
								node.getParent().getChild(1).setFlag(false);
								node.getParent().getChild(1).getFirstChild().setFlag(false);
								node.getParent().getChild(1).getFirstChild().getFirstChild().setFlag(false);
								// (VP (VBD reported) (SBAR (-NONE- 0) (S (-NONE- *T*-1) )))变为(VBD reported)
								if (node.getParent().getParent().getChildrenNum() == 2)
								{
									node.getParent().getParent().setFlag(false);
								}
							}
						}
					}
				}
				else if (node.getParent().getChildrenNum() == 1)
				{
					// 将NONE和NONE的子节点和父节点标记位false
					node.setFlag(false);
					node.getFirstChild().setFlag(false);
					node.getParent().setFlag(false);
					// (S(NP(-NONE- *-1))(VP(To to)(VP ....)))
					if (node.getParent().getParent().getChildrenNum() == 2)
					{
						node.getParent().getParent().setFlag(false);
					}
				}
			}
			else if (node.getNodeName().contains("-"))
			{
				if (!node.getNodeName().equals("-LRB-") && !(node.getNodeName().equals("-RRB-")))
				{
					node.setNewName(node.getNodeName().split("-")[0]);
				}
			}
			else if (Character.isDigit(node.getNodeName().charAt(node.getNodeName().length() - 1)))
			{
				if (Character.isDigit(node.getNodeName().charAt(node.getNodeName().length() - 2)))
				{
					node.setNewName(node.getNodeName().substring(0, node.getNodeName().length() - 3));
				}
				else
				{
					node.setNewName(node.getNodeName().substring(0, node.getNodeName().length() - 2));
				}
			}
		}
	}

	// havewordindex用yes no设定
	private static void usage()
	{
		System.out.println(TreePreprocessTool.class.getName()
				+ "-frompath <fromPath> -topath <toPath> -havewordindex <haveWordIndex>");
	}

	public static void main(String[] args) throws IOException
	{
		/// TODO: 语料编码作为命令行参数

		if (args.length < 1)
		{
			usage();
			return;
		}

		String frompath = null;
		String topath = null;
		String havewordindex = null;
		for (int i = 0; i < args.length; i++)
		{
			if (args[i].equals("-frompath"))
			{
				frompath = args[i + 1];
				i++;
			}
			if (args[i].equals("-topath"))
			{
				topath = args[i + 1];
				i++;
			}
			if (args[i].equals("-havewordindex"))
			{
				// TODO:  开关量参数不需要带参数值
				havewordindex = args[i + 1];
				i++;
			}
		}

		if (havewordindex.equals("yes"))
		{
			preProcess(frompath, topath, true);
		}
		else
		{
			preProcess(frompath, topath, false);
		}

		System.out.println("success");
	}
}
