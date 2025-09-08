# Cephra Scripts

Only one script is intentionally kept for collaboration:

- `clone-database.bat`: clones your current MySQL database (`cephradb` by default) to a new database, including schema, data, routines, triggers, and events.

Usage:

```bat
scripts\clone-database.bat                # creates cephradb_clone_YYYYMMDDHHMMSS
scripts\clone-database.bat cephra_teamdb  # clones into cephra_teamdb
```

Edit the variables at the top of the script if your MySQL credentials or database name differ.
