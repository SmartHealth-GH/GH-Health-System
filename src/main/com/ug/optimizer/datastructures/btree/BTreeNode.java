package main.com.ug.optimizer.datastructures.btree;

public class BTreeNode {

    private final int order;
    private final int t;
    private final int[] keys;
    private final BTreeNode[] children;
    private int numKeys;
    private boolean leaf;

    public BTreeNode(int order, boolean leaf) {
        this.order = order;
        this.t = order / 2;
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

    public void traverse() {
        int i;
        for (i = 0; i < numKeys; i++) {
            if (!leaf) {
                children[i].traverse();
            }
            System.out.print(keys[i] + " ");
        }
        if (!leaf) {
            children[i].traverse();
        }
    }

    public BTreeNode search(int key) {
        int i = 0;
        while (i < numKeys && key > keys[i]) {
            i++;
        }
        if (i < numKeys && keys[i] == key) {
            return this;
        }
        if (leaf) {
            return null;
        }
        return children[i].search(key);
    }

    public void insertNonFull(int key) {
        int i = numKeys - 1;
        if (leaf) {
            while (i >= 0 && keys[i] > key) {
                keys[i + 1] = keys[i];
                i--;
            }
            keys[i + 1] = key;
            numKeys++;
        } else {
            while (i >= 0 && keys[i] > key) {
                i--;
            }
            i++;
            if (children[i].numKeys == 2 * t - 1) {
                splitChild(i, children[i]);
                if (keys[i] < key) {
                    i++;
                }
            }
            children[i].insertNonFull(key);
        }
    }

    public void splitChild(int i, BTreeNode y) {
        BTreeNode z = new BTreeNode(order, y.leaf);
        z.numKeys = t - 1;
        for (int j = 0; j < t - 1; j++) {
            z.keys[j] = y.keys[j + t];
        }
        if (!y.leaf) {
            for (int j = 0; j < t; j++) {
                z.children[j] = y.children[j + t];
                y.children[j + t] = null;
            }
        }
        y.numKeys = t - 1;
        for (int j = numKeys; j >= i + 1; j--) {
            children[j + 1] = children[j];
        }
        children[i + 1] = z;
        for (int j = numKeys - 1; j >= i; j--) {
            keys[j + 1] = keys[j];
        }
        keys[i] = y.keys[t - 1];
        numKeys++;
    }

    public int findKey(int key) {
        int idx = 0;
        while (idx < numKeys && keys[idx] < key) {
            idx++;
        }
        return idx;
    }

    public void remove(int key) {
        int idx = findKey(key);
        if (idx < numKeys && keys[idx] == key) {
            if (leaf) {
                removeFromLeaf(idx);
            } else {
                removeFromNonLeaf(idx);
            }
        } else {
            if (leaf) {
                return;
            }
            boolean atLastChild = (idx == numKeys);
            if (children[idx].numKeys < t) {
                fill(idx);
            }
            if (atLastChild && idx > numKeys) {
                children[idx - 1].remove(key);
            } else {
                children[idx].remove(key);
            }
        }
    }

    private void removeFromLeaf(int idx) {
        for (int i = idx + 1; i < numKeys; i++) {
            keys[i - 1] = keys[i];
        }
        numKeys--;
    }

    private void removeFromNonLeaf(int idx) {
        int key = keys[idx];
        if (children[idx].numKeys >= t) {
            int pred = getPred(idx);
            keys[idx] = pred;
            children[idx].remove(pred);
        } else if (children[idx + 1].numKeys >= t) {
            int succ = getSucc(idx);
            keys[idx] = succ;
            children[idx + 1].remove(succ);
        } else {
            merge(idx);
            children[idx].remove(key);
        }
    }

    private int getPred(int idx) {
        BTreeNode cur = children[idx];
        while (!cur.leaf) {
            cur = cur.children[cur.numKeys];
        }
        return cur.keys[cur.numKeys - 1];
    }

    private int getSucc(int idx) {
        BTreeNode cur = children[idx + 1];
        while (!cur.leaf) {
            cur = cur.children[0];
        }
        return cur.keys[0];
    }

    private void fill(int idx) {
        if (idx != 0 && children[idx - 1].numKeys >= t) {
            borrowFromPrev(idx);
        } else if (idx != numKeys && children[idx + 1].numKeys >= t) {
            borrowFromNext(idx);
        } else {
            if (idx != numKeys) {
                merge(idx);
            } else {
                merge(idx - 1);
            }
        }
    }

    private void borrowFromPrev(int idx) {
        BTreeNode child = children[idx];
        BTreeNode sibling = children[idx - 1];
        for (int i = child.numKeys - 1; i >= 0; i--) {
            child.keys[i + 1] = child.keys[i];
        }
        if (!child.leaf) {
            for (int i = child.numKeys; i >= 0; i--) {
                child.children[i + 1] = child.children[i];
            }
        }
        child.keys[0] = keys[idx - 1];
        if (!child.leaf) {
            child.children[0] = sibling.children[sibling.numKeys];
        }
        keys[idx - 1] = sibling.keys[sibling.numKeys - 1];
        child.numKeys++;
        sibling.numKeys--;
    }

    private void borrowFromNext(int idx) {
        BTreeNode child = children[idx];
        BTreeNode sibling = children[idx + 1];
        child.keys[child.numKeys] = keys[idx];
        if (!child.leaf) {
            child.children[child.numKeys + 1] = sibling.children[0];
        }
        keys[idx] = sibling.keys[0];
        for (int i = 1; i < sibling.numKeys; i++) {
            sibling.keys[i - 1] = sibling.keys[i];
        }
        if (!sibling.leaf) {
            for (int i = 1; i <= sibling.numKeys; i++) {
                sibling.children[i - 1] = sibling.children[i];
            }
        }
        child.numKeys++;
        sibling.numKeys--;
    }

    private void merge(int idx) {
        BTreeNode child = children[idx];
        BTreeNode sibling = children[idx + 1];
        child.keys[t - 1] = keys[idx];
        for (int i = 0; i < sibling.numKeys; i++) {
            child.keys[i + t] = sibling.keys[i];
        }
        if (!child.leaf) {
            for (int i = 0; i <= sibling.numKeys; i++) {
                child.children[i + t] = sibling.children[i];
            }
        }
        for (int i = idx + 1; i < numKeys; i++) {
            keys[i - 1] = keys[i];
        }
        for (int i = idx + 2; i <= numKeys; i++) {
            children[i - 1] = children[i];
        }
        child.numKeys += sibling.numKeys + 1;
        numKeys--;
    }
}
