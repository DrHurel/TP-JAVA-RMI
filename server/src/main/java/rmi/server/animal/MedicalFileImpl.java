package rmi.server.animal;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.util.HashMap;

import java.util.UUID;
import rmi.common.animal.MedicalFile;

public class MedicalFileImpl extends UnicastRemoteObject implements MedicalFile {

    UUID uuid;

    public MedicalFileImpl(UUID uuid) throws RemoteException {
        super();
        this.uuid = uuid;
    }

    HashMap<String, String> entries = new HashMap<>();

    @Override
    public String info() throws RemoteException {

        return entries.toString();
    }

    @Override
    public void update(String field, String value) throws RemoteException {
        entries.put(field, value);
    }

    @Override
    public String get(String field) throws RemoteException {
        return entries.get(field);

    }

    @Override
    public boolean equals(Object object) {
        if (!object.getClass().equals(getClass())) {
            return false;
        }

        return ((MedicalFileImpl) object).uuid.equals(uuid);
    }
}
