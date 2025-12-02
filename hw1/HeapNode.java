class HeapNode {
    int item;      // e.g. vertex id
    int priority;  // e.g. distance

    HeapNode(int item, int priority) {
        this.item = item;
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "(" + item + ", " + priority + ")";
    }
}
