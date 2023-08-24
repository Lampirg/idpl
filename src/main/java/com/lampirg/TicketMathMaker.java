package com.lampirg;

import com.lampirg.json.Ticket;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class TicketMathMaker {

    public static Result countVvoAndTlv(List<Ticket> tickets) {
        Map<String, Duration> minTimeForCarrier = new HashMap<>();
        List<Integer> pricesCount = new ArrayList<>();
        double avg = 0;
        for (Ticket ticket : tickets) {
            if (!fromVvoToTlv(ticket))
                continue;
            minTimeForCarrier.merge(
                    ticket.getCarrier(),
                    Duration.between(ticket.getDepartureDateTime(), ticket.getArrivalDateTime()),
                    TicketMathMaker::min
            );
            pricesCount.add(ticket.getPrice());
            avg += ticket.getPrice();
        }
        double div = countDiv(pricesCount, avg);
        return new Result(minTimeForCarrier, div);
    }

    private static boolean fromVvoToTlv(Ticket ticket) {
        return ticket.getOrigin().equals("VVO") && ticket.getDestination().equals("TLV");
    }

    private static Duration min(Duration oldVal, Duration newVal) {
        if (oldVal.compareTo(newVal) < 0)
            return oldVal;
        return newVal;
    }

    private static double countDiv(List<Integer> pricesCount, double avg) {
        avg /= pricesCount.size();
        pricesCount.sort(Comparator.naturalOrder());
        double median = countMedian(pricesCount);
        return avg - median;
    }

    private static double countMedian(List<Integer> pricesCount) {
        int middle = pricesCount.size() / 2;
        if (pricesCount.size() % 2 == 1) {
            return pricesCount.get(middle);
        } else {
            return ((double) pricesCount.get(middle - 1) + pricesCount.get(middle)) / 2;
        }
    }
}
