export type CalendarDay = {
  number: number;
  isoDate: string;
};

export type CalendarMonth = {
  name: string;
  countOfEmptyBlocks: number;
  days: CalendarDay[];
};
