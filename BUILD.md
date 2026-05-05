# Build & Deploy

## Prérequis

- JDK 17
- Android SDK (défini dans `local.properties`)
- Être dans le répertoire racine du projet

## Build de l'APK

```bash
./gradlew :app:assembleLiteRelease
```

L'APK généré se trouve dans :
```
app/build/outputs/apk/lite/release/tt9smart-v*.apk
```

## Servir l'APK en local (Wi-Fi)

```bash
cd app/build/outputs/apk/lite/release
python3 -m http.server 8080 &
```

Puis sur le téléphone (même réseau Wi-Fi), ouvrir :
```
http://192.168.1.30:8080/tt9smart-v*.apk
```

> Remplace `192.168.1.30` par l'IP de ta machine si elle a changé (`hostname -I | awk '{print $1}'`).

## Arrêter le serveur

```bash
pkill -f "python3 -m http.server 8080"
```

## Build + serve en une commande

```bash
./gradlew :app:assembleLiteRelease && \
  cd app/build/outputs/apk/lite/release && \
  python3 -m http.server 8080
```
