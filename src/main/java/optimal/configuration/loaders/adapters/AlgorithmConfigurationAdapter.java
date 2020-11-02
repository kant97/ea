package optimal.configuration.loaders.adapters;

import com.google.gson.*;
import optimal.configuration.algorithms.AlgorithmConfig;
import optimal.configuration.algorithms.TwoRateConfig;
import optimal.oneStepAlgorithms.OneStepAlgorithmsManager;

import java.lang.reflect.Type;

public class AlgorithmConfigurationAdapter implements JsonDeserializer<AlgorithmConfig> {
    @Override
    public AlgorithmConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        final String stringAlgorithmType = jsonObject.get("type").getAsString();
        final OneStepAlgorithmsManager.AlgorithmType algorithmType =
                OneStepAlgorithmsManager.AlgorithmType.valueOf(stringAlgorithmType);
        final int lambda = jsonObject.get("lambda").getAsInt();
        if (algorithmType == OneStepAlgorithmsManager.AlgorithmType.TWO_RATE) {
            return context.deserialize(json, TwoRateConfig.class);
        }
        return new AlgorithmConfig(algorithmType, lambda);
    }
}
