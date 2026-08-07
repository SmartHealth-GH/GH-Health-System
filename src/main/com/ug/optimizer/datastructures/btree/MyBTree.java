package main.com.ug.optimizer.datastructures.btree;

public class MyBTree {

    private BTreeNode root;
    private int order;
    private int size;

    public MyBTree(int order) {
        this.order = order;
        this.root = new BTreeNode(order, true);
        this.size = 0;
    }

    public BTreeNode getRoot() {
        return root;
    }

    public int getOrder() {
        return order;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void insert(int key) {
    }

    public boolean search(int key) {
        return false;
    }

    public void delete(int key) {
    }

    public void traverse() {
    }
}
