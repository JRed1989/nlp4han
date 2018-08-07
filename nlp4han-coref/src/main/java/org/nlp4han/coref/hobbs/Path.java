package org.nlp4han.coref.hobbs;

import java.util.LinkedList;
import java.util.List;

import com.lc.nlp4han.constituent.TreeNode;

/**
 * 路径类
 * 
 * @author 杨智超
 *
 */
public class Path
{
	private List<TreeNode> path;
	private Direction dir = Direction.NONE;

	public enum Direction
	{
		UP, DOWN, NONE;
	}

	public Path()
	{
		path = new LinkedList<TreeNode>();
	}

	/**
	 * 获取路径方向
	 * 
	 * @return 路径方向
	 */
	public Direction getDirection()
	{
		return dir;
	}

	/**
	 * 获得路径上的第一个结点
	 * 
	 * @return 路径上的第一个结点
	 */
	public TreeNode getFirstNode()
	{
		return path.get(0);
	}

	/**
	 * 获得路径上的最后一个结点
	 * 
	 * @return 路径上的最后一个结点
	 */
	public TreeNode getLastNode()
	{
		return path.get(path.size() - 1);
	}

	/**
	 * 获取路径列表
	 * 
	 * @return 路径列表
	 */
	public List<TreeNode> getPathList()
	{
		return path;
	}

	/**
	 * 路径是否包含结点treeNode
	 * 
	 * @param treeNode
	 * @return
	 */
	public boolean contains(TreeNode treeNode)
	{
		return this.path.contains(treeNode);
	}

	/**
	 * 获取结点treeNode在路径上的位置
	 * 
	 * @param treeNode
	 * @return
	 */
	public int indexOf(TreeNode treeNode)
	{
		return this.path.indexOf(treeNode);
	}

	/**
	 * 在路径上添加路径结点
	 * 
	 * @param pathNode
	 */
	public void addPathNode(TreeNode pathNode)
	{
		path.add(pathNode);
	}

	/**
	 * 获取路径上指定位置的结点
	 * 
	 * @param index
	 * @return
	 */
	public TreeNode get(int index)
	{
		return path.get(index);
	}

	/**
	 * 路径是否为空
	 * 
	 * @return
	 */
	public boolean isEmpty()
	{
		return path.isEmpty();
	}

	/**
	 * 路径包含的结点数
	 * 
	 * @return
	 */
	public int size()
	{
		return path.size();
	}

	/**
	 * 在指定的位置上添加结点
	 * 
	 * @param index
	 * @param treeNode
	 */
	public void addPathNode(int index, TreeNode treeNode)
	{
		path.add(index, treeNode);
	}

	/**
	 * 获取从startNode到endNode间的路径
	 * 
	 * @param startNode
	 *            路径的起点
	 * @param endNode
	 *            路径的终点
	 * @return 返回startNode到endNode的路径
	 */
	public void getPath(TreeNode startNode, TreeNode endNode)
	{
		this.dir = getDirection(startNode, endNode);
		this.path.clear();
		if (dir != Direction.NONE)
		{

			TreeNode tmp;
			if (dir == Direction.UP)
			{
				this.addPathNode(startNode);
				tmp = startNode.getParent();
				while (tmp != endNode)
				{
					this.addPathNode(tmp);
					tmp = tmp.getParent();
				}
				this.addPathNode(tmp);
			}
			else if (dir == Direction.DOWN)
			{
				this.addPathNode(endNode);
				tmp = endNode.getParent();
				while (tmp != startNode)
				{
					this.addPathNode(0, tmp);
					tmp = tmp.getParent();
				}
				this.addPathNode(0, tmp);

			}
		}

	}

	/**
	 * 获取从startNode到endNode间的路径方向
	 * 
	 * @param startNode
	 * @param endNode
	 * @return
	 */
	public static Direction getDirection(TreeNode startNode, TreeNode endNode)
	{
		if (startNode != null && endNode != null)
		{
			TreeNode tmp = startNode.getParent();
			while (tmp != null)
			{
				if (tmp == endNode)
				{
					return Direction.UP;
				}
				tmp = tmp.getParent();
			}

			tmp = endNode.getParent();
			while (tmp != null)
			{
				if (tmp == startNode)
				{
					return Direction.DOWN;
				}
				tmp = tmp.getParent();
			}

		}
		return Direction.NONE;
	}

	/**
	 * 获得结点treeNode在路径中的孩子结点
	 * 
	 * @param treeNode
	 * @return
	 */
	public TreeNode getChileNodeInPath(TreeNode treeNode)
	{
		TreeNode result;
		if (this.contains(treeNode))
		{
			int index;
			if (dir == Direction.DOWN)
			{
				index = path.indexOf(treeNode) + 1;
				if (index >= 0 && index < path.size())
				{
					result = path.get(index);
					return result;
				}
			}
			else if (dir == Direction.UP)
			{
				index = path.indexOf(treeNode) - 1;
				if (index >= 0 && index < path.size())
				{
					result = path.get(index);
					return result;
				}
			}
			else if (dir == Direction.NONE)
			{
				return null;
			}

		}
		return null;
	}

	@Override
	public String toString()
	{
		String result = "";
		if (path.size() > 0)
		{
			result += path.get(0).getNodeName();
			for (int i = 1; i < path.size(); i++)
			{
				result += ", ";
				result += path.get(i).getNodeName();
			}
		}
		return " Path: [" + result + "]  Dir: " + dir;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Path other = (Path) obj;
		if (path == null)
		{
			if (other.path != null)
				return false;
		}
		else if (!path.equals(other.path))
			return false;
		return true;
	}
}
