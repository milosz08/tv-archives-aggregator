package pl.miloszgilga.archiver.backend.features.util;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.miloszgilga.archiver.backend.features.util.dto.DatabaseCapacityDetailsDto;

@RestController
@RequestMapping("/api/v1/util")
@RequiredArgsConstructor
class UtilController {
  private final UtilService utilService;

  @GetMapping("/db/capacity")
  ResponseEntity<DatabaseCapacityDetailsDto> getDatabaseCapacityDetails(
    @RequestParam("slug") String slug
  ) {
    return ResponseEntity.ok(utilService.getDatabaseCapacityDetails(slug));
  }
}
