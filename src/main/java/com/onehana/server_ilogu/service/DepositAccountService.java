package com.onehana.server_ilogu.service;

import com.onehana.server_ilogu.dto.DepositAccountDto;
import com.onehana.server_ilogu.entity.DepositAccount;
import com.onehana.server_ilogu.entity.User;
import com.onehana.server_ilogu.repository.DepositAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DepositAccountService {

    private final DepositAccountRepository depositAccountRepository;

    public DepositAccountDto createDepositAccount(User user) {
        DepositAccount depositAccount = new DepositAccount();
        depositAccount.setUser(user);
        user.getDepositAccounts().add(depositAccount);

        return DepositAccountDto.of(depositAccountRepository.save(depositAccount));
    }
}
