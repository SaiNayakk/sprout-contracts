# sprout-contracts

The agreements between Sprout's services: OpenAPI specs for every HTTP API and JSON Schemas for every event. A service may only talk to another through what's written here.

Part of **Sprout**, a simulated end-to-end brokerage built to learn how institutional systems work. Architecture, environments and test evidence live in [sprout-platform](https://github.com/SaiNayakk/sprout-platform).

## What's here

| Contract | File | Owner |
|---|---|---|
| Identity API v1 | `src/main/resources/sprout/contracts/openapi/identity-v1.yaml` | sprout-identity |
| Market Data API v1 | `src/main/resources/sprout/contracts/openapi/marketdata-v1.yaml` | sprout-marketdata |
| Accounts API v1 | `src/main/resources/sprout/contracts/openapi/accounts-v1.yaml` | sprout-accounts |
| Ledger API v1 (internal) | `src/main/resources/sprout/contracts/openapi/ledger-v1.yaml` | sprout-ledger |
| Payments API v1 | `src/main/resources/sprout/contracts/openapi/payments-v1.yaml` | sprout-payments |
| Sprout Bank API v1 (simulated bank) | `src/main/resources/sprout/contracts/openapi/bank-v1.yaml` | sprout-bank |
| Orders API v1 | `src/main/resources/sprout/contracts/openapi/oms-v1.yaml` | sprout-oms |
| Sprout Stock Exchange member API v1 (simulated exchange) | `src/main/resources/sprout/contracts/openapi/exchange-v1.yaml` | sprout-exchange |
| Sprout Depository API v1 (simulated depository) | `src/main/resources/sprout/contracts/openapi/depository-v1.yaml` | sprout-depository |
| Sprout Clearing Corporation API v1 (simulated clearing corporation) | `src/main/resources/sprout/contracts/openapi/clearing-v1.yaml` | sprout-clearing |
| Settlement API v1 (internal back office) | `src/main/resources/sprout/contracts/openapi/settlement-v1.yaml` | sprout-settlement |
| Statements API v1 | `src/main/resources/sprout/contracts/openapi/statements-v1.yaml` | sprout-statements |
| Reconciliation API v1 (internal) | `src/main/resources/sprout/contracts/openapi/recon-v1.yaml` | sprout-recon |
| Plans API v1 (SIPs) | `src/main/resources/sprout/contracts/openapi/plans-v1.yaml` | sprout-plans |
| Habits API v1 | `src/main/resources/sprout/contracts/openapi/habits-v1.yaml` | sprout-habits |
| `marketdata.tick` v1 | `src/main/resources/sprout/contracts/events/marketdata/tick.v1.schema.json` | sprout-marketdata |
| `identity.user.registered` v1 | `src/main/resources/sprout/contracts/events/identity/user-registered.v1.schema.json` | sprout-identity |

## Using it

Releases are git tags. Each one is built, tested and published to this repo's own Maven repository on
GitHub Pages (no login needed to download). Add the repository and pin a version:

```xml
<repositories>
  <repository><id>sprout-contracts</id><url>https://sainayakk.github.io/sprout-contracts/maven</url></repository>
</repositories>

<dependency>
  <groupId>com.github.SaiNayakk</groupId>
  <artifactId>sprout-contracts</artifactId>
  <version>0.3.2</version>
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
