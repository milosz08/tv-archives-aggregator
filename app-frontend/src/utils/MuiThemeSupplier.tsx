import * as React from 'react';
import { ThemeProvider } from '@emotion/react';
import { GlobalStyles, createTheme } from '@mui/material';
import CssBaseline from '@mui/material/CssBaseline';
import { teal } from '@mui/material/colors';

const darkTheme = createTheme({
  palette: {
    mode: 'dark',
    primary: teal,
  },
});

const globalStyles = {
  'body, #app-mount': {
    margin: 0,
    padding: 0,
    fontFamily: 'Roboto, sans-serif',
  },
};

type Props = {
  children: React.ReactNode;
};

const MuiThemeSupplier: React.FC<Props> = ({ children }): React.ReactElement => (
  <ThemeProvider theme={darkTheme}>
    <CssBaseline />
    <GlobalStyles styles={globalStyles} />
    {children}
  </ThemeProvider>
);

export { MuiThemeSupplier };
