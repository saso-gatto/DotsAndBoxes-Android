package it.dotsandboxes;

import it.dotsandboxes.classiEmbasp.Edge;
import it.dotsandboxes.classiEmbasp.Player;

public class HumanPlayer extends Player {
    private final Edge[] inputBuffer = new Edge[1];

    public HumanPlayer(String name) {
        super(name);
    }

    public void add(Edge line) {
        synchronized (inputBuffer) {
            inputBuffer[0] = line;
            inputBuffer.notify();
        }
    }

    private Edge getInput() {
        synchronized (inputBuffer) {
            if (inputBuffer[0] != null) {
                Edge temp = inputBuffer[0];
                inputBuffer[0] = null;
                return temp;
            }
            try {
                inputBuffer.wait();
            } catch (InterruptedException ignored) {
            }
            return this.getInput();
        }
    }

    @Override
    public Edge move() {
        return getInput();
    }
}
