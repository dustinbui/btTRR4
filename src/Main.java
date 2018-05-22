import java.util.*;

class Graph {
	// Number of vertices, edges
	private int n;
	// Array of lists for adjacency list representation
	private LinkedList<Integer> adj[];
	// Array contain weight between two vertices
	private int[][] w;
	// Array b->b' and g->g'
	private ArrayList<Integer> listB;
	private ArrayList<Integer> listG;

	// Constructor
	public Graph(int n) {
		super();
		this.n = n;
		listB = new ArrayList<>();
		listG = new ArrayList<>();
		adj = new LinkedList[n];
		for (int i = 0; i < n; i++)
			adj[i] = new LinkedList<>();
		w = new int[n][n];
		// Initialize the begin value is -1
		for (int i = 0; i < n; i++)
			Arrays.fill(w[i], -1);
	}

	// Add edge of graph
	public void addEdge(int x, int y, int weight) {
		adj[x].add(y);
		adj[y].add(x);
		w[x][y] = weight;
		w[y][x] = weight;
	}

	// Weight between two vertices
	public int weight(int x, int y) {
		return w[x][y];
	}

	public int dijkstra(int s, int k) {
		int[] dist = new int[n];
		int[] prev = new int[n];
		boolean[] set = new boolean[n];
		// set[i] is true if vertex i is include in shorted
		boolean[] visited = new boolean[n];
		for (int i = 0; i < n; i++) {
			dist[i] = Integer.MAX_VALUE;
			prev[i] = -1;
			set[i] = false;
			visited[i] = false;
		}
		dist[s] = 0;
		LinkedList<Integer> queue = new LinkedList<Integer>();
		queue.add(s);
		while (!queue.isEmpty()) {
			int u = queue.pop();
			if (u == k)
				return dist[k];
			set[u] = true;
			visited[u] = true;
			for (Integer v : adj[u]) {
				if (!set[v] && dist[v] > dist[u] + weight(v, u)) {
					dist[v] = dist[u] + weight(v, u);
					prev[v] = u;
				}
				if (!visited[v])
					queue.add(v);
			}
			Collections.sort(queue, new Comparator<Integer>() {
				@Override
				public int compare(Integer o1, Integer o2) {
					return dist[o1] - dist[o2];
				}
			});
		}
		return 0;
	}

	public boolean result(int b, int b1, int g, int g1, int r) {
		if (b == b1 && g == g1)
			return true;
		int p = -1, q = -1;
		if (adj[b].indexOf(b1) != -1)
			p = b1;
		if (adj[g].indexOf(g1) != -1)
			q = g1;
		for (Integer i : adj[b]) {
			if (p != b1)
				p = i;
			for (Integer j : adj[g]) {
				if (q != g1)
					q = j;
				// Can not repeat (b, g)
				boolean repeat = false;
				for (int k = 0; k < listB.size(); k++)
					if (listB.get(k) == p && q == listG.get(k)) {
						repeat = true;
						break;
					}
				if (repeat)
					continue;
				// Can not (b, g) -> (g, b)
				if (p == g && q == b)
					continue;
				// Can not (b, g) -> (b, g)
				if (p == b && q == g)
					continue;
				if (dijkstra(p, q) >= r) {
					listB.add(p);
					listG.add(q);
					return result(p, b1, q, g1, r);
				} else if (dijkstra(q, p) < r && p == b1 && q == g1)
					return false;
			}
		}
		return false;
	}

	public void print(int b, int g) {
		System.out.println("Result: ");
		System.out.println(b + "  " + g);
		for (int i = 0; i < listB.size(); i++)
			System.out.println(listB.get(i) + "  " + listG.get(i));
	}
}

public class Main {

	public static void main(String[] args) {
		Graph graph = new Graph(6);
		graph.addEdge(2, 1, 50);
		graph.addEdge(3, 0, 20);
		graph.addEdge(0, 2, 90);
		graph.addEdge(1, 3, 40);
		graph.addEdge(5, 0, 60);
		graph.addEdge(4, 1, 80);
		graph.addEdge(4, 2, 70);
		graph.addEdge(5, 4, 100);
		graph.addEdge(3, 5, 20);
		for (int i = 0; i < 6; i++)
			graph.addEdge(i, i, 0);
		System.out.println(graph.dijkstra(3, 2));
		Scanner sc = new Scanner(System.in);
		System.out.print("Input b: ");
		int b = sc.nextInt();
		sc.nextLine();
		System.out.print("Input g: ");
		int g = sc.nextInt();
		sc.nextLine();
		System.out.print("Input b': ");
		int b1 = sc.nextInt();
		sc.nextLine();
		System.out.print("Input g': ");
		int g1 = sc.nextInt();
		sc.nextLine();
		System.out.print("Input r: ");
		int r = sc.nextInt();
		sc.nextLine();
		sc.close();
		if (!graph.result(b, b1, g, g1, r))
			System.out.println("Không!");
		else
			graph.print(b, g);
	}
}
