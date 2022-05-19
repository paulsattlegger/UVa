import sys
from queue import PriorityQueue


# https://stackabuse.com/dijkstras-algorithm-in-python/
class Graph:
    def __init__(self, num_of_vertices):
        self.v = num_of_vertices
        self.edges = [[-1 for _ in range(num_of_vertices)] for _ in range(num_of_vertices)]

    def add_edge(self, u, v, weight):
        self.edges[u][v] = weight

    def k_shortest_path(self, start_vertex, end_vertex, k):
        # k <= 10
        # distances[visit_count][v_id]
        distances = [{v: float('inf') for v in range(self.v)} for _ in range(k)]
        distances[0][start_vertex] = 0
        # visit count for each node
        visited = [0 for _ in range(self.v)]

        pq = PriorityQueue()
        pq.put((0, start_vertex))

        # Adaption of Dijkstra algorithm
        while not pq.empty():
            (current_distance, current_vertex) = pq.get()

            # Check if end node has been reached for k-th time (starts with 0)
            if current_vertex == end_vertex and visited[current_vertex] == k - 1:
                return current_distance

            # Only consider maximum k visits per node
            if visited[current_vertex] >= k:
                continue

            # Save visit count together with total distance
            distances[visited[current_vertex]][current_vertex] = current_distance
            visited[current_vertex] += 1

            # Iterate through neighbours and put them into the priority queue
            for neighbor in range(self.v):
                if self.edges[current_vertex][neighbor] != -1:
                    edge_length = self.edges[current_vertex][neighbor]
                    pq.put((current_distance + edge_length, neighbor))

        return -1


def read_ints():
    return map(int, sys.stdin.readline().strip().split())


def main():
    while True:
        n, m = read_ints()
        if n == 0 and m == 0:
            break

        g = Graph(n + 1)
        x, y, k = read_ints()

        for _ in range(m):
            u, v, l = read_ints()
            g.add_edge(u, v, l)

        print(g.k_shortest_path(x, y, k))


if __name__ == "__main__":
    main()
