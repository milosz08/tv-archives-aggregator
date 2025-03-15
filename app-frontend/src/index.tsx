import * as React from 'react';
import { SnackbarProvider } from 'notistack';
import * as ReactDOM from 'react-dom/client';
import { AppRouter } from '@/app';
import { MuiThemeSupplier } from '@/utils';
import { AxiosWrapper, QueryWrapper } from './api';

const appMount = document.getElementById('app-mount')!;

ReactDOM.createRoot(appMount).render(
  <React.StrictMode>
    <MuiThemeSupplier>
      <SnackbarProvider>
        <AxiosWrapper>
          <QueryWrapper>
            <AppRouter />
          </QueryWrapper>
        </AxiosWrapper>
      </SnackbarProvider>
    </MuiThemeSupplier>
  </React.StrictMode>
);
