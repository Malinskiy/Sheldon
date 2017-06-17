package test;

import com.malinskiy.sheldon2.GatewayBuilder;
import com.malinskiy.sheldon2.IGateway;

public final class Test_Preferences implements PreferencesWithClear{
  private final IGateway provider;

  public Test_Preferences(GatewayBuilder builder) {
    this.provider = builder.namespace("test").build();
  }

  public void clear() {
    provider.clear();
  }

}
