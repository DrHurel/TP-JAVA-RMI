package rmi.client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Logger;

import rmi.common.animal.Animal;
import rmi.common.animal.AnimalDescription;
import rmi.common.animal.MedicalFile;
import rmi.common.animal.SpecieImpl;
import rmi.common.services.ClientCallback;
import rmi.common.services.Server;
import rmi.common.services.VeterinariesCabinet;
import java.rmi.server.UnicastRemoteObject;

import java.io.IOException;

import java.util.Scanner;

enum Command {
    ADD_CABINET("0"),
    REMOVE_CABINET("1"),
    GET_ANIMAL("2"),
    UPDATE_CURRENT_PATIENT_FILE("3"),
    GET_ANIMAL_ID_FROM_NAME("4"),
    GET_ANIMAL_ID_FROM_SPECIE("5"),
    SELECT_CABINET("6"),
    ADD_PATIENT("7"),
    REMOVE_PATIENT("8"),
    QUIT("!q"),
    DEFAULT("DEFAULT");

    private String arg;

    Command(String arg) {
        this.arg = arg;
    }

    public static Command fromString(String text) {
        for (Command b : Command.values()) {
            if (b.arg.equalsIgnoreCase(text)) {
                return b;
            }
        }
        return Command.DEFAULT;
    }
}

public class RmiClient extends UnicastRemoteObject implements ClientCallback {

    protected RmiClient() throws RemoteException {
        super();
    }

    static Scanner scanner = new Scanner(System.in);

    static Logger logger = Logger.getLogger("Server");
    static Server server;
    static VeterinariesCabinet currentCabinet;
    static Registry registry;
    static String uid;

    static Animal currAnimal;

    public static void main(String[] args) {

        try {
            // Lookup the remote object in the RMI registry

            registry = LocateRegistry.getRegistry();

            server = (Server) registry.lookup("server");
            logger.info("server loaded");
            logger.info(String.format("Cabinet list : %s", String.join(", ", server.getCabinetList())));
            System.out.println("\n======== Register id ========");
            uid = scanner.nextLine();
            boolean getInput = true;
            while (getInput) {
                getInput = command();
            }

        } catch (Exception e) {
            logger.severe(e.getMessage());
            System.exit(-1);
        }
    }

    private static boolean command() throws IOException {
        // Enter data using Bufferscanner

        // Reading data using readLine

        System.out.println("\n\n ========== chose an option ===========");
        System.out.println("ADD_CABINET : 0");
        System.out.println("REMOVE_CABINET : 1");
        System.out.println("GET_ANIMAL : 2");
        System.out.println("UPDATE_CURRENT_PATIENT_FILE : 3");
        System.out.println("GET_ANIMAL_ID_FROM_NAME : 4");
        System.out.println("GET_ANIMAL_ID_FROM_SPECIE : 5");
        System.out.println("SELECT_CABINET : 6");
        System.out.println("ADD_PATIENT : 7");
        System.out.println("REMOVE_PATIENT 8");
        System.out.println("QUIT : !q");

        Command command = Command.fromString(scanner.nextLine());

        switch (command) {
            case Command.ADD_CABINET:
                commandAddCabinet();
                break;

            case Command.REMOVE_CABINET:
                commandRemoveCabinet();
                break;

            case Command.GET_ANIMAL:
                commandGetAnimal();
                break;

            case Command.UPDATE_CURRENT_PATIENT_FILE:
                commandUpdatePatientFile();
                break;
            case Command.GET_ANIMAL_ID_FROM_NAME:
                commandGetAnimalFromName();
                break;

            case Command.GET_ANIMAL_ID_FROM_SPECIE:
                commandGetAnimalFromSpecie();
                break;
            case Command.SELECT_CABINET:
                commandSelectCabinet();
                break;
            case Command.ADD_PATIENT:
                commandAddPatient();
                break;
            case Command.REMOVE_PATIENT:
                commandRemovePatient();
                break;
            case Command.QUIT:
                return false;
            default:
                return true;

        }
        return true;
    }

    private static void commandUpdatePatientFile() {
        if (currAnimal == null) {
            System.out.println("not patient selected");
        }

        try {
            MedicalFile file = (MedicalFile) registry.lookup(currAnimal.getMEdicalFileID());
            String field;

            String value;
            boolean edit = true;

            while (edit) {
                System.out.println(file.info());

                System.out.println("");
                System.out.println("Select a field to edit or leave with !q");

                field = scanner.nextLine();

                if (field.equals("!q")) {

                    break;
                }

                System.out.println("Enter value to update field");
                value = scanner.nextLine();

                file.update(field, value);
            }

        } catch (RemoteException | NotBoundException e) {
            logger.warning(e.getMessage());
        }
    }

    private static void commandRemovePatient() {
        System.out.println("Enter a id for the patient to be remove from cabinet\n");
        String name;
        try {
            name = scanner.nextLine();
            if (currentCabinet.removePatient(name)) {
                logger.info(String.format("Patient %s has been remove", name));
            } else {
                logger.info("Fail to add new cabinet");
            }
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private static void commandAddPatient() {
        System.out.println("Enter a id of the patient\n");
        String name;
        try {
            name = scanner.nextLine();
            if (currentCabinet.addPatientFromGlobal(name)) {
                logger.info(String.format("Patient %s has been add", name));
            } else {

                System.out.println("Enter a name of the patient\n");

                name = scanner.nextLine();
                System.out.println("Enter a name of the patient master\n");

                String master = scanner.nextLine();
                System.out.println("Enter a name of the patient Specie\n");

                String specie = scanner.nextLine();

                System.out.println("Enter the avgLifeSpan of the patient specie\n");

                int avgLifeSpan = scanner.nextInt();

                SpecieImpl sp = new SpecieImpl(specie, avgLifeSpan);

                if (currentCabinet.addPatient(new AnimalDescription(name, master, sp))) {
                    logger.info(String.format("Patient %s has been add", name));
                }
            }
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private static void commandAddCabinet() {
        System.out.println("Enter a name for the cabinet\n");
        String name;
        try {
            name = scanner.nextLine();
            if (server.addCabinet(name)) {
                logger.info(String.format("Cabinet %s has been add", name));
            } else {
                logger.info("Fail to add new cabinet");
            }
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }

    }

    private static void commandRemoveCabinet() {
        System.out.println("Enter a name for the cabinet to be remove\n");
        String name;
        try {
            name = scanner.nextLine();
            if (server.removeCabinet(name)) {
                logger.info(String.format("Cabinet %s has been remove", name));
            } else {
                logger.info("Fail to remove cabinet");
            }
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private static void commandGetAnimal() {
        System.out.println("Enter animal id\n");

        String name;
        try {
            name = scanner.nextLine();

            currAnimal = (Animal) registry.lookup(name);
            System.out.println("\nanimal selected\n");
            System.out.println(currAnimal.info());
            System.out.println("");
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private static void commandGetAnimalFromName() {
        System.out.println("Enter a name to look for animals\n");
        String name;
        try {
            name = scanner.nextLine();
            logger.info(String.join(", ", currentCabinet.getPatientsIDsFromName(name)));
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private static void commandGetAnimalFromSpecie() {
        System.out.println("Enter a Specie to look for animals\n");
        String name;
        try {
            name = scanner.nextLine();
            logger.info(String.join(", ", currentCabinet.getPatientsIDsFromSpecie(name)));

        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private static void commandSelectCabinet() {

        System.out.println("Enter a name to select a cabinet\n");
        String name;
        try {
            name = scanner.nextLine();

            if (currentCabinet != null) {
                currentCabinet.unsubscribeNotification(uid);
            }

            currentCabinet = (VeterinariesCabinet) registry.lookup(name);
            currentCabinet.subscribeNotification(uid, new RmiClient());
            logger.info("cabinet selected");
        } catch (Exception e) {
            logger.info("fail to select cabinet");
            logger.severe(e.getMessage());
        }
    }

    @Override
    public void notifyClient(String message) throws RemoteException {
        System.out.println(message);
    }
}
