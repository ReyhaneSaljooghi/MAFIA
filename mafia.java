public class mafia extends Player{
    public mafia(String name) {
        super(name);
    }
    public void wakeup(){
        super.wakeupAtNight=true;
    }
}
