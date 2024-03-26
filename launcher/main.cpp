#include <iostream>
#include "fstream"
#include <cstring>


const std::string CONFIG_FILE = "launcher.config";
const std::string DEFAULT_CONFIG = ""
                                   "token: enter_token_here;\n"
                                   "bot_config: bot_config.json;\n"
                                   "updater: 1;\n"
                                   "git_repo: https://github.com/TureBentzin/1Hoever.git;\n"
                                   "debug: 0;\n";

bool debug = false;

int main(int argc, char **argv) {

    {
        //check if -d flag is present
        for (int i = 0; i < argc; i++) {
            if (strcmp(argv[i], "-d") == 0) {
                debug = true;
                std::cout << "Debug mode enabled!" << std::endl << std::endl;
            }
        }
    }

    std::cout << "Welcome to the 1Hoever Launcher!" <<
              std::endl;
    std::cout << "reading launcher configuration from file: " << CONFIG_FILE <<
              std::endl;
// check if the file exists and is readable. if not, create it.

    attempt_read_config:
    std::fstream file(CONFIG_FILE, std::ios::in | std::ios::out);
    if (!file.is_open()) {
        std::cout << "file not found, creating new file" << std::endl;
        file.open(CONFIG_FILE, std::ios::out);
        file << DEFAULT_CONFIG << std::endl;
        file.close();
        goto attempt_read_config;
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
