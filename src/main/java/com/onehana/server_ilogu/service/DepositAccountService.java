package com.onehana.server_ilogu.service;

import com.onehana.server_ilogu.dto.DepositAccountDto;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import com.onehana.server_ilogu.entity.DepositAccount;
import com.onehana.server_ilogu.entity.User;
import com.onehana.server_ilogu.exception.BaseException;
import com.onehana.server_ilogu.repository.DepositAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class DepositAccountService {

    private final UserService userService;
    private final DepositAccountRepository depositAccountRepository;

    public DepositAccountDto createDepositAccount(String email) {
        User user = userService.getUserOrException(email);

        DepositAccount depositAccount = new DepositAccount();
        depositAccount.setUser(user);
        user.getDepositAccounts().add(depositAccount);

        return DepositAccountDto.of(depositAccountRepository.save(depositAccount));
    }

    public void deleteDepositAccount(String email, Long accountId) {
        User user = userService.getUserOrException(email);

        DepositAccount depositAccount = depositAccountRepository.findById(accountId).orElseThrow(() ->
                new BaseException(BaseResponseStatus.ACCOUNT_NOT_FOUND));

        if (!user.getDepositAccounts().contains(depositAccount)) {
            throw new BaseException(BaseResponseStatus.INVALID_PERMISSION);
        }

        user.getDepositAccounts().remove(depositAccount);
        depositAccountRepository.delete(depositAccount);
    }

    @Transactional(readOnly = true)
    public List<DepositAccountDto> getDepositAccounts(String email) {
        User user = userService.getUserOrException(email);

        return user.getDepositAccounts().stream()
                .map(DepositAccountDto::of).toList();
    }
}
