package com.lampirg;

import com.lampirg.json.Ticket;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class TicketMathMaker {

    public static Result countVvoAndTlv(List<Ticket> tickets) {
        Map<String, Duration> minTimeForCarrier = new HashMap<>();
        Map<Integer, Integer> pricesCount = new HashMap<>();
        for (Ticket ticket : tickets) {
            if (!fromVvoToTlv(ticket))
                continue;
            minTimeForCarrier.merge(
                    ticket.getCarrier(),
                    Duration.between(ticket.getDepartureDateTime(), ticket.getArrivalDateTime()),
                    TicketMathMaker::min
            );
            pricesCount.merge(
                    ticket.getPrice(),
                    1,
                    Integer::sum
            );
        }
        double div = pricesCount.entrySet().stream()
                .collect(
                        Collectors.teeing(
                                collectMedian(),
                                Collectors.summarizingInt(entry -> entry.getKey() * entry.getValue()),
                                (median, average) -> average.getAverage() - median.doubleValue()
                        )
                );
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

    private static Collector<Map.Entry<Integer, Integer>, ?, Integer> collectMedian() {
        return Collectors.collectingAndThen(
                Collectors.maxBy(Comparator.comparingInt(Map.Entry::getValue)),
                res -> res.orElseThrow().getKey()
        );
    }
}
