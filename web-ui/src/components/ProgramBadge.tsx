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
import { Box, Typography } from '@mui/material';
import { orange } from '@mui/material/colors';

type Props = {
  badge: string | undefined;
};

const ProgramBadge: React.FC<Props> = ({ badge }): JSX.Element => (
  <>
    {badge && (
      <Box
        component={Typography}
        color="white"
        marginTop={2}
        paddingX={1}
        paddingY={0.7}
        gap={3}
        sx={{
          bgcolor: orange[500],
          borderRadius: 1,
        }}
        maxWidth="fit-content">
        {badge}
      </Box>
    )}
  </>
);

export default ProgramBadge;
