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
    void marketDataSpecParsesWithoutErrors() {
        SwaggerParseResult result = new OpenAPIV3Parser().readContents(Contracts.read(Contracts.MARKETDATA_V1));
        assertThat(result.getMessages()).isEmpty();
        assertThat(result.getOpenAPI().getPaths()).containsKeys(
                "/v1/market", "/v1/instruments", "/v1/instruments/{symbol}", "/v1/quotes",
                "/v1/candles/{symbol}", "/v1/stream");
    }

    @Test
    void moneySpecsParseWithoutErrors() {
        Object[][] specs = {
                {Contracts.ACCOUNTS_V1, new String[] {"/v1/accounts", "/v1/accounts/me"}},
                {Contracts.LEDGER_V1, new String[] {"/v1/journal-entries", "/v1/accounts/{account}", "/v1/trial-balance"}},
                {Contracts.PAYMENTS_V1, new String[] {"/v1/balance", "/v1/deposits", "/v1/withdrawals", "/internal/v1/bank-events"}},
                {Contracts.BANK_V1, new String[] {"/v1/accounts", "/v1/requests/{id}/approve", "/partner/v1/collect-requests",
                        "/partner/v1/payouts"}}};
        for (Object[] spec : specs) {
            SwaggerParseResult result = new OpenAPIV3Parser().readContents(Contracts.read((String) spec[0]));
            assertThat(result.getMessages()).as((String) spec[0]).isEmpty();
            assertThat(result.getOpenAPI().getPaths()).as((String) spec[0]).containsKeys((String[]) spec[1]);
        }
    }

    @Test
    void tradingSpecsParseWithoutErrors() {
        Object[][] specs = {
                {Contracts.OMS_V1, new String[] {"/v1/orders", "/v1/orders/{id}", "/v1/holdings", "/v1/positions", "/v1/funds",
                        "/internal/v1/exchange-events"}},
                {Contracts.EXCHANGE_V1, new String[] {"/member/v1/orders", "/member/v1/orders/{clientOrderId}"}}};
        for (Object[] spec : specs) {
            SwaggerParseResult result = new OpenAPIV3Parser().readContents(Contracts.read((String) spec[0]));
            assertThat(result.getMessages()).as((String) spec[0]).isEmpty();
            assertThat(result.getOpenAPI().getPaths()).as((String) spec[0]).containsKeys((String[]) spec[1]);
        }
    }

    @Test
    void everyOperationHasAnId() {
        for (String spec : new String[] {Contracts.IDENTITY_V1, Contracts.MARKETDATA_V1, Contracts.ACCOUNTS_V1,
                Contracts.LEDGER_V1, Contracts.PAYMENTS_V1, Contracts.BANK_V1}) {
            var openApi = new OpenAPIV3Parser().readContents(Contracts.read(spec)).getOpenAPI();
            openApi.getPaths().forEach((path, item) -> item.readOperations()
                    .forEach(op -> assertThat(op.getOperationId()).as(spec + " " + path).isNotBlank()));
        }
    }

    @Test
    void tickExampleMatchesItsSchema() throws Exception {
        JsonSchema schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012)
                .getSchema(Contracts.read(Contracts.TICK_V1));
        JsonNode example = json.readTree(Contracts.read("sprout/contracts/events/marketdata/tick.v1.example.json"));
        assertThat(schema.validate(example)).isEmpty();
    }

    @Test
    void tickRejectsAZeroPriceAndUnknownFields() throws Exception {
        JsonSchema schema = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V202012)
                .getSchema(Contracts.read(Contracts.TICK_V1));
        var example = (com.fasterxml.jackson.databind.node.ObjectNode) json.readTree(
                Contracts.read("sprout/contracts/events/marketdata/tick.v1.example.json"));
        assertThat(schema.validate(example.deepCopy().put("price", 0))).isNotEmpty();
        assertThat(schema.validate(example.deepCopy().put("exchange", "NSE"))).isNotEmpty();
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
