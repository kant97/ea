package optimal.configuration.loaders.adapters;

import com.google.gson.*;
import optimal.configuration.transitionsGeneration.PrecomputedTransitionsReadingConfiguration;
import optimal.configuration.transitionsGeneration.RunTimeTransitionsGenerationConfiguration;
import optimal.configuration.transitionsGeneration.TransitionsGenerationConfiguration;

import java.lang.reflect.Type;

public class TransitionsGenerationConfigurationAdapter implements JsonDeserializer<TransitionsGenerationConfiguration>, JsonSerializer<TransitionsGenerationConfiguration>  {
    @Override
    public TransitionsGenerationConfiguration deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        final String jsonStrategy = jsonObject.get("strategy").getAsString();
        final TransitionsGenerationConfiguration.TransitionsGenerationStrategy strategy = TransitionsGenerationConfiguration.TransitionsGenerationStrategy.valueOf(jsonStrategy);
        switch (strategy) {
            case RUN_TIME_TRANSITIONS_GENERATION:
                return context.deserialize(json, RunTimeTransitionsGenerationConfiguration.class);
            case PRECOMPUTED_TRANSITIONS_READING:
                return context.deserialize(json, PrecomputedTransitionsReadingConfiguration.class);
            default:
                throw new IllegalStateException("Strategy " + strategy + " is not supported");
        }
    }


    @Override
    public JsonElement serialize(TransitionsGenerationConfiguration configuration, Type type, JsonSerializationContext context) {
        final JsonElement serialize = context.serialize(configuration);
        serialize.getAsJsonObject().addProperty("strategy", configuration.getStrategy().name());
        return serialize;
    }
}
