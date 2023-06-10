package com.onehana.server_ilogu.service;

import com.onehana.server_ilogu.dto.DepositAccountDto;
import com.onehana.server_ilogu.dto.request.SendMoneyRequest;
import com.onehana.server_ilogu.dto.response.BaseResponseStatus;
import com.onehana.server_ilogu.dto.response.SendMoneyResponse;
import com.onehana.server_ilogu.entity.DepositAccount;
import com.onehana.server_ilogu.entity.User;
import com.onehana.server_ilogu.exception.BaseException;
import com.onehana.server_ilogu.repository.DepositAccountRepository;
import com.onehana.server_ilogu.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DepositAccountService {

    private final UserService userService;
    private final DepositAccountRepository depositAccountRepository;
    private final UserRepository userRepository;

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

    public SendMoneyResponse sendMoney(String email, SendMoneyRequest request) {
        User fromUser = userService.getUserOrException(email);
        User toUser = userRepository.findByNickname(request.getToUserName()).orElseThrow(() ->
                new BaseException(BaseResponseStatus.USER_NOT_FOUND));

        if(fromUser.getNickname().equals(toUser.getNickname())){
            throw new BaseException(BaseResponseStatus.CANNOT_SEND_TO_SELF);
        }

        if (fromUser.getDepositAccounts().isEmpty() || toUser.getDepositAccounts().isEmpty())
            throw new BaseException(BaseResponseStatus.ACCOUNT_NOT_FOUND);

        DepositAccount from = fromUser.getDepositAccounts().get(0);
        DepositAccount to = toUser.getDepositAccounts().get(0);

        from.withdraw(request.getMoney());
        to.deposit(request.getMoney());

        BigDecimal remainMoney = from.getBalance();

        return new SendMoneyResponse(toUser.getNickname(), request.getMoney(), remainMoney);
    }
}
