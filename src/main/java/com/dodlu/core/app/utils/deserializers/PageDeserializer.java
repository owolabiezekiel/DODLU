package com.dodlu.core.app.utils.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

public class PageDeserializer<T> extends JsonDeserializer<Page<T>> {
  private final ObjectMapper objectMapper;
  private final TypeReference<T> valueTypeRef;

  public PageDeserializer(ObjectMapper objectMapper, TypeReference<T> valueTypeRef) {
    this.objectMapper = objectMapper;
    this.valueTypeRef = valueTypeRef;
  }

  @Override
  public Page<T> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
      throws IOException {
    ObjectCodec codec = jsonParser.getCodec();
    ObjectNode rootNode = codec.readTree(jsonParser);

    JsonNode contentNode = rootNode.get("content");
    ArrayNode elementsNode =
        contentNode.isArray() ? (ArrayNode) contentNode : objectMapper.createArrayNode();

    List<T> elements = new ArrayList<>(elementsNode.size());
    for (JsonNode elementNode : elementsNode) {
      T element = objectMapper.convertValue(elementNode, valueTypeRef);
      elements.add(element);
    }

    int number = rootNode.get("number").asInt();
    int size = rootNode.get("size").asInt();
    long totalElements = rootNode.get("totalElements").asLong();

    PageRequest pageRequest = PageRequest.of(number, size);
    return new PageImpl<>(elements, pageRequest, totalElements);
  }
}
