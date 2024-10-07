package rmi.common.animal;

public class SpecieImpl implements Specie {

    String name;
    int avgLifeSpan;

    public SpecieImpl(String name, int avgLifeSpan) {
        this.name = name;
        this.avgLifeSpan = avgLifeSpan;
    }

    public String info() {
        return String.format("%s : %d year", name, avgLifeSpan);
    }

    @Override
    public String getName() {
        return name;
    }
}
