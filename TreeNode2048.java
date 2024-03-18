import java.util.ArrayList;
import java.util.List;

public class TreeNode2048<T> {
  private List<TreeNode2048<T>> children = new ArrayList<>();
  private TreeNode2048<T> parent = null;
  private T data = null;

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

  public TreeNode2048(
      List<TreeNode2048<T>> children, TreeNode2048<T> parent, T data, boolean isChanceNode) {
    this.children = children;
    this.parent = parent;
    this.data = data;
    this.isChanceNode = isChanceNode;
  }

  private boolean isChanceNode = false;

  public TreeNode2048(T data) {
    this.data = data;
  }

  public TreeNode2048(T data, TreeNode2048<T> parent) {
    this.data = data;
    this.parent = parent;
  }

  public List<TreeNode2048<T>> getChildren() {
    return children;
  }

  public void setParent(TreeNode2048<T> parent) {
    parent.addChild(this);
    this.parent = parent;
  }

  public void addChild(T data) {
    TreeNode2048<T> child = new TreeNode2048<T>(data);
    child.setParent(this);
    this.children.add(child);
  }

  public void addChild(TreeNode2048<T> child) {
    child.setParent(this);
    this.children.add(child);
  }

  public T getData() {
    return this.data;
  }

  // write getParent method
  public TreeNode2048<T> getParent() {
    return this.parent;
  }

  public void setData(T data) {
    this.data = data;
  }

  public boolean isRoot() {
    return (this.parent == null);
  }

  public boolean isChanceNode() {
    return (this.isChanceNode);
  }

  public void setChanceNode(boolean isChanceNode) {
    this.isChanceNode = isChanceNode;
  }
}
