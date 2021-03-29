public class Silencer extends mafia{
    public Silencer(String name) {
        super(name);
    }
    public  void makeSilent(Player player){
        player.isSilent=true;
    }
    { super.wakeupAtNight=true; }
}
