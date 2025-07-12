# 🥠 Fortune

A lightweight Fabric mod that gives players a random fortune whenever they log in — inspired by the classic Linux `fortune` command.

## ✨ Features

- Shows a random fortune when players join the server
- Fully customizable: edit `fortunes.json` in the config folder
- Reload fortunes in-game with `/fortune reload`
- Server-side only — no need for players to install anything

## 📂 Configuration

Fortunes are stored in a simple JSON file:

```json
[
  "Beware the sheep that watches you sleep.",
  "Your next jump will change everything."
]
```

Place this file at:

```
config/fortune/fortunes.json
```

You can add as many fortunes as you'd like. Edit the file while the server is running, then use:

```mc
/fortune reload
```

...to refresh the list without restarting.

## 📦 Installation

1. Install [Fabric Loader](https://fabricmc.net/)
2. Install [Fabric API](https://modrinth.com/mod/fabric-api)
3. Drop the Fortune mod `.jar` into your server's `mods/` folder

## 🌐 Try It Live

You can test the mod in-game on this public Minecraft server:

```
netherhood.blockworlds.io
```

Join and receive a random fortune on login — no mods required.

## 🔗 Links

- 🔮 [Modrinth Page](https://modrinth.com/mod/fortune)
- 💻 [GitHub Repo](https://github.com/wh0oo/fortune)