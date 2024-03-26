#include <iostream>
#include "fstream"

const std::string CONFIG_FILE = "launcher.config";

int main() {

    std::cout << "Welcome to the 1Hoever Launcher!" << std::endl;
    std::cout << "reading launcher configuration from file: " << CONFIG_FILE << std::endl;
    // check if the file exists and is readable. if not, create it.

    std::fstream file(CONFIG_FILE, std::ios::in | std::ios::out);
    if (!file.is_open()) {
        std::cout << "file not found, creating new file" << std::endl;
        file.open(CONFIG_FILE, std::ios::out);
        file << "Hello, World!" << std::endl;
        file.close();
    } else {
        std::cout << "file found, reading contents" << std::endl;
        std::string line;
        while (std::getline(file, line)) {
            std::cout << line << std::endl;
        }
        file.close();
    }
    return 0;
}
