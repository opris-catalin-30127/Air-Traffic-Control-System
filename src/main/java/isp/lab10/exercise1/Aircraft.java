package isp.lab10.exercise1;

public class Aircraft implements Runnable {
    private String id;
    private Integer altitude;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getAltitude() {
        return altitude;
    }

    public void setAltitude(Integer altitude) {
        this.altitude = altitude;
    }

    public Aircraft(String id) {
        this.id = id;
        this.altitude = 0;
    }

    @Override
    public String toString() {
        return "Aircraft{" +
                "id='" + id + '\'' +
                ", altitude=" + altitude +
                '}';
    }

    public void receiveAtcCommand(AtcCommand msg) {
        if (msg instanceof TakeoffCommand) {
            if (this.altitude == 0) { // hasn't taken off :)
                System.out.println(this + " is going to take off!");
                this.altitude = ((TakeoffCommand) msg).getAltitude();
                synchronized (this) {
                    this.notify();
                }
            } else {
                System.out.println("The plane as already taken off!");
            }

        } else if (msg instanceof LandCommand) {
            if (this.altitude == 0) {
                System.out.println("The plane hasn't taken off yet!");
            } else {
                System.out.println(this + " is going to land!");
                synchronized (this) {
                    this.notify();
                }
            }
        }

    }

    @Override
    public void run() {
        int maxAltitude = 0;
        long startTime = 0, endTime = 0;
        try {
            synchronized (this) {
                wait(); // waits for an AtcCommand, and only TAKEOFF commands will notify the Thread

                maxAltitude = altitude;
                altitude = 0;
                wait(15000); // taking off time
                for (int i = 1; i <= maxAltitude; i++) {
                    wait(10000);
                    altitude = i;
                }

                System.out.println("Plane " + id + " CRUISING");

                startTime = System.currentTimeMillis();

                wait();

                endTime = System.currentTimeMillis();


                for (int i = maxAltitude; i >= maxAltitude; i--) {
                    altitude = i;
                    wait(10000);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Plane " + id + " cruised for " + (double) (endTime - startTime) / 1000 + " seconds.");
    }
}
