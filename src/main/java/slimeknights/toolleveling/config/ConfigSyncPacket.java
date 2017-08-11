package slimeknights.toolleveling.config;

import slimeknights.mantle.config.AbstractConfig;
import slimeknights.mantle.config.AbstractConfigSyncPacket;

public class ConfigSyncPacket extends AbstractConfigSyncPacket {
  @Override
  protected AbstractConfig getConfig() {
    return Config.INSTANCE;
  }
}
