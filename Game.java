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
    static String result="";
    static String triedtokill="";
    public static void main(String[] args) {
        //all the roles in the game
        String roles[]={"Joker", "villager", "detective", "doctor", "bulletproof", "mafia", "godfather", "silencer"};
        boolean gameIsCreated=false;
        boolean gameIsStarted=false;
        String namesOfPlayers;


     outer:   while (true) {
            String order = input.nextLine();
            //This order creates a game
            if (order.equals("create_game") && !gameIsCreated) {
                gameIsCreated = true;
                namesOfPlayers = input.nextLine();
                names = namesOfPlayers.split(" ");

               players=new Player[names.length];
            }
            //This part is for assigning roles
            if (order.equals("assign_role")) {
                if (gameIsCreated == false) {
                    System.out.println("no game created");
                    continue;
                }

                    String name = input.next();
                  if(name.equals("get_game_state")){
                    get_game_state();
                    continue ;
                  }
                    String role = input.next();
                    //no user with this name
                    if (!nameexists(names, name)) {
                        System.out.println("user not found");
                        continue;
                    }
                    //this role is not in the game
                    if (!roleexists(roles, role)) {
                        System.out.println("role not found");
                        continue;
                    }
                    //the player is added to the array of players
                    if (nameexists(names, name) && roleexists(roles, role)) {
                        players[numbersOfplayers] = createrole(name, role);
                        numbersOfplayers++;
                        continue;
                    }
                }
            //the game starts
            if (order.equals("start_game")) {
                if (!hasrole(players, names.length)) {
                    System.out.println("one or more player do not have a role");
                    continue ;
                }
                else if (!gameIsCreated) {
                    System.out.println("no game created");
                    continue ;
                }
                else if (gameIsStarted) {
                    System.out.println("game has already started");
                    continue ;
                }
                else {
                    /*represent roles in the begining of the game*/
                    if (Daynumber == 0) {
                        for (int i = 0; i <numbersOfplayers; i++) {
                            System.out.println(players[i].name + ":" + players[i].getClass().getSimpleName());
                        }
                    }
                    gameIsStarted = true;
                    System.out.println("Hi!!!!Ready? Set! Go");
                    /*The days and nights come after each other till the end of the game*/
                    while (true) {
                        Daynumber++;
                        System.out.println("Day" + Daynumber);
                        System.out.println(result);
                        resetvote(players);
                        jokerwon=false;
                         noonedied=false;
                         /*the method day is called*/
                        Day();
                        /*if joker is killed in day*/
                        if (jokerwon) {
                            System.out.println("Joker won!");
                            break outer;
                        }
                        if (noonedied) {
                            System.out.println("nobody died");
                        }

                        for (int i = 0; i <numbersOfplayers; i++) {
                            if (players[i].getNumbersOfVotes() == maxvote(players)&&!noonedied) {
                                players[i].isKilled = true;
                                System.out.println(players[i].name + " died");
                            }

                        }
                        //check if the game is finished int the end of day
                        if (numbersOfMafia()>=numbersOfvillager()){
                            System.out.println("Mafia won!");
                            break outer;
                        }
                        if (numbersOfMafia()==0){
                            System.out.println("Villagers won!");
                            break outer;
                        }
                        //we reset vote and silent players in the end of the day
                        resetvote(players);
                        resetSilent(players);
                        //Night starts
                        Nightnumber++;
                        System.out.println("Night" + Nightnumber);
                        for (int i = 0; i < numbersOfplayers; i++) {
                            if(!players[i].isKilled&&players[i].wakeupAtNight)
                                System.out.println(players[i].name + ":" + players[i].getClass().getSimpleName());
                        }
                        Night();
                        //check if the game is finished int the end of day
                        if (numbersOfMafia()>=numbersOfvillager()){
                            System.out.println("Mafia won!");
                            break outer;
                        }
                        if (numbersOfMafia()==0){
                            System.out.println("Villagers won!");
                            break outer;
                        }

                    }
                }
            }
            if (order.equals("get_game_state ")){
                get_game_state();
                continue ;
            }

        }

    }
    //new each player with her/his role
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
    //check if the name is in the array name of players
    public static  boolean nameexists(String[]names,String name){
        for (String it:names){
            if (name.equals(it))
                return true;
        }
        return  false;
    }
    //check if the role is in game
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
     outer:  while (true){
            String voterOrEnd=input.next();
            if (voterOrEnd.equals("get_game_status")){
                get_game_state();
                continue ;
            }
            if (voterOrEnd.equals("end_vote")) {
                if (checkifvotesEqual(players)>=2) {
                    noonedied=true;
                    break outer;
                }
                for (int i=0;i<numbersOfplayers;i++) {
                    if (players[i].getNumbersOfVotes() == maxvote(players))
                        if (players[i] instanceof Joker) {
                           jokerwon=true;
                           break outer;

                        }
                }

                break outer;

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
        result="";
        int actionsOfsilencer=0;
        boolean detectiveAsked=false;
        outer:while (true){

            String doer=input.next();
            if (doer.equals("get_game_status")){
                get_game_state();
                continue ;
            }
            ///////////////////////////////
            if (doer.equals("end_Night")) {
                for (int i=0;i<numbersOfplayers;i++){
                    if (players[i]instanceof mafia){
                        if (((mafia) players[i]).numvoteesatnight!=0){
                            ThelastvoteOfmafia((mafia)players[i]);
                        }
                    }
                }
                ///////////////
                int numbersofequalvotes = checkifvotesEqual(players);
                //no one die
                if (numbersofequalvotes > 2) {
                    return;
                }
                //when the votes of 2 players are the same we check if one of them is saved or not
                else if(numbersofequalvotes==2){
                    for (int i=0;i<numbersOfplayers;i++) {
                        if (players[i].getNumbersOfVotes() == maxvote(players)) {
                            for (int j = i + 1; j < numbersOfplayers; j++) {
                                if (players[j].getNumbersOfVotes() == maxvote(players)&&players[j].isSavedByDoctor) {
                                    players[i].isKilled=true;
                                    result += players[i].name + " is killed"+"\n";
                                    return;
                                }
                                if (players[j].getNumbersOfVotes() == maxvote(players)&&players[i].isSavedByDoctor) {
                                    players[j].isKilled=true;
                                    result += players[j].name + " is killed"+"\n";
                                   return;
                                }

                            }

                        }
                    }
                    return;
                }
                else {

                    for (int i = 0; i < numbersOfplayers; i++) {
                        if (players[i].getNumbersOfVotes() == maxvote(players)) {
                            if(players[i] instanceof bulletproof){
                                //bulletproof has one life in the game
                                if (((bulletproof) players[i]).oneMorelife==1){
                                    ((bulletproof) players[i]).oneMorelife=0;
                                     return;
                                }
                                else {
                                    players[i].isKilled = true;
                                    result += players[i].name + " is killed" + "\n";
                                    return;
                                }
                            }
                            if (!players[i].isSavedByDoctor) {

                                players[i].isKilled = true;
                                result += players[i].name + " is killed" + "\n";
                               return;
                            }
                            return;

                        }
                        ///addd
                        ///return;
                    }
                }
            }

            String taker1=input.next();
            String[]split=taker1.split(" ");
            String taker=split[split.length-1];
            if (findplayer(doer).isKilled||findplayer(taker).isKilled) {
                System.out.println("user is dead");
                continue;
            }
            if(!findplayer(doer).wakeupAtNight) {
                System.out.println("user can not wake up during night");
                continue;
            }
            if (findplayer(doer)instanceof Silencer){
                //this plaer silent other players
                if (actionsOfsilencer==0) {
                    findplayer(taker).isSilent = true;
                    actionsOfsilencer++;
                    result+="Silenced "+findplayer(taker).name+"\n";
                    continue;
                }

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
                ///////////
               ((mafia) findplayer(doer)).votees[((mafia) findplayer(doer)).numvoteesatnight]=taker;
                ((mafia) findplayer(doer)).numvoteesatnight++;
                ////////////
                //findplayer(taker).numbersOfVotes++;
               // result+="mafia tried to kill "+findplayer(taker).name+"\n";
                continue ;
            }
            if (findplayer(doer) instanceof detective){
                //if the detective ask for more than one time at the night
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

        }

    }

    public static int maxvote(Player[]players){

        int max=0;
        for(int i=0;i<numbersOfplayers;i++) {
            if (players[i].getNumbersOfVotes() >= max) {
                max = players[i].getNumbersOfVotes();
            }
        }
            return max;
    }
    public static int checkifvotesEqual(Player []players){
        int max=maxvote(players);
        int nums=0;
        for (int i=0;i<numbersOfplayers;i++){
            if (players[i].getNumbersOfVotes()==max)
                nums++;
        }
        return nums;
    }
    public static Player findplayer(String name){
        for (int i=0;i<numbersOfplayers;i++){
            if (players[i].name.equals(name))
                return players[i];
        }
        return  null;
    }
    public static void  resetvote(Player[]players){
        for (int i=0;i<numbersOfplayers;i++)
           players[i].resetnemberofVotes();
    }
    public static void  resetSilent(Player[]players){
        for (int i=0;i<numbersOfplayers;i++)
            players[i].isSilent=false;
    }
    public static void get_game_state(){
        System.out.println("Mafia = "+numbersOfMafia());
        System.out.println("Villager = "+numbersOfvillager());
    }
    public static int numbersOfMafia() {
        int mafias=0;
        for (int i = 0; i <numbersOfplayers; i++) {
            if (!(players[i].isKilled) && players[i] instanceof mafia)
                mafias++;
        }
        return mafias;
    }
    public static int numbersOfvillager() {
        int villagers=0;

        for (int i = 0; i < numbersOfplayers; i++) {
            if (!(players[i].isKilled) && players[i] instanceof villager)
                villagers++;
        }
        return villagers;
    }
    public  static void ThelastvoteOfmafia(mafia a){

        (findplayer(a.votees[a.numvoteesatnight-1])).numbersOfVotes++;
    }
    /*https://github.com/ReyhaneSaljooghi/Mafia-.git*/
}
