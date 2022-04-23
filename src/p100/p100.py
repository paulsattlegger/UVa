#!/usr/bin/env python3

"""
def f(n):
    cnt = 1
    while n != 1:
        if n % 2 == 1:  # odd
            n = 3 * n + 1
        else:  # even
            n //= 2
        cnt += 1
    memory[n] = cnt
    return cnt


def f_recursive(n, cnt):
    print('f1(', n, cnt, ')')
    if n == 1:
        return cnt
    if n % 2 == 1:  # odd
        return f_recursive(3 * n + 1, cnt + 1)
    else:  # even
        return f_recursive(n // 2, cnt + 1)
"""

memory = {}


def calculate_cycle(n):
    cnt = 1
    # build up list of "new" numbers
    lst = []
    while n != 1:
        # as soon as we hit a known number, ...
        if n in memory:
            cnt += memory[n] - 1
            break
        lst.append(n)
        if n % 2 == 1:  # odd
            n = 3 * n + 1
        else:  # even
            n //= 2
        cnt += 1
    # ... memorize new numbers and return
    memorize(lst, cnt)
    return cnt


def memorize(lst, cnt):
    for el in lst:
        memory[el] = cnt
        cnt -= 1


def process_input(i, j):
    m = 0
    for p in range(min(i, j), max(i, j) + 1):
        m = max(m, calculate_cycle(p))
    print(i, j, m)


if __name__ == '__main__':
    try:
        while True:
            process_input(*map(int, input().split()))
    except EOFError:
        pass
