package ru.unn.agile.LeftistHeap;

import java.util.ArrayList;

public class LeftistHeap<Type extends Comparable<Type>> {
    private LeftistHeapNode<Type> root;
    private int size;

    LeftistHeap() {
        root = null;
        size = 0;
    }

    public Object[] toSortedArray() {
        ArrayList<Type> sortedList = new ArrayList<>();
        while (size > 0) {
            sortedList.add(extractMin());
        }

        return sortedList.toArray(new Object[sortedList.size()]);
    }

    public void clear() {
        root = null;
        size = 0;
    }

    public void delete(final LeftistHeapNode<Type> heapNode) {
        if (heapNode == null) {
            return;
        }

        if (heapNode.equals(root)) {
            extractMin();
            return;
        }

        cut(heapNode);
        size--;
        heapNode.clear();
    }

    public void decreaseKey(final LeftistHeapNode<Type> heapNode, final Type key) {
        if (key.compareTo(heapNode.getElement()) > 0) {
            return;
        }

        if (heapNode.equals(root)) {
            heapNode.setElement(key);
            return;
        }

        cut(heapNode);
        heapNode.setElement(key);
        root = merge(root, heapNode);
    }

    public boolean isEmpty() {
        return root == null;
    }

    public void merge(final LeftistHeap<Type> rhsHeap) {
        if (this == rhsHeap) {
            return;
        }

        root = merge(root, rhsHeap.getRoot());
        size += rhsHeap.getSize();
    }

    public void insert(final Type newElement) {
        root = merge(root, new LeftistHeapNode<>(newElement));
        size++;
    }

    public Type extractMin() {
        if (isEmpty()) {
            return null;
        }

        Type minElement = root.getElement();
        root = merge(root.getLeftChild(), root.getRightChild());
        size--;
        return minElement;
    }

    public final int getSize() {
        return size;
    }

    public final LeftistHeapNode<Type> getRoot() {
        return root;
    }

    private void cut(final LeftistHeapNode<Type> heapNode) {
        boolean left = heapNode.getParent().getLeftChild() == heapNode;
        LeftistHeapNode<Type> sub = merge(heapNode.getLeftChild(), heapNode.getRightChild());
        LeftistHeapNode<Type> parent = heapNode.getParent();
        if (left) {
            parent.setLeftChild(sub);
        } else {
            parent.setRightChild(sub);
        }

        if (sub != null) {
            sub.setParent(parent);
        }

        if (parent.hasLeftChild() && !parent.hasRightChild()) {
            parent.swapChildren();
            parent.setRank(0);
        } else {
            if (parent.haveChildren() && parent.isRightChildHasLargerRank()) {
                parent.swapChildren();
                parent.setRank(parent.getRightChild().getRank() + 1);
            } else {
                parent.setRank(0);
            }
        }

        heapNode.clear();
    }

    private LeftistHeapNode<Type> merge(final LeftistHeapNode<Type> firstRoot,
                                        final LeftistHeapNode<Type> secondRoot) {
        if (firstRoot == null) {
            return secondRoot;
        }

        if (secondRoot == null) {
            return firstRoot;
        }

        LeftistHeapNode<Type> first = firstRoot;
        LeftistHeapNode<Type> second = secondRoot;
        if (first.compareTo(second) > 0) {
            LeftistHeapNode<Type> temp = first;
            first = second;
            second = temp;
        }

        first.setRightChild(merge(first.getRightChild(), second));
        first.getRightChild().setParent(first);

        if (!first.hasLeftChild() || first.isRightChildHasLargerRank()) {
            first.swapChildren();
        }

        if (first.hasRightChild()) {
            first.setRank(first.getRightChild().getRank() + 1);
        }

        return first;
    }
}
