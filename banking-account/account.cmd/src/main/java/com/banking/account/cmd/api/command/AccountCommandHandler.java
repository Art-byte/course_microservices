package com.banking.account.cmd.api.command;

import com.banking.account.cmd.domain.AccountAggregate;
import com.banking.cqrs.core.handlers.EventSourcingHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountCommandHandler implements CommandHandler{

    @Autowired
    EventSourcingHandler<AccountAggregate> eventSourcingHandler;

    @Override
    public void handle(OpenAccountCommand command) {
        var aggregate = new AccountAggregate(command);
        eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(DepositFoundsCommand command) {
        var aggregate = eventSourcingHandler.getById(command.getId());
        aggregate.depositFounds(command.getAmount());
        eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(WithdrawFoundsCommand command) {
        var aggregate = eventSourcingHandler.getById(command.getId());
        if(command.getAmount() > aggregate.getBalance()){
            throw new IllegalStateException("Insuficientes fondos, no puedes retirar dinero");
        }
        aggregate.withdrawFounds(command.getAmount());
        eventSourcingHandler.save(aggregate);
    }

    @Override
    public void handle(CloseAccountCommand command) {
        var aggregate = eventSourcingHandler.getById(command.getId());
        aggregate.closeAccount();
        eventSourcingHandler.save(aggregate);
    }
}
