# Getting Started with Create React App

This project was bootstrapped with [Create React App](https://github.com/facebook/create-react-app).

## Available Scripts

In the project directory, you can run:

### `npm start`

Runs the app in the development mode.\
Open [http://localhost:3000](http://localhost:3000) to view it in your browser.

The page will reload when you make changes.\
You may also see any lint errors in the console.

### `npm test`

Launches the test runner in the interactive watch mode.\
See the section about [running tests](https://facebook.github.io/create-react-app/docs/running-tests) for more information.

### `npm run build`

Builds the app for production to the `build` folder.\
It correctly bundles React in production mode and optimizes the build for the best performance.

The build is minified and the filenames include the hashes.\
Your app is ready to be deployed!

See the section about [deployment](https://facebook.github.io/create-react-app/docs/deployment) for more information.

### `npm run eject`

**Note: this is a one-way operation. Once you `eject`, you can't go back!**

If you aren't satisfied with the build tool and configuration choices, you can `eject` at any time. This command will remove the single build dependency from your project.

Instead, it will copy all the configuration files and the transitive dependencies (webpack, Babel, ESLint, etc) right into your project so you have full control over them. All of the commands except `eject` will still work, but they will point to the copied scripts so you can tweak them. At this point you're on your own.

You don't have to ever use `eject`. The curated feature set is suitable for small and middle deployments, and you shouldn't feel obligated to use this feature. However we understand that this tool wouldn't be useful if you couldn't customize it when you are ready for it.

## Learn More

You can learn more in the [Create React App documentation](https://facebook.github.io/create-react-app/docs/getting-started).

To learn React, check out the [React documentation](https://reactjs.org/).

### Code Splitting

This section has moved here: [https://facebook.github.io/create-react-app/docs/code-splitting](https://facebook.github.io/create-react-app/docs/code-splitting)

### Analyzing the Bundle Size

This section has moved here: [https://facebook.github.io/create-react-app/docs/analyzing-the-bundle-size](https://facebook.github.io/create-react-app/docs/analyzing-the-bundle-size)

### Making a Progressive Web App

This section has moved here: [https://facebook.github.io/create-react-app/docs/making-a-progressive-web-app](https://facebook.github.io/create-react-app/docs/making-a-progressive-web-app)

### Advanced Configuration

This section has moved here: [https://facebook.github.io/create-react-app/docs/advanced-configuration](https://facebook.github.io/create-react-app/docs/advanced-configuration)

### Deployment

This section has moved here: [https://facebook.github.io/create-react-app/docs/deployment](https://facebook.github.io/create-react-app/docs/deployment)

### `npm run build` fails to minify

This section has moved here: [https://facebook.github.io/create-react-app/docs/troubleshooting#npm-run-build-fails-to-minify](https://facebook.github.io/create-react-app/docs/troubleshooting#npm-run-build-fails-to-minify)

---

## E2E Testing — Cypress v13 + Cucumber

### Prerequisites

- Node.js 18+
- The backend stack must be running (`./run.sh` from the backend project)
- The React dev server must be running (`npm start`)

### Install test dependencies

```bash
npm install
```

### Run the tests

```bash
# Open Cypress Test Runner — interactive, great for development
npm run cy:open

# Run all tests headlessly
npm run cy:run

# Run with a visible browser window
npm run cy:run:headed

# Run in Chrome
npm run cy:run:chrome

# Run only @smoke tagged tests
npm run cy:run:tags -- @smoke

# Run only @auth tagged tests
npm run cy:run:tags -- @auth
```

### Project structure

```
online-blog/
├── cypress.config.js                          ← Cypress + Cucumber config
├── cypress.env.json                           ← Local credential overrides (gitignored)
└── cypress/
    ├── support/
    │   ├── e2e.js                             ← Global setup (clears localStorage before each test)
    │   └── commands.js                        ← Custom commands
    └── e2e/
        ├── features/                          ← Gherkin .feature files
        │   ├── auth/
        │   │   ├── login.feature
        │   │   └── register.feature
        │   ├── home/
        │   │   └── home.feature
        │   └── posts/
        │       └── post-creator.feature
        └── step-definitions/
            ├── shared/
            │   └── common.steps.js            ← Shared steps (login state, navigation)
            ├── auth/
            │   ├── login.steps.js
            │   └── register.steps.js
            ├── home/
            │   └── home.steps.js
            └── posts/
                └── post-creator.steps.js
```

### Available custom commands

| Command | Description |
|---|---|
| `cy.loginViaUi(username, password)` | Fills and submits the real Login form, waits for redirect |
| `cy.loginViaApi(username, password)` | Calls the login API directly, seeds `localStorage` — faster for non-auth tests |
| `cy.authRequest(options)` | `cy.request()` with `Authorization: Bearer <token>` auto-attached |
| `cy.assertLoggedIn()` | Asserts `user-info` is present in `localStorage` with a valid token |
| `cy.logout()` | Clears `localStorage` and navigates to `/login` |

### Available tags

| Tag | Scope |
|---|---|
| `@smoke` | Critical happy-path tests |
| `@auth` | All authentication scenarios |
| `@login-success` | Successful login |
| `@login-failure` | Login failure cases |
| `@register` | Registration scenarios |
| `@register-success` | Successful registration |
| `@register-failure` | Registration failure cases |
| `@home` | Home page scenarios |
| `@posts` | Post management scenarios |
| `@create-post` | Post creation scenarios |

### Environment variable overrides

Edit `cypress.env.json` locally (gitignored) or pass via `--env`:

```bash
npx cypress run --env testUsername=myuser,testPassword=MyPass@1
```

| Variable | Default | Description |
|---|---|---|
| `testUsername` | `testuser` | Username for API login in Background steps |
| `testPassword` | `Test@12345` | Password for API login in Background steps |
| `apiBaseUrl` | `http://localhost:9000/api/v1` | Edge gateway base URL |

### Writing new tests

**1. Add a feature file** under `cypress/e2e/features/<domain>/`:

```gherkin
@search @smoke
Feature: Search Posts

  Background:
    Given I am logged in

  Scenario: Search returns results for a known topic
    When I navigate to "/search"
    And I type "React" into the search box
    Then I should see at least one result
```

**2. Add step definitions** under `cypress/e2e/step-definitions/<domain>/`:

```javascript
import { When, Then } from "@badeball/cypress-cucumber-preprocessor";

When("I type {string} into the search box", (query) => {
  cy.get("input[type='search'], input[placeholder*='search' i]")
    .first()
    .clear()
    .type(query);
});

Then("I should see at least one result", () => {
  cy.get(".post-tile, .search-result").should("have.length.greaterThan", 0);
});
```

Steps from `shared/common.steps.js` (`Given I am logged in`, `When I navigate to`, etc.) are available in every feature file automatically — no imports needed.
