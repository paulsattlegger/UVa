package p244;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private static final Integer FEET_IN_A_MILE = 5_280;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int scenario = 1;
        // listen for input
        while (scanner.hasNext()) {
            String stationsString = scanner.nextLine();
            // The list is terminated by the sentinel value 0.0.
            List<Double> stations = Arrays.stream(
                            // be careful here - values are separated by one or more spaces
                            stationsString.replaceAll(" +", " ")
                                    // split values by a whitespace
                                    .split(" "))
                    // map to double datatype
                    .map(s -> {
                        try {
                            return Double.valueOf(s);
                        } catch (Exception ignored) {
                            return null;
                        }
                    })
                    // remove nulls
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            //  The list is terminated by the sentinel value 0.0
            if (stations.get(0).equals(-1.)) {
                break;
            }
            stations = stations.subList(0, stations.indexOf(0.));
            double v = Double.parseDouble(scanner.nextLine());
            double s = Double.parseDouble(scanner.nextLine());
            double m = Double.parseDouble(scanner.nextLine());
            calc(stations, v, s, m, scenario++);
        }
    }


    /**
     *
     * A simulation consists of a
     * series of scenarios in which two trains, one starting at the metro center and one starting at the outermost
     * station of the same route, travel toward each other along the route. The transportation planners want
     * to find out where and when the two trains meet.
     *
     *  1.  All trains spend a fixed amount of time at each station.
     *  2.  All trains accelerate and decelerate at the same constant rate. All trains have the same maximum
     *      possible velocity.
     *  3.  When a train leaves a station, it accelerates (at a constant rate) until it reaches its maximum
     *      velocity. It remains at that maximum velocity until it begins to decelerate (at the same constant
     *      rate) as it approaches the next station. Trains leave stations with an initial velocity of zero (0.0)
     *      and they arrive at stations with terminal velocity zero. Adjacent stations on each route are far
     *      enough apart to allow a train to accelerate to its maximum velocity before beginning to decelerate.
     *  4.  Both trains in each scenario make their initial departure at the same time.
     *  5.  There are at most 31 stations along any route
     *  6.  The meeting time of both trains will never be at the departure of one of the trains from a station
     *
     * @param distanceOfStationsInRelationToStart list of distance between stations (in miles)
     * @param v                                   The maximum train velocity, in feet/minute
     * @param s                                   The constant train acceleration rate in feet/minute^2
     * @param m                                   The number of minutes a train stays in a station
     * @param scenario                            number of the current scenario
     */
    private static void calc(List<Double> distanceOfStationsInRelationToStart, double v, double s, double m, int scenario) {

        v = v / FEET_IN_A_MILE;
        s = s / FEET_IN_A_MILE;

        // same like deceleration time
        double timeNeededToAccelerate = v / s;
        double distanceTraveledWhileAcceleration = 0.5 * s * timeNeededToAccelerate * timeNeededToAccelerate;

        double totalTime = 0;

        // first let a single train go through all stations and store time needed between stations
        // result is a list with timeslots containing time between stations and time spent in stations
        List<Travel> travels = new ArrayList<>(distanceOfStationsInRelationToStart.size() * 2);

        for (int i = 0; i < distanceOfStationsInRelationToStart.size(); i++) {
            // calc distance from last station to current
            double distanceBetweenStation = distanceOfStationsInRelationToStart.get(i);
            if (i > 0)
                distanceBetweenStation -= distanceOfStationsInRelationToStart.get(i - 1);

            // calculate time
            double time = (timeNeededToAccelerate * 2) + (((distanceBetweenStation) - (distanceTraveledWhileAcceleration * 2)) / v);
            totalTime += time;
            travels.add(new Travel(distanceBetweenStation, time, totalTime, null));

            // add time spend in the station if there is another station
            if (i < distanceOfStationsInRelationToStart.size() - 1) {
                totalTime += m;
                travels.add(new Travel(0., m, totalTime, i + 1));
            }
        }

        // calculate timestamp when the trains meet
        double totalTimeTraveled = travels.get(travels.size() - 1).totalTime;
        double timeTraveledUntilMeet = totalTimeTraveled / 2;

        // here we know exactly the timestamp when the two trains met
        // get meeting travel
        // obviously there will always be a result
        Travel travel = travels.stream().filter(t -> t.totalTime >= timeTraveledUntilMeet).findFirst().get();

        // check if the trains met in a station
        if (travel.station != null) {
            // now we know the trains met in a station
            // the distance is already known since it is the value of the station
            // next calculate the travel time of the slower train (cause the trains can arrive at different times at the station)
            double fromStart = travel.totalTime - travel.time;
            double fromEnd = travels.subList(travels.indexOf(travel) + 1, travels.size()).stream().map(t -> t.time).reduce(0., Double::sum);
            printResult(scenario,
                    // time of slower train
                    Math.max(fromStart, fromEnd),
                    // sum of all distance values
                    travels.subList(0, travels.indexOf(travel)).stream().map(t -> t.distance).reduce(0., Double::sum),
                    travel.station);
        } else {
            // if they meet on track the timeTraveledUntilMeet is the exact value
            // next calculate the distance traveled of the first train
            // this is the distance from the start until the last station
            double distanceFromStartStationToLastStationBeforeMeet = travels.subList(0, travels.indexOf(travel)).stream().map(t -> t.distance).reduce(0., Double::sum);
            // calculate the time traveled from the last station until the trains meet
            double remainingTime = timeTraveledUntilMeet;
            if (travels.indexOf(travel) > 0) {
                remainingTime -= travels.get(travels.indexOf(travel) - 1).totalTime;
            }
            // calculate distance traveled between the station
            double distanceOnTrack = 0.;
            // calculate distance if they meet before the train is able to accelerate to full speed
            if (remainingTime < timeNeededToAccelerate) {
                distanceOnTrack += 0.5 * s * remainingTime * remainingTime;
            } else {
                double timeSpendBetweenAcAndDec = travel.time - timeNeededToAccelerate * 2;
                distanceOnTrack += distanceTraveledWhileAcceleration;
                // check if they meet when the train is decelerating
                if (timeNeededToAccelerate + timeSpendBetweenAcAndDec < remainingTime) {
                    distanceOnTrack += timeSpendBetweenAcAndDec * v;
                    double timeSpendDecelerating = remainingTime - timeNeededToAccelerate - timeSpendBetweenAcAndDec;
                    double finalSpeed = -s * timeSpendDecelerating + v;
                    distanceOnTrack += ((v + finalSpeed) / 2) * timeSpendDecelerating;
                    // distanceOnTrack += timeSpendDecelerating * timeSpendDecelerating * 0.5 * (-s);
                } else {
                    // here the trains meet on track while going full speed
                    remainingTime -= timeNeededToAccelerate;
                    distanceOnTrack += remainingTime * v;
                }
            }
            printResult(scenario,
                    timeTraveledUntilMeet,
                    distanceFromStartStationToLastStationBeforeMeet + distanceOnTrack,
                    null);
        }
    }

    static void printResult(int scenario, double timeTraveledUntilMeet, double distanceTraveledUntilMeet, Integer station) {
        if (scenario > 1) {
            System.out.println();
        }
        System.out.println("Scenario #" + scenario + ":");
        System.out.println("   Meeting time: " + String.format("%.1f", timeTraveledUntilMeet).replace(",", ".") + " minutes");
        System.out.println("   Meeting distance: " + String.format("%.3f", distanceTraveledUntilMeet).replace(",", ".") +
                " miles from metro center hub" +
                (station != null ? ", in station " + station : ""));
    }

    static class Travel {
        private final double distance;
        private final double time;
        private final double totalTime;
        private final Integer station;

        public Travel(Double distance, Double time, Double totalTime, Integer station) {
            this.distance = distance;
            this.time = time;
            this.totalTime = totalTime;
            this.station = station;
        }
    }

}
