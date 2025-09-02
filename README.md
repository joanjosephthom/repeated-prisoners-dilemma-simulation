# Repeated Prisoner's Dilemma Simulation

A Java/JavaFX simulation of the **Repeated Prisoner’s Dilemma**, modeling how different strategies evolve in a population over multiple generations. Built as part of the *Programming II* course project (July 2024).

---

## 🎯 Objectives
- Simulate various strategies in the repeated Prisoner’s Dilemma.  
- Analyze the long-term effectiveness of cooperative vs. selfish strategies.  
- Explore conditions that lead to equilibrium and evolutionary stability.  
- Provide a real-time visualization of strategy prevalence with JavaFX.  

---

## ⚙️ Features
- Adjustable parameters:  
  - Number of players (50–1000)  
  - Strategy distribution  
  - Number of interactions  
- Real-time line chart visualization of population dynamics.  
- Fitness-proportional reproduction (evolutionary mechanism).  
- Efficient multithreading with Java ExecutorService.  

---

## 🛠️ Technology Stack
- **Java 17**  
- **JavaFX** (UI & visualization)  
- **Maven** (build & dependencies)  
- **Java concurrency utilities**  

---

## 📂 Project Structure
- `Main.java` → Entry point & UI setup  
- `Simulation.java` → Core simulation engine  
- `Player.java` → Represents a player with a strategy  
- `Strategies/` → Contains strategy implementations:  
  - Always Cooperate  
  - Always Defect  
  - Tit for Tat  
  - Grim Trigger  
  - Pavlov  
  - Generous Tit for Tat  
  - Prober, Tit for Two Tats, etc.  
- `Interaction.java` → Models payoff from each round  
- `docs/report.pdf` → Full project report (background, methods, results)  

---

## 📊 Results (Summary)
- Forgiving strategies like **Tit for Tat** and **Generous Tit for Tat** consistently dominate.  
- Purely selfish strategies like **Always Defect** decline rapidly.  
- Results align with **Axelrod’s principles**: cooperation, reciprocity, and forgiveness are evolutionarily stable.  

---

## 🚀 How to Run
```bash
# Clone the repository
git clone git@github.com:joanjosephthom/repeated-prisoners-dilemma-simulation.git
cd repeated-prisoners-dilemma-simulation

# Build & run with Maven
mvn clean install
mvn javafx:run
```
---

## 📖 Report
For detailed background, methodology, and analysis, see the [project report](docs/report.pdf).

---

## 👨‍💻 Authors
- **Joan Joseph Thomas**  
- Fathima Shana Chirathodi  
- Miah Mariam Akter  

---

## 📜 License
This project is released under the [MIT License](LICENSE).  
Feel free to use, modify, and share with attribution.
