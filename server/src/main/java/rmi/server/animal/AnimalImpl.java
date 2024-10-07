package rmi.server.animal;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import rmi.common.animal.Animal;
import rmi.common.animal.AnimalDescription;
import rmi.common.animal.Specie;
import java.util.UUID;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Map.Entry;

class AnimalEntry implements Entry<String, Animal> {

    String key;
    Animal value;

    AnimalEntry(String k, AnimalImpl v) {
        key = k;
        value = v;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public Animal getValue() {
        return value;
    }

    @Override
    public Animal setValue(Animal arg0) {

        value = arg0;
        return value;
    }

}

public class AnimalImpl extends UnicastRemoteObject implements Animal {

    String name;
    String master;
    Specie specie;
    String medicalFile;

    public AnimalImpl(String name,
            String master,
            Specie specie, String medicalFileId) throws RemoteException {
        this.name = name;
        this.master = master;
        this.specie = specie;

        this.medicalFile = medicalFileId;

    }

    public static Entry<String, Animal> fromAnimalDescription(AnimalDescription animal)
            throws RemoteException {

        Registry registry = LocateRegistry.getRegistry();
        UUID uuid = UUID.randomUUID();
        AnimalImpl animalImpl;

        try {
            animalImpl = new AnimalImpl(animal.getName(), animal.getMaster(), animal.getSpecie(), uuid.toString());
            registry.bind("animal-" + uuid.toString(),
                    animalImpl);
        } catch (AlreadyBoundException e) {
            return null;
        }
        try {
            registry.bind("med-" + uuid.toString(), new MedicalFileImpl(uuid));

        } catch (AlreadyBoundException e) {
            try {
                registry.unbind("animal-" + uuid.toString());
            } catch (NotBoundException e1) {
                return null;
            }
            return null;
        }

        return new AnimalEntry("animal-" + uuid.toString(), animalImpl);
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public String getMasterName() throws RemoteException {
        return master;
    }

    @Override
    public Specie getSpecie() throws RemoteException {
        return specie;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((master == null) ? 0 : master.hashCode());
        result = prime * result + ((specie == null) ? 0 : specie.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        AnimalImpl other = (AnimalImpl) obj;
        if (name == null) {
            if (other.name != null)
                return false;
        } else if (!name.equals(other.name))
            return false;
        if (master == null) {
            if (other.master != null)
                return false;
        } else if (!master.equals(other.master))
            return false;
        if (specie == null) {
            if (other.specie != null)
                return false;
        } else if (!specie.equals(other.specie))
            return false;
        return true;
    }

    @Override
    public String getMEdicalFileID() throws RemoteException {
        return medicalFile;
    }
}