package isp.lab10.exercise1;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.LinkedList;


public class ATC {
    private List<Aircraft> aircrafts;

    public ATC() {
        this.aircrafts = new LinkedList<Aircraft>();
    }

    public void sendCommand(String aircraftId, AtcCommand cmd) {

        boolean planeExists = false;
        for (Aircraft a : aircrafts) {
            if (a.getId().equals(aircraftId)) {
                planeExists = true;
                a.receiveAtcCommand(cmd);
            }
        }
        if (!planeExists) {
            System.out.println("There's no such aircraft! The given {aircraftId} didn't correspond to any plane's.");
        }
    }
    public void addAircraft(String id) {
        Aircraft newAircraft = new Aircraft(id);
        aircrafts.add(newAircraft);
        Thread t = new Thread(newAircraft);
        t.start();
    }
    public void showAircrafts() {
        for (Aircraft a : aircrafts) {
            System.out.println(a);
        }
    }


    public static void main(String[] args) {
        boolean correctCommand = false, atcRunning = true;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String cmdString, command, aircraftid;
        String []cmdWords;
        int altitude;
        final String helpMessage = "The commands are:\n\tADD {aircraftId}\n\tTAKEOFF {aircraftId} {altitude}\n\tLAND {aircraftId}\n\tSHOW\n\tEXIT";

        ATC atc = new ATC();

        System.out.println(helpMessage);

        while (atcRunning) {
            try {
                correctCommand = false;
                cmdString = in.readLine().trim();
                cmdWords = cmdString.split(" +");
                if (cmdWords.length == 1 || cmdWords.length == 2 || cmdWords.length == 3) {
                    command = cmdWords[0].toUpperCase();
                    switch (command) {
                        case "ADD": // this just adds the plane (that also makes it wait)
                            if (cmdWords.length == 2) {
                                aircraftid = cmdWords[1];
                                atc.addAircraft(aircraftid);
                                correctCommand = true;
                            }
                            break;
                        case "TAKEOFF":
                            if (cmdWords.length == 3) {
                                aircraftid = cmdWords[1];
                                altitude = Integer.parseInt(cmdWords[2]); // may give error TODO
                                if (altitude > 0) { // TODO
                                    atc.sendCommand(
                                            aircraftid,
                                            new TakeoffCommand(altitude)
                                    );
                                    correctCommand = true;
                                }
                            }
                            break;
                        case "LAND":
                            if (cmdWords.length == 2) {
                                aircraftid = cmdWords[1];
                                atc.sendCommand(
                                        aircraftid,
                                        new LandCommand()
                                );
                                correctCommand = true;
                            }
                            break;
                        default:
                            if (cmdWords.length == 1) {
                                String word = cmdWords[0].trim().toUpperCase();
                                if (word.equals("EXIT")) {
                                    System.out.println("ATC stops.");
                                    correctCommand = true;
                                    atcRunning= false;
                                    break;
                                } else if (word.equals("SHOW")) {
                                    atc.showAircrafts();
                                    correctCommand = true;
                                } else if (word.equals("HELP")) {
                                    System.out.println(helpMessage);
                                    correctCommand = true;
                                }
                            }
                            break;
                    }
                }
            } catch(IOException e){
                e.printStackTrace();
            }

            if (!correctCommand) {
                System.out.println("The command was incorrect! :(");
                System.out.println(helpMessage);
            }
        }
    }
}
