package com.ads;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Fibonacci Heap
 * User: prabal
 * Date: 10/21/13
 * Time: 11:33 AM
 */
public final class FibHeap<T> {

    /**
     * Fibonacci Heap Node
     * @param <T>
     */
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
        private double priority;

        private FibNode(T item, double priority) {
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

        public double getPriority() {
            return priority;
        }
    }

    /* Pointer to the minimum element in the heap. */
    private FibNode<T> min = null;

    /* Cached size of the heap, so we don't have to recompute this explicitly. */
    private int nNodes = 0;

    /**
     * Insert / Enqueue
     * @param item
     * @param priority
     * @return
     */
    public FibNode<T> insert(T item, double priority) {
        checkPriority(priority);

        FibNode<T> result = new FibNode<T>(item, priority);
        // combine trees and update min
        this.min = combineTrees(this.min, result);

        ++nNodes;
        return result;
    }

    /**
     * return min ref
     * @return
     */
    public FibNode<T> min() {
        if (isEmpty())
            throw new NoSuchElementException("Heap is empty.");
        return this.min;
    }


    /**
     * check if heap is empty
     * @return true if heap is empty else false
     */
    public boolean isEmpty() {
        return this.min == null;
    }

    /**
     * nElements in heap
     * @return no elements in heap
     */
    public int size() {
        return nNodes;
    }

    /**
     * Remove min item from fibheap
     * @return The min element of the Fibonacci heap.
     * @throws NoSuchElementException If the heap is empty.
     */
    public FibNode<T> removeMin() throws NoSuchElementException {
        /* Check for whether we're empty. */
        if (isEmpty())
            throw new NoSuchElementException("Heap empty!");

        // decrement nNodes since we are removing item
        --nNodes;

        /* Grab the minimum element so we know what to return. */
        FibNode<T> minItem = this.min;

        // only root at top level list
        if (this.min.next == this.min) {
            this.min = null;
        }
        else {
            this.min.prev.next = this.min.next;
            this.min.next.prev = this.min.prev;
            this.min = this.min.next;
        }

        // Update child-parent references to null
        if (minItem.child != null) {
            FibNode<?> curr = minItem.child;
            do {
                curr.parent = null;
                curr = curr.next;
            } while (curr != minItem.child);
        }

        // Find new min
        this.min = combineTrees(this.min, minItem.child);

        // If there was only one element in the heap initially, return the only element
        if (this.min == null) return minItem;

        // consolidate the trees of equal degree
        List<FibNode<T>> trees = new ArrayList<FibNode<T>>();
        List<FibNode<T>> marked = new ArrayList<FibNode<T>>();

//        for (FibNode<T> curr = this.min; marked.isEmpty() || marked.get(0) != curr; curr = curr.next)
//            marked.add(curr);
        FibNode<T> currNode = min;
        marked.add(currNode);
        currNode = currNode.next;
        while (marked.get(0) != currNode) {
            marked.add(currNode);
            currNode = currNode.next;
        }

        // Perform union of trees
        for (FibNode<T> curr: marked) {
            /* Keep merging until a match arises. */
            while (true) {

                while (curr.degree >= trees.size())
                    trees.add(null);

                // nothing to combine with; process next node
                if (trees.get(curr.degree) == null) {
                    trees.set(curr.degree, curr);
                    break;
                }

                FibNode<T> that = trees.get(curr.degree);
                // clear the bucket
                trees.set(curr.degree, null);

//                // Determine the trees with lesser priority
//                FibNode<T> min = (that.priority < curr.priority)? that : curr;
//                FibNode<T> max = (that.priority < curr.priority)? curr  : that;

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
                max.next.prev = max.prev;
                max.prev.next = max.next;

                max.next = max;
                max.prev = max;

                min.child = combineTrees(min.child, max);

                max.parent = min;
                max.isChildCut = false;

                min.degree++;

                curr = min;
            }

            // reset the min reference to root level min (<=)
            if (curr.priority <= this.min.priority) this.min = curr;
        }
        return minItem;
    }

    /**
     * Decrease item priority and reorganize fibheap
     * @param item
     * @param newPriority
     */
    public void decreaseKey(FibNode<T> item, double newPriority) {
        checkPriority(newPriority);
        if (newPriority > item.priority)
            throw new IllegalArgumentException("New priority exceeds old.");

        /* Forward this to a helper function. */
        decreaseKeyUnchecked(item, newPriority);
    }

    /**
     * Decrease item priority and reorganize fibheap
     * @param item
     * @param newPriority
     */
    private void decreaseKeyUnchecked(FibNode<T> item, double newPriority) {
        // update priority
        item.priority = newPriority;

        // cascading cut
        if (item.parent != null && item.priority <= item.parent.priority)
            cascadingCut(item);

        // update min
        if (item.priority <= this.min.priority)
            this.min = item;
    }

    /**
     * Remove Arbitrary node from fibheap
     * @param item
     */
    public void removeArbitrary(FibNode<T> item) {
        /* Use decreaseKey to drop the entry's key to -infinity.  This will
         * guarantee that the node is cut and set to the global minimum.
         */
        decreaseKeyUnchecked(item, Double.NEGATIVE_INFINITY);

        removeMin();
    }

    /**
     * check if priority field is valid
     * @param priority
     */
    private void checkPriority(double priority) {
        if (Double.isNaN(priority))
            throw new IllegalArgumentException(priority + " is invalid.");
    }

    /**
     * Combine two fib heap nodes
     * @param ths
     * @param tht
     * @param <T>
     * @return
     */
    private static <T> FibNode<T> combineTrees(FibNode<T> ths, FibNode<T> tht) {

        if (ths == null && tht == null) return null;
        if (ths != null && tht == null) return ths;
        if (ths == null && tht != null) return tht;


        FibNode<T> tmp = ths.next; // Cache this since we're about to overwrite it.
        ths.next = tht.next;
        ths.next.prev = ths;
        tht.next = tmp;
        tht.next.prev = tht;

        return ths.priority < tht.priority? ths : tht;

    }



    /**
     * Cuts a node from its parent.  If the parent was already marked, recursively
     * cuts that node from its parent as well.
     *
     * @param node The node to cut from its parent.
     */
    private void cascadingCut(FibNode<T> node) {
        // mark the node
        node.isChildCut = false;

        if (node.parent == null) return;

        // reset the siblings
        if (node.next != node) {
            node.next.prev = node.prev;
            node.prev.next = node.next;
        }

        // reset the parent's child ref
        if (node.parent.child == node) {
            node.parent.child = (node.next != node) ? node.next : null;
        }

        // update degree of parent
        --node.parent.degree;

        // reset the child sibling ref
        node.prev = node;
        node.next = node;

        // combine trees
        this.min = combineTrees(this.min, node);

        // cascading cut
        if (node.parent.isChildCut)
            cascadingCut(node.parent);
        else
            node.parent.isChildCut = true;

        /* Clear the relocated node's parent; it's now a root. */
        node.parent = null;
    }
}
