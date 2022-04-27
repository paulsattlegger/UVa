#!/usr/bin/env python3

def common_letter(a, b):
    for i in a:
        for j in b:
            if i == j:
                return a.index(i), b.index(j)


def process_input(a, b, x, y):
    i_a, i_b = common_letter(a, b)
    for i, c in enumerate(b):
        print(' ' * i_a + c)
        if len(b) > x and b[x] == c:
            print(a)


if __name__ == '__main__':
    process_input(*['MATCHES', 'CHEESECAKE', 'PICNIC', 'EXCUSES'])
    """
    process_input(*['PEANUT', 'BANANA', 'VACUUM', 'GREEDY'])
    try:
        while True:
            i = input()
            if i.startswith('#'):
                break
            process_input(*input().split())
    except EOFError:
        pass
    """
