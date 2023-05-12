package pl.twojekursy.address;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.twojekursy.user.User;
import pl.twojekursy.user.UserService;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressService {
    private final AddressRepository addressRepository;

    private final UserService userService;

    @Transactional
    public void createOrUpdate(CreateAddressRequest addressRequest){
        User user = userService.findById(addressRequest.getUserId());

        Address address = addressRepository.findById(addressRequest.getUserId())
                .orElse(Address.builder()
                    .user(user)
                    .build()
                );

        address.setStreet(addressRequest.getStreet());
        address.setCity(addressRequest.getCity());

        addressRepository.save(address);
    }
}

