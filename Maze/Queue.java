public class Queue {
    private Node[] elements;
    private int front, rear, size, capacity;

    public Queue() {
        capacity = 100;
        elements = new Node[capacity];
        front = size = 0;
        rear = capacity - 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void enqueue(Node node) {
        if (size == capacity) throw new IllegalStateException("Queue đầy.");
        rear = (rear + 1) % capacity;
        elements[rear] = node;
        size++;
    }

    public Node dequeue() {
        if (isEmpty()) throw new IllegalStateException("Queue rỗng.");
        Node node = elements[front];
        front = (front + 1) % capacity;
        size--;
        return node;
    }
}
