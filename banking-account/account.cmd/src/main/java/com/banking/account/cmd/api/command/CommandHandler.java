package com.banking.account.cmd.api.command;

public interface CommandHandler {
    void handle(OpenAccountCommand command);
    void handle(DepositFoundsCommand command);
    void handle(WithdrawFoundsCommand command);
    void handle(CloseAccountCommand command);
}
