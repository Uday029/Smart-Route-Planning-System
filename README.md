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
- **Nearby Emergency Services**: Built using a Max-Heap (Priority Queue) to instantly locate the nearest Hospitals, Police Stations, and Petrol Pumps within a given radius.
- **Cost Estimation Engine**: Automatically calculates required fuel based on mileage and estimates total journey toll costs.
- **Admin Dashboard**: Dynamically inject new Cities and Roads into the graph directly from the user interface.
- **Interactive GUI**: Built using Java Swing, featuring an intuitive tabbed dashboard.

## 🛠️ Tech Stack

- **Core**: Java (Core Java, Object-Oriented Programming)
- **Data Structures**: Graphs (Adjacency List), Priority Queue / Heap, Trie
- **UI**: Java Swing / AWT
- **Database**: MySQL (Schema provided, UI currently runs in-memory for quick demonstration)

## 🚀 How to Run

No heavy IDE or Maven installation is strictly required to test the UI! You can run the project directly from your terminal using the included build script:

1. Clone the repository:
   ```bash
   git clone https://github.com/Uday029/Smart-Route-Planning-System.git
   cd Smart-Route-Planning-System
   ```
2. Make the script executable and run it:
   ```bash
   chmod +x run.sh
   ./run.sh
   ```
3. The **Smart Route Planner Dashboard** will pop up automatically!

## 📸 Modules Preview

1. **Route Planner**: Select Source and Destination, choose your algorithm, toggle live weather/traffic, and get instant pathfinding results.
2. **Nearby & Emergency**: Select a city and search for the closest Hospitals or Petrol Pumps.
3. **Admin Panel**: Add custom cities and roads to the graph on the fly without writing any code.

## 🧠 Core Algorithms Under the Hood
- **Graph (Adjacency List)**: To represent the network of cities and interconnecting highways.
- **Min-Heap (Priority Queue)**: Used internally by Dijkstra and A* to always expand the most promising path next.
- **Max-Heap (Priority Queue)**: Used in the Nearby Places module to keep track of the top `N` closest locations, discarding the rest.
- **Haversine Formula**: To calculate the great-circle geographic distance between two points on the Earth using their latitude and longitude.
