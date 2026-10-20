# Educational Game
A game to teach Java fundamentals, Object Oriented Programming, and Dynamic Data Structures

## How to run
Requires Java 17 or newer (JDK). From the repository root:

```bash
javac -d out $(find src -name '*.java' -not -path '*legacy*')
java -cp out codeFiles.Main
```

On Windows PowerShell:

```powershell
javac -d out (Get-ChildItem -Recurse src -Filter *.java | Where-Object { $_.FullName -notmatch 'legacy' } | ForEach-Object { $_.FullName })
java -cp out codeFiles.Main
```

Run the game from the repository root so it can find the `assets` folder. Press spacebar on the title screen to start.

## Credits

- **Background music:** "Level 1" from *5 Chiptunes (Action)* by Juhani Junkala (SubspaceAudio) — https://opengameart.org/content/5-chiptunes-action — License: CC0 (public domain). Converted to 22050 Hz 16-bit stereo WAV.
