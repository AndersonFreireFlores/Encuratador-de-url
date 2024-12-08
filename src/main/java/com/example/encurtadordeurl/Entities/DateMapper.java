package com.example.encurtadordeurl.Entities;

import org.mapstruct.Mapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Mapper(componentModel = "spring")
public class DateMapper {

    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public Date asDate(String date) {
        try {
            return new SimpleDateFormat(DATE_FORMAT).parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String asString(Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }
}