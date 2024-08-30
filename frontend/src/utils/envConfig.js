/**
 * Environment Configuration Utility
 *
 * This module provides functions to access environment-specific configuration values.
 * It uses environment variables to determine the current environment and configuration.
 */

/**
 * Get the current environment
 * @returns {string} The current environment (e.g., 'development', 'production')
 */
export const getCurrentEnv = () => import.meta.env.VITE_APP_ENV;

/**
 * Check if the current environment is production
 * @returns {boolean} True if the current environment is production, false otherwise
 */
export const isProduction = () => getCurrentEnv() === 'production';

/**
 * Get the API URL for the current environment
 * @returns {string} The API URL
 */
export const getApiUrl = () => isProduction()
   ? import.meta.env.VITE_PROD_API_URL
   : import.meta.env.VITE_DEV_API_URL;

/**
 * Check if Redux DevTools should be enabled
 * @returns {boolean} True if Redux DevTools should be enabled, false otherwise
 */
export const shouldEnableDevTools = () => !isProduction();