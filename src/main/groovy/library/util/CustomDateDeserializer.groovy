package library.util

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import org.springframework.stereotype.Component

import java.text.ParseException
import java.text.SimpleDateFormat

@Component
public class CustomDateDeserializer extends StdDeserializer<Date> {


    private static final long serialVersionUID = 1L
    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS'Z'",Locale.getDefault())


    public CustomDateDeserializer() {
        this(null)
    }

    public CustomDateDeserializer(Class<?> vc) {
        super(vc)
    }

    @Override
    public Date deserialize(JsonParser jsonparser, DeserializationContext context)
            throws IOException, JsonProcessingException {
        String date = jsonparser.getText()
        try {
            return formatter.parse(date)
        } catch (ParseException e) {
            throw new RuntimeException(e)
        }
    }
}
