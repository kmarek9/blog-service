package pl.twojekursy.user.settings;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.twojekursy.user.User;
import pl.twojekursy.user.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserSettingsService {
    private final UserSettingsRepository userSettingsRepository;

    private final UserService userService;

    @Transactional
    public void createOrUpdate(CreateUserSettingsRequest userSettingsRequest){
        User user = userService.findById(userSettingsRequest.getUserId());

        UserSettings userSettings = userSettingsRepository.findById(userSettingsRequest.getUserId())
                .orElse(UserSettings.builder()
                    .user(user)
                    .build()
                );

        userSettings.setShowPanel1(userSettingsRequest.getShowPanel1());
        userSettings.setDarkMode(userSettingsRequest.getDarkMode());

        userSettingsRepository.save(userSettings);
    }
}

