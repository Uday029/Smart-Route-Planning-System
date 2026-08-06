# 🗺️ Smart Route Planning & Traffic Analysis System

A comprehensive, Java-based intelligent routing system that determines the optimal path between cities while factoring in dynamic real-world conditions like live traffic, weather alerts, and toll/fuel costs.

## ✨ Key Features

- **Multi-Algorithm Routing Engine**: Choose between various graph algorithms based on specific use-cases:
  - **Dijkstra's Algorithm**: For finding the absolute shortest distance.
  - **A* (A-Star) Search**: Uses geographic heuristics (Haversine) for the fastest computation.
  - **Breadth-First Search (BFS)**: For routes with the minimum number of stops/transfers.
  - **Depth-First Search (DFS)**: For exploring alternative/scenic routes.
  - **Floyd-Warshall**: For all-pairs shortest paths.
- **Dynamic Traffic & Weather Simulation**: Real-time manipulation of graph edge weights based on simulated live traffic congestion or severe weather blockages.
- **Nearby Emergency Services**: Built using a Max-Heap (Priority Queue) to instantly locate the nearest Hospitals, Police Stations, and Petrol Pumps within a 50km radius.
- **Custom 2D Map Engine**: Features a bespoke coordinate-to-pixel mapping engine with interactive drag-to-pan, precise trackpad pinch-to-zoom, and automatic route-framing (auto-zoom).
- **Cost Estimation Engine**: Automatically calculates required fuel based on mileage and estimates total journey toll costs.
- **Admin Dashboard**: Dynamically inject new Cities, Roads, and Places into the graph directly from the user interface without a restart.
- **Modern GUI**: Powered by the **FlatLaf** engine, replacing standard Java swing components with sleek, minimalist, high-DPI-aware graphics.

## 🛠️ Tech Stack

- **Core**: Java (Core Java, JDK 17, Object-Oriented Programming)
- **Data Structures**: Graphs (Adjacency List), Priority Queue (Min/Max Heaps), HashMaps
- **UI**: Java Swing with FlatLaf styling
- **Deployment**: Standalone `.jar` package

## 🚀 How to Run

You do not need an IDE, Maven, or a terminal to run this project! It has been fully deployed as a standalone executable.

1. Clone or download this repository.
2. Locate the **`SmartRoutePlanner.jar`** file.
3. Double-click it to instantly launch the application!
   *(Alternatively, run `java -jar SmartRoutePlanner.jar` from your terminal).*

## 📸 Modules Preview

1. **Route Planner**: Select Source and Destination, choose your algorithm, toggle live weather/traffic, and get instant pathfinding results plotted on the interactive map.
2. **Nearby & Emergency**: Select a city and search for the closest infrastructure. The map will auto-zoom to the city and plot numbered markers corresponding to the proximity list.
3. **Admin Panel**: A perfectly structured panel to inject new geographic data into the system on the fly.

## 🧠 Core Algorithms Under the Hood
- **Graph (Adjacency List)**: To represent the network of cities and interconnecting highways efficiently.
- **Min-Heap (Priority Queue)**: Used internally by Dijkstra and A* to always expand the most promising path next.
- **Max-Heap (Priority Queue)**: Used in the Nearby Places module to keep track of the top `N` closest locations, discarding the rest.
- **Haversine Formula**: To calculate the great-circle geographic distance between two points on the Earth using spherical geometry.
