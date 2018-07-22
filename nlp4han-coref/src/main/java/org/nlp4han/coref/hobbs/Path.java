package org.nlp4han.coref.hobbs;

import java.util.LinkedList;
import java.util.List;

import org.nlp4han.coref.hobbs.Path.Direction;

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

	public Direction getDirection()
	{
		return dir;
	}

	public TreeNode getFirstNode()
	{
		return path.get(0);
	}

	public TreeNode getLastNode()
	{
		return path.get(path.size() - 1);
	}

	public List<TreeNode> getPathList()
	{
		return path;
	}
	
	public boolean contains(TreeNode treeNode)
	{
		return this.path.contains(treeNode);
	}
	
	public int indexOf(TreeNode treeNode)
	{
		return this.path.indexOf(treeNode);
	}

	public void addPathNode(TreeNode pathNode)
	{
		path.add(pathNode);
	}

	public TreeNode get(int index)
	{
		return path.get(index);
	}

	public boolean isEmpty()
	{
		return path.isEmpty();
	}

	public int size()
	{
		return path.size();
	}
	
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
				index = path.indexOf(treeNode)+1;
				if (index > 0 && index < path.size())
				{
					result = path.get(index);
					return result;
				}
			}
			else if (dir == Direction.UP)
			{
				index = path.indexOf(treeNode)-1;
				if (index > 0 && index < path.size())
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
		return " [" + path + "]";
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
