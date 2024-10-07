package rmi.common.services;

import java.rmi.Remote;
import java.rmi.RemoteException;

// Interface for client callback
public interface ClientCallback extends Remote {
    void notifyClient(String message) throws RemoteException;
}