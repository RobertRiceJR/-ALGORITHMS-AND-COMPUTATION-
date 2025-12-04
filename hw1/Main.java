import java.io.*;
import java.util.*;

/**
 * SAMER08A - Almost Shortest Path
 * Uses custom MinHeapPriorityQueue from Part I.
 */
public class Main {

    // Edge structure for adjacency list
    static class Edge {
        int to;
        int weight;
        Edge(int t, int w) {
            to = t;
            weight = w;
        }
    }

    static final int INF = 1_000_000_000;

    /**
     * Dijkstra's algorithm using MinHeapPriorityQueue.
     * If buildPrev == true, it also fills prev[] with all predecessors that lie
     * on some shortest path.
     * If buildPrev == false, prev[] is ignored.
     *
     * removed[u][v] == true means the edge u -> v should be ignored.
     */
    static void dijkstra(
            int N,
            int S,
            List<Edge>[] adj,
            int[] dist,
            List<Integer>[] prev,
            boolean[][] removed,
            boolean buildPrev) {

        // Initialize distances
        Arrays.fill(dist, INF);
        if (buildPrev) {
            for (int i = 0; i < N; i++) {
                prev[i].clear();
            }
        }

        MinHeapPriorityQueue pq = new MinHeapPriorityQueue(N);
        // Insert all vertices with initial priority INF
        for (int v = 0; v < N; v++) {
            pq.Insert(v, INF);
        }
        // Set source distance to 0
        dist[S] = 0;
        pq.ChangePriority(S, 0);

        boolean[] visited = new boolean[N];

        while (!pq.isEmpty()) {
            int u = pq.ExtractMinItem(); // get vertex with smallest dist
            if (u == -1) break;
            if (dist[u] == INF) break;   // remaining vertices are unreachable
            if (visited[u]) continue;
            visited[u] = true;

            for (Edge e : adj[u]) {
                int v = e.to;
                if (removed != null && removed[u][v]) continue; // skip removed edges
                if (visited[v]) continue;

                int alt = dist[u] + e.weight;
                if (alt < dist[v]) {
                    dist[v] = alt;
                    if (buildPrev) {
                        prev[v].clear();
                        prev[v].add(u);
                    }
                    pq.ChangePriority(v, alt);
                } else if (buildPrev && alt == dist[v]) {
                    // equally good shortest path
                    prev[v].add(u);
                }
            }
        }
    }

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder out = new StringBuilder();

        while (true) {
            String line = br.readLine();
            if (line == null) break;
            line = line.trim();
            if (line.isEmpty()) continue;

            StringTokenizer st = new StringTokenizer(line);
            int N = Integer.parseInt(st.nextToken());
            int M = Integer.parseInt(st.nextToken());
            if (N == 0 && M == 0) break;

            st = new StringTokenizer(br.readLine());
            int S = Integer.parseInt(st.nextToken());
            int D = Integer.parseInt(st.nextToken());

            @SuppressWarnings("unchecked")
            List<Edge>[] adj = new ArrayList[N];
            for (int i = 0; i < N; i++) {
                adj[i] = new ArrayList<>();
            }

            for (int i = 0; i < M; i++) {
                st = new StringTokenizer(br.readLine());
                int U = Integer.parseInt(st.nextToken());
                int V = Integer.parseInt(st.nextToken());
                int P = Integer.parseInt(st.nextToken());
                adj[U].add(new Edge(V, P));
            }

            // Data structures for Dijkstra and removal
            int[] dist = new int[N];
            @SuppressWarnings("unchecked")
            List<Integer>[] prev = new ArrayList[N];
            for (int i = 0; i < N; i++) {
                prev[i] = new ArrayList<>();
            }
            boolean[][] removed = new boolean[N][N];

            // 1. First Dijkstra: find all shortest paths and fill prev[]
            dijkstra(N, S, adj, dist, prev, removed, true);

            // If no shortest path at all, there is no "almost shortest" path
            if (dist[D] == INF) {
                out.append(-1).append('\n');
                continue;
            }

            // 2. Backward BFS from D using prev[] to mark all edges in any shortest path
            Queue<Integer> q = new ArrayDeque<>();
            boolean[] visitedPrev = new boolean[N];
            q.add(D);
            visitedPrev[D] = true;

            while (!q.isEmpty()) {
                int v = q.poll();
                for (int u : prev[v]) {
                    if (!removed[u][v]) {
                        removed[u][v] = true; // mark edge u -> v as removed
                        if (!visitedPrev[u]) {
                            visitedPrev[u] = true;
                            q.add(u);
                        }
                    }
                }
            }

            // 3. Second Dijkstra: ignore all removed edges
            dijkstra(N, S, adj, dist, prev, removed, false);

            if (dist[D] == INF) {
                out.append(-1).append('\n');
            } else {
                out.append(dist[D]).append('\n');
            }
        }

        System.out.print(out.toString());
    }
}

/**
 * MinHeapPriorityQueue from Part I, slightly extended with ExtractMinItem()
 * so Dijkstra can get just the vertex id.
 */
class MinHeapPriorityQueue {

    // Node storing (item, priority)
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

    private HeapNode[] heap;                 // 1-based heap array
    private int size;                        // current number of elements
    private Map<Integer, Integer> position;  // item -> index in heap

    public MinHeapPriorityQueue(int capacity) {
        StartHeap(capacity);
    }

    // Initialize / reset heap to hold up to N elements
    public void StartHeap(int capacity) {
        heap = new HeapNode[capacity + 1];  // use indices 1..capacity
        size = 0;
        position = new HashMap<>();
    }

    private int parent(int i) { return i / 2; }
    private int left(int i)   { return 2 * i; }
    private int right(int i)  { return 2 * i + 1; }

    public boolean isEmpty() {
        return size == 0;
    }

    public int getSize() {
        return size;
    }

    // Swap two nodes and update positions
    private void swap(int i, int j) {
        HeapNode temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;

        position.put(heap[i].item, i);
        position.put(heap[j].item, j);
    }

    // Move node up until heap property holds
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

    // Move node down until heap property holds
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

    // Insert(item, value) in O(log n)
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

    // FindMin() in O(1)
    public HeapNode FindMin() {
        if (size == 0) {
            return null;
        }
        return heap[1];
    }

    // Internal Delete(index) in O(log n)
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

        heap[index] = lastNode;
        heap[size] = null;
        size--;

        position.remove(nodeToDelete.item);
        position.put(lastNode.item, index);

        int parentIndex = parent(index);
        if (index > 1 && heap[index].priority < heap[parentIndex].priority) {
            Heapify_Up(index);
        } else {
            Heapify_Down(index);
        }
    }

    // ExtractMin() in O(log n)
    public HeapNode ExtractMin() {
        if (size == 0) {
            return null;
        }
        HeapNode min = heap[1];
        DeleteIndex(1);
        return min;
    }

    /**
     * Convenience method for Dijkstra:
     * returns only the item (vertex id) with minimum priority.
     * Returns -1 if heap is empty.
     */
    public int ExtractMinItem() {
        HeapNode min = ExtractMin();
        if (min == null) return -1;
        return min.item;
    }

    // Delete(item) in O(log n) via position map
    public void Delete(int item) {
        Integer index = position.get(item);
        if (index == null) {
            return;
        }
        DeleteIndex(index);
    }

    // ChangePriority(item, newPriority) in O(log n)
    public void ChangePriority(int item, int newPriority) {
        Integer index = position.get(item);
        if (index == null) {
            return;
        }

        int oldPriority = heap[index].priority;
        heap[index].priority = newPriority;

        if (newPriority < oldPriority) {
            Heapify_Up(index);
        } else if (newPriority > oldPriority) {
            Heapify_Down(index);
        }
        // if equal, no reheapification needed
    }
}
