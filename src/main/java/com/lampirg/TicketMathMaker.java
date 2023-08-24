package com.lampirg;

import com.lampirg.json.Ticket;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;

import static java.util.stream.Collectors.*;

public class TicketMathMaker {

    public static Result countVvoAndTlv(List<Ticket> tickets) {
        return tickets.stream()
                .filter(TicketMathMaker::fromVvoToTlv)
                .collect(teeing(
                        getMapWithCarriersAndMinDurations(),
                        getDivBetweenAverageAndMedian(),
                        Result::new
                ));
    }

    private static boolean fromVvoToTlv(Ticket ticket) {
        return ticket.getOrigin().equals("VVO") && ticket.getDestination().equals("TLV");
    }

    private static Collector<Ticket, ?, Map<String, Duration>> getMapWithCarriersAndMinDurations() {
        return groupingBy(
                Ticket::getCarrier,
                collectingAndThen(
                        minBy(Comparator.comparing(TicketMathMaker::getDuration)),
                        optionalTicket -> getDuration(optionalTicket.orElseThrow())
                )
        );
    }

    private static Duration getDuration(Ticket ticket) {
        return Duration.between(ticket.getDepartureDateTime(), ticket.getArrivalDateTime());
    }

    private static Collector<Ticket, ?, Double> getDivBetweenAverageAndMedian() {
        return teeing(
                averagingInt(Ticket::getPrice),
                mapping(Ticket::getPrice, collectingAndThen(toList(), TicketMathMaker::countMedian)),
                (avg, median) -> avg - median
        );
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
