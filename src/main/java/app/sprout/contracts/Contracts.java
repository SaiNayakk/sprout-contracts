package app.sprout.contracts;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;

/**
 * Classpath locations of Sprout's contracts. Services depend on a pinned version of this
 * artifact and load specs from here, so a service and its tests always agree on the same
 * contract version.
 */
public final class Contracts {

    /** OpenAPI spec for the identity service. */
    public static final String IDENTITY_V1 = "sprout/contracts/openapi/identity-v1.yaml";

    /** OpenAPI spec for the market-data service. */
    public static final String MARKETDATA_V1 = "sprout/contracts/openapi/marketdata-v1.yaml";

    /** OpenAPI spec for the accounts service. */
    public static final String ACCOUNTS_V1 = "sprout/contracts/openapi/accounts-v1.yaml";

    /** OpenAPI spec for the ledger (internal). */
    public static final String LEDGER_V1 = "sprout/contracts/openapi/ledger-v1.yaml";

    /** OpenAPI spec for the payments service. */
    public static final String PAYMENTS_V1 = "sprout/contracts/openapi/payments-v1.yaml";

    /** OpenAPI spec for Sprout Bank, the simulated bank. */
    public static final String BANK_V1 = "sprout/contracts/openapi/bank-v1.yaml";

    /** OpenAPI spec for the order management service (orders, holdings, positions, funds). */
    public static final String OMS_V1 = "sprout/contracts/openapi/oms-v1.yaml";

    /** OpenAPI spec for the Sprout Stock Exchange's member API, the simulated exchange. */
    public static final String EXCHANGE_V1 = "sprout/contracts/openapi/exchange-v1.yaml";

    /** OpenAPI spec for the Sprout Depository, the simulated securities depository. */
    public static final String DEPOSITORY_V1 = "sprout/contracts/openapi/depository-v1.yaml";

    /** OpenAPI spec for the Sprout Clearing Corporation, the simulated clearing corporation. */
    public static final String CLEARING_V1 = "sprout/contracts/openapi/clearing-v1.yaml";

    /** OpenAPI spec for Sprout's settlement back office (internal, and the clearing corporation's callback). */
    public static final String SETTLEMENT_V1 = "sprout/contracts/openapi/settlement-v1.yaml";

    /** OpenAPI spec for the statements service: contract notes, funds statements, P&amp;L, holdings statements. */
    public static final String STATEMENTS_V1 = "sprout/contracts/openapi/statements-v1.yaml";

    /** OpenAPI spec for daily reconciliation (internal). */
    public static final String RECON_V1 = "sprout/contracts/openapi/recon-v1.yaml";

    /** OpenAPI spec for systematic investment plans (SIPs). */
    public static final String PLANS_V1 = "sprout/contracts/openapi/plans-v1.yaml";

    /** OpenAPI spec for habits: streaks, levels, badges, points, squads, readiness, Future You. */
    public static final String HABITS_V1 = "sprout/contracts/openapi/habits-v1.yaml";

    /** OpenAPI spec for goals: pots invested in a share, and round-ups. */
    public static final String GOALS_V1 = "sprout/contracts/openapi/goals-v1.yaml";

    /** OpenAPI spec for rewards: the vault and referrals. */
    public static final String REWARDS_V1 = "sprout/contracts/openapi/rewards-v1.yaml";

    /** OpenAPI spec for the public sandbox: fictional customers to explore as. */
    public static final String SANDBOX_V1 = "sprout/contracts/openapi/sandbox-v1.yaml";

    /** JSON Schema for the marketdata.tick event, version 1. */
    public static final String TICK_V1 = "sprout/contracts/events/marketdata/tick.v1.schema.json";

    /** JSON Schema for the identity.user.registered event, version 1. */
    public static final String USER_REGISTERED_V1 = "sprout/contracts/events/identity/user-registered.v1.schema.json";

    private Contracts() {}

    /** Reads a contract file from the classpath as UTF-8 text. */
    public static String read(String path) {
        try (InputStream in = Contracts.class.getClassLoader().getResourceAsStream(path)) {
            if (in == null) {
                throw new IllegalArgumentException("No contract at " + path);
            }
            return new String(in.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
