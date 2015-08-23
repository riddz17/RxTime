package com.github.simonpercic.rxtime.data.api.typeadapter;

import com.github.simonpercic.rxtime.data.api.model.response.TimeResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

/**
 * TimeResponseDeserializer Instrumentation tests.
 *
 * @author Simon Percic <a href="https://github.com/simonpercic">https://github.com/simonpercic</a>
 */
public class TimeResponseDeserializerTest {

    private Gson gson;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        gson = new GsonBuilder()
                .registerTypeAdapter(TimeResponse.class, new TimeResponseDeserializer())
                .create();
    }

    @Test
    public void testValidFormat() throws Exception {
        String jsonStr = "{\n" +
                "  \"dateString\": \"2015-08-19T17:56:38+01:00\"\n" +
                "}";

        TimeResponse timeResponse = gson.fromJson(jsonStr, TimeResponse.class);

        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT+1"), Locale.US);
        calendar.setTimeInMillis(0);
        calendar.set(2015, Calendar.AUGUST, 19, 17, 56, 38);

        assertEquals(calendar.getTimeInMillis(), timeResponse.getTimeMillis());
    }

    @Test
    public void testValidFormatUtc() throws Exception {
        String jsonStr = "{\n" +
                "  \"dateString\": \"2015-08-19T17:56:38+00:00\"\n" +
                "}";

        TimeResponse timeResponse = gson.fromJson(jsonStr, TimeResponse.class);

        GregorianCalendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT"), Locale.US);
        calendar.setTimeInMillis(0);
        calendar.set(2015, Calendar.AUGUST, 19, 17, 56, 38);

        assertEquals(calendar.getTimeInMillis(), timeResponse.getTimeMillis());
    }

    @Test(expected = JsonParseException.class)
    public void testInvalidFormat() throws Exception {
        String jsonStr = "{\n" +
                "  \"dateString\": \"2015-08-19INVALID17:56:38+01:00\"\n" +
                "}";

        gson.fromJson(jsonStr, TimeResponse.class);
    }
}
