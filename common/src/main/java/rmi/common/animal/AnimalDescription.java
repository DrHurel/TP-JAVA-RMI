package rmi.common.animal;

import java.io.Serializable;

public class AnimalDescription implements Serializable {
    String name;
    String master;
    SpecieImpl specie;

    public AnimalDescription(
            String name,
            String master,
            SpecieImpl specie) {
        this.name = name;
        this.master = master;
        this.specie = specie;
    }

    public String getName() {
        return name;
    }

    public String getMaster() {
        return master;
    }

    public SpecieImpl getSpecie() {
        return specie;
    }

}
