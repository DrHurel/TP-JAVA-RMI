package rmi.server;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

import rmi.common.services.Server;
import rmi.server.services.VeterinariesCabinetImpl;

import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class RmiServer extends UnicastRemoteObject implements Server {

    protected RmiServer() throws RemoteException {
        super();
    }

    static Logger logger = Logger.getLogger("server");
    static Registry registry;

    static ArrayList<String> cabinetList = new ArrayList<>();

    public static void main(String[] args) {

        System.setSecurityManager(new SecurityManager());

        System.setProperty("java.rmi.server.codebase",
                "http://localhost:8000/common/build/libs/common.jar");

        try {

            // Start the RMI registry on port 1099
            registry = LocateRegistry.createRegistry(1099);
            logger.info("load registry");

            RmiServer server = new RmiServer();
            registry.bind("server", server);

            logger.info("Server is running...");

            server.addCabinet("default-cabinet");

        } catch (Exception e) {
            logger.severe(e.getMessage());
        }

    }

    @Override
    public boolean addCabinet(String name) throws RemoteException {
        try {
            registry.bind(name, new VeterinariesCabinetImpl(name, registry));
            cabinetList.add(name);
            return true;
        } catch (RemoteException | AlreadyBoundException e) {
            logger.warning(e.getLocalizedMessage());
            return false;
        }

    }

    @Override
    public boolean removeCabinet(String name) throws RemoteException {
        try {
            logger.info(name);
            registry.unbind(name);
            cabinetList.remove(name);

            return true;
        } catch (RemoteException | NotBoundException e) {
            return false;
        }
    }

    @Override
    public ArrayList<String> getCabinetList() throws RemoteException {
        return cabinetList;
    }

}