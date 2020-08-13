import axios from 'axios';
import { cacheAdapterEnhancer, throttleAdapterEnhancer } from 'axios-extensions';
import utils from '../utils';
import { baseUrl } from './config';

const axiosConfig = {
  headers: {
    'Cache-Control': 'no-cache',
  },
  adapter: throttleAdapterEnhancer(
    cacheAdapterEnhancer(axios.defaults.adapter, false),
  ),
};

const isDev = true; // TODO: replace
if (isDev) {
  axiosConfig.baseURL = baseUrl;
}

const jwt = utils.getJwtFromLocalStorage();
if (jwt) {
  axiosConfig.headers['x-access-token'] = jwt;
}

const instance = axios.create(axiosConfig);

instance.interceptors.response.use(response => (response)
, (error) => {
  if (error.response.status === 401) {
    const getUrl = window.location;
    const frontLoginUrl = `${getUrl.protocol}//${getUrl.host}/login?alert=expired`;
    window.location.href = frontLoginUrl;
  }
  return Promise.reject(error);
});

export default instance;
