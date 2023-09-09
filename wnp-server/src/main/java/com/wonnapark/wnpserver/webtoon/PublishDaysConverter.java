package com.wonnapark.wnpserver.webtoon;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.Arrays;
import java.util.List;

@Component
public class PublishDaysConverter implements Converter<String, List<DayOfWeek>> {

    @Override
    public List<DayOfWeek> convert(String value) {
        String[] s = value.replaceAll("[\\s\"\\[\\]]", "").split(",");
        return Arrays.stream(s).map(DayOfWeek::valueOf).toList();
    }

}
