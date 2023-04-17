package pl.twojekursy.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.data.domain.PageImpl;

import java.io.IOException;

public class PageImplSerializer extends JsonSerializer<PageImpl> {
    @Override
    public void serialize(PageImpl value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeObjectField("content", value.getContent());
        gen.writeNumberField("totalElements", value.getTotalElements());
        gen.writeEndObject();
    }
}
