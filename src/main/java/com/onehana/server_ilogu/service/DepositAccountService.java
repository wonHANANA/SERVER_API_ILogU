package com.onehana.server_ilogu.service;

import com.onehana.server_ilogu.repository.DepositAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class DepositAccountService {

    private final DepositAccountRepository depositAccountRepository;
}
