package rmi.common.animal;

import java.io.Serializable;


public interface Specie extends Serializable {
    String info();
    String getName();
}
