def read_int():
    while True:
        try:
            for i in input().split():
                yield int(i)
        except EOFError:
            break


def main():
    c = 1
    while True:
        it = iter(read_int())
        n, m = next(it), next(it)
        if n == 0 and m == 0:
            break
        if c > 1:
            print()
        print("Field #{}:".format(c))
        c += 1
        arr = [["." for _ in range(m + 2)]]
        for i in range(n):
            arr.append([".", *[c for c in input()], "."])
        arr.append(["." for _ in range(m + 2)])
        res = []
        for i in range(n + 2):
            res.append([0 for _ in range(m + 2)])
        for i in range(1, n + 1):
            for j in range(1, m + 1):
                if arr[i][j] == '*':
                    res[i - 1][j] += 1
                    res[i - 1][j + 1] += 1
                    res[i][j - 1] += 1
                    res[i + 1][j - 1] += 1
                    res[i - 1][j - 1] += 1
                    res[i][j + 1] += 1
                    res[i + 1][j] += 1
                    res[i + 1][j + 1] += 1
        for i in range(1, n + 1):
            for j in range(1, m + 1):
                if arr[i][j] == '*':
                    print('*', end="")
                else:
                    print(res[i][j], end="")
            print()


if __name__ == "__main__":
    main()
