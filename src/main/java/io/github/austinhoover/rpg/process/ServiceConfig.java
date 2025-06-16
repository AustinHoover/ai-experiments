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
    private List<String> mutuallyExclusiveServices;

    public ServiceConfig(String name, String command) {
        this.name = name;
        this.command = command;
        this.arguments = new ArrayList<>();
        this.workingDirectory = null;
        this.mutuallyExclusiveServices = new ArrayList<>();
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

    /**
     * Get the list of services that are mutually exclusive with this service
     * @return List of service names that cannot run alongside this service
     */
    public List<String> getMutuallyExclusiveServices() {
        return mutuallyExclusiveServices;
    }

    /**
     * Add a service to the list of mutually exclusive services
     * @param serviceName The name of the service that cannot run alongside this service
     */
    public void addMutuallyExclusiveService(String serviceName) {
        if (!mutuallyExclusiveServices.contains(serviceName)) {
            mutuallyExclusiveServices.add(serviceName);
        }
    }

    /**
     * Check if a given service is mutually exclusive with this service
     * @param serviceName The name of the service to check
     * @return true if the service cannot run alongside this service
     */
    public boolean isMutuallyExclusiveWith(String serviceName) {
        return mutuallyExclusiveServices.contains(serviceName);
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