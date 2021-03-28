public class doctor extends villager{
    doctor(String name){
        super(name);
    }
    public void saveAplayer(Player player){
        player.isSavedByDoctor=true;
    }
}
