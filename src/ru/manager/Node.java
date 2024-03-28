package ru.manager;

class Node<T extends Task> {
    public Node<T> prev;
    public Node<T> next;
    public T data;

    public Node(T data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}
