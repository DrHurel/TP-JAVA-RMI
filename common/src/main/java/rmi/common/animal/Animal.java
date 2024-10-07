package rmi.common.animal;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Animal extends Remote {

    String getName() throws RemoteException;

    String getMasterName() throws RemoteException;

    Specie getSpecie() throws RemoteException;

    public String getMEdicalFileID() throws RemoteException;

    default String info() throws RemoteException {

        return String.format("%s a %s owned by %s", getName(), getSpecie().info(), getMasterName());
    }

}