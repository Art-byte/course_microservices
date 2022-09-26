package com.banking.cqrs.core.domain;

import com.banking.cqrs.core.events.BaseEvent;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class AggregateRoot {
    protected String id;
    private int version = -1;

    //Contiene todos los cambios del aggregate en forma de eventos
    private final List<BaseEvent> changes = new ArrayList<>();
    private final Logger logger = Logger.getLogger(AggregateRoot.class.getName());

    //Obtener el Id
    public String getId(){
        return this.id;
    }

    //Setear la version y devolverla
    public int getVersion(){
        return this.version;
    }
    public void setVersion(int version){
        this.version = version;
    }

    //Devolvemos todos los cambios que no han sido confirmados
    public List<BaseEvent> getUnCommitedChanges(){
        return this.changes;
    }

    //Indicamos los cambios que han sido confirmados
    public void markChangesAsCommited(){
        this.changes.clear();
    }

    protected void applyChange(BaseEvent event, Boolean isNewEvent){
        try {
            //Encontramos al metodo, lo hacemos accesible y lo invocamos
            var method = getClass().getDeclaredMethod("apply", event.getClass());
            method.setAccessible(true);
            method.invoke(this, event);

        }catch (NoSuchMethodException e){
            logger.log(Level.WARNING, MessageFormat.format("El metodo apply no ha sido encontrado", event.getClass().getName()));
        }catch (Exception e){
            logger.log(Level.SEVERE, "Errores aplicando el evento al aggregate", e);
        }finally {
            if(isNewEvent){
                changes.add(event);
            }
        }
    }

    //Cuando quiera ejecutar un nuevo evento
    public void raiseEvent(BaseEvent event){
        applyChange(event, true);
    }

    //Cuando quiera reprocesar un evento existente
    public void replayEvents(Iterable<BaseEvent> events){
        events.forEach(event -> applyChange(event, false));
    }
}
