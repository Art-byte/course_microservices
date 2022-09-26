package com.banking.account.query.infrastructure.handlers;

import com.banking.account.common.events.AccountClosedEvent;
import com.banking.account.common.events.AccountOpenedEvent;
import com.banking.account.common.events.FoundsDepositedEvent;
import com.banking.account.common.events.FoundsWithdrawEvent;

public interface EventHandler {
    void on(AccountOpenedEvent event);
    void on(FoundsDepositedEvent event);
    void on(FoundsWithdrawEvent event);
    void on(AccountClosedEvent event);
}
