public class detective extends villager{
    detective(String name){
        super(name);
    }
    public  boolean checkIsMafia(Player player){
       if (player instanceof godfather)
            return false;
       else if (player instanceof mafia )
           return true;
       else  return  false;

    }
}
