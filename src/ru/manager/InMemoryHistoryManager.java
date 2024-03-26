package ru.manager;

import ru.manager.interfaces.HistoryManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    Map<Integer, Node<Task>> history = new HashMap<>();
    Node<Task> last = null;
    Node<Task> first = null;

    private void linkLast(Task task) {
        Node<Task> newNode = new Node<>(task);
        newNode.prev = last;
        if (history.size() == 0) {
            first = newNode;
        } else if (last != null) {
            last.next = newNode;
        }
        last = newNode;
        history.put(task.id, newNode);
    }

    private List<Task> getTasks() {
        List<Task> historyList = new ArrayList<>();
        Node<Task> nextNode = first;
        while (nextNode != null){
            historyList.add(nextNode.data);
            nextNode = nextNode.next;
        }
        return historyList;
    }

    private void removeNode(int id){
        Node<Task> removableNode = history.get(id);
        Node<Task> rNext = removableNode.next;
        Node<Task> rPrev = removableNode.prev;

        rNext.prev = rPrev;
        rPrev.next = rNext;

        history.remove(id);
    }
    @Override
    public void remove(int id){
        removeNode(id);
    }

    @Override
    public void add (Task task) {
        if (history.containsKey(task.id)) {
            removeNode(task.id);
        }
        linkLast(task);
    }

    @Override
    public List<Task> getHistory(){
        return getTasks();
    }
}

class Node <T extends Task> {
    public Node<T> prev;
    public Node<T> next;
    public T data;

    public Node(T data) {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}
