package com.bookingManagement.util;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class MyModelMapper extends ModelMapper {

    public MyModelMapper() {
        configureMapper();
    }

    private void configureMapper() {
        getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        addConverter(context -> {
            if (context.getSource() == null) {
                return null;
            }
            LocalDateTime sourceDateTime = (LocalDateTime) context.getSource();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            return sourceDateTime.format(formatter);
        }, LocalDateTime.class, String.class);
    }
}
