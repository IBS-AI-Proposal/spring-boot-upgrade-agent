package com.example.upgradeagent.exception;

public class UpgradeFailedException extends RuntimeException {
    public UpgradeFailedException(String message) {
        super(message);
    }
}
