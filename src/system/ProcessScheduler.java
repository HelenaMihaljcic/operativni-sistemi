package system;

import memory.Process;

import java.util.PriorityQueue;

public class ProcessScheduler extends Thread {

    //ne znam da li valja racunanje burst time

    private static PriorityQueue<Process> red = new PriorityQueue<>((p1, p2) -> {
        return Integer.compare((int) p1.getBurstTime(), (int) p2.getBurstTime());
    });

    @Override
    public void run() {
        while (true) {
            try {
                // Uzmi proces sa najmanjim preostalim vremenom obrade
                Process p = red.poll();

                if (p == null) {
                    // Ako nema procesa u redu, sačekaj
                    Thread.sleep(100);
                    continue;
                }

                if (p.getStanje() == ProcessState.TERMINATED) {
                    continue;
                }

                p.setStanje(ProcessState.RUNNING);
                p.start(); // Pokreni proces

                // Čekaj da se proces završi
                p.join(); // Čekaj da proces završi pre nego što nastavi

                if (p.getBurstTime() > 0) {
                    // Ako proces nije završen, ponovo ga dodaj u red
                    red.add(p);
                }

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void addProcess(Process process) {
        red.add(process);
    }

    public static void removeProcess(Process process) {
        red.remove(process);
    }

    public PriorityQueue<Process> getRed() {
        return red;
    }
}
