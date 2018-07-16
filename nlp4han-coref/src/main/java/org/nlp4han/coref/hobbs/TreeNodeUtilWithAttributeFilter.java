package org.nlp4han.coref.hobbs;



import com.lc.nlp4han.constituent.TreeNode;

/**
 * @author 杨智超
 *
 */
public class TreeNodeUtilWithAttributeFilter {
    /**
     * 在根结点treeNode下，路径path左侧，从左至右，广度遍历，获取第一个能通过属性过滤器filter过滤的NP或IP结点
     * @param treeNode 根结点
     * @param path 规定路径
     * @param filter 属性过滤器
     * @return 若存在，返回符合的NP或IP结点；若不存在，则返回null
     */
    public static TreeNode getNPOrIPNodeOnLeftOfPathWithAttributeFilter(TreeNode treeNode, Path path, AttributeFilter filter)
    {
	// TODO Auto-generated method stub
	return null;
    }
    
    /**
     * 在根结点treeNode下，路径path左侧，从左至右，广度遍历，获取第一个能通过属性过滤器filter过滤的NP结点
     * @param treeNode 根结点
     * @param path 规定路径
     * @param filter 属性过滤器
     * @return 若存在，返回符合的NP结点；若不存在，则返回null
     */
    public static TreeNode getNPNodeOnLeftOfPathWithAttributeFilter(TreeNode treeNode, Path path, AttributeFilter filter) {
	// TODO Auto-generated method stub
	return null;
    }
    
    /**
     * 在根结点treeNode下，从左至右，广度遍历，获取第一个能通过属性过滤器filter过滤的NP结点
     * @param treeNode 根结点
     * @param filter 属性过滤器
     * @return 若存在，返回符合的NP结点；若不存在，则返回null
     */
    public static TreeNode getNPNodeWithAttributeFilter(TreeNode treeNode, AttributeFilter filter) {
	// TODO Auto-generated method stub
	return null;
    }
    
    /**
     * 在根结点treeNode下，路径path右侧，从左至右，广度遍历，
     * 获取一个能通过属性过滤器filter过滤的NP结点，该过程不搜索遇到的任何NP或IP结点之下的分支
     * @param treeNode 根结点
     * @param path 规定路径
     * @param filter 属性过滤器
     * @return 若存在，返回符合的NP结点；若不存在，则返回null
     */
    public static TreeNode getSpecialNPNodeOnRightOfPathWithAttributeFilter(TreeNode treeNode, Path path, AttributeFilter filter) {
	// TODO Auto-generated method stub
	return null;
    }
}
