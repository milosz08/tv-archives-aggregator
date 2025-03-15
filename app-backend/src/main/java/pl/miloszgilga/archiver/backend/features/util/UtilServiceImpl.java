package pl.miloszgilga.archiver.backend.features.util;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import pl.miloszgilga.archiver.backend.db.DataHandler;
import pl.miloszgilga.archiver.backend.features.util.dto.DatabaseCapacityDetailsDto;

import java.util.List;

@Service
@RequiredArgsConstructor
class UtilServiceImpl implements UtilService {
  private final DataHandler dataHandler;

  @Override
  public DatabaseCapacityDetailsDto getDatabaseCapacityDetails(String channelSlug) {
    final boolean globalCapacity = StringUtils.equals(channelSlug, StringUtils.EMPTY);
    if (globalCapacity) {
      final List<String> persistedChannels = dataHandler.getPersistedChannels();
      return dataHandler.getGlobalDatabaseCapacity(persistedChannels);
    }
    return dataHandler.getChannelDatabaseCapacity(channelSlug);
  }
}
