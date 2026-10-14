# 🔍 Lines of Code Watcher (Observer & Strategy Pattern in Java)

Ein automatisches Werkzeug zur Dateiüberwachung und Zählung von Codezeilen (Source Lines of Code – SLOC), das das **Observer Pattern** (Beobachter) mit dem **Strategy Pattern** (Strategie) kombiniert.

Entwickelt im Rahmen des Gegenstands **Softwareentwicklung (SEW)** an der HTL TGM Wien (4. Jahrgang, 1. Semester).

---

## 🏛 Architektur & Entwurfsmuster

Das System demonstriert zwei GoF-Entwurfsmuster im perfekten Zusammenspiel:

```mermaid
graph TD
    subgraph ObserverPattern ["Observer Pattern (Dateiüberwachung)"]
        W["Watcher (Subject)"]
        O["<<interface>> Observer"]
        A["Anzeige (Observer)"]
        L["LoggingWatcher (Observer)"]
        
        W o-- "benachrichtigt bei Dateiänderung" --> O
        O <|.. A
        O <|.. L
    end

    subgraph StrategyPattern ["Strategy Pattern (Kommentarfilterung)"]
        CC["<<interface>> CommentChecker"]
        JCC["JavaCommentChecker"]
        HCC["HTMLCommentChecker"]
        CTC["CommentTypeChooser (Context)"]

        CTC --> CC
        CC <|.. JCC
        CC <|.. HCC
    end

    A --> CTC
```

1. **Observer Pattern**:
   - `Watcher` (*Subject*): Überwacht ein Verzeichnis (`watched/`) kontinuierlich auf Dateiänderungen, neue oder gelöschte Dateien.
   - `Observer` (*Interface*): Definiert die Callback-Methode für Benachrichtigungen.
   - `Anzeige` & `LoggingWatcher` (*Concrete Observers*): Verarbeiten die Datei-Events, stoßen die Zeilenzählung an und geben Ergebnisse in der Konsole bzw. Logdateien aus.

2. **Strategy Pattern**:
   - `CommentChecker` (*Strategy Interface*): Definiert den Algorithmus zur Zeilenzählung und Kommentarfilterung.
   - `JavaCommentChecker` (*Concrete Strategy*): Ignoriert Java-spezifische einzeilige (`//`) und mehrzeilige (`/* ... */`) Kommentare.
   - `HTMLCommentChecker` (*Concrete Strategy*): Ignoriert HTML/XML-Kommentare (`<!-- ... -->`).
   - `CommentTypeChooser` (*Context*): Wählt anhand der Dateiendung (`.java`, `.html`) automatisch die passende Prüfstrategie aus.

---

## ✨ Features

- **Automatisierte Verzeichnisüberwachung**:
  - Reagiert sofort, sobald im Zielordner Dateien hinzugefügt, verändert oder entfernt werden.
- **Berechnung reiner Codezeilen (SLOC)**:
  - Leerzeilen und reine Kommentarzeilen werden gefiltert.
  - Sprachsensitive Filterung über austauschbare Strategien.
- **Lose Kopplung**:
  - Neue Dateiformate (z. B. Python, C++) können einfach durch eine weitere `CommentChecker`-Implementierung hinzugefügt werden, ohne die Dateiüberwachung anzupassen.
- **UML-Dokumentation**:
  - Enthält ein vollständiges Astah Klassendiagramm (`UML_Diagramm.asta`).

---

## 📁 Projektstruktur

```
lines-of-code-watcher/
├── README.md                           # Projektdokumentation
├── UML_Diagramm.asta                   # Astah UML-Modell
├── watched/                            # Zu überwachender Ordner mit Beispieldateien
└── src/                                # Quellcode
    ├── Watcher.java                    # Subject: Überwacht das Verzeichnis
    ├── Observer.java                   # Observer Interface
    ├── Anzeige.java                    # Observer: Konsolenausgabe der SLOC
    ├── LoggingWatcher.java             # Observer: Loggt Änderungen mit Zeitstempel
    ├── CommentChecker.java             # Strategy Interface
    ├── JavaCommentChecker.java         # Strategy: Filter für Java-Syntax
    ├── HTMLCommentChecker.java         # Strategy: Filter für HTML-Syntax
    └── CommentTypeChooser.java         # Context: Strategieauswahl via Extension
```

---

## 🚀 Ausführung

### 1. Kompilieren
```bash
javac -d out/production/src src/*.java
```

### 2. Starten
```bash
java -cp out/production/src Anzeige
```
> Die Anwendung überwacht nun das Verzeichnis `watched/`. Sobald eine Datei hinzugefügt oder geändert wird, ermittelt die passende Strategie die exakte Anzahl der Codezeilen.

---

## 👤 Autor

**Timon Hollaus**  
HTL TGM Wien – Abteilung für Informationstechnologie
