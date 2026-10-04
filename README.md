# sprout-contracts

The agreements between Sprout's services: OpenAPI specs for every HTTP API and JSON Schemas for every event. A service may only talk to another through what's written here.

Part of **Sprout**, a simulated end-to-end brokerage built to learn how institutional systems work. Architecture, environments and test evidence live in [sprout-platform](https://github.com/SaiNayakk/sprout-platform).

## What's here

| Contract | File | Owner |
|---|---|---|
| Identity API v1 | `src/main/resources/sprout/contracts/openapi/identity-v1.yaml` | sprout-identity |
| Market Data API v1 | `src/main/resources/sprout/contracts/openapi/marketdata-v1.yaml` | sprout-marketdata |
| `marketdata.tick` v1 | `src/main/resources/sprout/contracts/events/marketdata/tick.v1.schema.json` | sprout-marketdata |
| `identity.user.registered` v1 | `src/main/resources/sprout/contracts/events/identity/user-registered.v1.schema.json` | sprout-identity |

## Using it

Releases are git tags, built by [JitPack](https://jitpack.io/#SaiNayakk/sprout-contracts). Add the repository and pin a version:

```xml
<repositories>
  <repository><id>jitpack.io</id><url>https://jitpack.io</url></repository>
</repositories>

<dependency>
  <groupId>com.github.SaiNayakk</groupId>
  <artifactId>sprout-contracts</artifactId>
  <version>v0.1.0</version>
</dependency>
```

Load a spec with `Contracts.read(Contracts.IDENTITY_V1)`. Services validate their own responses against these files in their tests, so a service can't drift from its contract unnoticed.

## Versioning rules

- **Patch** (`v0.1.1`): wording and examples only.
- **Minor** (`v0.2.0`): additive changes: new endpoints, new optional fields, new event types.
- **Breaking changes never edit a published file.** They add a new major version beside it (`identity-v2.yaml`, `user-registered.v2.schema.json`); producers publish both until every consumer has moved.
- CI compares every pull request's specs with the last release and **fails on an incompatible change**.
- Events are delivered at least once. Every event carries an `eventId`; consumers must ignore one they've already processed.

## License

MIT
