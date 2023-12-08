# Wiederholung Tag 1

## Patterns & Paradigmen

1. Publish- & Subscribe: Events werden publiziert und empfangen - analog zum Oberserver-Patterns
-> Punkt zu Mehrpunkt (Mehrpunkt zu Mehrpunkt)-Kommunikation: Nachrichten werden verteilt
-> Kanäle werden subscribed

2. Synchron / Asynchron
-> Synchron: Taktgeber oder Uhr existiert. Timeouts in der Kommunikation sind möglich
-> Asynchron: Keine Timeouts möglich. 

3. Active Polling
-> Kein Ping, wenn was neues kommt
-> Consumer fragen in regelmäßigen Abständen nach, ob es neue Nachrichten gibt
-> Retention Time: Nachrichten werden erstmal vorgehalten. Nachrichten werden irgendwann gelöscht
-> Poll bleibt aus, Consumer gilt als "tot", d.h. wird abgemeldet. Re-Balancing in der Consumer-Group, d.h
die zugeordneten Partitionen werden auf die verbliebenden Consumer verteilt.
-> Konsequenzen bei der Entwicklung:
--> Bei einem Update einer Anwendung nicht einfach Consumer stoppen (wichtig: Consumer offsets schreiben)
--> Bei der Entwicklung: Polling-Thread falls nötig nicht blockieren, Multi-Threading nutzen

4. Event Sourcing
-> Nicht (nur) den aktuellen Zustand speichern, sondern alle Events die dazu führen
-> Analysemuster
-> Kann mit Kafka umgesetzt werden.

## Komponenten Kafka

1. Consumer
- Liest Nachrichten aus Kafka
- Pollt Nachrichten
- Ist Mitglied in einer Consumer-Group
- Führt Active Polling durch
- Kommuniniziert synchron mit dem Broker und asynchron mit dem Producer
- Verwaltet die Consumer-Offsets (automatisch im Hintergrund, manuell oder externes System)
- Implementiert Semantik für wiederholten Aufruf (z.B. im Fehlerfall): At-least-once, at-most-once, exactly-once (alternativ: Idempotent)
 

2. Producer
- Erzeugt Nachrichten 
- Kommuniziert mit dem Broker
- Kann auf ACKs vom Broker warten
- Kann die Reihenfolge der Nachrichten beeinflussen
- Übernimmt das Sharding, d.h. bestimmt die Partition (bei Key: Hash, kein Key: Round Robin, alternativ: Per Hand / eigene Strategie)
 

3. Broker
- Teil eines Kafka Clusters. Ein Cluster besteht aus einem oder mehreren Broker
- Server
- Speichert Nachrichten (I/O)
- Sendet und empfängt Nachrichten
- Unterstützt das Consumer Group Management
- Verwaltet Topics und Partitionen (ggf. mit Unterstützung des Zookeepers)

4. Topic
- Logische Gruppe aus Nachrichten
- Name, d.h. Adressse für einen Kanal
- Besteht aus mehreren Partitionen
- Attribute, z.B. clean-policy, retention-time werden für das Topic konfiguriert

5. Partition
- Tatsächlicher Speicherbereich
- Besteht aus Segements - Head: Segment, auch im RAM, Tail: restlichen Segment, nur auf der Festplatte
- Enthält nur einen Teil der Nachrichten eines Topics
- Eindeutige, lokale Ordnung append only
- Kann repliziert werden. Fail-Over (Redundanz), Lesen / Schreiben: Nur auf dem Leader
- Anzahl der Partitions beschränkt die Größe der Consumer Group

6. Consumer Group
- Logischer Zusammenschluss von Konsumenten
- Wird ggf. im Zookeeper geloggt.
- Paraelle Verarbeitung von Nachrichten. Jeder Consumer in einer Group enthält nur einen Teil der Nachrichten.

7. Message
- Hat Magic Byte (intern, zur Protokollerkennung)
- Hat Header und Content
- Maximale Größe (default: 1 MiB)
- Kann komprimiert werden (Eigenschaft eines Topics), Flag im Header
- Key, Value: Bytes. Keine definierbare Datenstruktur. Praktisch: Selber serialisieren

