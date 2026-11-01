# SQLite

A Kotlin Android contact management application that uses SQLite for local data storage.

## Features

- Add new contacts with a name and phone number.
- Display saved contacts in a `ListView`.
- Update existing contact information.
- Delete contacts from the database.
- Initiate phone calls to selected contacts.
- Navigate between contact management screens using an options menu.

## Technologies

- Kotlin
- Android SDK
- SQLite
- XML Layouts
- `SQLiteOpenHelper`
- Android Intents

## How It Works

The application stores contact information in a local SQLite database using a `DatabaseHelper` class. Users can select contacts from the main screen and manage them through the application's menu.

The call feature uses `ACTION_CALL` and requests the required phone permission when needed.

## About

Developed as a university Android programming assignment to practice SQLite database operations and contact management.
