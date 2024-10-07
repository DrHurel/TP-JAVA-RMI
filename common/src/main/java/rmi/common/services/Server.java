package rmi.common.services;

import java.rmi.Remote;
import java.rmi.RemoteException;

import java.util.ArrayList;

public interface Server extends Remote {
    boolean addCabinet(String name) throws RemoteException;

    boolean removeCabinet(String uuid) throws RemoteException;

    ArrayList<String> getCabinetList() throws RemoteException;
}
