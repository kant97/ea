package optimal.configuration.loaders.adapters;

import com.google.gson.*;
import optimal.configuration.problems.ProblemConfig;
import optimal.configuration.problems.RuggednessConfig;
import problem.ProblemsManager;

import java.lang.reflect.Type;

public class ProblemConfigurationAdapter implements JsonDeserializer<ProblemConfig> {
    @Override
    public ProblemConfig deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        final JsonObject jsonObject = json.getAsJsonObject();
        final String stringProblemType = jsonObject.get("type").getAsString();
        final ProblemsManager.ProblemType problemType = ProblemsManager.ProblemType.valueOf(stringProblemType);
        final int problemSize = jsonObject.get("size").getAsInt();
        if (problemType == ProblemsManager.ProblemType.ONE_MAX_RUGGEDNESS) {
            final int r = jsonObject.get("r").getAsInt();
            return new RuggednessConfig(problemType, problemSize, r);
        }
        return new ProblemConfig(problemType, problemSize);
    }
}
