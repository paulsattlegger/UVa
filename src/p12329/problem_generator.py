from random import randrange, shuffle

with open("in/generated.txt", "w") as f:
    t = 50
    f.write("{}\n".format(t))
    for _ in range(t):
        n = randrange(100) + 1
        f.write("{}\n".format(n))
        v = list(range(1, n + 1))
        shuffle(v)
        f.write(" ".join(map(str, v)) + "\n")
        m = randrange(5) + 1
        f.write("{}\n".format(m))
        for _ in range(1, m + 1):
            r = randrange(n) + 1
            c = randrange(n) + 1
            r_i = randrange(200) + 1
            c_i = randrange(200) + 1
            f.write("{} {} {} {}\n".format(r, c, r_i, c_i))
