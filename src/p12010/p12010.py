class Node:
    def __init__(self):
        self.data = None
        self.right = None
        self.left = None

    def insert(self, data):
        if not self.data:
            self.data = data
        elif data < self.data:
            if not self.left:
                self.left = Node()
            self.left.insert(data)
        elif data > self.data:
            if not self.right:
                self.right = Node()
            self.right.insert(data)

    def print(self):
        if not self.left and not self.right:
            return ['o']

        if not self.left:
            r = self.right.print()
            r_len = len(r[0])
            r_center = r[0].find('o')

            s = ["o" + "-" * r_center + "+" + " " * (r_len - r_center - 1),
                 " " * (r_center + 1) + "|" + " " * (r_len - r_center - 1)]
            for i in range(len(r)):
                s.append(" " + r[i])

            return s

        if not self.right:
            l = self.left.print()
            l_len = len(l[0])
            l_center = l[0].find('o')

            s = [" " * l_center + "+" + "-" * (l_len - l_center - 1) + "o",
                 " " * l_center + "|" + " " * (l_len - l_center - 1) + " "]
            for i in range(len(l)):
                s.append(l[i] + " ")

            return s

        # both subtrees exist
        r = self.right.print()
        r_len = len(r[0])
        r_center = r[0].find('o')
        l = self.left.print()
        l_len = len(l[0])
        l_center = l[0].find('o')

        s = [" " * l_center + "+" + "-" * (l_len - l_center - 1) + "o" + "-" * r_center + "+" + " " * (
                r_len - r_center - 1),
             " " * l_center + "|" + " " * (l_len - l_center - 1) + " " * (r_center + 1) + "|" + " " * (
                     r_len - r_center - 1)]

        for i in range(max(len(l), len(r))):
            if i >= len(l):
                sl = " " * (len(l[0]) + 1)
            else:
                sl = l[i] + " "
            if i >= len(r):
                sl += " " * len(r[0])
            else:
                sl += r[i]
            s.append(sl)

        return s


def get_ints():
    return list(map(int, input().split()))


def main():
    t, = get_ints()
    for i in range(1, t + 1):
        print("Case #{}:".format(i))
        root = Node()
        vs = get_ints()
        for v in vs[:vs.pop(0)]:
            root.insert(v)
        for line in root.print():
            print(line.rstrip())


if __name__ == "__main__":
    main()
