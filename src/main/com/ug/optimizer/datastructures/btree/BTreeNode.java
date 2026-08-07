package main.com.ug.optimizer.datastructures.btree;

public class BTreeNode {

    private int order;
    private int[] keys;
    private BTreeNode[] children;
    private int numKeys;
    private boolean leaf;

    public BTreeNode(int order, boolean leaf) {
        this.order = order;
        this.keys = new int[order - 1];
        this.children = new BTreeNode[order];
        this.numKeys = 0;
        this.leaf = leaf;
    }

    public int getOrder() {
        return order;
    }

    public int[] getKeys() {
        return keys;
    }

    public BTreeNode[] getChildren() {
        return children;
    }

    public int getNumKeys() {
        return numKeys;
    }

    public void setNumKeys(int numKeys) {
        this.numKeys = numKeys;
    }

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }
}
