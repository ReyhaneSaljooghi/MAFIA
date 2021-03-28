public abstract class Player {
    String name;
    boolean isSavedByDoctor=false;
    boolean isKilled=false;
    boolean isSilent=false;
    boolean isMafia=false;
    int numbersOfVotes=0;

    public Player(String name) {
        this.name = name;
    }

    public int getNumbersOfVotes() {
        return numbersOfVotes;
    }

    public String getName() {
        return name;
    }
    public void resetnemberofVotes(){
        numbersOfVotes=0;
    }

}
