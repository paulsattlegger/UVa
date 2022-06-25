from collections import defaultdict
from collections import deque
from functools import lru_cache


@lru_cache(None)
def adjacent(w1, w2):
    diff = False
    for c1, c2 in zip(w1, w2):
        if c1 != c2:
            if diff:
                return False
            diff = True
    return diff


def min_distance(adjacencies, start, end):
    queue = deque([start])
    visited = set()
    distance = 0
    while queue:
        size = len(queue)
        while size != 0:
            node = queue.popleft()
            if node == end:
                return distance
            for child in adjacencies[node]:
                if child not in visited:
                    visited.add(child)
                    queue.append(child)
            size -= 1
        distance += 1


def main():
    n = int(input())
    while n != 0:
        n -= 1
        dictionary = defaultdict(set)
        while True:
            i = input()
            if i == "*":
                break
            dictionary[len(i)].add(i)
        adjacencies = {}
        for length in dictionary:
            for w1 in dictionary[length]:
                adjacencies[w1] = [w2 for w2 in dictionary[length] if adjacent(w1, w2) and w1 != w2]
        while True:
            try:
                i = input()
            except EOFError:
                break
            if len(i) == 0:
                break
            start, end = i.split()
            print(start, end, min_distance(adjacencies, start, end))
        if n != 0:
            print()


if __name__ == "__main__":
    main()
