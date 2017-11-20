package lesson3;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// Attention: comparable supported but comparator is not
@SuppressWarnings("WeakerAccess")
public class BinaryTree<T extends Comparable<T>> extends AbstractSet<T> implements SortedSet<T> {

    private static class Node<T> {
        final T value;

        Node<T> left = null;

        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> root = null;

    private int size = 0;

    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
        }
        else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
        }
        else {
            assert closest.right == null;
            closest.right = newNode;
        }
        size++;
        return true;
    }

    boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

    @Override
    public boolean remove(Object o) {
        if (contains(o)) {
            T t = (T) o;
            Node<T> closest = find(t);
            Node<T> parent = findParent(t);
            //удаление листа
            if (closest.left == null && closest.right == null) {
                if (parent == null)
                    root = null;
                else {
                    if (parent.left == closest) parent.left = null;
                    else parent.right = null;
                }
            } //удаление узла с одним дочерним узлом
            else if (closest.left == null || closest.right == null) {
                Node<T> child = (closest.left != null) ? closest.left : closest.right;
                if (parent == null)
                    root = closest;
                else {
                    if (parent.left == closest) parent.left = child;
                    else parent.right = child;
                }
            } //удаление узла с двумя дочерними узлами
            else {
                Node<T> maxNode = max(closest.left);
                if (parent.left == closest) parent.left = maxNode;
                else parent.right = maxNode;
                //найти родителя у maxNode
                Node<T> newParent = findParent(maxNode.value);
                if (newParent.left == maxNode) newParent.left = maxNode.left;
                else newParent.right = maxNode.left;
                maxNode.left = closest.left;
                maxNode.right = closest.right;
            } return true;
        }
        else return false;
    }

    private Node<T> max(Node<T> start) {
        if (start.right == null)
            return start;
        return max(start.right);
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    private Node<T> findParent (T value) {
        if (root == null) return null;
        return findParent(root, value);
    }

    private Node<T> findParent (Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return null;
        } else if (comparison < 0) {
            if (start.left == null) return null;
            else if (value.compareTo(start.left.value) == 0) return start;
            return findParent(start.left, value);
        }
        else {
            if (start.right == null) return null;
            else if (value.compareTo(start.right.value) == 0) return start;
            return findParent(start.right, value);
        }
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        }
        else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        }
        else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    public class BinaryTreeIterator implements Iterator<T> {

        private Node<T> current = null;

        private BinaryTreeIterator() {}

        private Node<T> findNext() {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean hasNext() {
            return findNext() != null;
        }

        @Override
        public T next() {
            current = findNext();
            if (current == null) throw new NoSuchElementException();
            return current.value;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinaryTreeIterator();
    }

    @Override
    public int size() {
        return size;
    }


    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        throw new UnsupportedOperationException();
    }

    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }
}
