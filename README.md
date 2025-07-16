### SimpleChat - 1.0.2

Plugin de chat mediocre para servidores Spigot/Paper.  
Incluye lo justo para funcionar y lo suficiente para romperse.

#### Funcionalidades (supuestamente):
- Soporte para [MiniMessage](https://docs.advntr.dev/minimessage/)
- Integración con [Vault](https://www.spigotmc.org/resources/vault.34315/) y [PlaceholderAPI](https://www.spigotmc.org/resources/placeholderapi.6245/)
- Integración básica con canales de [TownyChat](https://github.com/TownyAdvanced/TownyChat)
- Filtros básicos de malas palabras y anti-spam
- Comando de anuncios con bossbar y reproducción de sonidos

config.yml
```yml
# Configuración principal de SimpleChat
# Versión: 1.0.2

# Configuración general del plugin
general:
  # Idioma del plugin (es, en)
  language: es
  # Verificar actualizaciones al iniciar
  check-updates: true

# Configuración de MiniMessage
minimessage:
  # Habilitar soporte para MiniMessage (formato moderno)
  enabled: true
  # Permitir tags HTML básicos
  allow-html: false

# Configuración de PlaceholderAPI
placeholderapi:
  # Habilitar integración con PlaceholderAPI
  enabled: true
  # Prefijo para los placeholders del plugin
  prefix: "simplechat"

# Mundos donde el chat está habilitado
# Lista vacía = habilitado en todos los mundos
enabled-worlds:
  - world
  - world_nether
  - world_the_end

# Configuración de chat
chat:
  # Cancelar evento de chat por defecto
  cancel-default-chat: true
  # Alcance del chat (GLOBAL, WORLD, LOCAL)
  scope: WORLD
  # Distancia para chat local (en bloques)
  local-distance: 50
  # Formato cuando no hay grupos configurados
  default-format: "<white>%player_name%<gray>: <white>%message%"

# Configuración de filtros
filters:
  # Habilitar filtro de palabras prohibidas
  enabled: false
  # Palabras prohibidas (se pueden usar regex)
  blocked-words:
    - "palabra1"
    - "palabra2"
  # Acción cuando se detecta palabra prohibida (BLOCK, REPLACE, WARN)
  action: BLOCK
  # Mensaje de reemplazo
  replacement: "***"

# Configuración de spam
anti-spam:
  # Habilitar protección anti-spam
  enabled: true
  # Tiempo entre mensajes (en segundos)
  cooldown: 1
  # Máximo de mensajes iguales consecutivos
  max-same-messages: 2
  # Mensaje de cooldown
  cooldown-message: "<red>¡Espera %time% segundos antes de enviar otro mensaje!"

# Configuración de logging
logging:
  # Guardar chat en archivo
  enabled: true
  # Archivo de logs
  file: "chat.log"
  # Formato del log
  format: "[%date% %time%] [%world%] %player%: %message%"
```

formats.yml
```yml
# Formatos de chat dependiendo del grupo del jugador

default:
  format: "<gray>[Usuario]</gray> <white>%player_name%<gray>: <white>%message%"
admin:
  format: "<gradient:red:gold>[ADMIN]</gradient> <white>%player_name%<gray>: <yellow>%message%"
vip:
  format: "<aqua>[VIP]</aqua> <white>%player_name%<gray>: <white>%message%"
```

#### Compilación
Requiere Java 21 y Maven.

```bash
mvn clean package
```
