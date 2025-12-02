import java.util.HashMap;
import java.util.Map;

public class MinHeapPriorityQueue {

    private HeapNode[] heap;           // 1-based heap array; heap[0] unused
    private int size;                  // current number of elements
    private Map<Integer, Integer> position;  // item -> index in heap array

    // This plays the role of StartHeap(N)
    public MinHeapPriorityQueue(int capacity) {
        heap = new HeapNode[capacity + 1]; // 1..capacity
        size = 0;
        position = new HashMap<>();
    }

    // Optional reset method if you really want a StartHeap() method as in the spec
    public void startHeap(int capacity) {
        heap = new HeapNode[capacity + 1];
        size = 0;
        position.clear();
    }

    // Utility index helpers
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

        // **** VERY IMPORTANT ****
        // Keep the position map in sync
        position.put(heap[i].item, i);
        position.put(heap[j].item, j);
    }

    // 1. Heapify_Up(index)
    private void heapifyUp(int index) {
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

    // 2. Heapify_Down(index)
    private void heapifyDown(int index) {
        while (true) {
            int left = left(index);
            int right = right(index);
            int smallest = index;

            if (left <= size && heap[left].priority < heap[smallest].priority) {
                smallest = left;
            }
            if (right <= size && heap[right].priority < heap[smallest].priority) {
                smallest = right;
            }
            if (smallest != index) {
                swap(index, smallest);
                index = smallest;
            } else {
                break;
            }
        }
    }

    // 3. StartHeap(N) covered by constructor / startHeap()

    // 4. Insert(item, value)  -- O(log n)
    public void insert(int item, int priority) {
        if (size >= heap.length - 1) {
            throw new IllegalStateException("Heap is full");
        }
        if (position.containsKey(item)) {
            throw new IllegalArgumentException("Item already present in heap: " + item);
        }

        size++;
        heap[size] = new HeapNode(item, priority);
        position.put(item, size);
        heapifyUp(size);
    }

    // 5. FindMin() -- O(1)
    public HeapNode findMin() {
        if (size == 0) {
            return null;
        }
        return heap[1];
    }

    // 6. Delete(index) -- O(log n)
    private void deleteByIndex(int index) {
        if (index < 1 || index > size) {
            return; // or throw IllegalArgumentException
        }

        if (index == size) {
            // deleting the last element: easy
            int item = heap[index].item;
            position.remove(item);
            heap[index] = null;
            size--;
            return;
        }

        HeapNode nodeToDelete = heap[index];
        HeapNode lastNode = heap[size];

        // Move last node into "hole"
        heap[index] = lastNode;
        heap[size] = null;
        size--;

        // Update position map
        position.remove(nodeToDelete.item);
        position.put(lastNode.item, index);

        // Restore heap property (could go up or down)
        int parentIndex = parent(index);
        if (index > 1 && heap[index].priority < heap[parentIndex].priority) {
            heapifyUp(index);
        } else {
            heapifyDown(index);
        }
    }

    // 7. ExtractMin() -- O(log n)
    public HeapNode extractMin() {
        if (size == 0) {
            return null;
        }
        HeapNode min = heap[1];
        deleteByIndex(1);
        return min;
    }

    // 8. Delete(item) -- O(log n) via position map
    public void delete(int item) {
        Integer index = position.get(item);
        if (index == null) {
            return; // or throw if you want
        }
        deleteByIndex(index);
    }

    // 9. ChangePriority(item, newPriority) -- O(log n)
    public void changePriority(int item, int newPriority) {
        Integer index = position.get(item);
        if (index == null) {
            return; // or throw
        }

        int oldPriority = heap[index].priority;
        heap[index].priority = newPriority;

        if (newPriority < oldPriority) {
            // became "better" => move up
            heapifyUp(index);
        } else if (newPriority > oldPriority) {
            // became "worse" => move down
            heapifyDown(index);
        }
        // if equal, no heapify needed
    }

    // Simple sanity test
    public static void main(String[] args) {
        MinHeapPriorityQueue pq = new MinHeapPriorityQueue(10);

        pq.insert(1, 50);
        pq.insert(2, 30);
        pq.insert(3, 40);
        pq.insert(4, 10);
        pq.insert(5, 20);

        System.out.println("Min: " + pq.findMin()); // should be (4,10)

        pq.changePriority(1, 5); // item 1 becomes best
        System.out.println("Min after changePriority(1,5): " + pq.findMin()); // (1,5)

        while (!pq.isEmpty()) {
            System.out.println("Extracted: " + pq.extractMin());
        }
    }
    
}


