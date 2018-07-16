package org.nlp4han.coref.hobbs;



import com.lc.nlp4han.constituent.TreeNode;

/**
 * @author 杨智超
 *
 */
public class TreeNodeUtil {

    /**
     * 从treeNode开始，向上遍历，找到第一个NP结点
     * @param treeNode
     * @return 若存在，返回符合的NP结点；若不存在，则返回null
     */
    public static TreeNode getFistNPNodeUp(TreeNode treeNode) {
	// TODO Auto-generated method stub
	return null;
    }

    /**
     * 在结点sonNode和其祖先结点ancestorNode之间，是否存在NP或IP结点
     * @param sonNode 孙子结点
     * @param ancestorNode 祖先结点
     * @return 若存在NP或IP结点，返回true；否则，返回false
     */
    public static boolean isExistedNPOrIPNode(TreeNode sonNode, TreeNode ancestorNode) {
	// TODO Auto-generated method stub
	return false;
    }

    /**
     * 从treeNode开始，向上遍历，找到第一个NP或IP结点
     * @param treeNode
     * @return 若存在，返回符合的NP结点；若不存在，则返回null
     */
    public static TreeNode getFistNPOrIPNodeUp(TreeNode treeNode) {
	// TODO Auto-generated method stub
	return null;
    }

    /**
     * 结点treeNode是否为NP结点
     * @param treeNode 被验证的结点
     * @return 若存在，若该节点是NP结点，返回true；否则，返回false
     */
    public static boolean isNPNode(TreeNode treeNode) {
	// TODO Auto-generated method stub
	return false;
    }

    /**
     * 路径path是否穿过结点treeNode直接支配的一个Nominal结点
     * @param treeNode 
     * @param path
     * @return 若路径path穿过结点，返回true；否则，返回false
     */
    public static boolean dominateNNode(TreeNode treeNode, Path path) {
	// TODO Auto-generated method stub
	return false;
    }

    /**
     * 结点treeNode是否为IP结点
     * @param treeNode 被验证的结点
     * @return 若该节点是IP结点，返回true；否则，返回false
     */
    public static boolean isIPNode(TreeNode treeNode) {
	// TODO Auto-generated method stub
	return false;
    }
    
}
