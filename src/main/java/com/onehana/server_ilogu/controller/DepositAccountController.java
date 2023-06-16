package com.onehana.server_ilogu.controller;

import com.onehana.server_ilogu.service.DepositAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/account")
public class DepositAccountController {

    private final DepositAccountService depositAccountService;

}
