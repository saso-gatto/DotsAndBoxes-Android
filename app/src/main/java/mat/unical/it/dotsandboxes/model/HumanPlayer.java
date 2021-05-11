/*
 * Dots and Boxes
 * Copyright (C) 2016  zDuo (Adithya J, Vazbloke)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package mat.unical.it.dotsandboxes.model;

public class HumanPlayer extends Player {
    private final Edge[] inputBuffer = new Edge[1];

    public HumanPlayer(String name) {
        super(name);
    }

    public void add(Edge edge) {
        synchronized (inputBuffer) {
            inputBuffer[0] = edge;
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

    @Override
    public void moveDLV() {

    }
}
