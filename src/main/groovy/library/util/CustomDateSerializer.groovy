package library.util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import org.springframework.stereotype.Component

import java.text.SimpleDateFormat

@Component
public class CustomDateSerializer extends StdSerializer<Date> {

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS'Z'")

    public CustomDateSerializer() {
        this(null)
    }

    public CustomDateSerializer(Class<?> t) {
        super(t)
    }

    @Override
    public void serialize(
            Date value,
            JsonGenerator gen,
            SerializerProvider arg2)
            throws IOException, JsonProcessingException {
        gen.writeString(formatter.format(value))
    }
}