package main.com.ug.optimizer.datastructures.btree;

public class MyBTree {

    private BTreeNode root;
    private final int order;
    private final int t;
    private int size;

    public MyBTree(int order) {
        if (order < 4 || order % 2 != 0) {
            throw new IllegalArgumentException("order must be an even integer >= 4");
        }
        this.order = order;

        this.t = order / 2;
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
        BTreeNode r = root;
        if (r.getNumKeys() == 2 * t - 1) {
            BTreeNode s = new BTreeNode(order, false);
            s.getChildren()[0] = r;
            s.splitChild(0, r);
            int i = (s.getKeys()[0] < key) ? 1 : 0;
            s.getChildren()[i].insertNonFull(key);
            root = s;
        } else {
            r.insertNonFull(key);
        }
        size++;
    }

    public boolean search(int key) {
        return root.search(key) != null;
    }

    public void delete(int key) {
        if (!search(key)) {
            return;
        }
        root.remove(key);
        if (root.getNumKeys() == 0 && !root.isLeaf()) {
            root = root.getChildren()[0];
        }
        size--;
    }

    public void traverse() {
        if (root.getNumKeys() > 0) {
            root.traverse();
        }
        System.out.println();
    }
}