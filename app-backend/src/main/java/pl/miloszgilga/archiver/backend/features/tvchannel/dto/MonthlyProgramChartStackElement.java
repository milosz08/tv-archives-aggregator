package pl.miloszgilga.archiver.backend.features.tvchannel.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MonthlyProgramChartStackElement {
  private String name;
  private long total;
  private List<Integer> data;
  private String color;
  private boolean existInChart;

  public MonthlyProgramChartStackElement(String name, List<Integer> data) {
    this.name = name;
    this.data = data;
    total = calculateTotal();
  }

  public MonthlyProgramChartStackElement(
    String name,
    List<Integer> data,
    String color,
    boolean existInChart
  ) {
    this.name = name;
    this.data = data;
    this.color = color;
    this.existInChart = existInChart;
    total = calculateTotal();
  }

  private long calculateTotal() {
    return data.stream().reduce(0, Integer::sum);
  }
}
