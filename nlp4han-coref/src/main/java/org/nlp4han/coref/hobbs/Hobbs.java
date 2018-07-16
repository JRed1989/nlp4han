package org.nlp4han.coref.hobbs;



import java.util.List;

import com.lc.nlp4han.constituent.TreeNode;

/**
 * @author 杨智超
 *
 */
public class Hobbs{

    private TreeNode x;
    private Path path;
    private AttributeFilter filter;

    public Hobbs(AttributeFilter filter) {
	this.filter = filter;
    }
    
    public TreeNode hobbs(List<TreeNode> constituentTrees, TreeNode pronoun) {
	TreeNode tmp;
	TreeNode candidateNode;
	int index = constituentTrees.size()-1;
	
	tmp = TreeNodeUtil.getFistNPNodeUp(pronoun);	
	x = TreeNodeUtil.getFistNPNodeUp(tmp);
	path = Path.getPath(x, tmp);
	candidateNode = TreeNodeUtilWithAttributeFilter.getNPNodeOnLeftOfPathWithAttributeFilter(x, path, filter);
	
	if (candidateNode != null && TreeNodeUtil.isExistedNPOrIPNode(candidateNode, x))
	{ //若存在NP结点，并且在x和该候选结点间存在NP或IP结点，则返回该结点
	    return candidateNode;
	}
	while (index > 0)
	{
	    if (x.getParent() == null)
	    {//若结点x为最顶层IP结点，则在最近的前树中查找候选结点
		index--;
		x = constituentTrees.get(index);
		candidateNode = TreeNodeUtilWithAttributeFilter.getNPNodeWithAttributeFilter(x, filter);
		if (candidateNode != null)
		    return candidateNode; 
	    }
	    else
	    {
		tmp = TreeNodeUtil.getFistNPOrIPNodeUp(x); 
		path = Path.getPath(x, tmp);
		x  = tmp;
		if (TreeNodeUtil.isNPNode(x) && !TreeNodeUtil.dominateNNode(x, path))
		{//若结点x为NP结点，且path没有穿过x直接支配的Nominal结点,则返回x
		    return x;
		}
		candidateNode = TreeNodeUtilWithAttributeFilter.getNPNodeOnLeftOfPathWithAttributeFilter(x, path, filter);
		if (candidateNode != null)
		    return candidateNode;
		if (TreeNodeUtil.isIPNode(x))
		{
		    candidateNode = TreeNodeUtilWithAttributeFilter.getSpecialNPNodeOnRightOfPathWithAttributeFilter(x, path, filter);
		    if (candidateNode != null)
			return candidateNode;
		}
	    }
	}
	return null;
    }
}
