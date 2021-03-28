public class detective extends villager{
    public boolean wakeupAtNight=true;
    detective(String name){
        super(name);
    }
    public  void checkIsMafia(Player player){
       if (player instanceof mafia &&!(player instanceof godfather)){
           System.out.println("Yes");
           return;
       }
       System.out.println("No");

    }
}
