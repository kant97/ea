package optimal.configuration.loaders.adapters;

import com.google.gson.*;
import optimal.configuration.vectorGeneration.PrecomputedVectorReadingConfiguration;
import optimal.configuration.vectorGeneration.RunTimeGenerationConfiguration;
import optimal.configuration.vectorGeneration.VectorGenerationConfiguration;

import java.lang.reflect.Type;

public class VectorGenerationConfigurationAdapter implements JsonDeserializer<VectorGenerationConfiguration>,
        JsonSerializer<VectorGenerationConfiguration> {
    @Override
    public VectorGenerationConfiguration deserialize(JsonElement json, Type typeOfT,
                                                     JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        final String jsonStrategy = jsonObject.get("strategy").getAsString();
        final VectorGenerationConfiguration.VectorGenerationStrategy strategy =
                VectorGenerationConfiguration.VectorGenerationStrategy.valueOf(jsonStrategy);
        switch (strategy) {
            case RUN_TIME_VECTOR_GENERATION:
                return context.deserialize(json, RunTimeGenerationConfiguration.class);
            case PRECOMPUTED_VECTOR_READING:
                return context.deserialize(json, PrecomputedVectorReadingConfiguration.class);
            default:
                throw new IllegalStateException("Strategy " + strategy + " is not supported");
        }
    }

    @Override
    public JsonElement serialize(VectorGenerationConfiguration src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonElement serialize = context.serialize(src);
        serialize.getAsJsonObject().addProperty("strategy", src.getStrategy().name());
        return serialize;
    }
}
