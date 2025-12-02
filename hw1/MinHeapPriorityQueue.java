import java.util.HashMap;
import java.util.Map;

public class MinHeapPriorityQueue {

    private static class HeapNode {
        int item; 
        int priority; 

        HeapNode(int item, int priority) {
            this.item = item;
            this.priority = priority;
        }
        @Override
        public String toString() {
            return "(" + item + ", " + priority + ")";
        }
    }

    private HeapNode[] heap;
    private int size;

    // item -> current index in the heap array (for O(1) lookup)
    private Map<Integer, Integer> position;

    public MinHeapPriorityQueue(int capacity) {
        StartHeap(capacity);
    }

     // 3. StartHeap(N) – initialize / reset the heap to be empty with capacity N.
    public void StartHeap(int capacity) {
        heap = new HeapNode[capacity + 1];
        size = 0;
        position = new HashMap<>();
    }

    // utils
    private int parent(int i) { return i / 2; }
    private int left(int i)   { return 2 * i; }
    private int right(int i)  { return 2 * i + 1; }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getSize() {
        return size;
    }

    private void swap(int i, int j) {
        HeapNode temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;

        position.put(heap[i].item, i);
        position.put(heap[j].item, j);
    }


     // 1. Heapify_Up(index) - move node at index up until heap property holds.
    private void Heapify_Up(int index) {
        while (index > 1) {
            int p = parent(index);
            if (heap[index].priority < heap[p].priority) {
                swap(index, p);
                index = p;
            } else {
                break;
            }
        }
    }

     // 2. Heapify_Down(index) – move node at index down until heap property holds.
    private void Heapify_Down(int index) {
        while (true) {
            int leftChild = left(index);
            int rightChild = right(index);
            int smallest = index;

            if (leftChild <= size && heap[leftChild].priority < heap[smallest].priority) {
                smallest = leftChild;
            }
            if (rightChild <= size && heap[rightChild].priority < heap[smallest].priority) {
                smallest = rightChild;
            }
            if (smallest != index) {
                swap(index, smallest);
                index = smallest;
            } else {
                break;
            }
        }
    }

     // 4. Insert(item, value) – insert a new (item, priority) into the heap.

    public void Insert(int item, int priority) {
        if (size >= heap.length - 1) {
            throw new IllegalStateException("Heap is full");
        }
        if (position.containsKey(item)) {
            throw new IllegalArgumentException("Item already present in heap: " + item);
        }

        size++;
        heap[size] = new HeapNode(item, priority);
        position.put(item, size);
        Heapify_Up(size);
    }


     // 5. FindMin() – return the minimum element without removing it.

    public HeapNode FindMin() {
        if (size == 0) {
            return null;
        }
        return heap[1];
    }

     // 6. Delete(index) – remove the node at position index in O(log n).

    private void DeleteIndex(int index) {
        if (index < 1 || index > size) {
            return;
        }

        if (index == size) {
            int item = heap[index].item;
            position.remove(item);
            heap[index] = null;
            size--;
            return;
        }

        HeapNode nodeToDelete = heap[index];
        HeapNode lastNode = heap[size];

        // move last node to index
        heap[index] = lastNode;
        heap[size] = null;
        size--;

        // update position map.
        position.remove(nodeToDelete.item);
        position.put(lastNode.item, index);

        // restore heap property
        int parentIndex = parent(index);
        if (index > 1 && heap[index].priority < heap[parentIndex].priority) {
            Heapify_Up(index);
        } else {
            Heapify_Down(index);
        }
    }

     // 7. ExtractMin() – remove and return the minimum element.

    public HeapNode ExtractMin() {
        if (size == 0) {
            return null;
        }
        HeapNode min = heap[1];
        DeleteIndex(1);
        return min;
    }

     // 8. Delete(item) – delete the node with the given item key.

    public void Delete(int item) {
        Integer index = position.get(item);
        if (index == null) {
            return; // or throw if you prefer
        }
        DeleteIndex(index);
    }

     // 9. ChangePriority(item, newPriority) – change the priority of item

    public void ChangePriority(int item, int newPriority) {
        Integer index = position.get(item);
        if (index == null) {
            return; // or throw if you prefer
        }

        int oldPriority = heap[index].priority;
        heap[index].priority = newPriority;

        if (newPriority < oldPriority) {
            Heapify_Up(index);
        } else if (newPriority > oldPriority) {
            Heapify_Down(index);
        }
    }


     // test
    public static void main(String[] args) {
        MinHeapPriorityQueue pq = new MinHeapPriorityQueue(10);

        pq.Insert(1, 50);
        pq.Insert(2, 30);
        pq.Insert(3, 40);
        pq.Insert(4, 10);
        pq.Insert(5, 20);

        System.out.println("Min: " + pq.FindMin()); // should be (4,10)

        pq.ChangePriority(1, 5); // item 1 becomes best
        System.out.println("Min after ChangePriority(1,5): " + pq.FindMin()); // (1,5)

        while (!pq.isEmpty()) {
            System.out.println("Extracted: " + pq.ExtractMin());
        }

    }
}
