package com.company.archon.enums;

public enum GameStatus {
    RUNNING("running"),
    GAME_OVER("game over"),
    COMPLETED("completed"),
    PAUSED;

    public final String value;

    GameStatus(String value) {
        this.value = value;
    }

    GameStatus(){
        value = null;
    }
}
