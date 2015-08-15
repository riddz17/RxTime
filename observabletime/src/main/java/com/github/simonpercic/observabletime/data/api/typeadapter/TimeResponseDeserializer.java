package com.github.simonpercic.observabletime.data.api.typeadapter;

import com.github.simonpercic.observabletime.data.api.model.response.TimeResponse;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Simon Percic on 15/08/15.
 */
public class TimeResponseDeserializer implements JsonDeserializer<TimeResponse> {

    private final SimpleDateFormat simpleDateFormat;
    private final Type mapType;

    public TimeResponseDeserializer() {
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ", Locale.US);

        mapType = new TypeToken<Map<String, String>>() {
        }.getType();
    }

    @Override public TimeResponse deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {

        Map<String, String> map = context.deserialize(json, mapType);

        String dateString = map.get("dateString");

        try {
            Date date = simpleDateFormat.parse(dateString);
            return new TimeResponse(date.getTime());
        } catch (ParseException e) {
            throw new JsonParseException(e);
        }
    }
}
