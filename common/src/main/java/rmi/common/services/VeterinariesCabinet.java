package rmi.common.services;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import rmi.common.animal.AnimalDescription;

public interface VeterinariesCabinet extends Remote {

    ArrayList<String> getPatientsIDs() throws RemoteException;

    ArrayList<String> getPatientsIDsFromName(String name) throws RemoteException;

    ArrayList<String> getPatientsIDsFromSpecie(String name) throws RemoteException;

    boolean addPatient(AnimalDescription patient) throws RemoteException;

    boolean addPatientFromGlobal(String patient) throws RemoteException;

    boolean removePatient(String patient) throws RemoteException;

    void subscribeNotification(String uid, ClientCallback callback) throws RemoteException;

    void unsubscribeNotification(String uid) throws RemoteException;

    void notifyClients(String message) throws RemoteException;
}
