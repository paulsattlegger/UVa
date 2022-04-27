#!/usr/bin/env python3

# https://www.dummies.com/article/academics-the-arts/science/physics/how-to-calculate-time-and-distance-from-acceleration-and-velocity-174278/

class Track:
    def __init__(self, length, v, a):
        self.length = length
        self.minutes = self._minutes(v, a)

    def _minutes(self, v, s):
        t_accelerate = v / s
        d_accelerate = (1 / 2) * s * (t_accelerate ** 2)
        d_constant_speed = self.length - d_accelerate * 2
        t_constant_speed = d_constant_speed / v
        return t_constant_speed + t_accelerate * 2

    def distance_travelled_after_minutes(self, t_travelled, v, s):
        t_accelerate = v / s
        if t_travelled < t_accelerate:
            # meeting during acceleration
            return (1 / 2) * s * (t_travelled ** 2)
        d_accelerate = (1 / 2) * s * (t_accelerate ** 2)

        t_constant_speed = self.minutes - t_accelerate * 2
        # Adjacent stations are not far enough apart to allow a train to accelerate to its maximum velocity before
        # beginning to decelerate.
        assert t_constant_speed >= 0
        if t_travelled < t_accelerate + t_constant_speed:
            # meeting at constant speed
            return d_accelerate + (t_travelled - t_accelerate) * v

        d_constant_speed = t_constant_speed * v
        if t_travelled < t_accelerate + t_constant_speed + t_accelerate:
            # meeting during deceleration
            t_decelerate = t_travelled - t_constant_speed - t_accelerate
            d_decelerate = (1 / 2) * -s * (t_decelerate ** 2) + v * t_decelerate
            return d_accelerate + d_constant_speed + d_decelerate

        assert False

    def __repr__(self):
        return "Block(minutes={}, length={})".format(self.minutes, self.length)


class Station:
    def __init__(self, m, number):
        self.minutes = m
        self.number = number

    def __repr__(self):
        return "Station(minutes={}, number={})".format(self.minutes, self.number)


def construct_route(ds, v, s, m):
    route = []
    route.extend([Track(ds[0], v, s), Station(m, 1)])
    n = 2
    for d1, d2 in zip(ds, ds[1:]):
        route.extend([Track(d2 - d1, v, s), Station(m, n)])
        n += 1
    route.pop()
    return route


def meeting_time(route):
    # the trains must meet here at the latest
    return sum(s.minutes for s in route) / 2


def meeting_location(route):
    t_meeting = meeting_time(route)
    t_total = 0
    for section in route:
        t_total += section.minutes
        if t_meeting < t_total:
            return section


def simulate(ds, v, s, m):
    v /= 5280
    s /= 5280
    route = construct_route(ds, v, s, m)
    location = meeting_location(route)

    if isinstance(location, Station):
        t_from_start = sum(s.minutes for s in route[:route.index(location)])
        t_from_end = sum(s.minutes for s in route[route.index(location) + 1:])
        d_other_sections = sum(s.length for s in route[:route.index(location)] if isinstance(s, Track))
        return max(t_from_start, t_from_end), d_other_sections, location.number
    elif isinstance(location, Track):
        t_other_sections = sum(s.minutes for s in route[:route.index(location)])
        d_other_sections = sum(s.length for s in route[:route.index(location)] if isinstance(s, Track))
        t_meeting_section = meeting_time(route) - t_other_sections
        d_meeting_section = location.distance_travelled_after_minutes(t_meeting_section, v, s)
        return meeting_time(route), d_other_sections + d_meeting_section, None


def output(scenario, time, distance, station=None):
    print("Scenario #{}:".format(scenario))
    print("   Meeting time: {:.1f} minutes".format(time))
    s__ = "   Meeting distance: {:.3f} miles from metro center hub".format(distance)
    if station:
        print("{}, in station {}".format(s__, station))
    else:
        print(s__)


def read_floats():
    fs = []
    while True:
        fs.extend(map(float, input().split()))
        if -1 in fs:
            return fs[:fs.index(-1)]


def main():
    c = 1
    state = "d"
    ds = []
    v, s, m = None, None, None
    for i in read_floats():
        if state == "d":
            if i == 0:
                state = "v"
            else:
                ds.append(i)
        elif state == "v":
            v = i
            state = "s"
        elif state == "s":
            s = i
            state = "m"
        elif state == "m":
            m = i
            state = None
        if not state:
            if v != 0 and s != 0 and len(ds) > 0:
                if c != 1:
                    print()
                output(c, *simulate(ds, v, s, m))
            c += 1
            state = "d"
            ds = []


if __name__ == '__main__':
    main()
