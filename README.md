# Pathfinding Visualizer

This project is a Java Swing-based pathfinding visualization tool designed to demonstrate the A* algorithm (among other pathfinding algorithms) finding the shortest path from a start point to an end point on a grid. Users can dynamically create start points, end points, and obstacles (walls) on the grid through mouse and keyboard interactions, and watch the algorithm's real-time pathfinding process.

## Features

- **Interactive Interface**: Set start points, end points, and obstacles on the grid via mouse clicks and dragging.
- **Dynamic Pathfinding**: Real-time display of the A* algorithm's search process, including open set, closed set, and the final path.
- **Adjustable Parameters**: Adjust the algorithm's speed and the grid size through keyboard input, and initiate pathfinding.
- **Random Maze Generation**: A feature to automatically generate random mazes, adding complexity and fun to the pathfinding challenge.

## Operating Guide

- **Add Start Point**: Left-click on the grid to set a start point.
- **Add End Point**: Right-click on the grid to set an end point.
- **Create and Remove Walls**: Drag with the left mouse button to create walls; drag with the right mouse button to remove walls.
- **Generate Maze**: Press the `w` key to automatically generate a random maze.
- **Start Pathfinding**: Press the `f` key to start the A* algorithm for pathfinding.
- **Speed Adjustment**: Use `,` and `.` keys to adjust the execution speed of the algorithm.
- **Reset Grid**: Press the `r` key to reset the entire grid.

## Completion Time

- Initial Completion: November 2022
- Latest Update: February 2024

## Changelog

### February 2024 Update

- **Performance Optimization**: Transitioned from using `ArrayList` to `ConcurrentHashMap.newKeySet()` for declaring node collections, enhancing performance and thread safety during multi-threaded operations. This change is primarily reflected in the management of open sets, closed sets, path sets, and wall sets in the `Grid` class.
- **Introduction of Thread Techniques**: Introduced thread-related techniques to optimize the pathfinding process. In particular, in the `PathFinding` class, pathfinding is executed in a new thread to prevent UI freezing and ensure smoother user interface responsiveness.
- **Improvements in Interface and User Interaction**: Added mouse wheel event listening to allow users to adjust the grid size, further improving user experience.

### November 2022 Update

- **Basic Functionality Implemented**: Achieved the primary objectives of visualizing pathfinding on a grid. Implemented user interactions for setting up the environment (start points, end points, walls) and integrated the A* pathfinding algorithm with real-time visualization.
