package pl.twojekursy.user.settings;

import org.springframework.data.repository.CrudRepository;
import pl.twojekursy.address.Address;

public interface UserSettingsRepository extends CrudRepository<UserSettings, Long> {
}
