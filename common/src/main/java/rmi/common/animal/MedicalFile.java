package rmi.common.animal;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MedicalFile extends Remote {
    String info() throws RemoteException;

    void update(String field, String value) throws RemoteException;

    String get(String field) throws RemoteException;
}
