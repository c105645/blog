// =============================================================
// e2e.js — loaded automatically before every spec
// =============================================================

import "./commands";

// Suppress React's uncaught ResizeObserver noise
Cypress.on("uncaught:exception", (err) => {
  if (err.message.includes("ResizeObserver loop")) return false;
  return true;
});

beforeEach(function () {
  // Clear auth state before every test so sessions don't bleed between specs
  localStorage.removeItem("user-info");
});
