
# ERP Login System

This is a simple ERP (Enterprise Resource Planning) system with a login for admins and students.

## Features

*   **Admin Dashboard:**
    *   View and manage student details.
    *   Manage student attendance.
    *   Manage student fee status.
    *   Manage student admit cards.
    *   Manage student timetables.
*   **Student Dashboard:**
    *   View their own details.
    *   View their attendance.
    *   View their fee status.
    *   View their admit card.
    *   View their timetable.

## Architecture

The project is organized into the following packages:

*   **`com.erp.db`:** Contains DAO (Data Access Object) classes for interacting with the database.
*   **`com.erp.gui`:** Contains the GUI (Graphical User Interface) classes for the admin and student dashboards.
*   **`com.erp.models`:** Contains the model classes for representing data.
*   **`com.erp.services`:** Contains service classes for business logic.
*   **`com.erp.utils`:** Contains utility classes.

## Setup and Installation

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/your-username/erp-login-system.git
    ```
2.  **Open the project in your IDE.**
3.  **Create the database:**
    *   The database connection details are in `src/com/erp/db/DBConnection.java`.
    *   You will need to create a database and tables for admins, students, attendance, fees, admit cards, and timetables.
4.  **Run the application:**
    *   Run the `main` method in `src/com/erp/gui/Main.java`.

## Usage

1.  **Login:**
    *   Select whether you are an admin or a student.
    *   Enter your username and password.
2.  **Admin Dashboard:**
    *   Click on the different tabs to manage student information.
3.  **Student Dashboard:**
    *   Click on the different tabs to view your information.
