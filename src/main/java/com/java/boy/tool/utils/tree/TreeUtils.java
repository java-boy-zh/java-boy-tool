package com.java.boy.tool.utils.tree;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author 王青玄
 * @Contact 1121586359@qq.com
 * @create 2024年12月30日 14:53
 * @Description 树形生成器工具类
 * @Version V1.0
 */
public class TreeUtils {

    public static <T extends TreeNode> List<T> buildTree(List<T> nodes) {

        if (CollectionUtils.isEmpty(nodes)) {
            return Collections.emptyList();
        }
        Map<Long, List<TreeNode>> groups = nodes.stream().collect(Collectors.groupingBy(TreeNode::getNodePId));
        return nodes.stream().filter(Objects::nonNull).peek(pnd -> {
            List<TreeNode> ts = groups.get(pnd.getNodeId());
            pnd.setChildren(ts);
        }).filter(TreeNode::getRootNode).collect(Collectors.toList());

    }

    public static <T extends TreeNode> void findAll(List<T> result, TreeNode node, Long targetId) {

        if (node.getNodeId().equals(targetId) || node.getNodePId().equals(targetId)) {
            addAll(result, node);
        } else {
            if (!CollectionUtils.isEmpty(node.getChildren())) {
                for (TreeNode child : node.getChildren()) {
                    findAll(result, child, targetId);
                }
            }
        }

    }


    private static <T extends TreeNode> void addAll(List<T> result, TreeNode node) {
        result.add((T) node);
        if (!CollectionUtils.isEmpty(node.getChildren())) {
            for (TreeNode child : node.getChildren()) {
                addAll(result, child);
            }
        }
    }

}
