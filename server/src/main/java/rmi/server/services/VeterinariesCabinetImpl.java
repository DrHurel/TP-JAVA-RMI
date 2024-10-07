package rmi.server.services;

import java.rmi.AccessException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import rmi.common.animal.Animal;
import rmi.common.animal.AnimalDescription;
import rmi.common.services.VeterinariesCabinet;
import rmi.server.animal.AnimalImpl;
import rmi.server.animal.MedicalFileImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import java.rmi.registry.Registry;

import java.rmi.server.UnicastRemoteObject;
import java.util.UUID;
import rmi.common.services.ClientCallback;

public class VeterinariesCabinetImpl extends UnicastRemoteObject implements VeterinariesCabinet {

    HashMap<String, Animal> animals = new HashMap<>();
    String name;
    Registry registry;

    HashMap<String, ClientCallback> clients = new HashMap<>();

    public VeterinariesCabinetImpl(String name, Registry registry) throws RemoteException {
        this.registry = registry;
        this.name = name;
    }

    @Override
    public ArrayList<String> getPatientsIDs() throws RemoteException {
        ArrayList<String> res = new ArrayList<>();
        for (String k : animals.keySet()) {
            res.add(k);
        }

        return res;
    }

    @Override
    public ArrayList<String> getPatientsIDsFromName(String name) throws RemoteException {
        ArrayList<String> res = new ArrayList<>();
        for (Entry<String, Animal> e : animals.entrySet()) {
            if (e.getValue().getName().equals(name)) {
                res.add(e.getKey());
            }
        }

        return res;
    }

    @Override
    public boolean equals(Object object) {
        if (!object.getClass().equals(getClass())) {
            return false;
        }
        return ((VeterinariesCabinetImpl) object).name.equals(name);
    }

    @Override
    public ArrayList<String> getPatientsIDsFromSpecie(String name) throws RemoteException {
        ArrayList<String> res = new ArrayList<>();
        for (Entry<String, Animal> e : animals.entrySet()) {
            if (e.getValue().getSpecie().getName().equals(name)) {
                res.add(e.getKey());
            }
        }

        return res;
    }

    @Override
    public boolean addPatient(AnimalDescription patient) throws RemoteException {
        Entry<String, Animal> fromAnimalDescription = AnimalImpl.fromAnimalDescription(patient);
        animals.put(fromAnimalDescription.getKey(), fromAnimalDescription.getValue());
        if (animals.size() > 100) {
            notifyClients("warning more that 100 patients register");
        }
        return fromAnimalDescription != null;
    }

    @Override
    public boolean removePatient(String patient) throws RemoteException {

        return animals.remove(patient) != null;
    }

    @Override
    public boolean addPatientFromGlobal(String patient) throws RemoteException {
        try {
            animals.put(patient, (Animal) registry.lookup(patient));
            if (animals.size() > 100) {
                notifyClients("warning more that 100 patients register");
            }
        } catch (NotBoundException e) {
            return false;
        }
        return true;
    }

    public void unsubscribeNotification(String uid) throws RemoteException {
        clients.remove(uid);
    }

    public void notifyClients(String message) throws RemoteException {
        System.out.println("Triggering event, notifying clients...");

        for (ClientCallback client : clients.values()) {
            try {
                client.notifyClient(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void subscribeNotification(String uid, ClientCallback callback) throws RemoteException {
        clients.put(uid, callback);
        callback.notifyClient("subscription done");
    }

}
