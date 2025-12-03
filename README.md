# 🦈 Muscle Shark - Gym Management System

![License](https://img.shields.io/github/license/AntonMG4/gym_management?style=for-the-badge&logo=github)
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate-59666C?style=for-the-badge&logo=hibernate&logoColor=white)
![MariaDB](https://img.shields.io/badge/MariaDB-003545?style=for-the-badge&logo=mariadb&logoColor=white)
![Oracle](https://img.shields.io/badge/Oracle-F80000?style=for-the-badge&logo=oracle&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)

> A robust desktop application designed for the comprehensive administration of gym members, trainers, and sports activities, implementing the MVC design pattern and advanced data persistence.

---

## 📖 Project Overview

**Muscle Shark Gym Centre** is a system developed in Java to optimize the daily workflow of a fitness center. The project addresses the need to efficiently manage member enrollments and cancellations across various directed activities.

Unlike simple academic projects, this system implements a strict **Model-View-Controller (MVC)** architecture, decoupling business logic from the user interface, and utilizes **Hibernate** for an agnostic data access layer (compatible with both MariaDB and Oracle).

---

## 🚀 Key Features

Based on the included user manual:

* **Member & Trainer Management:** Full CRUD (Create, Read, Update, Delete) operations for user information.
* **Activity Management:** Administration of classes such as Body Combat, Pilates, Yoga, etc.
* **Smart Enrollment System:**
    * **Activity Registration:** Allows enrolling members by filtering only those activities where they are NOT currently registered.
    * **Activity Cancellation:** Displays only the member's active subscriptions for management.
* **Graphical User Interface (Swing):** Intuitive design with dynamic tables for visualizing large datasets.
* **Dual Persistence:** Configured to work seamlessly with both **Oracle Database** and **MariaDB** via external configuration files.

---

## 🛠️ Tech Stack

This project leverages industry-standard Enterprise technologies:

* **Language:** Java (JDK 11+).
* **Architecture:** MVC (Clear separation into `Controller`, `Model`, `View` packages).
* **ORM (Object-Relational Mapping):** Hibernate.
* **Database:** Support for MariaDB and Oracle.
* **Dependency Management:** Apache Maven.
* **GUI:** Java Swing / AWT.

---

## 📂 Project Structure

The code organization reflects software engineering best practices:

```text
gym_management/
├── src/main/java/
│   ├── Controlador/      # Logic coordinating user-system interaction
│   ├── Modelo/           # Hibernate entities and data logic
│   ├── Vista/            # GUI (Windows, Tables, Panels)
│   ├── Config/           # Connection settings
│   └── Aplicacion/       # Entry point (Main)
├── src/main/resources/
│   ├── hibernateMariaDB.cfg.xml  # MariaDB connection config
│   └── hibernateOracle.cfg.xml   # Oracle connection config
├── docs/
│   └── manual_de_instrucciones.pdf # Complete user manual
└── pom.xml               # Maven dependencies
