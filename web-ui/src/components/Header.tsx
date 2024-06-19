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
import { Link as RouterLink } from 'react-router-dom';
import { PageLink } from '@/types/page-link';
import { AppBar, Box, Button, Toolbar, Typography } from '@mui/material';

type Props = {
  links: PageLink[];
};

const Header: React.FC<Props> = ({ links }): JSX.Element => (
  <AppBar position="fixed">
    <Toolbar>
      <Typography variant="h6" component="div" marginRight={4}>
        TV Archive aggregator
      </Typography>
      {links.map(({ name, slug }) => (
        <Box key={slug}>
          <Button component={RouterLink} to={slug} style={{ color: 'white' }}>
            {name}
          </Button>
        </Box>
      ))}
    </Toolbar>
  </AppBar>
);

export default Header;
