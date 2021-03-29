public class doctor extends villager{
    {super.wakeupAtNight=true;}
    doctor(String name){
        super(name);
    }
    public void saveAplayer(Player player){
        player.isSavedByDoctor=true;
    }


}

