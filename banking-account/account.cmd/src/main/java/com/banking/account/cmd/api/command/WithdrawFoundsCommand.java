package com.banking.account.cmd.api.command;

import com.banking.cqrs.core.commands.BaseCommand;
import lombok.Data;

@Data
public class WithdrawFoundsCommand extends BaseCommand {
    private Double amount;
}
