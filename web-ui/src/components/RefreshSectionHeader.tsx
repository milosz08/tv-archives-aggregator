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
import RefreshIcon from '@mui/icons-material/Refresh';
import { Box, Button, Typography } from '@mui/material';

type Props = {
  children: React.ReactNode;
  onRefresh: () => void;
};

const RefreshSectionHeader: React.FC<Props> = ({
  children,
  onRefresh,
}): JSX.Element => (
  <Box display="flex" justifyContent="space-between" marginBottom={2}>
    <Typography fontSize={20}>{children}</Typography>
    <Button variant="contained" onClick={onRefresh}>
      <RefreshIcon />
    </Button>
  </Box>
);

export default RefreshSectionHeader;
