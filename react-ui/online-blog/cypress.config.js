const { defineConfig } = require("cypress");
const createBundler = require("@bahmutov/cypress-esbuild-preprocessor");
const {
  addCucumberPreprocessorPlugin,
} = require("@badeball/cypress-cucumber-preprocessor");
const {
  createEsbuildPlugin,
} = require("@badeball/cypress-cucumber-preprocessor/esbuild");

// Credentials resolve from Jenkins env vars → cypress.env.json → placeholders
// See cypress/config/credentials.js for the full resolution order
const credentials = require("./cypress/config/credentials");

async function setupNodeEvents(on, config) {
  await addCucumberPreprocessorPlugin(on, config);

  on(
    "file:preprocessor",
    createBundler({
      plugins: [createEsbuildPlugin(config)],
    })
  );

  return config;
}

module.exports = defineConfig({
  e2e: {
    setupNodeEvents,

    specPattern: "cypress/e2e/features/**/*.feature",

    // baseUrl — overridden by CYPRESS_BASE_URL in Jenkins,
    // or cypress.env.json locally
    baseUrl: credentials.baseUrl,

    defaultCommandTimeout: 10000,
    requestTimeout: 15000,
    responseTimeout: 15000,
    pageLoadTimeout: 30000,

    viewportWidth: 1280,
    viewportHeight: 720,

    video: false,
    screenshotOnRunFailure: true,
    screenshotsFolder: "cypress/reports/screenshots",
    videosFolder: "cypress/reports/videos",

    retries: {
      runMode: 1,   // one retry in CI
      openMode: 0,  // no retries during development
    },

    env: {
      // Credentials loaded from credentials.js
      // Jenkins overrides via CYPRESS_TEST_USERNAME / CYPRESS_TEST_PASSWORD etc.
      testUsername: credentials.testUsername,
      testPassword: credentials.testPassword,
      apiBaseUrl:   credentials.apiBaseUrl,

      // Tag filtering — override with: --env tags=@smoke
      tags: "",
    },
  },
});
