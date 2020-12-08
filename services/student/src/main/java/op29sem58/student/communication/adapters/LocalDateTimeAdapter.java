package op29sem58.student.communication.adapters;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.time.LocalDateTime;

public class LocalDateTimeAdapter implements
        JsonDeserializer<LocalDateTime>, JsonSerializer<LocalDateTime> {
    @Override
    public LocalDateTime deserialize(
            JsonElement json, Type typeOfT, JsonDeserializationContext context
    ) throws JsonParseException {
        return LocalDateTime.parse(json.getAsString());
    }

    @Override
    public JsonElement serialize(
            LocalDateTime src, Type typeOfSrc, JsonSerializationContext context
    ) {
        return new JsonPrimitive(src.toString());
    }
}