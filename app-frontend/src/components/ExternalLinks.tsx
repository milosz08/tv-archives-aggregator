import * as React from 'react';
import { Box } from '@mui/material';

type Props = {
  name: string;
};

const ExternalLinks: React.FC<Props> = ({ name }): React.ReactElement => (
  <Box display="flex" marginTop={2} columnGap={2}>
    <a href={`https://www.filmweb.pl/search#/?query=${name}`} target="_blank" rel="noreferrer">
      <img src="/filmweb-logo.png" width={30} alt="" />
    </a>
    <a href={`https://www.imdb.com/find/?q=${name}`} target="_blank" rel="noreferrer">
      <img src="/imdb-logo.png" height={30} alt="" />
    </a>
  </Box>
);

export { ExternalLinks };
