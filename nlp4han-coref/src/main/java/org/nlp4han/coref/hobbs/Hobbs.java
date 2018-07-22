package org.nlp4han.coref.hobbs;

import java.util.LinkedList;
import java.util.List;

import com.lc.nlp4han.constituent.TreeNode;

/**
 * @author 杨智超
 *
 */
public class Hobbs
{
	
	
	private Filter filter;

	public Hobbs(Filter filter)
	{
		this.filter = filter;
	}

	public TreeNode hobbs(List<TreeNode> constituentTrees, TreeNode pronoun)
	{
		TreeNode x;
		Path path = new Path();
		
		TreeNode tmp;
		List<TreeNode> candidateNodes;
		int index = constituentTrees.size() - 1;

		tmp = getFirstNPNodeUp(pronoun);
		x = getFirstNPNodeUp(tmp);
		path.getPath(x, tmp);
		candidateNodes = getNPNodeOnLeftOfPath(x, path);
		filter.setUp(candidateNodes);
		filter.filtering();

		if (!candidateNodes.isEmpty())
		{ // 若存在NP结点，并且在x和该候选结点间存在NP或IP结点，则返回该结点
			candidateNodes = existNpOrIPNodeBetween2Nodes(candidateNodes, x);
			if (!candidateNodes.isEmpty())
				return candidateNodes.get(0);
		}
		while (index > 0)
		{
			if (x.getParent() == null)
			{// 若结点x为最顶层IP结点，则在最近的前树中查找候选结点
				index--;
				x = constituentTrees.get(index);
				candidateNodes = getNPNodes(x);
				filter.setUp(candidateNodes);
				filter.filtering();
				if (!candidateNodes.isEmpty())
					return candidateNodes.get(0);
			}
			else
			{
				tmp = getFirstNPOrIPNodeUp(x);
				path.getPath(x, tmp);
				x = tmp;
				if (isNPNode(x) && !dominateNNode(x, path))
				{// 若结点x为NP结点，且path没有穿过x直接支配的Nominal结点,则返回x
					return x;
				}
				candidateNodes = getNPNodeOnLeftOfPath(x, path);
				filter.setUp(candidateNodes);
				filter.filtering();
				if (!candidateNodes.isEmpty())
					return candidateNodes.get(0);
				if (isIPNode(x))
				{
					candidateNodes = getNPNodeOnRightOfPath(x, path);
					filter.setUp(candidateNodes);
					filter.filtering();
					if (!candidateNodes.isEmpty())
						return candidateNodes.get(0);
				}
			}
		}
		return null;
	}

	/**
	 * 在根结点rootNode下，路径path右侧，从左至右，广度优先搜索NP结点。但不遍历低于任何遇到的NP或IP结点的分支。
	 * @param rootNode
	 * @param path
	 * @return
	 */
	private List<TreeNode> getNPNodeOnRightOfPath(TreeNode rootNode, Path path)
	{
		List<TreeNode> result = new LinkedList<TreeNode>();
		List<TreeNode> candidates = TreeNodeUtil.getNodesWithSpecifiedNameOnLeftOrRightOfPath(rootNode, path, "right", new String[] {"NP"});
		boolean flag = false;
		for (TreeNode treeNode: candidates)
		{
			List<TreeNode> tmp = TreeNodeUtil.getNodesUpWithSpecifiedName(treeNode, new String[] {"NP"});
			for (int i=0 ; i<tmp.size() ; i++)
			{
				if (TreeNodeUtil.isNodeWithSpecifiedName(tmp.get(i), new String[] {"NP", "IP"}) && tmp.get(i).getParent() != null)
				{
					flag = true;
					break;
				}
			}
			if (flag)
			{
				flag = false;
				break;
			}
			result.add(treeNode);
		}
		return result;
	}

	/**
	 * 获得根节点rootNode下的所有NP结点
	 * @param rootNode 根结点
	 * @return 根节点rootNode下的所有NP结点
	 */
	private List<TreeNode> getNPNodes(TreeNode rootNode)
	{
		return TreeNodeUtil.getNodesWithSpecified(rootNode, new String[] {"NP"});
	}

	/**
	 * 获得candidateNodes中与结点treeNode间存在NP或IP结点的结点
	 * @param candidateNodes 
	 * @param treeNode
	 * @return
	 */
	private List<TreeNode> existNpOrIPNodeBetween2Nodes(List<TreeNode> candidateNodes, TreeNode treeNode)
	{
		List<TreeNode> result = new LinkedList<TreeNode>();
		for (int i=0 ; i<candidateNodes.size() ; i++)
		{
			TreeNode tmp;
			tmp = candidateNodes.get(i);
			if (TreeNodeUtil.getNodesWithSpecifiedNameBetween2Nodes(tmp, treeNode, new String[] {"NP", "IP"}).size() > 0)
			{
				result.add(tmp);
			}
		}
		return result;
	}

	
	/**
	 * 根结点rootNode下，路径path左侧，从左至右，广度优先遍历得到的所有NP结点
	 * @param rootNode
	 * @param path
	 * @return
	 */
	private List<TreeNode> getNPNodeOnLeftOfPath(TreeNode rootNode, Path path)
	{
		List<TreeNode> result = TreeNodeUtil.getNodesWithSpecifiedNameOnLeftOrRightOfPath(rootNode, path, "Left", new String[] {"NP"});
		return result;
	}

	/**
	 * 路径path是否穿过结点treeNode直接支配的一个Nominal结点
	 * 
	 * @param treeNode
	 * @param path
	 * @return 若路径path穿过结点，返回true；否则，返回false
	 */
	private boolean dominateNNode(TreeNode treeNode, Path path)
	{
		List<TreeNode> candidates = TreeNodeUtil.getChildNodeWithSpecifiedName(treeNode, new String[] {"NP"});
		for (TreeNode tn: candidates)
		{
			if (path.contains(tn))
				return true;
		}
		return false;
	}

	/**
	 * 结点treeNode是否为NP结点
	 * 
	 * @param treeNode
	 *            被验证的结点
	 * @return 若存在，若该节点是NP结点，返回true；否则，返回false
	 */
	private boolean isNPNode(TreeNode treeNode)
	{
		return TreeNodeUtil.isNodeWithSpecifiedName(treeNode, new String[] { "NP" });
	}

	/**
	 * 结点treeNode是否为IP结点
	 * 
	 * @param treeNode
	 *            被验证的结点
	 * @return 若该节点是IP结点，返回true；否则，返回false
	 */
	private boolean isIPNode(TreeNode treeNode)
	{
		return TreeNodeUtil.isNodeWithSpecifiedName(treeNode, new String[] { "IP" });
	}

	/**
	 * 从treeNode开始，向上遍历，找到第一个NP或IP结点
	 * 
	 * @param treeNode
	 * @return 若存在，返回符合的NP结点；若不存在，则返回null
	 */
	private TreeNode getFirstNPOrIPNodeUp(TreeNode treeNode)
	{
		return TreeNodeUtil.getFirstNodeUpWithSpecifiedName(treeNode, new String[] { "NP", "IP" });
	}

	/**
	 * 从treeNode开始，向上遍历，找到第一个NP结点
	 * 
	 * @param treeNode
	 *            起始结点
	 * @return 若存在，返回符合的NP结点；若不存在，则返回null
	 */
	private TreeNode getFirstNPNodeUp(TreeNode treeNode)
	{
		return TreeNodeUtil.getFirstNodeUpWithSpecifiedName(treeNode, "NP");
	}
	
}
