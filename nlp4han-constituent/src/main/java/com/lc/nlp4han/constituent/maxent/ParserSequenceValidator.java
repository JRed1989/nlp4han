package com.lc.nlp4han.constituent.maxent;

import java.util.List;

import com.lc.nlp4han.constituent.HeadTreeNode;


/**
 * 序列校验接口
 * @author 王馨苇
 *
 */
public interface ParserSequenceValidator {

	/**
	 * 检验chunk步的标记是否正确
	 * @param i 当前位置
	 * @param posTree pos步得到的树
	 * @param outcomes 当前位置之前的结果序列
	 * @param out 当前位置的结果
	 * @return
	 */
	boolean validSequenceForChunk(int i, List<HeadTreeNode> posTree, List<String> outcomes, String out);
	
	/**
	 * 检验build和check步骤的标记是否正确
	 * @param i 当前位置
	 * @param combineChunkTree 合并后chunk步的结果
	 * @param out 当前位置的结果
	 * @return
	 */
	boolean validSequenceForBuildAndCheck(int i, List<HeadTreeNode> combineChunkTree, String out );
}
