/**
 * Environment Configuration Utility
 *
 * This module provides functions to access environment-specific configuration values.
 * It uses environment variables to determine the current environment and configuration.
 */
import CryptoJS from 'crypto-js';
import {jwtDecode} from 'jwt-decode'

const secretKey = import.meta.env.VITE_SECRET_KEY;
const tokenItemName = import.meta.env.VITE_TOKEN_ITEM_NAME;

/**
 * Encrypt data using AES encryption.
 *
 * @param {string} data - The plaintext data to encrypt.
 * @returns {string} The encrypted data as a string.
 */
const encryptData = (data) => {
   return CryptoJS.AES.encrypt(data, secretKey).toString();
};

/**
 * Decrypt data using AES decryption.
 *
 * @param {string} encryptedData - The encrypted data to decrypt.
 * @returns {string} The decrypted plaintext data.
 */
export const decryptData = (encryptedData) => {
   return CryptoJS.AES.decrypt(encryptedData, secretKey).toString(CryptoJS.enc.Utf8);
};

/**
 * Store encrypted data in local storage.
 *
 * @param {string} value - The plaintext data to encrypt and store.
 */
export const setEncryptedItem = (value) => {
   localStorage.setItem(hashKey(tokenItemName), encryptData(value));
};

/**
 * Retrieve decrypted data from local storage.
 *
 * @returns {string|null} The decrypted data from local storage, or null if not found.
 */
export const getEncryptedItem = () => {
   return localStorage.getItem(hashKey(tokenItemName)) ? decryptData(localStorage.getItem(hashKey(tokenItemName))) : null;
};

/**
 * Remove an encrypted item from local storage.
 */
export const removeEncryptedItem = () => {
   localStorage.removeItem(hashKey(tokenItemName));
};

/**
 * Generate a hashed key using SHA-512.
 *
 * @param {string} key - The plaintext key to hash.
 * @returns {string} The hashed key as a string.
 */
const hashKey = (key) => {
   return CryptoJS.SHA512(key).toString(CryptoJS.enc.Hex);
};

/**
 * Get the current environment.
 *
 * @returns {string} The current environment (e.g., 'development', 'production').
 */
export const getCurrentEnv = () => import.meta.env.VITE_APP_ENV;

/**
 * Check if the current environment is production.
 *
 * @returns {boolean} True if the current environment is production, false otherwise.
 */
export const isProduction = () => getCurrentEnv() === 'production';

/**
 * Get the API URL for the current environment.
 *
 * @returns {string} The API URL.
 */
export const getApiUrl = () => isProduction()
   ? import.meta.env.VITE_PROD_API_URL
   : import.meta.env.VITE_DEV_API_URL;

/**
 * Check if Redux DevTools should be enabled.
 *
 * @returns {boolean} True if Redux DevTools should be enabled, false otherwise.
 */
export const shouldEnableDevTools = () => !isProduction();


/**
 * Decode a JWT token.
 *
 * @param {string} token - The JWT token to decode.
 * @returns {Object|null} The decoded token payload, or null if the token is invalid.
 */

export const decodeJwtToken = (token) => {
   try {
      return jwtDecode(token);
   } catch (error) {
      console.error('Error decoding token:', error);
      return null;
   }
};

export const isTokenExpired = (token) => {
   try {
      const decodedToken = jwtDecode(token);
      return decodedToken.exp * 1000 < Date.now();
   } catch (error) {
      console.error('Error decoding token or checking expiration:', error);
      return true;
   }
};

/**
 * Manage storage event listener.
 *
 * @param {string} action - The action to perform ('add' or 'remove').
 * @param {Function} handler - The event handler function.
 * @param key
 * @param value
 */
export const manageStorageEventListener = (action, handler, key = null, value = null) => {
   if (action === 'add') {
      window.addEventListener('storage', handler);
   } else if (action === 'remove') {
      window.removeEventListener('storage', handler);
   } else if (action === 'set' && key && value) {
      localStorage.setItem(key, value);
   } else {
      console.warn('Invalid action. Use "add", "remove", or "set".');
   }
};

/**
 * Get the secured role from environment variables.
 *
 * @returns {string} The secured role.
 */
export const userSecuredRole = () => {
   return encryptData(import.meta.env.VITE_SECURED_ROLE)
};