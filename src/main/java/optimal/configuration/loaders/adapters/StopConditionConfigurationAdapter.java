package optimal.configuration.loaders.adapters;

import com.google.gson.*;
import optimal.configuration.runs.FixedRunsConfiguration;
import optimal.configuration.runs.FixedSuccessConfiguration;
import optimal.configuration.runs.StopConditionConfiguration;

import java.lang.reflect.Type;

public class StopConditionConfigurationAdapter implements JsonDeserializer<StopConditionConfiguration>,
        JsonSerializer<StopConditionConfiguration> {
    @Override
    public StopConditionConfiguration deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        final String jsonStrategy = jsonObject.get("strategy").getAsString();
        final StopConditionConfiguration.Strategy strategy = StopConditionConfiguration.Strategy.valueOf(jsonStrategy);
        switch (strategy) {
            case FIXED_SUCCESS:
                return context.deserialize(json, FixedSuccessConfiguration.class);
            case FIXED_RUNS:
                return context.deserialize(json, FixedRunsConfiguration.class);
            default:
                throw new IllegalStateException("Strategy " + strategy + " is not supported");
        }
    }

    @Override
    public JsonElement serialize(StopConditionConfiguration src, Type typeOfSrc, JsonSerializationContext context) {
        final JsonElement serialize = context.serialize(src);
        serialize.getAsJsonObject().addProperty("strategy", src.getMyStrategy().name());
        return serialize;
    }
}
