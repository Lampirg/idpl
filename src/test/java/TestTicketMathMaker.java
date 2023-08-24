import com.lampirg.Result;
import com.lampirg.TicketMathMaker;
import com.lampirg.json.Ticket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class TestTicketMathMaker {

    @Test
    void givenOddNumberOfTickets() {
        List<Ticket> tickets = List.of(
                new Ticket(
                        "VVO", "", "TLV", "",
                        LocalDate.EPOCH, LocalTime.NOON, LocalDate.EPOCH, LocalTime.NOON.plusHours(3),
                        "AA", 0, 10000
                ),
                new Ticket(
                        "VVO", "", "TLV", "",
                        LocalDate.EPOCH.plusWeeks(1), LocalTime.NOON, LocalDate.EPOCH.plusWeeks(1), LocalTime.NOON.plusHours(1),
                        "AA", 0, 10500
                ),
                new Ticket(
                        "VVG", "", "TLV", "",
                        LocalDate.EPOCH.plusWeeks(1), LocalTime.NOON, LocalDate.EPOCH.plusWeeks(1), LocalTime.NOON.plusHours(1),
                        "AA", 0, 10500
                ),
                new Ticket(
                        "VVO", "", "TLV", "",
                        LocalDate.EPOCH.plusWeeks(1), LocalTime.NOON, LocalDate.EPOCH.plusWeeks(1), LocalTime.NOON.plusHours(6),
                        "SK", 0, 12700
                )
        );
        Result expected = new Result(
                Map.of("AA", Duration.ofHours(1), "SK", Duration.ofHours(6)),
                (33200d / 3) - 10500
        );
        Result actual = TicketMathMaker.countVvoAndTlv(tickets);
        Assertions.assertEquals(expected, actual);
    }

    @Test
    void givenEvenNumberOfTickets() {
        List<Ticket> tickets = List.of(
                new Ticket(
                        "VVO", "", "TLV", "",
                        LocalDate.EPOCH, LocalTime.NOON, LocalDate.EPOCH, LocalTime.NOON.plusHours(3),
                        "AA", 0, 10000
                ),
                new Ticket(
                        "VVO", "", "TLV", "",
                        LocalDate.EPOCH.plusWeeks(1), LocalTime.NOON, LocalDate.EPOCH.plusWeeks(1), LocalTime.NOON.plusHours(1),
                        "AA", 0, 10500
                ),
                new Ticket(
                        "VVG", "", "TLV", "",
                        LocalDate.EPOCH.plusWeeks(1), LocalTime.NOON, LocalDate.EPOCH.plusWeeks(1), LocalTime.NOON.plusHours(1),
                        "AA", 0, 10500
                ),
                new Ticket(
                        "VVO", "", "TLV", "",
                        LocalDate.EPOCH, LocalTime.NOON, LocalDate.EPOCH, LocalTime.NOON.plusHours(6),
                        "SK", 0, 12700
                ),
                new Ticket(
                        "VVO", "", "TLV", "",
                        LocalDate.EPOCH.plusWeeks(1), LocalTime.NOON, LocalDate.EPOCH.plusWeeks(1), LocalTime.NOON.plusHours(6),
                        "SK", 0, 16700
                )
        );
        Result expected = new Result(
                Map.of("AA", Duration.ofHours(1), "SK", Duration.ofHours(6)),
                (49900d / 4) - (23200d / 2)
        );
        Result actual = TicketMathMaker.countVvoAndTlv(tickets);
        Assertions.assertEquals(expected, actual);
    }
}
