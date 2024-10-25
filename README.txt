# Installazione di Rubrica

Questa applicazione è stata progettata per funzionare principalmente con **MariaDB**, ma è anche compatibile con **MySQL**. Assicurati di avere uno dei due sistemi installati sul tuo computer.

## Istruzioni di Installazione

1. Decomprimere l'archivio `Rubrica.zip`.
2. Aprire il file `credenziali_database.properties` e sostituire i valori con le credenziali del proprio sistema MariaDB o MySQL:
   ```properties
   jdbc.username=root               # Nome utente per l'accesso al database
   jdbc.password=newpassword        # Password per l'accesso al database
   jdbc.url=jdbc:mariadb://localhost:3306/rubrica_db  # URL per la connessione al database
   jdbc.driver=org.mariadb.jdbc.Driver  # Driver JDBC da utilizzare
