package com.banking.account.cmd.domain;

import com.banking.account.cmd.api.command.OpenAccountCommand;
import com.banking.account.common.events.AccountClosedEvent;
import com.banking.account.common.events.AccountOpenedEvent;
import com.banking.account.common.events.FoundsDepositedEvent;
import com.banking.account.common.events.FoundsWithdrawEvent;
import com.banking.cqrs.core.domain.AggregateRoot;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
public class AccountAggregate extends AggregateRoot {
    private Boolean active;
    private Double balance;

    public double getBalance(){
        return this.balance;
    }

    /**
     * Cuando se mande a llamar al constructor del accoun aggregate
     * se crea un nuevo evento con el constructor
     *
     */
    public AccountAggregate(OpenAccountCommand command){
        //Siempre que mandamos a llamar a raise event, es porque se creara un nuevo evento
        raiseEvent(AccountOpenedEvent.builder()
                .id(command.getId())
                .accountHolder(command.getAccountHolder())
                .createdDate(new Date())
                .accountType(command.getAccountType())
                .openingBalance(command.getOpeningBalance())
                .build());
    }

    public void apply(AccountOpenedEvent event){
        this.id = event.getId();
        this.active = true;
        this.balance = event.getOpeningBalance();
    }

    //==============================ABONAR DINERO=========================
    public void depositFounds(double amount){
        if(!this.active){
            throw new IllegalStateException("Los fondos no se pueden depositar en esta cuenta");
        }
        if(amount <= 0){
            throw new IllegalStateException("El deposito no puede ser cero o menos que cero");
        }

        raiseEvent(FoundsDepositedEvent.builder()
                        .id(this.id)
                        .amount(amount)
                        .build());
    }

    public void apply(FoundsDepositedEvent event){
        this.id = event.getId();
        this.balance += event.getAmount();
    }

    //==========================RETIRAR DINERO============================================
    public void withdrawFounds(double amount){
        if(!this.active){
            throw new IllegalStateException("La cuenta bancaria se encuentra cerrada");
        }
        raiseEvent(FoundsWithdrawEvent.builder()
                .id(this.id)
                .amount(amount)
                .build());
    }

    public void apply(FoundsWithdrawEvent event){
        this.id = event.getId();
        this.balance -= event.getAmount();
    }


    //==========================CERRAR CUENTA DE BANCO============================================
    public void closeAccount(){
        if(!active){
            throw new IllegalStateException("La cuenta de banco esta cerrada");
        }
        raiseEvent(AccountClosedEvent.builder()
                .id(this.id).build());
    }

    public void apply(AccountClosedEvent event){
        this.id = event.getId();
        this.active = false;
    }

}
