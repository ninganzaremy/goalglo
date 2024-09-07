import axios from 'axios';
import {getApiUrl, getEncryptedItem} from "../security/securityConfig";

/**
 * Axios instance configured with the base URL and credentials.
 *
 * @type {AxiosInstance}
 */
const apiService = axios.create({
   baseURL: getApiUrl(),
   withCredentials: true,
});

/**
 * Request interceptor for adding the Authorization header.
 *
 * This interceptor adds the JWT token from local storage (if present) to the `Authorization`
 * header of every outgoing request. It also sets the `Content-Type` to `application/json`.
 *
 * @param {Object} config - The Axios request configuration object.
 * @returns {Object} The modified Axios request configuration object.
 */
apiService.interceptors.request.use(
   (config) => {
      const authToken = getEncryptedItem();
      if (authToken) {
         config.headers['Authorization'] = `Bearer ${authToken}`;
      }
      if (!config.headers['Content-Type']) {
         config.headers['Content-Type'] = 'application/json';
      }
      return config;
   },
   (error) => {
      return Promise.reject(error);
   }
);

export default apiService;