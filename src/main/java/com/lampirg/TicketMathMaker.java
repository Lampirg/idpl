package com.lampirg;

import com.lampirg.json.Ticket;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

import static java.util.stream.Collectors.*;

public class TicketMathMaker {

    public static Result countVvoAndTlv(List<Ticket> tickets) {
        return tickets.stream()
                .filter(TicketMathMaker::fromVvoToTlv)
                .collect(teeing(
                        groupingBy(
                                Ticket::getCarrier,
                                collectingAndThen(
                                        minBy(Comparator.comparing(ticket -> Duration.between(ticket.getDepartureDateTime(), ticket.getArrivalDateTime()))),
                                        ticket -> Duration.between(ticket.orElseThrow().getDepartureDateTime(), ticket.orElseThrow().getArrivalDateTime())
                                        )
                        ),
                        teeing(
                                averagingInt(Ticket::getPrice),
                                mapping(Ticket::getPrice, collectingAndThen(toList(), TicketMathMaker::countMedian)),
                                (avg, median) -> avg - median
                        ),
                        Result::new
                ));
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
