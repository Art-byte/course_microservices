package com.banking.cqrs.core.commands;

//Esta interface solo puede tener una funcionalidad
@FunctionalInterface
public interface CommandHandlerMethod<T extends BaseCommand> {
    void handle(T command);
}
