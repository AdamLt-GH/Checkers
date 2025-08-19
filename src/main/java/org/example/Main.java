package org.example;

import processing.core.PApplet;

public class Main extends PApplet {
    private BoardRenderer boardRenderer;

    public void settings() {
        size(BoardRenderer.BOARD_SIZE, BoardRenderer.BOARD_SIZE);
    }

    public void setup() {
        boardRenderer = new BoardRenderer();
        noStroke();
    }

    public void draw() {
        boardRenderer.draw(this);
    }

    public static void main(String[] args) {
        PApplet.main(Main.class.getName());
    }
}
