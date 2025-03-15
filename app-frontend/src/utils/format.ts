import byteSize from 'byte-size';
import { SearchRecord } from '@/api/types/search-content';

const ROWS_PER_PAGE = [10, 20, 25, 100];

const formatLargeNumber = (num: number): string => {
  return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ' ');
};

const formatSeasonAndEpisode = (row: SearchRecord): string => {
  const { season, episode } = row;
  return `${season ?? '-'}/${episode ?? '-'}`;
};

const parseBytes = (bytes: number): string => {
  const { value, unit } = byteSize(bytes);
  return `${value} ${unit}`;
};

const isInteger = (value: unknown): boolean =>
  typeof value === 'number' || (typeof value === 'string' && /^\s*-?\d+\s*$/.test(value));

export { ROWS_PER_PAGE, formatLargeNumber, formatSeasonAndEpisode, parseBytes, isInteger };
