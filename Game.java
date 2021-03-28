import java.util.Scanner;

public class Game {
    static Scanner input = new Scanner(System.in);
   static int numbersOfplayers=0;
   static  Player[]players=null;
   static  String[]names=null;
   static boolean jokerwon=false;
   static  boolean noonedied=false;
    static  int Daynumber=0 ;
    static  int Nightnumber=0;
    static String result=null;
    public static void main(String[] args) {

        String roles[]={"Joker", "villager", "detective", "doctor", "bulletproof", "mafia", "godfather", "silencer"};
        boolean gameIsCreated=false;
        boolean gameIsStarted=false;
        String namesOfPlayers;


     outer:   while (true) {
            String order = input.nextLine();
            if (order.equals("create_game") && !gameIsCreated) {
                gameIsCreated = true;
                namesOfPlayers = input.nextLine();
                names = namesOfPlayers.split(" ");
               players=new Player[names.length];
            }
            if (order.equals("assign_role")) {
                if (gameIsCreated == false) {
                    System.out.println("no game created");
                    continue;
                }

                    String name = input.next();
                    String role = input.next();
                    if (!nameexists(names, name)) {
                        System.out.println("user not found");
                        continue;
                    }
                    if (!roleexists(roles, role)) {
                        System.out.println("role not found");
                        continue;
                    }
                    if (nameexists(names, name) && roleexists(roles, role)) {
                        players[numbersOfplayers] = createrole(name, role);
                        numbersOfplayers++;
                        continue;
                    }
                }

            if (order.equals("start_game")) {
                if (!hasrole(players, numbersOfplayers)) {
                    System.out.println("one or more player do not have a role");
                }
                else if (!gameIsCreated) {
                    System.out.println("no game created");
                }
                else if (gameIsStarted) {
                    System.out.println("game has already started");
                }
                else {
                    /*represent roles in the begining of the game*/
                    if (Daynumber == 0) {
                        for (int i = 0; i <=numbersOfplayers; i++) {
                            System.out.println(players[i].name + ":" + players[i].getClass().getSimpleName());
                        }
                    }
                    else System.out.println(result);
                    gameIsStarted = true;
                    System.out.println("Ready? Set! Go");

                    while (true) {
                        Daynumber++;
                        System.out.println("Day" + Daynumber);
                        Day();
                        if (jokerwon) {
                            System.out.println("Joker won!");
                            break outer;
                        }
                        if (noonedied) {
                            System.out.println("nobody died");
                        }
                        for (int i = 0; i <= numbersOfplayers; i++) {
                            if (players[i].getNumbersOfVotes() == maxvote(players)) {
                                players[i].isKilled = true;
                                System.out.println(players[i].name + "died");
                            }

                        }
                        resetvote(players);
                        //Night
                        Nightnumber++;
                        System.out.println("Night" + Nightnumber);
                        if (Nightnumber == 0) {
                            for (int i = 0; i <= numbersOfplayers; i++) {
                                if(!players[i].isKilled&&players[i].wakeupAtNight)
                                System.out.println(players[i].name + ":" + players[i].getClass().getSimpleName());
                            }
                        }
                        Night();

                    }
                }
            }
        }

    }
    public static Player createrole(String name,String role){
        switch (role){
            case "Joker":return new Joker(name);
            case "silencer":return new Silencer(name);
            case "godfather":return new godfather(name);
            case "mafia":return new mafia(name);
            case "villager":return new villager(name);
            case "doctor":return new doctor(name);
            case "bulletproof":return new bulletproof(name);
            case "detective":return new detective(name);
        }
        return  null;
    }
    public static  boolean nameexists(String[]names,String name){
        for (String it:names){
            if (name.equals(it))
                return true;
        }
        return  false;
    }
    public static boolean roleexists(String[]roles,String role){
        for (String it:roles){
            if (role.equals(it))
                return true;
        }
        return  false;
    }
    public static boolean hasrole(Player[]players,int size){
        for (int i=0;i<size;i++){
            if (players[i]==null)
                return false;
        }
        return  true;
    }
    public static void Day(){
       while (true){
            String voterOrEnd=input.next();
            if (voterOrEnd.equals("end_vote")) {
                if (checkifvotesEqual(players)>=2) {
                    noonedied=true;
                    break;
                }
                for (int i=0;i<=numbersOfplayers;i++) {
                    if (players[i].getNumbersOfVotes() == maxvote(players))
                        if (players[i] instanceof Joker) {
                           jokerwon=true;
                           break;
                        }
                }

            }
            String votee=input.next();
            if (!nameexists(names,votee)||!nameexists(names,voterOrEnd)){
                System.out.println("user not found");
                continue;
            }

            if (findplayer(voterOrEnd).isSilent) {
                System.out.println("voter is silenced");
                continue;
            }
            if (findplayer(votee).isKilled){
                System.out.println("votee already dead");
                continue ;
            }
            findplayer(votee).numbersOfVotes++;


        }

    }
    public static void Night(){
        result=null;
        resetvote(players);
        int actionsOfsilencer=0;
        boolean detectiveAsked=false;
        while (true){

            String doer=input.next();
            String taker=input.next();
            if (findplayer(doer).isKilled||findplayer(taker).isKilled) {
                System.out.println("user is dead");
                continue;
            }
            if(!findplayer(doer).wakeupAtNight||!findplayer(taker).wakeupAtNight) {
                System.out.println("user can not wake up during night");
                continue;
            }
            if (findplayer(doer) instanceof mafia){
                if (findplayer(taker)==null) {
                    System.out.println("user not joined");
                    continue;
                }
                if (findplayer(taker).isKilled){
                    System.out.println("votee already dead");
                    continue;
                }
                findplayer(taker).numbersOfVotes++;
                result+="mafia tried to kill"+findplayer(taker).name+"\n";
            }
            if (findplayer(doer) instanceof detective){
                if (detectiveAsked){
                    System.out.println("detective has already asked");
                    continue;
                }
                detectiveAsked=true;
               ((detective)findplayer(doer)).checkIsMafia(findplayer(taker));
               continue;
            }
            if (findplayer(doer) instanceof doctor){
                findplayer(taker).isSavedByDoctor=true;
                continue;
            }

            if (findplayer(doer)instanceof Silencer){
                if (actionsOfsilencer==0) {
                    findplayer(taker).isSilent = true;
                    actionsOfsilencer++;
                    result+="Silenced"+findplayer(taker).name+"\n";
                    continue;
                }

            }
            if (doer.equals("end_Night")) {
                    int numbersofequalvotes = checkifvotesEqual(players);
                    if (numbersofequalvotes > 2) {
                        break;
                    }
                    else if(numbersofequalvotes==2){
                        for (int i=0;i<=numbersOfplayers;i++) {
                            if (players[i].getNumbersOfVotes() == maxvote(players)) {
                                for (int j = i + 1; j <= numbersOfplayers; j++) {
                                    if (players[j].getNumbersOfVotes() == maxvote(players)&&players[j].isSavedByDoctor) {
                                        result += players[i].name + "is killed"+"\n";
                                        break;
                                    }
                                    if (players[j].getNumbersOfVotes() == maxvote(players)&&players[i].isSavedByDoctor) {
                                        result += players[j].name + "is killed"+"\n";
                                        break;
                                    }

                                }

                            }
                        }
                    }
                    else {
                        for (int i = 0; i <= numbersOfplayers; i++) {
                            if (players[i].getNumbersOfVotes() == maxvote(players)) {
                                players[i].isKilled = true;
                                result += players[i].name + "is killed"+"\n";
                                break;

                            }

                        }
                    }
            }

        }


    }

    public static int maxvote(Player[]players){

        int max=0;
        for(int i=0;i<=numbersOfplayers;i++) {
            if (players[i].getNumbersOfVotes() > max) {
                max = players[i].getNumbersOfVotes();
            }
        }
            return max;
    }
    public static int checkifvotesEqual(Player []players){
        int max=maxvote(players);
        int nums=0;
        for (Player it:players){
            if (it.getNumbersOfVotes()==max)
                nums++;
        }
        return nums;
    }
    public static Player findplayer(String name){
        for (Player it:players){
            if (it.name.equals(name))
                return it;
        }
        return  null;
    }
    public static void  resetvote(Player[]players){
        for (Player it:players)
            it.resetnemberofVotes();
    }
}
