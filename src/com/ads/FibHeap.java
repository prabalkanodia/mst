package com.ads;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: prabal
 * Date: 10/21/13
 * Time: 11:33 AM
 * To change this template use File | Settings | File Templates.
 */
public class FibHeap<T> {

    public static final class FibNode<T> {
        // Number of children
        private int degree;

        // true if node has lost a child
        private boolean isChildCut;

        // Parent
        private FibNode<T> parent;
        // Child
        private FibNode<T> child;

        // Previous and Next references to maintain doubly linked circular list
        private FibNode<T> next;
        private FibNode<T> prev;

        // Item
        private T item;

        // Priority
        private int priority;

        private FibNode(T item, int priority) {
            this.degree = 0;
            this.isChildCut = false;
            this.parent = null;
            this.child = null;
            this.prev = this;
            this.next = this;
            this.item = item;
            this.priority = priority;
        }

        public T getItem() {
            return item;
        }

        public void setItem(T item) {
            this.item = item;
        }

        public int getPriority() {
            return priority;
        }
    }

    private FibNode<T> min = null;

    private int nNodes = 0;

    public FibNode<T> getMin() {
        return min;
    }

    public int size() {
        return nNodes;
    }

    public boolean isEmpty() {
        return nNodes == 0;
    }

    public FibNode<T> insert(T item, int priority) {
        FibNode<T> node = new FibNode<T>(item, priority);

        // combine two trees
        min = combineTrees(min, node);

        nNodes++;

        return node;
    }

    public FibNode<T> removeMin() throws Exception {
        if (isEmpty()) throw new Exception("Heap empty");

        FibNode<T> top = min;

        // only root at top level list
        if (min.next == min) {
            min = null;
        } else {
            min.prev.next = min.next;
            min.next.prev = min.prev;
            min = min.next;
        }

        if (top.child != null) {
            FibNode<T> visited = top.child;
            do {
                visited.parent = null;
                visited = visited.next;
            } while (visited != top.child);
        }

        // Find new min
        min = combineTrees(min, top.child);

        // If there was only one element in the heap initially, return the only element
        if (min == null) return top;

        // consolidate the trees of equal degree
        List<FibNode<T>> trees = new ArrayList<FibNode<T>>();
        List<FibNode<T>> marked = new ArrayList<FibNode<T>>();

        FibNode<T> currNode = min;
        marked.add(currNode);
        currNode = currNode.next;
        while (marked.get(0) != currNode) {
            marked.add(currNode);
            currNode = currNode.next;
        }
        // Ensure, currNode must be pointing to  min


        // Perform Union of trees
        for (FibNode<T> curr: marked) {
            while (true) {
                while (currNode.degree >= trees.size())
                    trees.add(null);

                // nothing to combine with; process next node
                if (trees.get(currNode.degree) == null) {
                    trees.set(currNode.degree, currNode);
                    break;
                }
                // else

                FibNode<T> that = trees.get(curr.degree);
                // clear the slot
                trees.set(curr.degree, null);

                // Determine the trees with lesser priority
                FibNode<T> min;
                FibNode<T> max;
                if (that.priority < curr.priority) {
                    min = that;
                    max = curr;
                } else {
                    min = curr;
                    max = that;
                }

                // Remove max item from top level list and combine with min child
                max.prev.next = max.next;
                max.next.prev = max.prev;

                max.prev = max;
                max.next = max;

                min.child = combineTrees(min.child, max);

                max.parent = min;
                max.isChildCut = false;

                min.degree++;

                curr = min;
            }

            // reset the min reference to root level min (<=)
            if (curr.priority <= this.min.priority)
                this.min = curr;
        }

        return min;
    }

    public void remove(FibNode<T> node) throws Exception {
       decreaseKey(node, -1);
       removeMin();
    }

    public void decreaseKey(FibNode<T> node, int newPriority) {
        node.priority = newPriority;

        if (node.parent != null && node.priority <= node.parent.priority)
            cascadingCut(node);

        if (node.priority <= min.priority)
            min = node;
    }

    public FibNode<T> combineTrees(FibNode<T> ths, FibNode<T> tht) {
        if (ths == null && tht == null) return null;
        if (ths != null && tht == null) return ths;
        if (ths == null && tht != null) return tht;

        FibNode<T> tmp = ths.next;

        ths.next = tht.next;
        ths.next.prev = ths;

        tht.next = tmp;
        tht.next.prev = tht;

        return ths.priority < tht.priority ? ths : tht;
    }

    public void cascadingCut(FibNode<T> node) {
        node.isChildCut = false;

        if (node.parent == null) return;

        // reset the siblings
        if (node.next != node) {
            node.prev.next = node.next;
            node.next.prev = node.prev;
        }

        // reset the parent's child ref
        if (node.parent.child == node) {
            node.parent.child = (node.next != node) ? node.next : null;
        }
        // update degree of parent
        node.parent.degree--;

        // reset the child sibling ref
        node.next = node;
        node.prev = node;

        // combine trees
        min = combineTrees(min, node);

        // cascading cut
        if (node.parent.isChildCut)
            cascadingCut(node.parent);
        else
            node.parent.isChildCut = true;

        // node is in top level list, so clear parent ref
        node.parent = null;
    }
}
