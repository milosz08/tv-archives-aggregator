/*
 * Copyright (c) 2024 by Miłosz Gilga <https://miloszgilga.pl>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     <http://www.apache.org/licenses/LICENSE-2.0>
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import { createContext, useContext } from 'react';
import axios from 'axios';
import { useSnackbar } from 'notistack';
import fetchApi from './FetchApi';

type AxiosWrapperContextType = {
  api: ReturnType<typeof fetchApi>;
};

const AxiosWrapperContext = createContext<AxiosWrapperContextType>(
  {} as AxiosWrapperContextType
);

type Props = {
  children: React.ReactNode;
};

const AxiosWrapper: React.FC<Props> = ({ children }): JSX.Element => {
  const { enqueueSnackbar } = useSnackbar();

  const instance = axios.create({
    baseURL: import.meta.env.VITE_SERVER_URL,
    headers: { 'Content-Type': 'application/json' },
  });

  instance.interceptors.response.use(
    response => response,
    error => {
      const message = error?.response?.data?.message;
      console.log(message);
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

export const useAxios = () => useContext(AxiosWrapperContext);

export default AxiosWrapper;
