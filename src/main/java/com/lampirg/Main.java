package com.lampirg;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.lampirg.json.TicketList;
import lombok.SneakyThrows;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        TicketList ticketList = readTicketList();
        Result result = TicketMathMaker.countVvoAndTlv(ticketList.getTickets());
        result.getMin().forEach((key, val) -> System.out.println(key + ": " + val.toMinutes() + " minutes"));
        System.out.println(result.getDiv());
    }

    @SneakyThrows
    private static TicketList readTicketList() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper.readValue(new File("src/main/resources/tickets.json"), TicketList.class);
    }
}