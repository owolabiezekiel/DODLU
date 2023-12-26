package com.dodlu.core.app.utils;

import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS;

import com.dodlu.core.app.model.PagingAndSortingParams;
import com.dodlu.core.app.utils.deserializers.PageDeserializer;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@UtilityClass
public class MapperUtil {
  public static final ObjectMapper MAPPER;

  static {
    MAPPER =
        new ObjectMapper()
            .disable(FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(FAIL_ON_EMPTY_BEANS)
            .disable(WRITE_DATES_AS_TIMESTAMPS)
            .registerModule(new JavaTimeModule());

    SimpleModule module = new SimpleModule();
    module.addDeserializer(Page.class, new PageDeserializer<>(MAPPER, new TypeReference<>() {}));
    module.addDeserializer(String.class, new EmptyObjectToStringDeserializer());
    MAPPER.registerModule(module);
  }

  public static class EmptyObjectToStringDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
        throws IOException {
      JsonNode node = jsonParser.readValueAsTree();
      if (node.isObject() && node.size() == 0) {
        return null;
      }
      return node.asText();
    }
  }

  public static Pageable buildPageable(PagingAndSortingParams paging) {
    Sort.Direction direction =
        "asc".equalsIgnoreCase(paging.getOrder()) ? Sort.Direction.ASC : Sort.Direction.DESC;
    return PageRequest.of(paging.getPage(), paging.getSize(), Sort.by(direction, paging.getSort()));
  }
}
