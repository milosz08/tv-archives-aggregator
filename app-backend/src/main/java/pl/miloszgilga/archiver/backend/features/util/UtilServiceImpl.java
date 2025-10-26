package pl.miloszgilga.archiver.backend.features.util;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.miloszgilga.archiver.backend.db.DataHandler;
import pl.miloszgilga.archiver.backend.features.util.dto.DatabaseCapacityDetailsDto;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
class UtilServiceImpl implements UtilService {
  private final DataHandler dataHandler;

  @Override
  public DatabaseCapacityDetailsDto getDatabaseCapacityDetails(String channelSlug) {
    final boolean globalCapacity = Objects.equals(channelSlug, "");
    if (globalCapacity) {
      final List<String> persistedChannels = dataHandler.getPersistedChannels();
      return dataHandler.getGlobalDatabaseCapacity(persistedChannels);
    }
    return dataHandler.getChannelDatabaseCapacity(channelSlug);
  }
}
