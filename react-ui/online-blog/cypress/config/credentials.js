// =============================================================
// cypress/config/credentials.js
//
// Credential resolution order (highest wins):
//   1. Jenkins / CI environment variables  (CYPRESS_TEST_USERNAME etc.)
//   2. cypress.env.json                    (local developer overrides)
//   3. Placeholder values below            (never real credentials)
//
// In Jenkins, add these as Secret Text / String credentials and
// inject them via the "withCredentials" or "environment" block:
//
//   environment {
//     CYPRESS_TEST_USERNAME = credentials('cypress-test-username')
//     CYPRESS_TEST_PASSWORD = credentials('cypress-test-password')
//     CYPRESS_API_BASE_URL  = credentials('cypress-api-base-url')
//     CYPRESS_BASE_URL      = credentials('cypress-base-url')
//   }
//
// Cypress automatically picks up any CYPRESS_* env var and makes
// it available via Cypress.env() — no extra config needed.
// =============================================================

module.exports = {
  // Replace these placeholder values in cypress.env.json locally.
  // In Jenkins these are overridden by CYPRESS_* environment variables.
  testUsername: process.env.CYPRESS_TEST_USERNAME || "C105645",
  testPassword: process.env.CYPRESS_TEST_PASSWORD || "Vihaan@2020",
  apiBaseUrl:   process.env.CYPRESS_API_BASE_URL  || "http://localhost:9000",
  baseUrl:      process.env.CYPRESS_BASE_URL       || "http://localhost:3000",
};
