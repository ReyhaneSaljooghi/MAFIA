public class mafia extends Player{
    public mafia(String name) {
        super(name);
    }
    String votees[]=new String[100];
    int numvoteesatnight=0;

    { super.wakeupAtNight=true; }
}
