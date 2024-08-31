package com.br.pessoal_sync.service.address;

import com.br.pessoal_sync.domain.dto.AddressDto;
import com.br.pessoal_sync.domain.model.User;

public interface AddressService {

    public void createAddress(User user, AddressDto addressDto);

    public void deleteAddress(Long id);

}
