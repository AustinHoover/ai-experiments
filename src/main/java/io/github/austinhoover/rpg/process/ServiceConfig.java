package io.github.austinhoover.rpg.process;

import java.util.List;
import java.util.ArrayList;

/**
 * Configuration for a service that can be managed by the ProcessManager
 */
public class ServiceConfig {
    private String name;
    private String command;
    private List<String> arguments;
    private String workingDirectory;

    public ServiceConfig(String name, String command) {
        this.name = name;
        this.command = command;
        this.arguments = new ArrayList<>();
        this.workingDirectory = null;
    }

    public String getName() {
        return name;
    }

    public String getCommand() {
        return command;
    }

    public List<String> getArguments() {
        return arguments;
    }

    public void addArgument(String argument) {
        this.arguments.add(argument);
    }

    public String getWorkingDirectory() {
        return workingDirectory;
    }

    public void setWorkingDirectory(String workingDirectory) {
        this.workingDirectory = workingDirectory;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(command);
        for (String arg : arguments) {
            sb.append(" ").append(arg);
        }
        return sb.toString();
    }
} 