import * as React from 'react';
import { createContext, useContext } from 'react';
import axios from 'axios';
import { useSnackbar } from 'notistack';
import { fetchApi } from './FetchApi';

type AxiosWrapperContextType = {
  api: ReturnType<typeof fetchApi>;
};

const AxiosWrapperContext = createContext<AxiosWrapperContextType>({} as AxiosWrapperContextType);

type Props = {
  children: React.ReactNode;
};

const AxiosWrapper: React.FC<Props> = ({ children }): React.ReactElement => {
  const { enqueueSnackbar } = useSnackbar();

  const instance = axios.create({
    baseURL: '/api',
    headers: { 'Content-Type': 'application/json' },
  });

  instance.interceptors.response.use(
    response => response,
    error => {
      const message = error?.response?.data?.message;
      enqueueSnackbar(message || 'Unexpected server error', {
        variant: 'error',
      });
      return Promise.reject(error);
    }
  );

  return (
    <AxiosWrapperContext.Provider value={{ api: fetchApi(instance) }}>
      {children}
    </AxiosWrapperContext.Provider>
  );
};

const useAxios = () => useContext(AxiosWrapperContext);

export { AxiosWrapper, useAxios };
