### SimpleChat - 1.0.1

Plugin de chat mediocre para servidores Spigot/Paper.  
Incluye lo justo para funcionar y lo suficiente para romperse.

#### Funcionalidades (supuestamente):
- Soporte para [MiniMessage](https://docs.advntr.dev/minimessage/)
- Sistema de grupos personalizables para formatear el chat
- Filtros básicos de malas palabras y anti-spam
- Compatibilidad con PlaceholderAPI (si está presente)
- "Integración" con TownyChat (solo se hace a un lado cuando detecta canales externos)
- Comando de anuncios con bossbar y reproducción de sonidos

config.yml
```yml
# Configuración principal de SimpleChat
# Versión: 1.0.1

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
  prefix: simplechat

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
  default-format: '<white>%player_name%<gray>: <white>%message%'

# Configuración de integraciones con otros plugins
# Integración sin necesidad de configuración, esta sección será eliminada en futuras versiones
integrations:
  # Integración con TownyChat
  townychat:
    # Habilitar integración con TownyChat
    enabled: true
    # Canales donde SimpleChat NO debe interferir
    ignore-channels:
    - town
    - nation
    - mod
    - admin

# Configuración de filtros
filters:
  # Habilitar filtro de palabras prohibidas
  enabled: false
  # Palabras prohibidas (se pueden usar regex)
  blocked-words:
  - palabra1
  - palabra2
  # Acción cuando se detecta palabra prohibida (BLOCK, REPLACE, WARN)
  action: BLOCK
  # Mensaje de reemplazo
  replacement: '***'

# Configuración de spam
anti-spam:
  # Habilitar protección anti-spam
  enabled: true
  # Tiempo entre mensajes (en segundos)
  cooldown: 1
  # Máximo de mensajes iguales consecutivos
  max-same-messages: 2
  # Mensaje de cooldown
  cooldown-message: <red>¡Espera %time% segundos antes de enviar otro mensaje!

# Configuración de logging
logging:
  # Guardar chat en archivo
  enabled: true
  # Archivo de logs
  file: chat.log
  # Formato del log
  format: '[%date% %time%] [%world%] %player%: %message%'
```
groups.yml
```yml
# Configuración de grupos de chat
# Los grupos se evalúan por prioridad (mayor número = mayor prioridad)

groups:
  # Grupo por defecto (OBLIGATORIO)
  default:
    priority: 0
    format: '<gray>%player_name%<dark_gray>: <white>%message%'
    permission: simplechat.use
  
  # Grupo para miembros VIP
  vip:
    priority: 10
    format: '<gold>[VIP] <yellow>%player_name%<gray>: <white>%message%'
    permission: simplechat.vip
  
  # Grupo para moderadores
  moderator:
    priority: 50
    format: '<green>[MOD] <dark_green>%player_name%<gray>: <white>%message%'
    permission: simplechat.moderator
  
  # Grupo para administradores
  admin:
    priority: 100
    format: '<red>[ADMIN] <dark_red>%player_name%<gray>: <white>%message%'
    permission: simplechat.admin
  
  # Grupo para el owner
  owner:
    priority: 999
    format: '<dark_purple>[OWNER] <light_purple>%player_name%<gray>: <white>%message%'
    permission: simplechat.owner

# Configuración de prefijos personalizados
custom-prefixes:
  # Habilitar prefijos personalizados
  enabled: true
# Ejemplo: "550e8400-e29b-41d4-a716-446655440000": "<rainbow>ESPECIAL</rainbow>"

# Configuración de sufijos personalizados
custom-suffixes:
  # Habilitar sufijos personalizados
  enabled: true
# Ejemplo: "550e8400-e29b-41d4-a716-446655440000": "<gray>[Beta Tester]"
```
#### Compilación
Requiere Java 21 y Maven.

```bash
mvn clean package
```
