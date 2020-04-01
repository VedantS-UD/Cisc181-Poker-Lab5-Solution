package pkgUtil;

public class Node<T> {
    private Node<T> next;
    private T item;

    public Node(T item) {
        this.item = item;
    }

    public void setNext(Node<T> next) {
        this.next = next;
    }

    public Node<T> getNext() {
        return this.next;
    }

    public T getValue() {
        return this.item;
    }
}