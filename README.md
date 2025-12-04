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

![Gameplay Screenshot](src/main/resources/images/muscleshark-removebg-preview%20(2).png)

**Muscle Shark Gym Centre** is a system developed in Java to optimize the daily workflow of a fitness center. The project addresses the need to efficiently manage member enrollments and cancellations across various directed activities.

Unlike simple academic projects, this system implements a strict **Model-View-Controller (MVC)** architecture, decoupling business logic from the user interface, and utilizes **Hibernate** for an agnostic data access layer (compatible with both MariaDB and Oracle).

---

## 🚀 Key Features

This project meets strict quality standards, including:

* **Advanced Data Integrity:**
    * Uses **Database Transactions** to ensure safe CRUD operations.
    * Implements a mix of **HQL (Hibernate Query Language)**, Named Queries, and Native SQL for optimized performance.
* **Robust Input Validation:**
    * Strict validation logic: Prevents invalid DNI formats, enforces correct email patterns, and validates phone number length (9 digits).
    * Business logic constraints: Ensures members are over 18 years old and prevents duplicate records.
* **Smart Enrollment System:**
    * **Conflict Resolution:** Only allows enrolling members in activities they are not currently attending.
    * **Dynamic Filtering:** Includes a search filter to quickly locate members in the database.
* **Enhanced UI/UX:**
    * Integrated **JCalendar** components for intuitive date selection.
    * Customized **Look and Feel** for a professional visual experience.
    * Modal windows with context-aware error messages.

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
