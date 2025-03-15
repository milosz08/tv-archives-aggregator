package pl.miloszgilga.archiver.backend.features.util;

import pl.miloszgilga.archiver.backend.features.util.dto.DatabaseCapacityDetailsDto;

interface UtilService {
  DatabaseCapacityDetailsDto getDatabaseCapacityDetails(String channelSlug);
}
