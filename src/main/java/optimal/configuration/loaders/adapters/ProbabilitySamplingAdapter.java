package optimal.configuration.loaders.adapters;

import com.google.gson.*;
import optimal.configuration.probability.ExponentialGridConfiguration;
import optimal.configuration.probability.IterativeProbabilityConfiguration;
import optimal.configuration.probability.ProbabilitySamplingConfiguration;
import optimal.probabilitySampling.ProbabilitySamplingStrategy;

import java.lang.reflect.Type;

public class ProbabilitySamplingAdapter implements JsonSerializer<ProbabilitySamplingConfiguration>,
        JsonDeserializer<ProbabilitySamplingConfiguration> {
    @Override
    public ProbabilitySamplingConfiguration deserialize(JsonElement json, Type typeOfT,
                                                        JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        final ProbabilitySamplingStrategy strategy =
                ProbabilitySamplingStrategy.valueOf(jsonObject.get("strategy").getAsString());
        if (strategy == ProbabilitySamplingStrategy.ITERATIVE) {
            return context.deserialize(json, IterativeProbabilityConfiguration.class);
        } else if (strategy == ProbabilitySamplingStrategy.EXPONENTIAL_GRID) {
            return context.deserialize(json, ExponentialGridConfiguration.class);
        }
        throw new IllegalStateException("Strategy " + strategy.name() + " is not supported");
    }

    @Override
    public JsonElement serialize(ProbabilitySamplingConfiguration src, Type typeOfSrc,
                                 JsonSerializationContext context) {
        final JsonElement serialize = context.serialize(src);
        serialize.getAsJsonObject().addProperty("strategy", src.getStrategy().name());
        return serialize;
    }
}
