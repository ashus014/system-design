const express = require("express");
const axios = require("axios");
const crypto = require("crypto");

// OAuth configuration
const config = {
  clientId: process.env.CLIENT_ID,
  clientSecret: process.env.CLIENT_SECRET,
  redirectUri: process.env.REDIRECT_URI,
  authorizationEndpoint: "https://accounts.google.com/o/oauth2/v2/auth",
  tokenEndpoint: "https://oauth2.googleapis.com/token",
  scope: "openid email profile",
};

// Store for authorization codes and tokens
const codeStore = new Map();
const tokenStore = new Map();

// Generate random state for CSRF protection
function generateState() {
  return crypto.randomBytes(16).toString("hex");
}

// Generate authorization URL
function getAuthorizationUrl(state) {
  const params = new URLSearchParams({
    client_id: config.clientId,
    redirect_uri: config.redirectUri,
    response_type: "code",
    scope: config.scope,
    state: state,
    access_type: "offline",
    prompt: "consent",
  });

  return `${config.authorizationEndpoint}?${params.toString()}`;
}

// Exchange authorization code for tokens
async function exchangeCodeForTokens(code) {
  try {
    const response = await axios.post(config.tokenEndpoint, {
      code: code,
      client_id: config.clientId,
      client_secret: config.clientSecret,
      redirect_uri: config.redirectUri,
      grant_type: "authorization_code",
    });

    return response.data;
  } catch (error) {
    console.error("Error exchanging code for tokens:", error);
    throw error;
  }
}

// Refresh access token
async function refreshAccessToken(refreshToken) {
  try {
    const response = await axios.post(config.tokenEndpoint, {
      client_id: config.clientId,
      client_secret: config.clientSecret,
      refresh_token: refreshToken,
      grant_type: "refresh_token",
    });

    return response.data;
  } catch (error) {
    console.error("Error refreshing token:", error);
    throw error;
  }
}

// Express middleware to handle OAuth flow
function oauthMiddleware() {
  const router = express.Router();

  // Start OAuth flow
  router.get("/auth", (req, res) => {
    const state = generateState();
    codeStore.set(state, { timestamp: Date.now() });

    const authUrl = getAuthorizationUrl(state);
    res.redirect(authUrl);
  });

  // OAuth callback handler
  router.get("/callback", async (req, res) => {
    const { code, state } = req.query;

    // Verify state to prevent CSRF
    if (!codeStore.has(state)) {
      return res.status(400).send("Invalid state parameter");
    }

    try {
      const tokens = await exchangeCodeForTokens(code);

      // Store tokens
      tokenStore.set(state, {
        accessToken: tokens.access_token,
        refreshToken: tokens.refresh_token,
        expiresAt: Date.now() + tokens.expires_in * 1000,
      });

      // Clean up code store
      codeStore.delete(state);

      res.send("Authentication successful! You can close this window.");
    } catch (error) {
      res.status(500).send("Authentication failed");
    }
  });

  // Middleware to check if token is valid and refresh if needed
  router.use(async (req, res, next) => {
    const state = req.headers["x-oauth-state"];
    if (!state || !tokenStore.has(state)) {
      return res.status(401).send("Unauthorized");
    }

    const tokenData = tokenStore.get(state);

    // Check if token needs refresh
    if (Date.now() >= tokenData.expiresAt) {
      try {
        const newTokens = await refreshAccessToken(tokenData.refreshToken);
        tokenStore.set(state, {
          accessToken: newTokens.access_token,
          refreshToken: tokenData.refreshToken,
          expiresAt: Date.now() + newTokens.expires_in * 1000,
        });
      } catch (error) {
        return res.status(401).send("Token refresh failed");
      }
    }

    // Add token to request for downstream use
    req.oauthToken = tokenStore.get(state).accessToken;
    next();
  });

  return router;
}

module.exports = {
  oauthMiddleware,
  getAuthorizationUrl,
  exchangeCodeForTokens,
  refreshAccessToken,
};
