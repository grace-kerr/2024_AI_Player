import java.util.ArrayList;
import java.util.List;


public class TreeNode2048<T>{
    private T data = null;
    private List<TreeNode2048> children = new ArrayList<>();
    private TreeNode2048 parent = null;


    public boolean isRoot() {
        return (this.parent == null);
    }

    public void setChildren(List<TreeNode2048> children) {
        this.children = children;
    }

    public float getNodeChance() {
        return nodeChance;
    }

    public void setNodeChance(float nodeChance) {
        this.nodeChance = nodeChance;
    }

    public float getNodeUtil() {
        return nodeUtil;
    }

    public void setNodeUtil(float nodeUtil) {
        this.nodeUtil = nodeUtil;
    }

    private float nodeChance = 0;

    private float nodeUtil = 0;

    private boolean isChanceNode = false;



    public TreeNode2048(T data) {
        this.data = data;
    }

    public TreeNode2048(List<TreeNode2048> children, TreeNode2048<T> parent, T data, boolean isChanceNode) {
        this.children = children;
        this.parent = parent;
        this.data = data;
        this.isChanceNode = isChanceNode;
    }


    public void addChild(TreeNode2048 child) {
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(T data) {
        TreeNode2048<T> newChild = new TreeNode2048<>(data);
        this.addChild(newChild);
    }

    public void addChildren(List<TreeNode2048> children) {
        for(TreeNode2048 t : children) {
            t.setParent(this);
        }
        this.children.addAll(children);
    }

    public List<TreeNode2048> getChildren() {
        return children;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    private void setParent(TreeNode2048 parent) {
        this.parent = parent;
    }

    public TreeNode2048 getParent() {
        return parent;
    }

    public boolean isChanceNode() {
        return this.isChanceNode;
    }
}

//public class TreeNode2048<T> {
//    private List<TreeNode2048<T>> children = new ArrayList<>();
//    private TreeNode2048<T> parent = null;
//    private T data = null;
//
//    public float getNodeChance() {
//        return nodeChance;
//    }
//
//    public void setNodeChance(float nodeChance) {
//        this.nodeChance = nodeChance;
//    }
//
//    public float getNodeUtil() {
//        return nodeUtil;
//    }
//
//    public void setNodeUtil(float nodeUtil) {
//        this.nodeUtil = nodeUtil;
//    }
//
//    private float nodeChance = 0;
//    private float nodeUtil = 0;
//
//    public TreeNode2048(List<TreeNode2048<T>> children, TreeNode2048<T> parent, T data, boolean isChanceNode) {
//        this.children = children;
//        this.parent = parent;
//        this.data = data;
//        this.isChanceNode = isChanceNode;
//    }
//
//    private boolean isChanceNode = false;
//
//    public TreeNode2048(T data) {
//        this.data = data;
//    }
//
//    public TreeNode2048(T data, TreeNode2048<T> parent) {
//        this.data = data;
//        this.parent = parent;
//    }
//
//    public List<TreeNode2048<T>> getChildren() {
//        return children;
//    }
//
//    public void setParent(TreeNode2048<T> parent) {
//        parent.addChild(this);
//        this.parent = parent;
//    }
//
//    public void addChild(T data) {
//        TreeNode2048<T> child = new TreeNode2048<T>(data);
//        child.setParent(this);
//        this.children.add(child);
//    }
//
//    public void addChild(TreeNode2048<T> child) {
//        child.setParent(this);
//        this.children.add(child);
//    }
//
//    public T getData() {
//        return this.data;
//    }
//
//    public void setData(T data) {
//        this.data = data;
//    }
//
//    public boolean isRoot() {
//        return (this.parent == null);
//    }
//
//    public boolean isChanceNode() {
//        return (this.isChanceNode);
//    }
//
//    public void setChanceNode(boolean isChanceNode) {
//        this.isChanceNode = isChanceNode;
//    }
//}