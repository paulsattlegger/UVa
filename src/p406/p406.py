#!/usr/bin/env python3
from functools import cache


@cache
def is_prime(n):
    if n == 0:
        return False
    for i in range(2, n // 2 + 1):
        if n % i == 0:
            return False
    return True


def process_input(n, c):
    # list of prime numbers between 1 and N
    lst = [str(p) for p in range(1, n + 1) if is_prime(p)]
    cntr = len(lst) // 2
    print(str(n), str(c) + ": ", end="")
    if c > cntr:
        print(" ".join(lst))
    elif len(lst) % 2 == 0:  # even
        print(" ".join(lst[cntr - c:cntr + c]))
    else:  # odd
        print(" ".join(lst[cntr - c + 1:cntr + c]))
    print()


if __name__ == '__main__':
    try:
        while True:
            process_input(*map(int, input().split()))
    except EOFError:
        pass
