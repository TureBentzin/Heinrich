cmake_minimum_required(VERSION 3.26)
project(bin)

set(CMAKE_CXX_STANDARD 17)

add_executable(bin main.cpp)

# Rename the bin.exe to cHoever.exe
set(EXECUTABLE_OUTPUT_PATH ${CMAKE_BINARY_DIR})
add_custom_command(TARGET bin POST_BUILD
        COMMAND ${CMAKE_COMMAND} -E copy $<TARGET_FILE:bin> ${CMAKE_BINARY_DIR}/cHoever.exe
)

# Copy cHoever.exe to /out directory
add_custom_command(TARGET bin POST_BUILD
        COMMAND ${CMAKE_COMMAND} -E copy $<TARGET_FILE:bin> ${CMAKE_BINARY_DIR}/../out/cHoever.exe
)
