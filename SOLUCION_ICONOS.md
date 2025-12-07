# 🔧 Solución para Iconos Faltantes

## Problema
El error indica que faltan los iconos `ic_launcher` e `ic_launcher_round` en las carpetas `mipmap`.

## ✅ Solución Temporal Aplicada
Se modificó el `AndroidManifest.xml` para usar iconos por defecto de Android. Esto permite que la app compile y funcione.

## 🎨 Solución Permanente (Recomendada)

### Opción 1: Usar Android Studio Image Asset

1. En Android Studio, haz clic derecho en `app/src/main/res`
2. Selecciona **New → Image Asset**
3. Configura:
   - **Icon Type**: Launcher Icons (Adaptive and Legacy)
   - **Foreground Layer**: Sube una imagen o usa el texto
   - **Background Layer**: Color de fondo
4. Haz clic en **Next** y luego **Finish**

Esto generará automáticamente todos los iconos en las carpetas `mipmap-*`.

### Opción 2: Usar un Generador Online

1. Ve a: https://icon.kitchen/ o https://www.appicon.co/
2. Sube tu imagen (512x512 px recomendado)
3. Descarga el paquete de iconos
4. Extrae y copia las carpetas `mipmap-*` a `app/src/main/res/`

### Opción 3: Crear Iconos Manualmente

Crea archivos PNG en estas carpetas:
- `mipmap-mdpi/ic_launcher.png` (48x48 px)
- `mipmap-hdpi/ic_launcher.png` (72x72 px)
- `mipmap-xhdpi/ic_launcher.png` (96x96 px)
- `mipmap-xxhdpi/ic_launcher.png` (144x144 px)
- `mipmap-xxxhdpi/ic_launcher.png` (192x192 px)

Y lo mismo para `ic_launcher_round.png`.

Luego actualiza el `AndroidManifest.xml` para usar:
```xml
android:icon="@mipmap/ic_launcher"
android:roundIcon="@mipmap/ic_launcher_round"
```

## 📝 Nota
La solución temporal permite que la app funcione, pero es recomendable crear iconos personalizados para una mejor experiencia de usuario.

