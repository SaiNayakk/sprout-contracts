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
