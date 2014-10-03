import java.util.*;
import java.io.*;

// uses array of N nodes, referenced by id and an adjacency list for graph representation, and an efficient priority queue implementation
public class dijkstra {
	public static void main(String[] args) throws IOException {
		int N = 1000; // number of nodes
		int sourceNode = 0;
		ArrayList<Edge>[] adjList = new ArrayList[N];
		int[] minDists = new int[N];
		Arrays.fill(minDists, Integer.MAX_VALUE);
		// make sure distances won't overflow, otherwise change the dist stuff to longs
		minDists[sourceNode] = 0;
		FastHeap pq = new FastHeap(N, minDists);
		pq.offer(sourceNode);
		int cur, nextDist;
		while (pq.size() > 0) {
			cur = pq.poll();
			if (adjList[cur] != null) {
				for (Edge e: adjList[cur]) {
					nextDist = minDists[cur] + e.w;
					if (minDists[e.v] == Integer.MAX_VALUE) {
						minDists[e.v] = nextDist;
						pq.offer(e.v);
					} else if (minDists[e.v] > nextDist) {
						minDists[e.v] = nextDist;
						pq.decreaseKey(e.v);
					}
				}
			}
		}
		System.out.println(Arrays.toString(minDists));
	}
	
	//TODO remove
	static class Node {
		int node, dist;
		Node(int node, int d) {
			this.node = node;
			this.dist = d;
		}
		public boolean equals(Object other) {
			if (!(other instanceof Node)) return false;
			Node o = (Node)other;
			return this.node == o.node;
		}
		public String toString() {
			return "" + this.node;
		}
	}
	
	static class Edge {
		// (u, v) with weight w
		int v, w;
		Edge(int v, int w) {
			this.v = v;
			this.w = w;
		}
	}

	static class FastHeap {
		int capacity;
		int size;
		int[] contents;
		int[] indexMap;
		int[] dists;
		FastHeap(int cap, int[] d) {
			capacity = cap;
			size = 0;
			dists = d;
			contents = new int[cap];
			indexMap = new int[cap];
		}
		int size() {
			return size;
		}
		void offer(int n) {
			contents[size] = n;
			indexMap[n] = size;
			size++;
			decreaseKey(n);
		}
		void decreaseKey(int n) {
			int index = indexMap[n];
			int nextIndex = (index-1)/2;
				
			while (index > 0 && dists[contents[index]] < dists[contents[nextIndex]]) {
				swap(index, nextIndex);
				index = nextIndex;
				nextIndex = (index-1)/2;
			}
		}
		int poll() {
			int toRet = contents[0];
			size--;
			if (size > 0) {
				swap(0, size);
				moveDown(0);
			}
			return toRet;
		}
		void moveDown(int i) {
			int left = i*2 + 1;
			int right = left + 1;
			int toSwap;
			if (right >= size) {
				if (left >= size) {
					return;
				} else {
					toSwap = left;
				}
			} else if (dists[contents[left]] <= dists[contents[right]]) {
				toSwap = left;
			} else {
				toSwap = right; 
			}
			if (dists[contents[i]] > dists[contents[toSwap]]) {
				swap(i, toSwap);
				moveDown(toSwap);
			}
		}
		void swap(int i1, int i2) {
			int temp = contents[i1];
			contents[i1] = contents[i2];
			contents[i2] = temp;
			indexMap[contents[i1]] = i1;
			indexMap[contents[i2]] = i2;
		}

	}
}