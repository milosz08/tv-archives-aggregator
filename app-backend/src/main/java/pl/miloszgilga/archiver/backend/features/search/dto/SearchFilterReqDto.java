package pl.miloszgilga.archiver.backend.features.search.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SearchFilterReqDto {
  @NotNull
  private String searchPhrase;

  private boolean fullTextSearch;

  @NotNull
  private List<String> selectedTvChannels;

  @NotNull
  private List<String> selectedProgramTypes;

  @NotNull
  private List<Integer> selectedWeekdays;

  private boolean tvShowsActive;

  private String startDate;

  private String endDate;

  private Integer season;

  private Integer episode;

  private boolean sortNowToPrev;
}
