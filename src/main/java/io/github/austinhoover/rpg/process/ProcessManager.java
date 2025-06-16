package io.github.austinhoover.rpg.process;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import com.google.gson.Gson;

import java.util.logging.Level;

/**
 * Manages external processes that can be started and stopped programmatically
 */
public class ProcessManager {
    private static final Logger logger = Logger.getLogger(ProcessManager.class.getName());
    private final Map<String, Process> runningProcesses;
    private final Map<String, ServiceConfig> serviceConfigs;

    public static final ProcessManager INSTANCE = new ProcessManager();

    public ProcessManager() {
        this.runningProcesses = new ConcurrentHashMap<>();
        this.serviceConfigs = new HashMap<>();
    }

    /**
     * Load a service configuration from a JSON file
     * @param configName The name of the configuration file (without .json extension)
     * @return The contents of the config file as a string, or null if not found
     */
    public ServiceConfig loadConfigFile(String configName) {
        String configPath = "data/cfg/" + configName + ".json";
        File configFile = new File(configPath);
        
        if (!configFile.exists()) {
            logger.warning("Config file not found: " + configPath);
            return null;
        }

        try {
            String content = new String(java.nio.file.Files.readAllBytes(configFile.toPath()));
            Gson gson = new Gson();
            ServiceConfig config = gson.fromJson(content, ServiceConfig.class);
            return config;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error reading config file: " + configPath, e);
            return null;
        }
    }

    /**
     * Register a service configuration
     * @param config The service configuration to register
     */
    public void registerService(ServiceConfig config) {
        serviceConfigs.put(config.getName(), config);
    }

    /**
     * Start a service by its registered name
     * @param serviceName The name of the service to start
     * @return true if the service was started successfully
     */
    public boolean startService(String serviceName) {
        ServiceConfig config = serviceConfigs.get(serviceName);
        if (config == null) {
            logger.warning("Service " + serviceName + " not found in configuration");
            return false;
        }

        if (runningProcesses.containsKey(serviceName)) {
            logger.warning("Service " + serviceName + " is already running");
            return false;
        }

        try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            
            // Set up the command and arguments
            processBuilder.command().add(config.getCommand());
            processBuilder.command().addAll(config.getArguments());

            // Set working directory if specified
            if (config.getWorkingDirectory() != null) {
                processBuilder.directory(new File(config.getWorkingDirectory()));
            }

            // Redirect error stream to output stream
            processBuilder.redirectErrorStream(true);

            System.out.println(config.getWorkingDirectory() + " " + config.getCommand() + " " + config.getArguments());

            // Start the process
            Process process = processBuilder.start();
            runningProcesses.put(serviceName, process);

            // Start a thread to monitor the process
            new Thread(() -> {
                try {
                    int exitCode = process.waitFor();
                    logger.info("Service " + serviceName + " exited with code " + exitCode);
                    runningProcesses.remove(serviceName);
                } catch (InterruptedException e) {
                    logger.log(Level.WARNING, "Service " + serviceName + " was interrupted", e);
                }
            }).start();

            return true;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to start service " + serviceName, e);
            return false;
        }
    }

    /**
     * Stop a running service
     * @param serviceName The name of the service to stop
     * @return true if the service was stopped successfully
     */
    public boolean stopService(String serviceName) {
        Process process = runningProcesses.get(serviceName);
        if (process == null) {
            logger.warning("Service " + serviceName + " is not running");
            return false;
        }

        try {
            // For Windows, we need to use taskkill to ensure the process tree is terminated
            if (System.getProperty("os.name").toLowerCase().contains("windows")) {
                new ProcessBuilder("taskkill", "/F", "/T", "/PID", String.valueOf(process.pid()))
                    .start()
                    .waitFor();
            } else {
                // For Unix-like systems, we can use destroyForcibly
                process.destroyForcibly().waitFor();
            }
            
            runningProcesses.remove(serviceName);
            return true;
        } catch (IOException | InterruptedException e) {
            logger.log(Level.SEVERE, "Failed to stop service " + serviceName, e);
            return false;
        }
    }

    /**
     * Check if a service is currently running
     * @param serviceName The name of the service to check
     * @return true if the service is running
     */
    public boolean isServiceRunning(String serviceName) {
        Process process = runningProcesses.get(serviceName);
        if (process == null) {
            return false;
        }
        return process.isAlive();
    }

    /**
     * Get a list of all running services
     * @return A map of service names to their running status
     */
    public Map<String, Boolean> getRunningServices() {
        Map<String, Boolean> status = new HashMap<>();
        for (String serviceName : serviceConfigs.keySet()) {
            status.put(serviceName, isServiceRunning(serviceName));
        }
        return status;
    }

    /**
     * Shuts down all currently running processes
     */
    public void shutdownAll() {
        logger.info("Shutting down all running processes...");
        for (String serviceName : new ArrayList<>(runningProcesses.keySet())) {
            stopService(serviceName);
        }
        runningProcesses.clear();
    }
} 