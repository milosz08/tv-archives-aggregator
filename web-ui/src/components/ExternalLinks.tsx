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
import { Box } from '@mui/material';

type Props = {
  name: string;
};

const ExternalLinks: React.FC<Props> = ({ name }): JSX.Element => (
  <Box display="flex" marginTop={2} columnGap={2}>
    <a
      href={`https://www.filmweb.pl/search#/?query=${name}`}
      target="_blank"
      rel="noreferrer">
      <img src="/filmweb-logo.png" width={30} />
    </a>
    <a
      href={`https://www.imdb.com/find/?q=${name}`}
      target="_blank"
      rel="noreferrer">
      <img src="/imdb-logo.png" height={30} />
    </a>
  </Box>
);

export default ExternalLinks;
