#include <iostream>
#include <fstream>


std::string readConfigToken(const std::string& configFilePath, const std::string& key) {
    std::string line;
    std::ifstream configFile(configFilePath);

    if (configFile.is_open()) {
        while (getline(configFile, line)) {
            size_t delimiterPos = line.find('=');
            if (delimiterPos != std::string::npos) {
                std::string configKey = line.substr(0, delimiterPos);
                std::string configValue = line.substr(delimiterPos + 1);

                // Trim whitespace from both key and value
                configKey.erase(0, configKey.find_first_not_of(" \t"));
                configKey.erase(configKey.find_last_not_of(" \t") + 1);
                configValue.erase(0, configValue.find_first_not_of(" \t"));
                configValue.erase(configValue.find_last_not_of(" \t") + 1);

                if (configKey == key) {
                    configFile.close();
                    return configValue;
                }
            }
        }
        configFile.close();
    } else {
        std::cerr << "Config file not found. Creating a new one with default token." << std::endl;

        // Create a new config file with the default token
        std::ofstream newConfigFile(configFilePath);
        if (newConfigFile.is_open()) {
            newConfigFile << key << "=000" << std::endl;
            newConfigFile.close();
        } else {
            std::cerr << "Unable to create a new config file." << std::endl;
        }
    }

    return "000"; // Default token if key not found or config file not opened
}

int main() {
    std::cout << "Starting 1Hoever Bot" << std::endl;
    std::string configFilePath = "cHoever.ini"; // Specify the path to your config file
    std::string token = readConfigToken(configFilePath, "YourTokenKey");

    if (!token.empty()) {
        std::string command = "java -jar ..\\target\\Hoever-1.0-SNAPSHOT.jar " + token;
        system(command.c_str());
    } else {
        std::cout << "Token not found in config." << std::endl;
    }

    return 0;
}
