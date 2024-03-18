import java.util.ArrayList;
import java.util.List;

public class TreeNode2048<T> {
  private List<TreeNode2048<T>> children = new ArrayList<>();
  private TreeNode2048<T> parent = null;
  private T data = null;
  private boolean isChanceNode = false;
  private float nodeChance = 0;
  private float nodeUtil = 0;

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

  public TreeNode2048<T> getParent() {
    return parent;
  }

  public void setParent(TreeNode2048<T> parent) {
    if (this.parent != parent) {
      parent.addChild(this);
      this.parent = parent;
    }
  }

  public void addChild(TreeNode2048<T> child) {
    if (!this.children.contains(child)) {
      child.setParent(this);
      this.children.add(child);
    }
  }

  public T getData() {
    return this.data;
  }

  public void setData(T data) {
    this.data = data;
  }

  public boolean isRoot() {
    return (this.parent == null);
  }

  public boolean isChanceNode() {
    return isChanceNode;
  }

  public void setChanceNode(boolean isChanceNode) {
    this.isChanceNode = isChanceNode;
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
}
