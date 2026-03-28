// =============================================================
// commands.js — custom Cypress commands for online-blog
//
// Credentials come from cypress/config/credentials.js.
// Locally replace placeholder values in cypress.env.json.
// In Jenkins they are injected via CYPRESS_* environment variables.
//
// Auth is stored in localStorage under "user-info" as:
//   { token, username, isAdmin, roles, user: { ... } }
//
// Login form selectors:  #username  #password  button "Sign In"
// =============================================================

/**
 * Login by typing credentials into the real UI form.
 * Reads username and password from Cypress.env() so they are
 * sourced from credentials.js → Jenkins env vars or cypress.env.json.
 *
 * Usage:
 *   cy.login()                           // uses testUsername / testPassword from config
 *   cy.login("otheruser", "OtherPass@1") // explicit override for edge-case tests
 */
Cypress.Commands.add("login", (
  username = Cypress.env("testUsername"),
  password = Cypress.env("testPassword")
) => {
  cy.visit("/login");

  // Guard: fail fast if credentials are still placeholders
  if (!username || username.startsWith("REPLACE_ME")) {
    throw new Error(
      "Login credentials are not set. " +
      "Replace placeholder values in cypress.env.json locally, " +
      "or set CYPRESS_TEST_USERNAME / CYPRESS_TEST_PASSWORD in Jenkins."
    );
  }

  cy.get("#username").should("be.visible").clear().type(username);
  cy.get("#password").should("be.visible").clear().type(password);
  cy.get("button").contains("Sign In").click();

  // Wait for successful redirect away from /login
  cy.url().should("not.include", "/login");
});

/**
 * Make an authenticated API request using the stored JWT token.
 * Token is read from Cypress.env("authToken") which is set after
 * a successful login by intercepting the response.
 *
 * Usage: cy.authRequest({ method: "GET", url: "/post" })
 */
Cypress.Commands.add("authRequest", (options) => {
  const token = Cypress.env("authToken");
  return cy.request({
    ...options,
    url: `${Cypress.env("apiBaseUrl")}${options.url}`,
    headers: {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json",
      ...(options.headers ?? {}),
    },
    failOnStatusCode: options.failOnStatusCode ?? false,
  });
});

/**
 * Assert the current user is authenticated (localStorage populated).
 */
Cypress.Commands.add("assertLoggedIn", () => {
  cy.window().then((win) => {
    const userInfo = JSON.parse(win.localStorage.getItem("user-info") ?? "null");
    expect(userInfo).to.not.be.null;
    expect(userInfo).to.have.property("token");
  });
});

/**
 * Capture the JWT token from the login API response and store it
 * in Cypress.env("authToken") for use with cy.authRequest().
 * Call this once in a Background step after cy.login().
 */
Cypress.Commands.add("captureAuthToken", () => {
  cy.window().then((win) => {
    const userInfo = JSON.parse(win.localStorage.getItem("user-info") ?? "null");
    if (userInfo?.token) {
      Cypress.env("authToken", userInfo.token);
    }
  });
});

/**
 * Log out by clearing localStorage and navigating to /login.
 */
Cypress.Commands.add("logout", () => {
  cy.window().then((win) => win.localStorage.removeItem("user-info"));
  cy.visit("/login");
});
