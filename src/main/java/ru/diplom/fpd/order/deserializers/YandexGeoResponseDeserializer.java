package ru.diplom.fpd.order.deserializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import java.io.IOException;
import java.util.stream.StreamSupport;
import ru.diplom.fpd.order.dto.yandex.GeoObject;
import ru.diplom.fpd.order.dto.yandex.GeoObjectCollection;

public class YandexGeoResponseDeserializer extends StdDeserializer<GeoObjectCollection> {

    protected YandexGeoResponseDeserializer() {
        super(GeoObjectCollection.class);
    }

    protected YandexGeoResponseDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public GeoObjectCollection deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException {

        JsonNode node = jp.getCodec().readTree(jp);
        ArrayNode root = node.get("response").get("GeoObjectCollection").withArray("featureMember");


        return new GeoObjectCollection(StreamSupport.stream(root.spliterator(), false)
                .map(item -> item.get("GeoObject"))
                .map(item -> GeoObject.builder()
                        .name(item.get("name").asText())
                        .description(item.get("description").asText())
                        .point(item.get("Point").get("pos").asText())
                        .build()
                ).toList());

    }
}
