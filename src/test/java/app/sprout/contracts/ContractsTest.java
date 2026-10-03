package app.sprout.contracts;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import io.swagger.v3.parser.OpenAPIV3Parser;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ContractsTest {

    private final ObjectMapper json = new ObjectMapper();

    @Test
    void identitySpecParsesWithoutErrors() {
        SwaggerParseResult result = new OpenAPIV3Parser().readContents(Contracts.read(Contracts.IDENTITY_V1));
        assertThat(result.getMessages()).isEmpty();
        assertThat(result.getOpenAPI().getPaths()).containsKeys(
                "/v1/users", "/v1/users/me", "/v1/sessions", "/v1/sessions/totp",
                "/v1/sessions/current", "/v1/tokens/refresh", "/v1/users/me/totp",
                "/v1/users/me/totp/confirm", "/.well-known/jwks.json");
    }

    @Test
    void everyOperationHasAnId() {
        var openApi = new OpenAPIV3Parser().readContents(Contracts.read(Contracts.IDENTITY_V1)).getOpenAPI();
        openApi.getPaths().forEach((path, item) -> item.readOperations()
                .forEach(op -> assertThat(op.getOperationId()).as(path).isNotBlank()));
    }

    @Test
    void userRegisteredExampleMatchesItsSchema() throws Exception {
        JsonSchema schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012)
                .getSchema(Contracts.read(Contracts.USER_REGISTERED_V1));
        JsonNode example = json.readTree(Contracts.read(
                "sprout/contracts/events/identity/user-registered.v1.example.json"));
        Set<ValidationMessage> errors = schema.validate(example);
        assertThat(errors).isEmpty();
    }

    @Test
    void userRegisteredRejectsUnknownFields() throws Exception {
        JsonSchema schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012)
                .getSchema(Contracts.read(Contracts.USER_REGISTERED_V1));
        JsonNode bad = json.readTree("""
                {"eventId":"0b9a3c34-6f4e-4f7e-9a51-3d2f8f0d9c11","eventType":"identity.user.registered",
                 "eventVersion":1,"occurredAt":"2026-10-04T18:30:00Z","producer":"sprout-identity",
                 "data":{"userId":"5d1f2b8e-2a47-4c39-9e1b-7c0f6a2d4e90","email":"a@b.co","displayName":"A","password":"x"}}
                """);
        assertThat(schema.validate(bad)).isNotEmpty();
    }
}
