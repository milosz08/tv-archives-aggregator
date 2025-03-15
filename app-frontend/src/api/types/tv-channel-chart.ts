export type MonthlyProgramsChartDto = {
  series: MonthlyProgramChartStackElement[];
  table: MonthlyProgramChartStackElement[];
  months: string[];
  stackKey: string;
  allPerMonths: number[];
};

export type MonthlyProgramChartStackElement = {
  name: string;
  total: number;
  data: number[];
  color: string;
  existInChart: boolean;
};
